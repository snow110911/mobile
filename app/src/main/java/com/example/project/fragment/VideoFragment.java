package com.example.project.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.example.project.R;
import com.example.project.activity.LogInActivity;
import com.example.project.activity.SendVideoActivity;
import com.example.project.adapter.VideoAdapter;
import com.example.project.entity.VideoEntity;
import com.example.project.listener.OnItemChildClickListener;
import com.example.project.utils.Database;
import com.example.project.utils.Tag;
import com.example.project.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class VideoFragment extends Fragment implements View.OnClickListener
                                                {
    private View mRootView;
    private ImageButton btn_sendVideo;
    private RecyclerView mRecyclerview;
    private SmartRefreshLayout mRefreshLayout;

    protected VideoView mVideoView;
    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;
    private LinearLayoutManager mLinearLayoutManager;


    private List<VideoEntity> videos = new ArrayList<>();
    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;
    public void initView(){
        initVideoView();
        btn_sendVideo = mRootView.findViewById(R.id.btn_sendVideo);
        mRefreshLayout = mRootView.findViewById(R.id.refreshlayout);
        mRecyclerview = mRootView.findViewById(R.id.recyclerview);
    }
    public void initData(){

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishRefresh(2000/*,false*/);
                loadVideos();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishLoadMore(2000/*,false*/);
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        mRecyclerview.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
            }
            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });
        setClickListener();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.frag_video,container,false);
        initView();
        loadVideos();
        return mRootView;
    }
    protected void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mVideoView.setOnStateChangeListener(new VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == VideoView.STATE_IDLE) {
                    Utils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(getActivity());
        mErrorView = new ErrorView(getActivity());
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(getActivity());
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(getActivity());
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(getActivity()));
        mController.addControlComponent(new GestureView(getActivity()));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }

    protected boolean isLazyLoad() {
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    /**
     * 由于onPause必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onPause的逻辑
     */
    protected void pause() {
        releaseVideoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    /**
     * 由于onResume必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onResume的逻辑
     */
    protected void resume() {
        if (mLastPos == -1)
            return;

        //恢复上次播放的位置
        startPlay(mLastPos);
    }

    /**
     * PrepareView被点击
     */

    /**
     * 开始播放
     * @param position 列表位置
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        VideoEntity videoEntity = videos.get(position);
        //边播边存
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);
        mVideoView.setUrl(videoEntity.getVideoPath());
        mTitleView.setTitle(videoEntity.getPostText());
        View itemView = mLinearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoAdapter.ViewHolder viewHolder = (VideoAdapter.ViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        getVideoViewManager().add(mVideoView, Tag.LIST);
        mVideoView.start();
        mCurPos = position;

    }

    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        if(getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }
    private void loadVideos(){
        ValueEventListener postsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //dataSnapshot.getValue() get a hashMap type
                    //Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue(Map.class);
                    GenericTypeIndicator<Map<String, VideoEntity>> genericTypeIndicator = new GenericTypeIndicator<Map<String, VideoEntity>>() {};
                    Map<String, VideoEntity> map = dataSnapshot.getValue(genericTypeIndicator);
                    //List<VideoEntity> videos = new ArrayList<>();
                    //                        Log.e("Add", String.valueOf(posts.size()));
                    videos.clear();
                    videos.addAll(map.values());
                    Collections.sort(videos, new Comparator<VideoEntity>() {
                        @Override
                        public int compare(VideoEntity o1, VideoEntity o2) {
                            return -o1.getPostTime().compareTo(o2.getPostTime());
                        }
                    });
                    VideoAdapter videoAdapter = new VideoAdapter(getActivity(),videos);
                    mRecyclerview.setAdapter(videoAdapter);
                    videoAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(int position) {
                            startPlay(position);
                        }
                    });


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LogIn", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.mFdatabase.child("videos").addListenerForSingleValueEvent(postsListener);

    }




    private void setClickListener(){
        btn_sendVideo.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_sendVideo:
                if(Database.mAuth.getCurrentUser() == null){
                    Toast.makeText(getContext(),"Sign In Please!", Toast.LENGTH_SHORT).show();
                    intent  = new Intent(getActivity(), LogInActivity.class);
                    startActivity(intent);
                }else{
//                    Database.loadCurrentUser(getContext());
                    intent = new Intent(getActivity(), SendVideoActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
    protected VideoViewManager getVideoViewManager() {
        return VideoViewManager.instance();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

}
