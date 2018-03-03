package com.agmcs.countdown.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by agmcs on 2015/3/4.
 */
public class Event implements Serializable{
    private int id;
    private String title;
    private  String note;
    private int color;
    private Long date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
