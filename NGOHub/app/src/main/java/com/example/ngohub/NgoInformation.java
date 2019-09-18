package com.example.ngohub;

public class NgoInformation {
    public String Name,Unique_id,contact_no,email_id;

    public NgoInformation() {
    }

    public NgoInformation(String name, String unique_id, String contact_no,String email_id) {
        Name = name;
        Unique_id = unique_id;
        this.contact_no = contact_no;
        this.email_id=email_id;
    }
}
