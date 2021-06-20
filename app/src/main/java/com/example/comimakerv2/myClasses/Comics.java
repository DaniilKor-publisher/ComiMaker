package com.example.comimakerv2.myClasses;

import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class Comics {
    ArrayList<PhotoEditorView> frames;

    public Comics(){}

    public Comics(ArrayList<PhotoEditorView> frames){
        this.frames = frames;
    }

    public int getNumberOfFrames(){
        return frames.size();
    }
}

