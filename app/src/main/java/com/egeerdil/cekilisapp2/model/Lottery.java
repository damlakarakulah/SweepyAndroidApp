package com.egeerdil.cekilisapp2.model;

public class Lottery {
    private String _id;
    private String name;
    private String category;
    private String link;
    private String photo_link;
    private float __v;
    private boolean isFaved;


    // Getter Methods

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getLink() {
        return link;
    }

    public String getPhoto_link() {
        return photo_link;
    }

    public float get__v() {
        return __v;
    }

    public boolean getIsFaved() {
        return isFaved;
    }

    // Setter Methods

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPhoto_link(String photo_link) {
        this.photo_link = photo_link;
    }

    public void set__v(float __v) {
        this.__v = __v;
    }

    public void setIsFaved(boolean isFaved) {
        this.isFaved = isFaved;
    }
}