package com.uvce.uvceconnect;

public class Hompage_ListItem {
    private String name, logo, content, timesignature, image;
    int type; //0-normal priority, 1-high priority

    public Hompage_ListItem(String logo, String name, String content, String image, String timesignature, int type) {
        this.logo = logo;
        this.name = name;
        this.content = content;
        this.image = image;
        this.timesignature = timesignature;
        this.type = type;
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
}
