package com.example.project.utils;

import androidx.annotation.DrawableRes;
public class Note {
    private String mText;
    @DrawableRes
    private int mIconRes;
    Note(String text, @DrawableRes int res) {
        mText = text;
        mIconRes = res;
    }

    public String getText() {
        return mText;
    }

    public int getIconRes() {
        return mIconRes;
    }
}
