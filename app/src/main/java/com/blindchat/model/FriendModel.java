package com.blindchat.model;

public class FriendModel
{
    private String id;
    private String friendname;
    private String friendid;
    private String status;
    private String token;


    public FriendModel()
    {

    }

    public FriendModel(String id, String friendname, String friendid) {
        this.id = id;
        this.friendname = friendname;
        this.friendid = friendid;
    }

    public FriendModel(String id, String friendname, String friendid,String status,String token) {
        this.id = id;
        this.friendname = friendname;
        this.friendid = friendid;
        this.status=status;
        this.token=token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriendname() {
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
