package com.caitlynwiley.pettracker.models;

import android.content.Context;

import com.caitlynwiley.pettracker.R;
import com.google.firebase.database.Exclude;

import java.util.Locale;
import java.util.Objects;

public class Day extends TrackerItem {
    @Exclude
    private Context mContext;
    private String prettyDate;

    public Day() {}

    public Day(Context context, String date) {
        setDate(date);
        setContext(context);
        setItemType("day");
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
