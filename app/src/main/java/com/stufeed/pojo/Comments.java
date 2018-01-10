package com.stufeed.pojo;

/**
 * Created by sowmitras on 2/12/17.
 */

public class Comments {

    private String comments;
    private String replied;
    private String post_by;
    private String time;

    public Comments() {
    }

    public Comments(String post_by, String time, String comments, String replied) {
        this.post_by = post_by;
        this.time = time;
        this.comments = comments;
        this.replied = replied;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getReplied() {
        return replied;
    }

    public void setReplied(String replied) {
        this.replied = replied;
    }

    public String getPost_by() {
        return post_by;
    }

    public void setPost_by(String post_by) {
        this.post_by = post_by;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
