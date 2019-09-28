package com.example.ngohub;

public class NGO_EventPosts {

    private String Caption;
    private String Title;
    private String Venue;
    private String ImageAddress;
    private String NgoInformation_UUID;


    public NGO_EventPosts() {
    }

    //*******************Constructor*********************

    public NGO_EventPosts(String caption, String title, String venue, String imageAddress, String ngoInformation_UUID) {
        Caption = caption;
        Title = title;
        Venue = venue;
        ImageAddress = imageAddress;
        NgoInformation_UUID = ngoInformation_UUID;
    }
    //*******************Getters & Setters*********************
    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getImageAddress() {
        return ImageAddress;
    }

    public void setImageAddress(String imageAddress) {
        ImageAddress = imageAddress;
    }

    public String getNgoInformation_UUID() {
        return NgoInformation_UUID;
    }

    public void setNgoInformation_UUID(String ngoInformation_UUID) {
        NgoInformation_UUID = ngoInformation_UUID;
    }
}