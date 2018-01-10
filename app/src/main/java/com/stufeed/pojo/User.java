package com.stufeed.pojo;

/**
 * Created by sowmitras on 24-10-2017.
 */

public class User {




    /**
     * @params user_id;
     *
     * @params userimage;
     *
     * @params name;
     *
     * @params program;
     *
     * @params branch;
     *
     * @params graduationYear;
     *
     * @params college;
     *
     * @params aboutStatus;
     *
     * @params gender;
     *
     * @params contact;
     *
     * @params email;
     *
     * @params birthday;
     *
     * @params role;
     *
     * @params search;
     *
     * @params degree;
     *
     * @params designation;
     *
     * @params department;
     */


    private String accountkey;
    private String user_id;
    private String userimage;
    private String name;
    private String program;
    private String branch;
    private String graduationYear;
    private String college;
    private String aboutStatus;
    private String gender;
    private String contact;
    private String email;
    private String birthday;
    private String role;
    private String search;
    private String degree;
    private String designation;
    private String department;
    private String faculty;
    private String request;
    private String userUI;
    private int mainData;


    public User() {
    }


    public User(String accountkey, String userimage, String name,
                String program, String branch,
                String graduationYear, String college,
                String aboutStatus, String gender,
                String contact, String email,
                String birthday, String role,
                String search, String degree,
                String designation, String department, String faculty, String request,int main) {
        this.accountkey = accountkey;
        this.aboutStatus = aboutStatus;
        this.birthday = birthday;
        this.branch = branch;
        this.college = college;
        this.contact = contact;
        this.degree = degree;
        this.department = department;
        this.designation = designation;
        this.email = email;
        this.faculty = faculty;
        this.gender = gender;
        this.graduationYear = graduationYear;
        this.name = name;
        this.program = program;
        this.request = request;
        this.role = role;
        this.search = search;
        this.userimage = userimage;
      }


    public User(String accountkey, String user_id, String userimage,
                String name, String program,
                String branch, String graduationYear,
                String college, String aboutStatus,
                String gender, String contact,
                String email, String birthday,
                String role, String search,
                String degree, String designation,
                String department,String faculty) {
        this.accountkey = accountkey;
        this.user_id = user_id;
        this.userimage = userimage;
        this.name = name;
        this.program = program;
        this.branch = branch;
        this.graduationYear = graduationYear;
        this.college = college;
        this.aboutStatus = aboutStatus;
        this.gender = gender;
        this.contact = contact;
        this.email = email;
        this.birthday = birthday;
        this.role = role;
        this.search = search;
        this.degree = degree;
        this.designation = designation;
        this.department = department;
        this.faculty = faculty;
    }

    public User(String accountkey, String user_id, String userimage, String name, String program, String branch, String graduationYear, String college, String aboutStatus, String gender, String contact, String email, String birthday, String role, String search, String degree, String designation, String department, String faculty, String request, String userUI, int mainData) {
        this.accountkey = accountkey;
        this.user_id = user_id;
        this.userimage = userimage;
        this.name = name;
        this.program = program;
        this.branch = branch;
        this.graduationYear = graduationYear;
        this.college = college;
        this.aboutStatus = aboutStatus;
        this.gender = gender;
        this.contact = contact;
        this.email = email;
        this.birthday = birthday;
        this.role = role;
        this.search = search;
        this.degree = degree;
        this.designation = designation;
        this.department = department;
        this.faculty = faculty;
        this.request = request;
        this.userUI = userUI;
        this.mainData = mainData;
    }


    public User(String user_id) {
        this.user_id = user_id;
    }

    public String getUserUI() {
        return userUI;
    }

    public void setUserUI(String userUI) {
        this.userUI = userUI;
    }

    public int getMainData() {
        return mainData;
    }

    public void setMainData(int mainData) {
        this.mainData = mainData;
    }

    public String getAccountkey() {
        return accountkey;
    }

    public void setAccountkey(String accountkey) {
        this.accountkey = accountkey;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(String graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getAboutStatus() {
        return aboutStatus;
    }

    public void setAboutStatus(String aboutStatus) {
        this.aboutStatus = aboutStatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

}