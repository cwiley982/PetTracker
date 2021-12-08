package com.caitlynwiley.pettracker.models

import android.annotation.TargetApi
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Today
import androidx.compose.ui.graphics.vector.ImageVector
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.view.icons.CustomIcons
import com.google.firebase.database.Exclude
import com.google.gson.annotations.SerializedName
import java.util.*

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

    fun setDate(c: Calendar) {
        this.date = date
        year = c[Calendar.YEAR]
        month = c[Calendar.MONTH] + 1
        day = c[Calendar.DAY_OF_MONTH]
    }

    fun setUtcMillis(utcMillis: Long) {
        this.utcMillis = utcMillis
        val c = Calendar.getInstance()
        c.timeInMillis = utcMillis
        setLocalTime(c)
        setDate(c)
    }

    @Exclude
    @TargetApi(24)
    fun setLocalTime(c: Calendar) {
        localTime = String.format(Locale.US, "%d:%02d %s",
            if (c[Calendar.HOUR] == 0) 12 else c[Calendar.HOUR],
            c[Calendar.MINUTE],
            if (c[Calendar.AM_PM] == Calendar.AM) "am" else "pm")
    }

    @get:Exclude
    val drawableResId: ImageVector
        get() {
            if (itemType.equals("day", ignoreCase = true)) return Icons.Outlined.Today
            return when (eventType) {
                EventType.POTTY -> CustomIcons.DogPoop
                EventType.FEED -> CustomIcons.DogBowl
                EventType.WALK -> CustomIcons.DogWalk
                else -> Icons.Outlined.Menu // default for now
            }
        }

    fun setWalkLength(hours: Int, minutes: Int) {
        walkLength = WalkLength(hours, minutes)
    }

    fun getDisplayDate(context: Context): String {
        if (prettyDate.isEmpty()) {
            prettyDate = String.format(Locale.US, "%s %s",
                context.resources.getStringArray(R.array.months)[month - 1],
                context.resources.getStringArray(R.array.formatted_day_of_month)[day - 1])
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