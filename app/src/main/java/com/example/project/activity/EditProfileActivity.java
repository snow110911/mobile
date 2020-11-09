package com.example.project.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.project.R;
import com.example.project.adapter.ImageAdapter;
import com.example.project.entity.PostEntity;
import com.example.project.entity.UserEntity;
import com.example.project.fragment.MeFragment;
import com.example.project.utils.Database;
import com.example.project.view.CircleTransform;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name, age, phone, location;
    private RadioButton male, female;
    private RadioGroup gender;
    private Button btnsave;
    private ImageView image;
    private Uri imguri;
    private ImageButton picture, camera;


    public static final int TAKE_PHOTO_CODE = 1;
    public static final int SELECT_IMAGE_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        name = findViewById(R.id.et_username);
        age = findViewById(R.id.et_age);
        gender = findViewById(R.id.rg_gender);
        phone = findViewById(R.id.et_phone);
        male = findViewById(R.id.rb_male);
        female = findViewById(R.id.rb_female);
        image = findViewById(R.id.image_view);
        location = findViewById(R.id.et_location);
        male = findViewById(R.id.rb_male);
        female = findViewById(R.id.rb_female);
        btnsave = findViewById(R.id.btn_update);
        btnsave.setOnClickListener(this);
        picture = findViewById(R.id.ed_picture);
        picture.setOnClickListener(this);
        camera = findViewById(R.id.ed_camera);
        camera.setOnClickListener(this);
        if(MeFragment.currentuser.getHeader()!=null&&!MeFragment.currentuser.getHeader().equals("")){
            Database.download_headerImage(MeFragment.currentuser.getHeader(),EditProfileActivity.this,image);

        }
        name.setText(MeFragment.currentuser.username);
        age.setText(MeFragment.currentuser.age);
        phone.setText(MeFragment.currentuser.phone);
        location.setText(MeFragment.currentuser.location);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                MeFragment.currentuser.sex = radioButton.getText().toString();
                Log.e("Gender",MeFragment.currentuser.sex);
            }
        });
        if(MeFragment.currentuser.sex.equals("Male")){
            male.setChecked(true);
        }
        else if(MeFragment.currentuser.sex.equals("Female")){
            female.setChecked(true);
        }

    }
// inovke camera service
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
                imguri = FileProvider.getUriForFile(this,
                        "com.example.assignment2.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imguri);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_CODE);
            }
        }
        else{
            Toast.makeText(EditProfileActivity.this,"There is not app that support this action",
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
//        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void pickImageFromAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE_CODE);
    }


    public void addImage(){
        Picasso.with(EditProfileActivity.this).load(imguri).transform(new CircleTransform()).into(image);

//        image.setImageURI(imguri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO_CODE:
                if (resultCode == RESULT_OK){
                    //showPicture.setImageURI(photoURI);
                    addImage();
                }
                break;
            case SELECT_IMAGE_CODE:
                if (resultCode == RESULT_OK && data!=null){
                    imguri = data.getData();
                    //showPicture.setImageURI(photoURI);
                    addImage();
                }
            default:
                break;
        }
    }

    private void update() throws FileNotFoundException {
        MeFragment.currentuser.username = name.getText().toString();
        MeFragment.currentuser.age = age.getText().toString();
        MeFragment.currentuser.phone = phone.getText().toString();
        MeFragment.currentuser.location = location.getText().toString();
        if(imguri!=null&& !imguri.toString().equals("")){
            MeFragment.currentuser.setHeader("headers/"+imguri.getLastPathSegment());
        }

        Log.e("CUR",MeFragment.currentuser.age+MeFragment.currentuser.phone+MeFragment.currentuser.location);
        Database.upload_header(imguri,EditProfileActivity.this);

        Toast.makeText(EditProfileActivity.this,"Update",Toast.LENGTH_SHORT).show();
//        List<PostEntity> userposts = new ArrayList<>();
        List<PostEntity> newPostList = new ArrayList<>();
//        userposts = MeFragment.currentuser.postList;
//        Log.e("size",String.valueOf(userposts.size()));
        Log.e("currentuser", MeFragment.currentuser.toString());

        if(MeFragment.currentuser.postList.size()>0){
            for(PostEntity post : MeFragment.currentuser.postList){
                PostEntity newPost = new PostEntity(post);
                if(imguri!=null&& !imguri.toString().equals("")){
                    newPost.setHeader("headers/"+imguri.getLastPathSegment());
                    newPost.setUserID(name.getText().toString());
                    Database.update(newPost);
                    newPostList.add(newPost);
                }else{
                    newPost.setHeader("");
                    newPost.setUserID(name.getText().toString());
                    Database.update(newPost);
                    newPostList.add(newPost);
                }
            }
        }
        MeFragment.currentuser.postList = newPostList;
        Log.e("currentuser new", MeFragment.currentuser.toString());
        Database.update(MeFragment.currentuser);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ed_camera:
                dispatchTakePictureIntent();
                break;
            case R.id.ed_picture:
                pickImageFromAlbum();
                break;
            case R.id.btn_update:
                try {
                    update();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Intent toProfile = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(toProfile);
                break;
        }

    }
}
