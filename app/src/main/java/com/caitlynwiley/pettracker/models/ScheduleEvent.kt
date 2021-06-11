package com.caitlynwiley.pettracker.models

import android.widget.Spinner
import java.util.*


class ScheduleEvent(type: Type, title: String, note: String) {

    lateinit var type: Type
    lateinit var title: String
    private lateinit var start: Calendar
    private lateinit var end: Calendar
    var note: String? = null

    fun setStartTime(hour: Spinner, minute: Spinner) {
        // TODO
    }

    fun setEndTime(hour: Spinner, minute: Spinner) {
        // TODO
    }

    enum class Type {
        WALK, FEED, SLEEP
    }
}