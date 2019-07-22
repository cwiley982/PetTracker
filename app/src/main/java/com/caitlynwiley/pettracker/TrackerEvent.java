package com.caitlynwiley.pettracker;

import java.util.Objects;

import androidx.annotation.Nullable;

public class TrackerEvent {

    private EventType type;
    private String time;
    private String date;
    private String title;
    private String note;
    private String id;
    private String petId;

    @SuppressWarnings("unused")
    public TrackerEvent() {

    }

    public TrackerEvent(String when, EventType type, String title, String note) {
        // when has format mm/dd/yyyy hh:mm am/pm
        setWhen(when);
        this.type = type;
        setTitle(title);
        setNote(note);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getPetId() {
        return petId;
    }

    private void setWhen(String when) {
        date = when.substring(0, 10);
        time = when.substring(11);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getDrawableResId() {
        switch (type) {
            case POOP:
                return R.drawable.ic_dog_poop_64dp;
            case FEED:
                return R.drawable.ic_dog_bowl_64dp;
            case WALK:
                return R.drawable.ic_dog_walk_64dp;
        }
        return R.drawable.ic_clock_black_24dp; // default for now
    }

    public EventType getType() {
        return type;
    }

    private void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    public String getTitle() {
        return title;
    }

    private void setNote(String note) {
        this.note = note == null ? "" : note;
    }

    public String getNote() {
        return note;
    }

    public enum EventType {
        POOP,
        WALK,
        FEED
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, time, date, title, note, id, petId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackerEvent that = (TrackerEvent) o;
        return type == that.type &&
                Objects.equals(time, that.time) &&
                Objects.equals(date, that.date) &&
                Objects.equals(title, that.title) &&
                Objects.equals(note, that.note) &&
                Objects.equals(id, that.id) &&
                Objects.equals(petId, that.petId);
    }
}
