package com.caitlynwiley.pettracker;

import androidx.annotation.Nullable;
import android.text.Editable;
import android.widget.Spinner;

import java.util.Calendar;

public class ScheduleEvent {

    private Type type;
    private String title;
    private Calendar start;
    private Calendar end;
    @Nullable
    private String note;

    public ScheduleEvent(Type type, String title, Editable note) throws IllegalArgumentException {
        this.type = type;
        this.title = title;
        this.note = note == null ? null : note.toString();
    }

    public void setStartTime(Spinner hour, Spinner minute) {
        // TODO
        if (start == null) {
            throw new IllegalArgumentException("Invalid start time.");
        }
    }

    public void setEndTime(Spinner hour, Spinner minute) {
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
