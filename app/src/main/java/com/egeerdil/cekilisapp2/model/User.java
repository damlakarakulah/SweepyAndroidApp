package com.egeerdil.cekilisapp2.model;

import java.util.List;

public class User {
    private String name;
    private List<Lottery> favList;

    // Getter Methods

    public String getName() {
        return name;
    }
    public List<Lottery> getFavList() {
        return favList;
    }
    // Setter Methods

    public void setName(String username) {
        this.name = name;
    }

    public void setFavList(List<Lottery> favList) {
        this.favList = favList;
    }
}
