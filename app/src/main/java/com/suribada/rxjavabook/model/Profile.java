package com.suribada.rxjavabook.model;

public class Profile {

    private String id;
    private String image;

    private String name;
    private String phoneNumber;

    public Profile(String id, String image) {
        this.id = id;
        this.image = image;
    }

    public Profile() {
    }

    public static Profile create(String name, String phoneNumber) {
        Profile profile = new Profile();
        profile.name = name;
        profile.phoneNumber = phoneNumber;
        return profile;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}
