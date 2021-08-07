package com.caitlynwiley.pettracker.views.fragments

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.models.TrackerItem
import com.caitlynwiley.pettracker.theme.PetTrackerTheme
import java.util.*
import kotlin.collections.ArrayList

val defaultEvent: TrackerItem = TrackerItem.Builder()
    .setEventType(TrackerItem.EventType.FEED)
    .setCupsFood(2.0)
    .setMillis(System.currentTimeMillis())
    .setItemType("event")
    .setId("789")
    .setPetId("456")
    .build()
val defaultDay: TrackerItem = TrackerItem.Builder()
    .setItemType("day")
    .setMillis(System.currentTimeMillis())
    .setId("000")
    .setPetId("456")
    .build()
val defaultTrackerItems: ArrayList<TrackerItem> = ArrayList(listOf(defaultDay, defaultEvent, defaultEvent))

@ExperimentalAnimationApi
@Composable
fun TrackerScreen(items: List<TrackerItem>) {
    var dialogType by remember { mutableStateOf<TrackerItem.EventType?>(null) }
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val fabGroup = createRef()

        // if dialog type is set, show that type's dialog, otherwise it should be hidden
        dialogType?.let {
            TrackerDialog(onDismiss = {dialogType = null}) {
                when (dialogType) {
                    TrackerItem.EventType.FEED -> TrackMeal()
                    TrackerItem.EventType.POTTY -> TrackPotty()
                    TrackerItem.EventType.WALK -> TrackWalk()
                }
            }
        }

        if (items.isEmpty()) {
            val label = createRef()
            NoEventsLabel(modifier = Modifier.constrainAs(label) {
                this.centerTo(parent)
            })
        } else {
            EventList(list = items)
        }
        FabGroup(modifier = Modifier.constrainAs(fabGroup) {
            end.linkTo(parent.end, 8.dp)
            bottom.linkTo(parent.bottom, 8.dp)
        }, showDialog = fun (type: TrackerItem.EventType) {
            Log.d("TrackerScreen", "FAB clicked to track $type event")
            dialogType = type
        })
    }
}

@ExperimentalAnimationApi
@Composable
fun FabGroup(modifier: Modifier = Modifier, showDialog: (TrackerItem.EventType) -> Unit) {
//    var transition = updateTransition(targetState = groupOpenState, label = "toggleTrackerFabGroup")

    ConstraintLayout(modifier = modifier.wrapContentSize(align = Alignment.BottomEnd)) {
        var groupOpenState by remember { mutableStateOf(FabState.CLOSED) }
        val (fab, smallFabs) = createRefs()

        val angle: Float by animateFloatAsState(
            targetValue = if (groupOpenState == FabState.CLOSED) 135f else 0f,
            animationSpec = tween(
                durationMillis = 2000, // duration
                easing = FastOutSlowInEasing
            ),
            finishedListener = {
                groupOpenState = if (groupOpenState == FabState.OPEN) FabState.CLOSED else FabState.OPEN
            }
        )

        FloatingActionButton(
            onClick = {
                groupOpenState = if (groupOpenState == FabState.OPEN) FabState.CLOSED else FabState.OPEN
            },
            backgroundColor = MaterialTheme.colors.secondary,
            modifier = Modifier.constrainAs(fab) {
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_white_24dp),
                tint = MaterialTheme.colors.onSecondary,
                contentDescription = "plus icon",
                modifier = Modifier.rotate(angle)
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .constrainAs(smallFabs) {
                    bottom.linkTo(fab.top)
                    end.linkTo(parent.end)
                }
                .padding(end = 8.dp)
                .wrapContentSize(),
            visible = groupOpenState == FabState.OPEN,
            enter = fadeIn(animationSpec = tween(durationMillis = 200))
                    + expandIn(expandFrom = Alignment.CenterEnd, animationSpec = tween(durationMillis = 200)),
            exit = fadeOut(animationSpec = tween(durationMillis = 200))
                    + shrinkOut(shrinkTowards = Alignment.CenterEnd, animationSpec = tween(durationMillis = 200))
        ) {
            SmallFABs(onClick = showDialog)
        }
    }
}

@Composable
fun SmallFABs(modifier: Modifier = Modifier, onClick: (TrackerItem.EventType) -> Unit) {
    Column(horizontalAlignment = Alignment.End, modifier = modifier.wrapContentSize()) {

        SmallEventFAB(
            labelText = "Walk",
            iconResId = R.drawable.ic_dog_walk,
            iconContentDesc = "dog bowl",
            eventType = TrackerItem.EventType.WALK,
            onClick = onClick
        )

        SmallEventFAB(
            labelText = "Potty",
            iconResId = R.drawable.ic_dog_poop,
            iconContentDesc = "poop icon",
            eventType = TrackerItem.EventType.POTTY,
            onClick = onClick
        )

        SmallEventFAB(
            labelText = "Meal",
            iconResId = R.drawable.ic_dog_bowl,
            iconContentDesc = "dog bowl",
            eventType = TrackerItem.EventType.FEED,
            onClick = onClick
        )
    }
}

@Composable
fun SmallEventFAB(labelText: String, iconResId: Int, iconContentDesc: String, eventType: TrackerItem.EventType, onClick: (TrackerItem.EventType) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
        Text(labelText, color = MaterialTheme.colors.onBackground, modifier = Modifier.padding(end = 8.dp))
        FloatingActionButton(onClick = { onClick(eventType) }, backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary, modifier = Modifier
                .width(40.dp)
                .height(40.dp)) {
            Icon(painter = painterResource(iconResId), contentDescription = iconContentDesc)
        }
    }
}

@Composable
fun EventList(list: List<TrackerItem>) {
    LazyColumn {
        items(list.size) {
            val item = list[it]
            if (item.itemType == "day") DateHeader(
                date = getDisplayDate(
                    month = item.month,
                    day = item.day
                )
            )
            else EventListItem(item)
        }
    }
}

@Composable
fun NoEventsLabel(modifier: Modifier = Modifier) {
    Text(text = "No events for this pet",
        color = MaterialTheme.colors.primary,
        fontSize = 24.sp,
        modifier = modifier.wrapContentSize())
}

@Composable
fun getDisplayDate(month: Int, day: Int): String {
    return String.format(Locale.US, "%s %s", stringArrayResource(R.array.months)[month - 1],
        stringArrayResource(R.array.formatted_day_of_month)[day - 1])
}

@Composable
fun EventListItem(event: TrackerItem) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .wrapContentHeight()
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colors.background)) {
        Icon(painter = painterResource(id = event.drawableResId),
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                .width(48.dp)
                .height(48.dp),
            contentDescription = "Dog on leash",
            tint = MaterialTheme.colors.secondary)
        Text(text = event.eventType?.name ?: "", modifier = Modifier
            .padding(start = 24.dp)
            .weight(1f, fill = true),
            fontSize = 22.sp, color = MaterialTheme.colors.onBackground)
        Text(text = event.localTime ?: "", modifier = Modifier
            .padding(end = 8.dp)
            .requiredWidth(IntrinsicSize.Max),
            fontSize = 22.sp, color = MaterialTheme.colors.onBackground)
    }
}

@Composable
fun DateHeader(date: String) {
    Column {
        Divider(Modifier.height(8.dp), color = Color.Transparent)
        Box(modifier = Modifier
            .background(color = Color.Transparent.copy(alpha = 0f))
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center) {
            Divider(
                Modifier
                    .background(MaterialTheme.colors.primary)
                    .height(1.dp))
            Box(modifier = Modifier
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                // oval border around date below vv
//                .border(
//                    width = 2.dp,
//                    color = MaterialTheme.colors.primary,
//                    shape = RoundedCornerShape(16.dp)
//                )
            ) {
                Text(text = date.uppercase(), modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colors.primary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
        Divider(Modifier.height(8.dp), color = Color.Transparent)
    }
}

@Composable
fun TrackerDialog(onDismiss: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        content()
    }
}

@Composable
fun TrackWalk() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)
    ) {
        Text(text = "DURATION", fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 32.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(value = "0",
                onValueChange = {},
                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 22.sp),
                modifier = Modifier
                    .wrapContentSize()
                    .weight(1f))

            Text(text = "Hours",
                fontSize = 18.sp,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .weight(1f))

            TextField(value = "0",
                onValueChange = {},
                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 22.sp),
                modifier = Modifier
                    .wrapContentSize()
                    .weight(1f))

            Text(text = "Minutes",
                fontSize = 18.sp,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .weight(1f)
                    .requiredWidth(IntrinsicSize.Min))
        }
    }
}

@Composable
fun TrackPotty() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)
    ) {
        Text(
            text = "TYPE", fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = false, onCheckedChange = {}, modifier = Modifier.padding(end = 8.dp))
            Text(text = "Number 1", fontSize = 18.sp, modifier = Modifier.padding(end = 16.dp))
            Checkbox(checked = false, onCheckedChange = {}, modifier = Modifier.padding(end = 8.dp))
            Text(text = "Number 2", fontSize = 18.sp)
        }
    }
}

@Composable
fun TrackMeal() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)
    ) {
        Text(
            text = "AMOUNT", fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = "0",
                onValueChange = {},
                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 22.sp),
                modifier = Modifier.width(56.dp)
            )

            Text(text = "Cups",
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 8.dp))
        }

        Text(text = "Please enter a number greater than 0",
            fontSize = 18.sp,
            color = MaterialTheme.colors.error,
            modifier = Modifier.padding(top = 16.dp))
    }
}

private enum class FabState {
    OPEN,
    CLOSED
}

@Composable
@Preview(name = "Preview Dialog", group = "actual dialog")
fun PreviewDialog() {
    TrackerDialog({}) {
        TrackWalk()
    }
}

@ExperimentalAnimationApi
@Composable
@Preview("Tracker Fragment",
    uiMode = UI_MODE_NIGHT_YES,
    showSystemUi = true,
    group = "full_screen")
fun PreviewTrackerFragment() {
    PetTrackerTheme {
        Surface {
            TrackerScreen(items = defaultTrackerItems)
        }
    }
}

@Composable
@Preview(name = "Walk",
    group = "Dialogs",
    uiMode = UI_MODE_NIGHT_YES)
fun PreviewTrackWalk() {
    PetTrackerTheme {
        Surface {
            TrackWalk()
        }
    }
}

@Composable
@Preview(name = "Potty",
    group = "Dialogs",
    uiMode = UI_MODE_NIGHT_YES)
fun PreviewTrackPotty() {
    PetTrackerTheme {
        Surface {
            TrackPotty()
        }
    }
}

@Composable
@Preview(name = "Meal",
    group = "Dialogs",
    uiMode = UI_MODE_NIGHT_YES)
fun PreviewTrackMeal() {
    PetTrackerTheme {
        Surface {
            TrackMeal()
        }
    }
}

@Composable
@Preview("Tracker Event List",
    backgroundColor = 0xff000000,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "individual_items")
fun PreviewTrackerList() {
    PetTrackerTheme {
        EventList(defaultTrackerItems)
    }
}

@Composable
@Preview("Event Item",
    backgroundColor = 0xff000000,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "individual_items")
fun PreviewEvent() {
    PetTrackerTheme {
        EventListItem(defaultEvent)
    }
}

@Composable
@Preview("Date Header",
    backgroundColor = 0xff000000,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "individual_items")
fun PreviewDate() {
    PetTrackerTheme {
        DateHeader(date = "July 26th")
    }
}