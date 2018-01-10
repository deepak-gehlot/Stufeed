package com.stufeed.pojo;

/**
 * Created by sowmitras on 28/11/17.
 */

public class LikesAndSave {
    private String time;
    private String likesave;
    private String getKey;
    public LikesAndSave() {
    }

    public LikesAndSave(String time, String likesave, String getKey) {
        this.time = time;
        this.likesave = likesave;
        this.getKey = getKey;
    }

    public String getGetKey() {
        return getKey;
    }

    public void setGetKey(String getKey) {
        this.getKey = getKey;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLikesave() {
        return likesave;
    }

    public void setLikesave(String likesave) {
        this.likesave = likesave;
    }
}
