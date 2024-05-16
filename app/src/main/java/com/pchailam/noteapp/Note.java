package com.pchailam.noteapp;

public class Note {
    private int id;
    private String title;
    private String content;
    private String date;
    private int id_type;

    public Note(int id, String title, String content, String date,int id_type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.id_type=id_type;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }
}
