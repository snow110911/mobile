package com.example.project.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostEntity implements Serializable {
    private String postID;
    private String userID;
    private String postTime;
    private String postText;
    private String header;
    private int likeCount;
    private int collectCount;
    private int commentCount;
    private String location;
    private List<String> postImgPath;

    public List<String> getPostImgPath() {
        return postImgPath;
    }

    public void setPostImgPath(List<String> postImgPath) {
        this.postImgPath = postImgPath;
    }

    public PostEntity(){
    }
    public PostEntity(String postID,String userID,String postTime, String postText, String location, String header, List<String> postImgPath){
        this.postImgPath = new ArrayList<>();
        this.postID = postID;
        this.userID = userID;
        this.postTime = postTime;
        this.postText = postText;
        this.location = location;
        this.header = header;
        this.postImgPath = postImgPath;

        likeCount = 0;
        collectCount= 0 ;
        commentCount = 0;
    }
    public PostEntity(PostEntity post){
        this.postImgPath = new ArrayList<>();
        this.postID = post.getPostID();
        this.userID = post.getUserID();
        this.postTime = post.getPostTime();
        this.postText = post.getPostText();
        this.location = post.getLocation();
        this.header = post.getHeader();
        this.postImgPath = post.getPostImgPath();

        likeCount = post.getLikeCount();
        collectCount= post.getCollectCount();
        commentCount = post.getCommentCount();
    }

    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
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

    public String getHeader(){
        return header;
    }

    public void setHeader(String header){
        this.header = header;
    }
}
