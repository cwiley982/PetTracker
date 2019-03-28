package com.caitlynwiley.pettracker;

import java.util.ArrayList;

public class Account {

    private String userId;
    private ArrayList<String> pets;
    private String email;

    public Account(String userId, String email) {
        pets = new ArrayList<>();
        setEmail(email);
        setUserId(this.userId);
    }

    public void addPet(String id) {
        pets.add(id);
    }

    public void removePet(String id) {
        pets.remove(id);
    }

    public ArrayList getPets() {
        return pets;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

