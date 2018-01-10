package com.stufeed.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sowmitras on 26/11/17.
 */

public class FeedPost {
    private String post_by;
    private String post_comments;
    private String post_time;
    private String post_text;
    private String post_privacy;
    private String post_media_link;
    private String post_type;
    private String post_reference_key;

    private String likesave;
    private String time;
    private String getKey;

    public Map<String , Comments> comments;





    public FeedPost() {
    }



    public FeedPost(String post_by, String post_comments, String post_time, String post_text, String post_privacy, String post_media_link, String post_type, String post_reference_key, String likesave, String time, String getKey,Map<String, Comments> comments) {
        this.post_by = post_by;
        this.post_comments = post_comments;
        this.post_time = post_time;
        this.post_text = post_text;
        this.post_privacy = post_privacy;
        this.post_media_link = post_media_link;
        this.post_type = post_type;
        this.post_reference_key = post_reference_key;
        this.likesave = likesave;
        this.time = time;
        this.getKey = getKey;
        this.comments = comments;
    }

    public FeedPost(String post_by, String post_comments, String post_time, String post_text, String post_privacy, String post_media_link, String post_type, String post_reference_key,Map<String, Comments> comments) {
        this.post_by = post_by;
        this.post_comments = post_comments;
        this.post_time = post_time;
        this.post_text = post_text;
        this.post_privacy = post_privacy;
        this.post_media_link = post_media_link;
        this.post_type = post_type;
        this.post_reference_key = post_reference_key;
        this.comments = comments;
    }


    public Map<String, Comments> getComments() {
        return comments;
    }

    public void setComments(Map<String, Comments> comments) {
        this.comments = comments;
    }

    public String getGetKey() {
        return getKey;
    }

    public void setGetKey(String getKey) {
        this.getKey = getKey;
    }

    public String getLikesave() {
        return likesave;
    }

    public void setLikesave(String likesave) {
        this.likesave = likesave;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPost_by() {
        return post_by;
    }

    public void setPost_by(String post_by) {
        this.post_by = post_by;
    }

    public String getPost_comments() {
        return post_comments;
    }

    public void setPost_comments(String post_comments) {
        this.post_comments = post_comments;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getPost_privacy() {
        return post_privacy;
    }

    public void setPost_privacy(String post_privacy) {
        this.post_privacy = post_privacy;
    }

    public String getPost_media_link() {
        return post_media_link;
    }

    public void setPost_media_link(String post_media_link) {
        this.post_media_link = post_media_link;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getPost_reference_key() {
        return post_reference_key;
    }

    public void setPost_reference_key(String post_reference_key) {
        this.post_reference_key = post_reference_key;
    }
}
