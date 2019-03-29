package com.caitlynwiley.pettracker;

import java.util.Calendar;

import static com.caitlynwiley.pettracker.Pet.Gender.FEMALE;
import static com.caitlynwiley.pettracker.Pet.Gender.MALE;
import static com.caitlynwiley.pettracker.Pet.Species.CAT;
import static com.caitlynwiley.pettracker.Pet.Species.DOG;

public class Pet {

    private Species species;
    private String breed;
    private String name;
    private Age age;
    private Calendar birthday;
    private Gender gender;
    private String id;

    public Pet() {

    }

    Pet(String name, String years, String months, int gender, int species) {
        this.name = name;
        setAge(years, months);
        setGender(gender);
        setSpecies(species);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecies() {
        return species.name();
    }

    private void setSpecies(int species) {
        switch (species) {
            case R.id.dog_btn:
                this.species = DOG;
                break;
            case R.id.cat_btn:
                this.species = CAT;
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

    public Age getAge() {
        return age;
    }

    private void setAge(String y, String m) {
        int years = Integer.parseInt(y);
        int months = Integer.parseInt(m);
        age = new Age(years, months);
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender.name();
    }

    private void setGender(int gender) {
        this.gender = gender == R.id.male_btn ? MALE : FEMALE;
    }

    enum Gender {
        MALE, FEMALE
    }

    enum Species {
        DOG, CAT
    }

    class Age {
        int years;
        int months;

        public Age(int years, int months) {
            this.years = years;
            this.months = months;
        }

        public int getYears() {
            return years;
        }

        public int getMonths() {
            return months;
        }
    }
}
