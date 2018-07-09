package com.caitlynwiley.pettracker;

import android.support.annotation.Nullable;
import android.text.Editable;

import java.util.Calendar;

public class Event {

    private Type type;
    private String title;
    private Calendar start;
    private Calendar end;
    @Nullable
    private String note;

    public Event(Type type, String title, Editable start, Editable end, Editable note) throws IllegalArgumentException {
        this.type = type;
        this.title = title;
        setStart(start);
        setEnd(end);
        this.note = note == null ? null : note.toString();
    }

    public void setStart(Editable start) {
        // TODO
        if (start == null) {
            throw new IllegalArgumentException("Invalid start time.");
        }
    }

    public void setEnd(Editable end) {
        // TODO
        if (end == null) {
            throw new IllegalArgumentException("Invalid end time.");
        }
    }

    private Calendar parseText(String text) {
       // TODO: maybe get rid of
        return null;
    }

    public enum Type {
        WALK, FEED, SLEEP
    }
}
