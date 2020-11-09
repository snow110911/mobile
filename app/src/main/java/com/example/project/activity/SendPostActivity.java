package com.example.project.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.entity.PostEntity;
import com.example.project.fragment.MeFragment;
import com.example.project.utils.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapter.ImageAdapter;
import com.example.project.utils.EmotionData;
import com.example.project.utils.EmotionInputDetector;
import com.example.project.utils.Note;
import com.example.project.utils.SpannableMaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SendPostActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private EmotionInputDetector mDetector;
    ImageButton send,cancel,btn_emoji,btn_camera,btn_picture,btn_at,btn_hash;
    EditText content;
    //ImageView showPicture;
    String currentPhotoPath;
    RecyclerView mRvEditImage;
    private final int MAX_IMAGES = 9;
    public static final int TAKE_PHOTO_CODE = 1;
    public static final int SELECT_IMAGE_CODE = 111;
    public Uri photoURI;
    public List<Uri> imageUris;
    ImageAdapter mImageAdapter;
    GridLayoutManager gridLayoutManager;
    private List<Note> mNote = EmotionData.getNotes();
    LocationManager locationManager;
    private String address="";


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_post);
        init();
        setOnClickListener();
       grantPermission();
       checkLocationEnabled();
       getLocation();
        //getWindow().setSoftInputMode(getWindowManager().LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    private void init(){
        send = findViewById(R.id.btn_send);
        cancel = findViewById(R.id.btn_cancel);
        content = findViewById(R.id.post_et_content);
        btn_camera = findViewById(R.id.post_btn_camera);
        btn_emoji = findViewById(R.id.btn_emoji);
        btn_picture = findViewById(R.id.post_btn_picture);
        mRvEditImage = findViewById(R.id.rv_editImage);
        btn_at = findViewById(R.id.btn_post_at);
        btn_hash = findViewById(R.id.btn_post_hash);
        gridLayoutManager = new GridLayoutManager(SendPostActivity.this,3);
        mRvEditImage.setLayoutManager(gridLayoutManager);
        imageUris = new ArrayList<>();
        mImageAdapter = new ImageAdapter(SendPostActivity.this, imageUris, new ImageAdapter.OnItemClickListener() {
            @Override
            public void onTakePhotoClick() {
                // 点击图片，放大图片
                Toast.makeText(SendPostActivity.this,"Click on photo",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //长按删除图片
                Toast.makeText(SendPostActivity.this,"ready to delete",Toast.LENGTH_SHORT).show();
                imageUris.remove(position);
                mImageAdapter.notifyDataSetChanged();
            }
            @Override
            public void onAddButtonClick() {
                pickImageFromAlbum();
            }
        });
        mRvEditImage.setAdapter(mImageAdapter);
        // set emoji keyboard
        GridView gridView = findViewById(R.id.grid_emoji_view);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note = (Note)adapterView.getAdapter().getItem(i);

                EditText editText = content;

                int start = editText.getSelectionStart();

                Editable editable = editText.getEditableText();

                Spannable spannable = SpannableMaker.buildEmotionSpannable(SendPostActivity.this, note.getText(), (int)editText.getTextSize());

                editable.insert(start, spannable);
            }
        });
        gridView.setAdapter(new EmojiGridViewAdapter());

        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(findViewById(R.id.emoji_keyboard))
                .bindToContent(findViewById(R.id.content_view))
                .bindToEditText(content)
                .bindToEmotionButton(btn_emoji)
                .build();
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }
    private void setOnClickListener(){
        send.setOnClickListener(this);
        cancel.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        btn_at.setOnClickListener(this);
        btn_hash.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_cancel:
                intent = new Intent(SendPostActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_send:
                try {

                    sendPost();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                intent = new Intent(SendPostActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_camera:
                dispatchTakePictureIntent();
                break;
            case R.id.btn_picture:
                pickImageFromAlbum();
                break;
            case R.id.btn_post_at:
                content.setText(content.getText().toString()+"@");
                break;
            case R.id.btn_post_hash:
                content.setText(content.getText().toString()+"#");
                break;
        }
    }
    private void sendPost() throws FileNotFoundException {
       List<String> cloudPath = new ArrayList<>();
        if(!(imageUris == null || imageUris.size() == 0)){
            for(Uri uri : imageUris){
                Database.upload_image(uri, SendPostActivity.this);
                cloudPath.add(Database.mAuth.getUid()+"/"+uri.getLastPathSegment());
            }
        }
        Log.d("imagePost list", String.valueOf(imageUris.size()));
        String postText = content.getText().toString();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+11"));
        String postTime = simpleDateFormat.format(new Date());
//        grantPermission();
//        checkLocationEnabled();
//        getLocation();
        PostEntity post = new PostEntity(Database.mAuth.getUid()+"-"+postTime, MeFragment.currentuser.getUsername(), postTime,postText,this.address,MeFragment.currentuser.getHeader(),cloudPath);
        Log.e("Send list", String.valueOf(post.getPostImgPath().size()));
        Log.e("test1",this.address);
        Database.update(post);
        MeFragment.currentuser.addPost(post);
        Log.e("test2",post.getLocation());
        Database.update(MeFragment.currentuser);
        imageUris.clear();
        Toast.makeText(SendPostActivity.this,"already send",Toast.LENGTH_SHORT).show();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("SendPostActivity","Error occurred while creating the File" );
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.project.fileprovider",
                        photoFile);
                Log.e("file",photoFile.toString());
                Log.e("open URI",photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_CODE);
            }
        }
        else{
            Toast.makeText(SendPostActivity.this,"There is not app that support this action",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+11"));
        String timeStamp = simpleDateFormat.format(new Date());
        Log.d("Time",timeStamp);
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    public void pickImageFromAlbum() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE_CODE);
    }
    public void addImage(){
        if (imageUris.size() < MAX_IMAGES) {
            Log.e("add URI",photoURI.toString());
            imageUris.add(photoURI);
            mImageAdapter.notifyDataSetChanged();
            ImageView imageView = (ImageView) gridLayoutManager.findViewByPosition(0);
            imageView.setVisibility(View.VISIBLE);

        }else {
            Toast.makeText(SendPostActivity.this,"Maximum number is 9",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO_CODE:
                if (resultCode == RESULT_OK){
                    //showPicture.setImageURI(photoURI);
                    Log.e("check","enter if ");
                    addImage();
                }
                break;
            case SELECT_IMAGE_CODE:
                if (resultCode == RESULT_OK && data!=null){
                    photoURI = data.getData();
                    //showPicture.setImageURI(photoURI);
                    addImage();
                }
            default:
                break;
        }
    }

    private void getLocation() {
        try{
            locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void grantPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    private void checkLocationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networdEnabled = false;
        try{
            gpsEnabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            networdEnabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }
        if( !gpsEnabled && !networdEnabled){
            new AlertDialog.Builder(SendPostActivity.this)
                    .setTitle("Enable GPS Service")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //redirect to location setting
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel", null)
                    .show();
        }
    }






    @Override
    public void onLocationChanged(@NonNull Location location) {
            try {
                Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addressList= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                String country = addressList.get(0).getCountryName();
                String adminArea = addressList.get(0).getAdminArea();
                String locality  = addressList.get(0).getLocality();
                this.address = country+", "+adminArea+", "+locality;
                Log.e("L",this.address);
                System.out.println(this.address);
//                tvState.setText(addressList.get(0).getAdminArea() );
//                tvCity.setText(addressList.get(0).getLocality());
//                tvPin.setText(addressList.get(0).getPostalCode());
//                tvAds.setText(addressList.get(0).getAddressLine(0));



            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class EmojiGridViewAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (!(view instanceof ImageView)) {
                view = new ImageView(SendPostActivity.this);
            }
            ImageView imageView = (ImageView) view;
            imageView.setImageResource(((Note) getItem(position)).getIconRes());
            return view;
        }

        @Override
        public int getCount() {
            return mNote.size();
        }

        @Override
        public Object getItem(int position) {
            return mNote.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

}