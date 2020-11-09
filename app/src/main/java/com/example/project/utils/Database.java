package com.example.project.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.project.entity.PostEntity;
import com.example.project.entity.UserEntity;
import com.example.project.entity.VideoEntity;
import com.example.project.fragment.MeFragment;
import com.example.project.view.CircleTransform;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static FirebaseDatabase mfirebaseDatabase = FirebaseDatabase.getInstance();
    private static FirebaseStorage mfirebaseStorage = FirebaseStorage.getInstance();
    public static StorageReference mStorageRef = mfirebaseStorage.getReference();
    public static DatabaseReference mFdatabase = mfirebaseDatabase.getReference();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static List<PostEntity> postList;


    public static FirebaseStorage getMfirebaseStorage(){
        return mfirebaseStorage;
    }

    public static FirebaseDatabase getMfirebaseDatabase(){
        return mfirebaseDatabase;
    }
    //check if user in realtime database. If not store to the realtime database.
    public static void createUser(){
        final FirebaseUser user = mAuth.getCurrentUser();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
//                  User user = dataSnapshot.getValue(User.class);
//                  Toast.makeText(LogInActivity.this,user.toString(),Toast.LENGTH_SHORT).show();
                    //default username is id
                    UserEntity new_user = new UserEntity(user.getUid(),user.getUid(),user.getEmail());
                    update(new_user);
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LogIn", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mFdatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(userListener);

    }

    public static void updateUser(final PostEntity post){
        final FirebaseUser user = mAuth.getCurrentUser();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                    UserEntity temp_user = dataSnapshot.getValue(UserEntity.class);
//                  Toast.makeText(LogInActivity.this,user.toString(),Toast.LENGTH_SHORT).show();

//                    if(temp_user.postList == null){
//                        temp_user.postList = new ArrayList<>();
//                    }
                    temp_user.addPost(post);
                    Log.e("D",String.valueOf(temp_user.postList.size()));
                    Log.e("D",temp_user.toString());
//                    update(temp_user);
//                    mFdatabase.child("users").child(temp_user.id).child("postList").child(post.getPostID()).setValue(post);
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LogIn", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mFdatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(userListener);

    }

    public static void loadCurrentUser(final TextView tv_name, final TextView tv_sex, final TextView tv_age,
                                       final TextView tv_email, final TextView tv_location, final TextView tv_phone,
                                       final ImageView img_header, final Activity activity){
        FirebaseUser user = Database.mAuth.getCurrentUser();
        if(user==null){
            Toast.makeText(activity,"Sign in Please",Toast.LENGTH_SHORT).show();
            return;
        }
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    UserEntity loaduser = dataSnapshot.getValue(UserEntity.class);
                    MeFragment.currentuser = new UserEntity((loaduser));
//                    Toast.makeText(getActivity(),loadUser.toString(),Toast.LENGTH_SHORT).show();
                    tv_name.setText(MeFragment.currentuser.username);
                    tv_email.setText(MeFragment.currentuser.email);
                    Log.e("Database",MeFragment.currentuser.getHeader());
                    if(MeFragment.currentuser.getHeader()!=null&&!MeFragment.currentuser.getHeader().equals("")){
                        Database.download_headerImage(MeFragment.currentuser.getHeader(),activity,img_header);
                        Log.e("Database",MeFragment.currentuser.getHeader());
                    }
                    if(!MeFragment.currentuser.age.equals("") && MeFragment.currentuser.age!=null){
                        tv_age.setVisibility(View.VISIBLE);
                        tv_age.setText(MeFragment.currentuser.age);
                    }
                    if(!MeFragment.currentuser.phone.equals("") && MeFragment.currentuser.phone!=null){
                        tv_phone.setVisibility(View.VISIBLE);
                        tv_phone.setText(MeFragment.currentuser.phone);
                    }
                    if(!MeFragment.currentuser.sex.equals("") && MeFragment.currentuser.sex!=null){
                        tv_sex.setVisibility(View.VISIBLE);
                        tv_sex.setText(MeFragment.currentuser.sex);
                    }
                    if(!MeFragment.currentuser.location.equals("") && MeFragment.currentuser.location!=null){
                        tv_location.setVisibility(View.VISIBLE);
                        tv_location.setText(MeFragment.currentuser.location);
                        Log.e("location",tv_location.getText().toString());
                    }
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LogIn", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        Database.mFdatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(userListener);

    }


    public static void update(UserEntity user){
        mFdatabase.child("users").child(user.id).setValue(user);
    }

    public static void update(PostEntity post){
        mFdatabase.child("posts").child(post.getPostID()).setValue(post);
    }

    public static void update(VideoEntity video){
        mFdatabase.child("videos").child(video.getVideoID()).setValue(video);
    }

    public static void download_image(String image_name, final Context activity, final ImageView image){

        mStorageRef.child(image_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(activity).load(uri).into(image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(activity,"load fail",Toast.LENGTH_SHORT).show();
                // Handle any errors
            }
        });
    }

    public static void download_headerImage(String image_name, final Context activity, final ImageView image){

        mStorageRef.child(image_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.with(activity).load(uri).transform(new CircleTransform()).into(image);
//                Glide.with(activity).load(uri).into(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(activity,"Header load fail",Toast.LENGTH_SHORT).show();
                // Handle any errors
            }
        });
    }


    public static void upload_image(Uri file, final Activity activity) throws FileNotFoundException {
        StorageReference mStorageRef = mfirebaseStorage.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            Toast.makeText(activity,"Please log in.",Toast.LENGTH_SHORT).show();
            return;
        }
//        Uri file = Uri.fromFile(new File(path));
        if(file == null){
            Toast.makeText(activity,"file not found",Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference riversRef = mStorageRef.child(user.getUid()+"/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(activity,"upload fail",Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(activity,"upload success",Toast.LENGTH_SHORT).show();


            }
        });
    }



    public static void upload_header(Uri file, final Activity activity) throws FileNotFoundException {
        StorageReference mStorageRef = mfirebaseStorage.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            Toast.makeText(activity,"Please log in.",Toast.LENGTH_SHORT).show();
            return;
        }
//        Uri file = Uri.fromFile(new File(path));
        if(file == null){
            Toast.makeText(activity,"file not found",Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference riversRef = mStorageRef.child("headers/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(activity,"upload fail",Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(activity,"upload success",Toast.LENGTH_SHORT).show();


            }
        });
    }

}
