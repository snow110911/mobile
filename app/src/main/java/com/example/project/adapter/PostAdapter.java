package com.example.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.text.Editable;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.activity.LogInActivity;
import com.example.project.activity.MainActivity;
import com.example.project.activity.SendPostActivity;
import com.example.project.entity.PostEntity;
import com.example.project.entity.UserEntity;
import com.example.project.fragment.MeFragment;
import com.example.project.utils.SpannableMaker;
import com.example.project.view.SquareImageView;
import com.example.project.utils.Database;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PostEntity> posts;

    public PostAdapter(Context context, List<PostEntity> posts){
        this.mContext = context;
        this.posts = posts;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.gridLayout.removeAllViews();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        final PostEntity postEntity = posts.get(position);
        vh.tvAuthor.setText(postEntity.getUserID());
        vh.tvTime.setText(postEntity.getPostTime());
        vh.tvComment.setText(String.valueOf(postEntity.getCommentCount()));
        vh.tvCollect.setText(String.valueOf(postEntity.getCollectCount()));
        vh.tvLike.setText(String.valueOf(postEntity.getLikeCount()));
        vh.mPosition = position;
//        vh.flagLike = false;
        checkLiked(vh,postEntity);
        checkCollect(vh,postEntity);

//        Log.e("Location", postEntity.getLocation());
        if(postEntity.getLocation()!=null&&!postEntity.getLocation().equals("")){
            vh.tvLocation.setVisibility(View.VISIBLE);
            vh.tvLocation.setText(postEntity.getLocation());
//            Log.e("Location2",vh.tvLocation.getText().toString());
        }
        if(postEntity.getHeader()!=null && !postEntity.getHeader().equals("")){
            Database.download_headerImage(postEntity.getHeader(),mContext,vh.ivHeader);
            //Database.download_image(postEntity.getHeader(),mContext,vh.ivHeader);
            Log.e("Database",postEntity.getHeader());
        }
       
        if(postEntity.getPostText()!=null && !postEntity.getPostText().equals("")) {
            Spannable spannable = SpannableMaker.buildEmotionSpannable(mContext, postEntity.getPostText(), (int)vh.tvPostContent.getTextSize());
            vh.tvPostContent.setText(spannable);
            vh.tvPostContent.setVisibility(View.VISIBLE);
        }
        int columnCount= vh.gridLayout.getColumnCount();//get column
//        int marginSize = PixelUtils.dp2px(mContext, 4);//得到经过dp转化的margin值
    if (postEntity.getPostImgPath()!=null && postEntity.getPostImgPath().size()!=0){

        for (int i = 0; i < postEntity.getPostImgPath().size(); i++) {
            GridLayout.Spec rowSpec = GridLayout.spec(i / columnCount);//rows
            GridLayout.Spec columnSpec = GridLayout.spec(i % columnCount, 1.0f);//列数 列宽的比例 weight=1
            ImageView imageView = new SquareImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.rowSpec = rowSpec;
            layoutParams.columnSpec = columnSpec;
            Database.download_image(postEntity.getPostImgPath().get(i), mContext, imageView);
//            layoutParams.setMargins(marginSize, marginSize, marginSize, marginSize);
            vh.gridLayout.addView(imageView, layoutParams);
        }
        vh.gridLayout.setVisibility(View.VISIBLE);
    }
    // set image header here
    }
    @Override
    public int getItemCount() {
        return this.posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvAuthor;
        private TextView tvTime;
        private TextView tvLike;
        private TextView tvComment;
        private TextView tvCollect;
        private TextView tvPostContent;
        private ImageView ivHeader;
        private GridLayout gridLayout;
        private TextView tvLocation;
        public int mPosition;
        private ImageView ivLike;
        private ImageView ivCollect;
        private boolean flagLike, flagCollect;
        public ViewHolder(@NonNull View View) {
            super(View);
            tvAuthor = View.findViewById(R.id.author);
            tvTime = View.findViewById(R.id.time);
            tvLike = View.findViewById(R.id.like);
            tvCollect = View.findViewById(R.id.collect);
            tvComment= View.findViewById(R.id.comment);
            tvPostContent = View.findViewById(R.id.postContent);
            ivHeader = View.findViewById(R.id.img_header);
            tvLocation= View.findViewById(R.id.location);
            gridLayout = View.findViewById(R.id.gridlayout_post);
            ivLike = View.findViewById(R.id.img_like);
            ivCollect = View.findViewById(R.id.img_collect);
//            flagCollect = false;
//            flagLike = false;
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    int likeNum = Integer.parseInt(tvLike.getText().toString());
                    if(flagLike){
                        if(likeNum>0){
                            tvLike.setText(String.valueOf(--likeNum));
                            posts.get(mPosition).setLikeCount(posts.get(mPosition).getLikeCount()-1);
                            Database.update(posts.get(mPosition));
                            tvLike.setTextColor(Color.parseColor("#161616"));
                            ivLike.setImageResource(R.mipmap.dianzan);
                            updateLike(posts.get(mPosition).getPostID(), !flagLike);
                        }

                    }
                    else{
                        tvLike.setText(String.valueOf(++likeNum));
                        posts.get(mPosition).setLikeCount(posts.get(mPosition).getLikeCount()+1);
                        Database.update(posts.get(mPosition));
                        tvLike.setTextColor(Color.parseColor("#E21918"));
                        ivLike.setImageResource(R.mipmap.dianzan_select);
                        updateLike(posts.get(mPosition).getPostID(), !flagLike);
                    }
                    flagLike = !flagLike;
                }
            });

            ivCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    int collectNum = Integer.parseInt(tvCollect.getText().toString());
                    if(flagCollect){
                        if(collectNum>0){
                            tvCollect.setText(String.valueOf(--collectNum));
                            posts.get(mPosition).setCollectCount(posts.get(mPosition).getCollectCount()-1);
                            Database.update(posts.get(mPosition));
                            tvCollect.setTextColor(Color.parseColor("#161616"));
                            ivCollect.setImageResource(R.mipmap.collect);
                            updateCollect(posts.get(mPosition), !flagCollect);
                        }

                    }
                    else{
                        tvCollect.setText(String.valueOf(++collectNum));
                        posts.get(mPosition).setCollectCount(posts.get(mPosition).getCollectCount()+1);
                        Database.update(posts.get(mPosition));
                        tvCollect.setTextColor(Color.parseColor("#E21918"));
                        ivCollect.setImageResource(R.mipmap.collect_select);
                        updateCollect(posts.get(mPosition),!flagCollect);
                    }
                    flagCollect = !flagCollect;
                }
            });


        }
    }
    private void updateLike(final String postID, final boolean flag){
        FirebaseUser user = Database.mAuth.getCurrentUser();
        if(user == null){
            Toast.makeText(mContext,"Sign In Please!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, LogInActivity.class);
            mContext.startActivity(intent);
            return;
        }
        if(flag){
            MeFragment.currentuser.addLike(postID);
            Database.update(MeFragment.currentuser);
            if(MeFragment.currentuser.postList.size()>0){
                for(PostEntity post:MeFragment.currentuser.postList){
                    if(post.getPostID().equals(postID)){
                        post.setLikeCount(post.getLikeCount()+1);
                    }
                }
            }
            if(MeFragment.currentuser.getCollect().size()>0){
                for(PostEntity post:MeFragment.currentuser.getCollect()){
                    if(post.getPostID().equals(postID)){
                        post.setLikeCount(post.getLikeCount()+1);
                    }
                }
            }
        }
        else{
            if(MeFragment.currentuser.getLiked().size()>0){
                MeFragment.currentuser.deleteLike(postID);
            }
            if(MeFragment.currentuser.postList.size()>0){
                for(PostEntity post:MeFragment.currentuser.postList){
                    if(post.getPostID().equals(postID)){
                        post.setLikeCount(post.getLikeCount()-1);
                    }
                }
            }
            if(MeFragment.currentuser.getCollect().size()>0){
                for(PostEntity post:MeFragment.currentuser.getCollect()){
                    if(post.getPostID().equals(postID)){
                        post.setLikeCount(post.getLikeCount()-1);
                    }
                }
            }
        }
        Database.update(MeFragment.currentuser);

    }


    private void updateCollect(final PostEntity postEntity, final boolean flag){
        FirebaseUser user = Database.mAuth.getCurrentUser();
        if(user == null){
            Toast.makeText(mContext,"Sign In Please!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, LogInActivity.class);
            mContext.startActivity(intent);
            return;
        }
        if(flag){
            MeFragment.currentuser.addCollect(postEntity);
            Database.update(MeFragment.currentuser);
            if(MeFragment.currentuser.postList.size()>0){
                for(PostEntity post:MeFragment.currentuser.postList){
                    if(post.getPostID().equals(postEntity.getPostID())){
                        post.setCollectCount(post.getCollectCount()+1);
                    }
                }
            }
        }
        else{
            if(MeFragment.currentuser.getCollect().size()>0){
                MeFragment.currentuser.deleteCollect(postEntity);
            }
            if(MeFragment.currentuser.postList.size()>0){
                for(PostEntity post:MeFragment.currentuser.postList){
                    if((post.getPostID()).equals(postEntity.getPostID())){
                        post.setCollectCount(post.getCollectCount()-1);
                    }
                }
            }
        }
        Database.update(MeFragment.currentuser);

    }
    private void checkLiked(final ViewHolder vh, final PostEntity postEntity){
        FirebaseUser user = Database.mAuth.getCurrentUser();
        if(user!=null){
            final ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        UserEntity temp_user = dataSnapshot.getValue(UserEntity.class);
                        if(temp_user.getLiked()!=null &&temp_user.getLiked().size()>0){
                            for(String like : temp_user.getLiked()){
                                if(like.equals(postEntity.getPostID())){
//                                    Log.e("Like equals",like);
                                    vh.flagLike=true;
                                    vh.tvLike.setTextColor(Color.parseColor("#E21918"));
                                    vh.ivLike.setImageResource(R.mipmap.dianzan_select);
                                }
                            }
                        }
                        Database.update(MeFragment.currentuser);
//                        Log.e("CurrrentUSer like",String.valueOf(MeFragment.currentuser.getLiked().size()));
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
    }

    private void checkCollect(final ViewHolder vh, final PostEntity postEntity){
        FirebaseUser user = Database.mAuth.getCurrentUser();
        if(user!=null){
            final ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        UserEntity temp_user = dataSnapshot.getValue(UserEntity.class);
                        if(temp_user.getCollect()!=null &&temp_user.getCollect().size()>0){
                            for(PostEntity collect : temp_user.getCollect()){
                                if((collect.getPostID()).equals(postEntity.getPostID())){
//                                    Log.e("Like equals",collect.getPostID());
                                    vh.flagCollect=true;
                                    vh.tvCollect.setTextColor(Color.parseColor("#E21918"));
                                    vh.ivCollect.setImageResource(R.mipmap.collect_select);
                                }
                            }
                        }
                        Database.update(MeFragment.currentuser);
//                        Log.e("CurrrentUSer collect",String.valueOf(MeFragment.currentuser.getLiked().size()));
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
    }

    public void filteredList(List<PostEntity> filteredList){
        posts = filteredList;
        notifyDataSetChanged();
    }
}
