package com.uvce.uvceconnect;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Homepage_ListItem {
    private String name, logo, content, timesignature, image, admin,link,filename;
    int type; //0-normal priority, 1-high priority
    int key;


    public Homepage_ListItem(String logo, String name, String content, String image, String timesignature, int type, String admin, int key, String link, String filename) {
        this.logo = logo;
        this.name = name;
        this.content = content;
        this.image = image;
        this.timesignature = timesignature;
        this.type = type;
        this.admin = admin;
        this.key = key;
        this.link=link;
        this.filename=filename;

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

    public String getLink(){return link;}
    public  String getFilename(){return filename; }


}
