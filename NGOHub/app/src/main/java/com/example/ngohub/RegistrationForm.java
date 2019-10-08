package com.example.ngohub;

public class RegistrationForm {
    String first_name,middle_name,last_name,gender,contact_no,email_id;

    public RegistrationForm() {
    }

    public RegistrationForm(String first_name, String middle_name, String last_name, String gender, String contact_no, String email_id) {
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.gender = gender;
        this.contact_no = contact_no;
        this.email_id = email_id;
    }

}
