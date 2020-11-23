package com.blindchat.model;

public class TaskModel
{
    private String id;
    private String title;
    private String description;
    private String points;
    private String status;

    public TaskModel()
    {

    }


    public TaskModel(String id, String title, String description, String points,String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.points = points;
        this.status=status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
