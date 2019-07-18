package com.example.imdb.Model;

import android.view.Display;

import java.util.ArrayList;
import java.util.List;

public class ParentModel {
    private String title;
    private List<Model> models;

    public enum Type{
        Movie,
        Celebrity,
        TVShow,
    }
    public Type type;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public List<Model> getModels() {
        return models;
    }

}
