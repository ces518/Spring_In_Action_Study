package me.june.spring;

import java.util.Date;

public class Note {
    private Date time = new Date();
    private String text;

    public Note() {
    }

    public Note(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
