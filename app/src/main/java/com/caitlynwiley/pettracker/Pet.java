package com.caitlynwiley.pettracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Pet {

    private String species;
    private String breed;
    private String name;
    private double age;
    private String birthday;
    private String gender;
    private String id;
    private List<ScheduleEvent> events;

    public Pet() {

    }

    Pet(String name, String years, String months, int gender, int species) {
        this.name = name;
        setAge(years, months);
        setGender(gender);
        setSpecies(species);
        events = new ArrayList<>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecies() {
        return species;
    }

    private void setSpecies(int species) {
        switch (species) {
            case R.id.dog_btn:
                this.species = "dog";
                break;
            case R.id.cat_btn:
                this.species = "cat";
                break;
        }
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

    private void setGender(int gender) {
        this.gender = gender == R.id.male_btn ? "male" : "female";
    }

    private List<ScheduleEvent> getEvents() {
        return events;
    }

    public void addEvent(ScheduleEvent e) {
        events.add(e);
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
