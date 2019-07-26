package com.caitlynwiley.pettracker.models;

import java.util.ArrayList;
import java.util.List;

public class Day {
    private List<TrackerEvent> events;
    private String date;
    private int day;
    private int month;
    private int year;
    private String id;

    public Day(String date) {
        setDate(date);
        events = new ArrayList<>();
    }

    public List<TrackerEvent> getEvents() {
        return events;
    }

    public void addEvent(TrackerEvent e) {
        events.add(e);
    }

    public Day setEvents(List<TrackerEvent> events) {
        this.events = events;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Day setDate(String date) {
        this.date = date;
        month = Integer.parseInt(date.substring(0, 2));
        day = Integer.parseInt(date.substring(3, 5));
        year = Integer.parseInt(date.substring(6));
        return this;
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
}
