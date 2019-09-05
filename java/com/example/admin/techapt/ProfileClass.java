package com.example.admin.techapt;

public class ProfileClass {
    String first_name,last_name,phone_no;
    String email,uid;

    public ProfileClass(String first_name, String last_name, String phone_no, String email, String uid) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_no = phone_no;
        this.email = email;
        this.uid = uid;
    }

    public void ProfileClassfirst_name(String first_name,String uid) {

        this.first_name = first_name;
        this.uid = uid;
    }

    public ProfileClass(String last_name, String uid,int a) {
        this.last_name = last_name;
        this.uid = uid;
    }

    public ProfileClass(String phone_no, String uid,float a) {
        this.phone_no = phone_no;
        this.uid = uid;
    }

    public ProfileClass(String first_name, String uid) {
        this.first_name = first_name;
        this.uid = uid;
    }

    public void ProfileClassphone_no(String phone_no) {

        this.phone_no = phone_no;
    }

    public void ProfileClasslast_name(String last_name) {

        this.last_name = last_name;
    }

    public ProfileClass() {

    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public void setFirst_name(String first_name) {

        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
