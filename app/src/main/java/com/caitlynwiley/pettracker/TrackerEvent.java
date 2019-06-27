package com.caitlynwiley.pettracker;

public class TrackerEvent {

    private EventType type;
    private String time;
    private String date;
    private String title;
    private String note;

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
        return R.drawable.ic_clock; // default for now
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
}
