package com.blindchat.model;

public class ChatModel
{
    private String id;
    private String from_id;
    private String to_id;
    private String msg;
    private String type;
    private String times;

    public ChatModel(String id, String from_id, String to_id, String msg,String type, String times) {
        this.id = id;
        this.from_id = from_id;
        this.to_id = to_id;
        this.msg = msg;
        this.type=type;
        this.times = times;
    }

    public ChatModel()
    {

    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}


