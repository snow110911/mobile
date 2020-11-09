package com.example.project.entity;

import java.util.ArrayList;
import java.util.List;

public class UserEntity {
    public String id;
    public String username;
    public String email;
    public String phone = "";
    public String age = "";
    public String sex = "";
    public String location = "";
    private String header="";
    public List<PostEntity> postList;
    private List<String> liked;
    private List<PostEntity> collect;
//    private String password;

    public UserEntity(){

    }

    public UserEntity(String id, String username, String email){
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = "";
        this.age = "";
        this.sex = "";
        this.location = "";
        this.header = "";
        this.postList = new ArrayList<>();
        this.liked = new ArrayList<>();
        this.collect = new ArrayList<>();
//        this.password = password;
    }

    public UserEntity(String id, String username, String location,String email, String phone, String sex, String age, String header,
                      List<PostEntity> postList, List<String> liked, List<PostEntity> collect){
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.location = location;
        this.postList = new ArrayList<>();
        this.postList = postList;
        this.header = header;
        this.sex = sex;
        this.liked = new ArrayList<>();
        this.liked = liked;
        this.collect = new ArrayList<>();
        this.collect = collect;
        //this.postList.addAll(postList);

    }

    public UserEntity(UserEntity user){
        this.id = user.id;
        this.username = user.username;
        this.email = user.email;
        this.phone = user.phone;
        this.age = user.age;
        this.sex = user.sex;
        this.location = user.location;
        this.postList = new ArrayList<>();
        this.postList = user.postList;
        this.header = user.getHeader();
        this.liked = new ArrayList<>();
        this.liked = user.getLiked();
        this.collect = new ArrayList<>();
        this.collect = user.collect;

    }


    public List<String> getLiked(){
        return this.liked;
    }
    public void setLiked(List<String> liked){
        if(this.liked ==null){
            this.liked = new ArrayList<>();
        }
        this.liked = liked;
    }

    public void addLike(String postID){
        if(liked == null){
            liked = new ArrayList<>();
        }
        liked.add(postID);
    }
    public void deleteLike(String postID){
        if(liked.size()>0){
            liked.remove(postID);
        }
    }


    public List<PostEntity> getCollect(){
        return this.collect;
    }
    public void setCollect(List<PostEntity> collect){
        if(this.collect ==null){
            this.collect = new ArrayList<>();
        }
        this.collect = collect;
    }

    public void addCollect(PostEntity post){
        if(collect == null){
            collect = new ArrayList<>();
        }
        collect.add(post);
    }
    public void deleteCollect(PostEntity post){
        if(collect.size()>0){
            collect.remove(post);
        }
    }

    public void addPost(PostEntity post){
        if(postList == null){
            postList = new ArrayList<>();
        }
        postList.add(post);
    }

    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getHeader(){
        return header;
    }
    public void setHeader(String header){
        this.header = header;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public String toString(){
        if(postList!=null){
            return String.valueOf(postList.size());
        }
        return username+" "+email;
    }

//    public void setPassword(String password){
//        this.password = password;
//    }
}
