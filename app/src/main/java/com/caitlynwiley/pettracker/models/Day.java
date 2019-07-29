package com.caitlynwiley.pettracker.models;

import android.content.Context;

import com.caitlynwiley.pettracker.R;
import com.google.firebase.database.Exclude;

import java.util.Locale;
import java.util.Objects;

public class Day {
    private String date;
    private int day;
    private int month;
    private int year;
    private String id;
    @Exclude
    private Context mContext;

    public Day(Context context, String date) {
        setDate(date);
        mContext = context;
    }

    public String getDate() {
        return date;
    }

    public Day setDate(String date) {
        this.date = date;
        month = Integer.parseInt(date.substring(0, 2).trim());
        day = Integer.parseInt(date.substring(3, 5).trim());
        year = Integer.parseInt(date.substring(6));
        return this;
    }

    public String getPrettyDate() {
        return String.format(Locale.US, "%s %d", mContext.getResources().getStringArray(R.array.months)[month - 1], day);
    }

    public int getDay() {
        return day;
    }

    public Day setDay(int day) {
        this.day = day;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public Day setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getYear() {
        return year;
    }

    public Day setYear(int year) {
        this.year = year;
        return this;
    }

    public String getId() {
        return id;
    }

    public Day setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day1 = (Day) o;
        return getDay() == day1.getDay() &&
                getMonth() == day1.getMonth() &&
                getYear() == day1.getYear();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getDay(), getMonth(), getYear());
    }
}
