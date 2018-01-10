package com.stufeed.pojo;

/**
 * Created by sowmitras on 29/11/17.
 */

public class Follow_Follower {
    private String time;
    private String userName;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Follow_Follower() {
    }

    public Follow_Follower(String time, String userName) {
        this.time = time;
        this.userName = userName;
    }
}
