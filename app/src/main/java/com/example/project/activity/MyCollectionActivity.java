package com.example.project.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.entity.PostEntity;
import com.example.project.entity.UserEntity;
import com.example.project.fragment.MeFragment;
import com.example.project.utils.Database;
import com.example.project.R;
import com.example.project.adapter.MyCollectionAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyCollectionActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerview;
    private ImageView mMyCollection;
    private MyCollectionAdapter myCollectionAdapter;
//    private List<PostEntity> postList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        mRecyclerview = findViewById(R.id.collect_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyCollectionActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        loadCollectionPosts();
    }

    private void loadCollectionPosts(){
        FirebaseUser user = Database.mAuth.getCurrentUser();
        if(user==null){
//            Toast.makeText(getContext(),"Sign In Please!", Toast.LENGTH_SHORT).show();
            Log.d("Load Post:", "no user");
            return;
        }
        ValueEventListener postsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //dataSnapshot.getValue() get a hashMap type
//                    GenericTypeIndicator<Map<String, PostEntity>> genericTypeIndicator = new GenericTypeIndicator<Map<String, PostEntity>>() {};
//                    Map<String, PostEntity> map = dataSnapshot.getValue(genericTypeIndicator);
                    UserEntity load_user = dataSnapshot.getValue(UserEntity.class);
                    MeFragment.currentuser = new UserEntity(load_user);
                    List<PostEntity> posts = new ArrayList<>();
                    if(load_user.getCollect()!= null && load_user.getCollect().size()>0){
                        for(PostEntity key : load_user.getCollect()){
                            posts.add(key);
//                            Log.e("ME Add", String.valueOf(posts.size()));
                        }
                        Collections.sort(posts, new Comparator<PostEntity>() {
                            @Override
                            public int compare(PostEntity postEntity, PostEntity t1) {
                                return -postEntity.getPostTime().compareTo(t1.getPostTime());
                            }
                        });
                    }
                    Log.e("Collection_size",String.valueOf(MeFragment.currentuser.getCollect().size()));
                    MyCollectionAdapter myCollectionAdapter = new MyCollectionAdapter(MyCollectionActivity.this, posts);
                    mRecyclerview.setAdapter(myCollectionAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LogIn", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.mFdatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(postsListener);

    }

    @Override
    public void onClick(View v) {

    }
}