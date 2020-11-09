package com.example.project.entity;

import java.io.Serializable;

public class VideoEntity implements Serializable {
    private String videoID;
    private String userID;
    private String postTime;
    private String postText;
    private String header;
    private int likeCount;
    private int collectCount;
    private int commentCount;
    private String location;
    private String videoPath;
    //private String playUrl;
    private String coverUrl;

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

//    public String getPlayUrl() {
//        return playUrl;
//    }
//
//    public void setPlayUrl(String playUrl) {
//        this.playUrl = playUrl;
//    }

    public VideoEntity(){
    }
    public VideoEntity(String videoID, String userID, String postTime, String postText, String location, String header, String videoPath, String coverUrl){
        this.videoID = videoID;
        this.userID = userID;
        this.postTime = postTime;
        this.postText = postText;
        this.location = location;
        this.header = header;
        this.videoPath= videoPath;
        this.coverUrl = coverUrl;
        likeCount = 0;
        collectCount= 0 ;
        commentCount = 0;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
