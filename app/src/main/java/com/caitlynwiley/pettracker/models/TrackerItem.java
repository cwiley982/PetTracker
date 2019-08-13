package com.caitlynwiley.pettracker.models;

public class TrackerItem {
    private String date;
    private String id;
    private String itemType;
    private int month;
    private int day;
    private int year;
    private long utcMillis;

    public TrackerItem(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        month = Integer.parseInt(date.substring(0, 2).trim());
        day = Integer.parseInt(date.substring(3, 5).trim());
        year = Integer.parseInt(date.substring(6));
    }

    public void setUtcMillis(long utcMillis) {
        this.utcMillis = utcMillis;
    }

    public long getUtcMillis() {
        return utcMillis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
