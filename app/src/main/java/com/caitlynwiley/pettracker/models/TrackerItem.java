package com.caitlynwiley.pettracker.models;

public class TrackerItem {
    private String date;
    private String id;
    private String itemType;
    private long utcMillis;

    public TrackerItem(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
