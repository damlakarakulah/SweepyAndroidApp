package com.egeerdil.cekilisapp2.model;

import java.util.List;

public class User {
    private String username;
    private String password;
    private String email;
    private List<Lottery> favList;

    // Getter Methods

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Lottery> getFavList() {
        return favList;
    }
    // Setter Methods

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFavList(List<Lottery> favList) {
        this.favList = favList;
    }
}
