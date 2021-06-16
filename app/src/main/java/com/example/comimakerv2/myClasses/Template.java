package com.example.comimakerv2.myClasses;

import java.io.Serializable;

public class Template {
    public String title;
    public String imageLink;
    public String category;

    public Template(String title, String imageLink) {
        this.title = title;
        this.imageLink = imageLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Template(String title, String imageLink, String category) {
        this.title = title;
        this.imageLink = imageLink;
        this.category = category;
    }

    public Template() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}

