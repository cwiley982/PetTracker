package com.caitlynwiley.pettracker.models;

import java.util.HashMap;
import java.util.Map;

public class Account {

    private String userId;
    private Map<String, Boolean> pets;
    private String email;

    public Account() {

    }

    public Account(String userId, String email) {
        pets = new HashMap<>();
        setEmail(email);
        setUserId(this.userId);
    }

    public void addPet(String id) {
        pets.put(id, true);
    }

    public void removePet(String id) {
        pets.remove(id);
    }

    public Map<String, Boolean> getPets() {
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

