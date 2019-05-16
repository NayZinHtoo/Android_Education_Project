package com.example.scrollex.model;

public class Subject {
    String name,text;

    public Subject(String name, String text) {
        this.name = name;
        this.text = text;
    }
    public Subject(String word){
        this.text=word;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
