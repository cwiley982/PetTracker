package com.caitlynwiley.pettracker.models;

import android.content.Context;

import com.caitlynwiley.pettracker.R;
import com.google.firebase.database.Exclude;

import java.util.Locale;
import java.util.Objects;

public class Day extends TrackerItem {
    private int day;
    private int month;
    private int year;
    @Exclude
    private Context mContext;

    public Day() {}

    public Day(Context context, String date) {
        setDate(date);
        setContext(context);
        setItemType("day");
    }

    public String getPrettyDate() {
        return String.format(Locale.US, "%s %d", mContext.getResources().getStringArray(R.array.months)[month - 1], day);
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void setDate(String date) {
        super.setDate(date);
        month = Integer.parseInt(date.substring(0, 2).trim());
        day = Integer.parseInt(date.substring(3, 5).trim());
        year = Integer.parseInt(date.substring(6));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Day)) return false;
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
