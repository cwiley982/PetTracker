package com.caitlynwiley.pettracker.view.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.models.TrackerItem
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import com.caitlynwiley.pettracker.theme.PetTrackerTheme
import com.caitlynwiley.pettracker.view.icons.CustomIcons
import com.caitlynwiley.pettracker.viewmodel.TrackerViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.util.*

@ExperimentalAnimationApi
@Composable
fun TrackerFragment() {
    val viewModel = viewModel<TrackerViewModel>(
        factory = TrackerViewModel.Factory(PetTrackerRepository(), "")
    )

    val trackerItems by viewModel.trackerItems.observeAsState(listOf())
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { viewModel.refresh() }
    ) {
        TrackerScreen(items = trackerItems)
    }
}

@ExperimentalAnimationApi
@Composable
private fun TrackerScreen(items: List<TrackerItem>) {
    var dialogType by remember { mutableStateOf<TrackerItem.EventType?>(null) }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val fabGroup = createRef()

        // if dialog type is set, show that type's dialog, otherwise it should be hidden
        dialogType?.let {
            TrackerDialog(onDismiss = {dialogType = null}, type = it)
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
private fun FabGroup(modifier: Modifier = Modifier, showDialog: (TrackerItem.EventType) -> Unit) {
    ConstraintLayout(modifier = modifier.wrapContentSize(align = Alignment.BottomEnd)) {
        var groupOpenState by remember { mutableStateOf(FabState.CLOSED) }
        val (fab, smallButtons) = createRefs()

        val angle: Float by animateFloatAsState(
            targetValue = if (groupOpenState == FabState.CLOSED) 0f else 135f,
            animationSpec = tween(
                durationMillis = 200, // duration
                easing = FastOutSlowInEasing
            )
        )

        FloatingActionButton(
            onClick = {
                groupOpenState = if (groupOpenState == FabState.OPEN) FabState.CLOSED else FabState.OPEN
            },
//            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(fab) {
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
//                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "add icon",
                modifier = Modifier.rotate(angle)
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .constrainAs(smallButtons) {
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
private fun SmallFABs(modifier: Modifier = Modifier, onClick: (TrackerItem.EventType) -> Unit) {
    Column(horizontalAlignment = Alignment.End, modifier = modifier.wrapContentSize()) {

        SmallEventFAB(
            labelText = "Walk",
            vector = CustomIcons.DogWalk,
            iconContentDesc = "dog bowl",
            eventType = TrackerItem.EventType.WALK,
            onClick = onClick
        )

        SmallEventFAB(
            labelText = "Potty",
            vector = CustomIcons.DogPoop,
            iconContentDesc = "poop icon",
            eventType = TrackerItem.EventType.POTTY,
            onClick = onClick
        )

        SmallEventFAB(
            labelText = "Meal",
            vector = CustomIcons.DogBowl,
            iconContentDesc = "dog bowl",
            eventType = TrackerItem.EventType.FEED,
            onClick = onClick
        )
    }
}

@Composable
private fun SmallEventFAB(labelText: String, vector: ImageVector, iconContentDesc: String, eventType: TrackerItem.EventType, onClick: (TrackerItem.EventType) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
        Text(labelText, color = MaterialTheme.colors.onBackground, modifier = Modifier.padding(end = 8.dp))
        FloatingActionButton(onClick = { onClick(eventType) }, backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primary, modifier = Modifier
                .width(40.dp)
                .height(40.dp)) {
            Icon(imageVector = vector, contentDescription = iconContentDesc)
        }
    }
}

@Composable
private fun EventList(list: List<TrackerItem>) {
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
private fun NoEventsLabel(modifier: Modifier = Modifier) {
    Text(text = "No events for this pet",
        color = MaterialTheme.colors.primary,
        fontSize = 24.sp,
        modifier = modifier.wrapContentSize())
}

@Composable
private fun getDisplayDate(month: Int, day: Int): String {
    return String.format(Locale.US, "%s %s", stringArrayResource(R.array.months)[month - 1],
        stringArrayResource(R.array.formatted_day_of_month)[day - 1])
}

@Composable
private fun EventListItem(event: TrackerItem) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .wrapContentHeight()
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colors.surface)) {
        Icon(imageVector = event.drawableResId,
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
private fun DateHeader(date: String) {
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
                    color = MaterialTheme.colors.background,
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
private fun TrackerDialog(onDismiss: () -> Unit, type: TrackerItem.EventType) {
    AlertDialog(
        dismissButton = {
            Text(text = "Cancel", Modifier.clickable { onDismiss() })
        },
        onDismissRequest = { onDismiss() },
        confirmButton = { Text("Save") },
//        backgroundColor = MaterialTheme.colors.background,
        title = { Text(text = getDialogTitleForType(type)) },
        text = {
            // 24.dp padding horizontally by default for text content
            DialogContentForType(type)
        }
    )
}

private fun getDialogTitleForType(eventType: TrackerItem.EventType) : String {
    return when (eventType) {
        TrackerItem.EventType.FEED -> "Cups"
        TrackerItem.EventType.POTTY -> "Type"
        TrackerItem.EventType.WALK -> "Duration"
    }
}

@Composable
private fun DialogContentForType(eventType: TrackerItem.EventType) {
    return when (eventType) {
        TrackerItem.EventType.FEED -> TrackMeal()
        TrackerItem.EventType.POTTY -> TrackPotty()
        TrackerItem.EventType.WALK -> TrackWalk()
    }
}


@Composable
private fun TrackWalk() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = "0",
            onValueChange = {},
            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 22.sp),
            modifier = Modifier
                .wrapContentSize()
                .weight(1f)
        )

        Text(
            text = "Hours",
            fontSize = 18.sp,
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
                .weight(1f)
        )

        TextField(
            value = "0",
            onValueChange = {},
            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 22.sp),
            modifier = Modifier
                .wrapContentSize()
                .weight(1f)
        )

        Text(
            text = "Minutes",
            fontSize = 18.sp,
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
                .weight(1f)
                .requiredWidth(IntrinsicSize.Min)
        )
    }
}

@Composable
private fun TrackPotty() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = false, onCheckedChange = {}, modifier = Modifier.padding(end = 8.dp))
        Text(text = "Number 1", fontSize = 18.sp, modifier = Modifier.padding(end = 16.dp))
        Checkbox(checked = false, onCheckedChange = {}, modifier = Modifier.padding(end = 8.dp))
        Text(text = "Number 2", fontSize = 18.sp)
    }
}

@Composable
private fun TrackMeal() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
@Preview(name = "Preview Dialog",
    group = "actual dialog")
fun PreviewDialog() {
    TrackerDialog({}, TrackerItem.EventType.WALK)
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
            TrackerFragment()
        }
    }
}