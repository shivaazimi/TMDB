package com.example.imdb.Model;

import java.util.ArrayList;
import java.util.List;

public class ParentModel {
    private String title;
    private ArrayList<Model> models;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setModels(ArrayList<Model> models) {
        this.models = models;
    }

    public ArrayList<Model> getModels() {
        return models;
    }

}
