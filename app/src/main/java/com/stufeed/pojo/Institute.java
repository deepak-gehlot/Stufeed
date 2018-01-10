package com.stufeed.pojo;

/**
 * Created by sowmitras on 20/12/17.
 */

public class Institute {
   String address;
   String affiliation_no ;
   String college_name;
   String college_type;
   String district;
   String location;
   String management;
   String specialised_in;
   String state;
   String university_name;
   String university_type;
   String upload_year;
   String website;
   String year_of_establishment;

    public Institute(String address, String affiliation_no, String college_name, String college_type, String district, String location, String management, String specialised_in, String state, String university_name, String university_type, String upload_year, String website, String year_of_establishment) {
        this.address = address;
        this.affiliation_no = affiliation_no;
        this.college_name = college_name;
        this.college_type = college_type;
        this.district = district;
        this.location = location;
        this.management = management;
        this.specialised_in = specialised_in;
        this.state = state;
        this.university_name = university_name;
        this.university_type = university_type;
        this.upload_year = upload_year;
        this.website = website;
        this.year_of_establishment = year_of_establishment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAffiliation_no() {
        return affiliation_no;
    }

    public void setAffiliation_no(String affiliation_no) {
        this.affiliation_no = affiliation_no;
    }

    public String getCollege_name() {
        return college_name;
    }

    public void setCollege_name(String college_name) {
        this.college_name = college_name;
    }

    public String getCollege_type() {
        return college_type;
    }

    public void setCollege_type(String college_type) {
        this.college_type = college_type;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getManagement() {
        return management;
    }

    public void setManagement(String management) {
        this.management = management;
    }

    public String getSpecialised_in() {
        return specialised_in;
    }

    public void setSpecialised_in(String specialised_in) {
        this.specialised_in = specialised_in;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }

    public String getUniversity_type() {
        return university_type;
    }

    public void setUniversity_type(String university_type) {
        this.university_type = university_type;
    }

    public String getUpload_year() {
        return upload_year;
    }

    public void setUpload_year(String upload_year) {
        this.upload_year = upload_year;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getYear_of_establishment() {
        return year_of_establishment;
    }

    public void setYear_of_establishment(String year_of_establishment) {
        this.year_of_establishment = year_of_establishment;
    }
}
