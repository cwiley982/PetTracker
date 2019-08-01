package com.caitlynwiley.pettracker.models;

public class TrackerItem {
    private String date;
    private String id;
    private String itemType;
    private long millis;

    public TrackerItem(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public long getMillis() {
        return millis;
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
}
