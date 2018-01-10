package com.stufeed.pojo;

import java.util.Map;

/**
 * Created by sowmitras on 5/11/17.
 */

public class Board {

    private String board_name;
    private String board_description;
    private String board_key;
    private String created_by;
    private String board_privacy;
    private Map<String, Object> post_index;
    private Map<String, Object>  members;
    private String user;
    private String admin;
    private String joined;

    public Board() {
    }

    public Board(String joined) {
        this.joined = joined;
    }

    public Board(String board_name, String board_key) {
        this.board_name = board_name;
        this.board_key = board_key;
    }

    public Board(String board_name, String board_description, String board_key, String created_by, String board_privacy, Map<String, Object> post_index, Map<String, Object> members, String user, String admin, String joined) {
        this.board_name = board_name;
        this.board_description = board_description;
        this.board_key = board_key;
        this.created_by = created_by;
        this.board_privacy = board_privacy;
        this.post_index = post_index;
        this.members = members;
        this.user = user;
        this.admin = admin;
        this.joined = joined;
    }

    public Board(String board_name, String board_description, String board_key, String created_by, String board_privacy, Map<String, Object> post_index, Map<String, Object> members) {
        this.board_name = board_name;
        this.board_description = board_description;
        this.board_key = board_key;
        this.created_by = created_by;
        this.board_privacy = board_privacy;
        this.post_index = post_index;
        this.members = members;
    }


    public Board(String board_name, String board_description, String board_key, String created_by, String board_privacy, Map<String, Object> post_index, Map<String, Object> members, String user, String admin) {
        this.board_name = board_name;
        this.board_description = board_description;
        this.board_key = board_key;
        this.created_by = created_by;
        this.board_privacy = board_privacy;
        this.post_index = post_index;
        this.members = members;
        this.user = user;
        this.admin = admin;
    }


    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBoard_name() {
        return board_name;
    }

    public void setBoard_name(String board_name) {
        this.board_name = board_name;
    }

    public String getBoard_description() {
        return board_description;
    }

    public void setBoard_description(String board_description) {
        this.board_description = board_description;
    }

    public String getBoard_key() {
        return board_key;
    }

    public void setBoard_key(String board_key) {
        this.board_key = board_key;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getBoard_privacy() {
        return board_privacy;
    }

    public void setBoard_privacy(String board_privacy) {
        this.board_privacy = board_privacy;
    }

    public Map<String, Object> getPost_index() {
        return post_index;
    }

    public void setPost_index(Map<String, Object> post_index) {
        this.post_index = post_index;
    }

    public Map<String, Object> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Object> members) {
        this.members = members;
    }
}
