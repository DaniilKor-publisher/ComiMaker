package com.example.comimakerv2.myClasses;

public class MyAdjusts {
    public String name;
    public int drawableId;

    public MyAdjusts(String title, int imageLink) {
        this.name = title;
        this.drawableId = imageLink;
    }

    public MyAdjusts() {}

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int imageLink) {
        this.drawableId = imageLink;
    }
}
