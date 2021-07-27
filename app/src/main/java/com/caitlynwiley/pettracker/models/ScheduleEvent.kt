package com.caitlynwiley.pettracker.models

import android.widget.Spinner

class ScheduleEvent(var type: Type, var title: String, var note: String) {

    fun setStartTime(hour: Spinner, minute: Spinner) {
        // TODO
    }

    fun setEndTime(hour: Spinner, minute: Spinner) {
        // TODO
    }

    enum class Type {
        WALK, FEED, SLEEP, OTHER
    }
}