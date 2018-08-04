package com.uvce.uvceconnect;

public class Hompage_ListItem {
    private String name, logo, content, timesignature, image;

    public Hompage_ListItem(String logo, String name, String content, String image, String timesignature) {
        this.logo = logo;
        this.name = name;
        this.content = content;
        this.image = image;
        this.timesignature = timesignature;
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
}
