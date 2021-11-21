package com.example.indianludobattle.Model;

public class MainModel {
    private String tab_name;
    private String label;
    private String image;
    private int count;

    public MainModel(String tab_name, String label, String image, int count)
    {
        this.tab_name = tab_name;
        this.count = count;
        this.image = image;
        this.label = label;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCount() {
        return count;
    }

    public void setTab_name(String tab_name) {
        this.tab_name = tab_name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTab_name() {
        return tab_name;
    }

    public String getLabel() {
        return label;
    }
}
