package com.stufeed.pojo;

/**
 * Created by sowmitras on 24-10-2017.
 */

public class CreateAccount {

    private String email;
    private String username;
    private String contact;
    private String role;
    private String college;
    private String cg_id;

    public CreateAccount(String email, String username, String contact, String role, String college, String cg_id) {
        this.email = email;
        this.username = username;
        this.contact = contact;
        this.role = role;
        this.college = college;
        this.cg_id = cg_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCg_id() {
        return cg_id;
    }

    public void setCg_id(String cg_id) {
        this.cg_id = cg_id;
    }

    public CreateAccount() {
    }
}

