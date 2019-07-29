package com.caitlynwiley.pettracker.models;

import java.util.Calendar;
import java.util.Locale;

public class Pet {

    private String species;
    private String breed;
    private String name;
    private double age;
    private String birthday;
    private String gender;
    private String id;

    public Pet() {
    }

    public Pet(String name, String years, String months, String gender, String species) {
        this.name = name;
        setAge(years, months);
        setGender(gender);
        setSpecies(species);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecies() {
        return species;
    }

    private void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAge() {
        return age;
    }

    private void setAge(String y, String m) {
        double years = Integer.parseInt(y);
        double months = Integer.parseInt(m);
        age = years + (months / 12.0);
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar c) {
        this.birthday = String.format(Locale.US, "%2d/%2d/%4d", c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
    }

    public String getGender() {
        return gender;
    }

    private void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pet) {
            Pet other = (Pet) obj;
            return other.getId().equals(this.id);
        } else {
            return false;
        }
    }
}
