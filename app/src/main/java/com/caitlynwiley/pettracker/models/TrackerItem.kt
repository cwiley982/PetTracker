package com.caitlynwiley.pettracker.models

import android.annotation.TargetApi
import android.content.Context
import com.caitlynwiley.pettracker.R
import com.google.firebase.database.Exclude
import com.google.gson.annotations.SerializedName
import java.util.*
import java.util.Calendar.*

class TrackerItem {
    @SerializedName("date")
    var date: String? = null
        private set

    @Exclude
    @Transient
    var localTime: String? = null
        private set

    @SerializedName("item_id")
    var itemId: String = ""

    @SerializedName("item_type")
    var itemType: String = ""

    @SerializedName("month")
    var month = 0

    @SerializedName("day")
    var day = 0

    @SerializedName("year")
    var year = 0

    @SerializedName("utcmillis")
    private var utcMillis: Long = 0

    @SerializedName("event_type")
    var eventType: EventType? = null

    @SerializedName("walk_length")
    private var walkLength: WalkLength? = null

    @SerializedName("pet_id")
    var petId: String = ""

    @SerializedName("cups_food")
    var cupsFood = 0.0

    @SerializedName("number_1")
    var isNumber1 = false

    @SerializedName("number_2")
    var isNumber2 = false

    @SerializedName("pretty_date")
    private var prettyDate: String = ""

    fun setDate(date: String) {
        this.date = date
        month = date.substring(0, 2).trim { it <= ' ' }.toInt()
        day = date.substring(3, 5).trim { it <= ' ' }.toInt()
        year = date.substring(6).toInt()
    }

    fun setUtcMillis(utcMillis: Long) {
        this.utcMillis = utcMillis
        setLocalTime()
    }

    @Exclude
    @TargetApi(24)
    fun setLocalTime() {
        val c = getInstance()
        c.timeInMillis = utcMillis
        localTime = String.format(Locale.US, "%d:%02d %s", if (c[HOUR] == 0) 12 else c[HOUR],
                c[MINUTE], if (c[AM_PM] == AM) "am" else "pm")
    }

    @get:Exclude
    val drawableResId: Int
        get() {
            if (itemType.equals("day", ignoreCase = true)) return -1
            when (eventType) {
                EventType.POTTY -> return R.drawable.ic_dog_poop_64dp
                EventType.FEED -> return R.drawable.ic_dog_bowl_64dp
                EventType.WALK -> return R.drawable.ic_dog_walk_64dp
            }
            return R.drawable.ic_clock_black_24dp // default for now
        }

    fun setWalkLength(hours: Int, minutes: Int) {
        walkLength = WalkLength(hours, minutes)
    }

    fun getPrettyDate(context: Context): String {
        if (prettyDate.isEmpty()) {
            prettyDate = String.format(Locale.US, "%s %d", context.resources.getStringArray(R.array.months)[month - 1], day)
        }
        return prettyDate
    }

    enum class EventType {
        @SerializedName("potty") POTTY,
        @SerializedName("walk") WALK,
        @SerializedName("feed") FEED
    }

    class Builder {
        private val item: TrackerItem = TrackerItem()

        // ... (setters)
        fun setDate(date: String): Builder {
            item.setDate(date)
            return this
        }

        fun setMillis(millis: Long): Builder {
            item.setUtcMillis(millis)
            return this
        }

        fun setId(id: String?): Builder {
            item.itemId = id ?: ""
            return this
        }

        fun setPetId(id: String?): Builder {
            item.petId = id ?: ""
            return this
        }

        fun setCupsFood(cups: Double): Builder {
            item.cupsFood = cups
            return this
        }

        fun setNumber1(went: Boolean): Builder {
            item.isNumber1 = went
            return this
        }

        fun setNumber2(went: Boolean): Builder {
            item.isNumber2 = went
            return this
        }

        fun setWalkLength(hours: Int, minutes: Int): Builder {
            item.setWalkLength(hours, minutes)
            return this
        }

        fun setItemType(type: String): Builder {
            item.itemType = type
            return this
        }

        fun setEventType(type: EventType): Builder {
            item.eventType = type
            return this
        }

        fun build(): TrackerItem {
            return item
        }

    }
}