package com.example.ngohub;

public class EventRegister {

    String first_name,middle_name,last_name,gender,contact_no,email_id;

    public EventRegister() {
    }

    public EventRegister(String first_name, String middle_name, String last_name, String gender, String contact_no, String email_id)
    {
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.gender = gender;
        this.contact_no = contact_no;
        this.email_id = email_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }
}

