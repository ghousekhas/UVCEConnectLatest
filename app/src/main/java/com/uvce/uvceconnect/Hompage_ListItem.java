package com.uvce.uvceconnect;

public class Hompage_ListItem {
    private String name, logo, content, timesignature, image, admin;
    int type; //0-normal priority, 1-high priority
    int key;

    public Hompage_ListItem(String logo, String name, String content, String image, String timesignature, int type, String admin, int key) {
        this.logo = logo;
        this.name = name;
        this.content = content;
        this.image = image;
        this.timesignature = timesignature;
        this.type = type;
        this.admin = admin;
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }

    public String getTimesignature() {
        return timesignature;
    }

    public int getType() {
        return type;
    }

    public String getAdmin() { return admin; }

    public int getKey() { return key; }
}
