package com.salim.ta3limes.Models.response;

public class StartModel{
    private int id;
    private String content;

    public StartModel(int id, String description) {
        this.id = id;
        this.content = description;
    }
    public String getContent() {
        return content;
    }
}