package com.caitlynwiley.pettracker.models;

import android.annotation.TargetApi;
import android.content.Context;

import com.caitlynwiley.pettracker.R;
import com.google.firebase.database.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Locale;

public class TrackerItem {
    @SerializedName("date")
    private String date;

    @Exclude
    private String localTime;

    @SerializedName("item_id")
    private String itemId;

    @SerializedName("item_type")
    private String itemType;

    @SerializedName("month")
    private int month;

    @SerializedName("day")
    private int day;

    @SerializedName("year")
    private int year;

    @SerializedName("utcmillis")
    private long utcMillis;

    @SerializedName("type")
    private EventType type;

    @SerializedName("walk_length")
    private WalkLength walkLength;

    @SerializedName("pet_id")
    private String petId;

    @SerializedName("cups_food")
    private double cupsFood;

    @SerializedName("number_1")
    private boolean number1;

    @SerializedName("number_2")
    private boolean number2;

    @SerializedName("pretty_date")
    private String prettyDate;

    @Exclude
    private Context mContext;

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
        setLocalTime(utcMillis);
    }

    @Exclude
    @TargetApi(24)
    private void setLocalTime(long utcMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(utcMillis);
        localTime = String.format(Locale.US, "%d:%02d %s", c.get(Calendar.HOUR) == 0 ? 12 : c.get(Calendar.HOUR), c.get(Calendar.MINUTE),
                c.get(Calendar.AM_PM) == Calendar.AM? "am" : "pm");
    }

    public long getUtcMillis() {
        return utcMillis;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getPetId() {
        return petId;
    }

    public String getLocalTime() {
        return localTime;
    }

    @Exclude
    public int getDrawableResId() {
        if (itemType.equalsIgnoreCase("day")) return -1;
        switch (type) {
            case POTTY:
                return R.drawable.ic_dog_poop_64dp;
            case FEED:
                return R.drawable.ic_dog_bowl_64dp;
            case WALK:
                return R.drawable.ic_dog_walk_64dp;
        }
        return R.drawable.ic_clock_black_24dp; // default for now
    }

    @Exclude
    public EventType getTypeAsEnum(){
        return type;
    }

    // these methods are a Firebase 9.0.0 hack to handle the enum
    public String getType(){
        if (type == null){
            return null;
        } else {
            return type.name();
        }
    }

    public void setType(String type){
        if (type == null){
            this.type = null;
        } else {
            this.type = EventType.valueOf(type);
        }
    }

    public double getCupsFood() {
        return cupsFood;
    }

    public void setCupsFood(double cupsFood) {
        this.cupsFood = cupsFood;
    }

    public boolean isNumber1() {
        return number1;
    }

    public void setNumber1(boolean number1) {
        this.number1 = number1;
    }

    public boolean isNumber2() {
        return number2;
    }

    public void setNumber2(boolean number2) {
        this.number2 = number2;
    }

    public void setWalkLength(int hours, int minutes) {
        this.walkLength = new WalkLength(hours, minutes);
    }

    public void setPrettyDate(String prettyDate) {
        this.prettyDate = prettyDate;
    }

    public String getPrettyDate() {
        if (prettyDate == null) {
            prettyDate = String.format(Locale.US, "%s %d", mContext.getResources().getStringArray(R.array.months)[getMonth() - 1], getDay());
        }
        return prettyDate;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public enum EventType {
        @SerializedName("POTTY")
        POTTY,
        @SerializedName("WALK")
        WALK,
        @SerializedName("FEED")
        FEED
    }

    public static class Builder {

        private TrackerItem item;

        public Builder() {
            item = new TrackerItem();
        }

        // ... (setters)
        public Builder setDate(String date) {
            item.setDate(date);
            return this;
        }

        public Builder setMillis(long millis) {
            item.setUtcMillis(millis);
            return this;
        }

        public Builder setId(String id) {
            item.setItemId(id);
            return this;
        }

        public Builder setPetId(String id) {
            item.setPetId(id);
            return this;
        }

        public Builder setCupsFood(double cups) {
            item.setCupsFood(cups);
            return this;
        }

        public Builder setNumber1(boolean went) {
            item.setNumber1(went);
            return this;
        }

        public Builder setNumber2(boolean went) {
            item.setNumber2(went);
            return this;
        }

        public Builder setWalkLength(int hours, int minutes) {
            item.setWalkLength(hours, minutes);
            return this;
        }

        public Builder setItemType(String type) {
            item.setItemType(type);
            return this;
        }

        public Builder setType(EventType type) {
            item.setType(type.name());
            return this;
        }

        public Builder setContext(Context context) {
            item.setContext(context);
            return this;
        }

        public TrackerItem build() {
            return item;
        }
    }
}
