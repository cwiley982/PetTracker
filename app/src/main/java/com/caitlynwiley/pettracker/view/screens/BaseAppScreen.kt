package com.caitlynwiley.pettracker.view.screens

import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.theme.PetTrackerTheme
import com.caitlynwiley.pettracker.view.icons.CustomIcons
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun BaseAppScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(drawerState = drawerState)
    val currentScreen = remember { mutableStateOf(MainScreen.Tracker.route) }

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar (
                title = { Text("Pet Tracker") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(imageVector = Icons.Outlined.Menu, contentDescription = "menu icon")
                    }
                }
            )
        },
        drawerContent = { DrawerContent(scope = this, currentScreen = currentScreen) },
        drawerBackgroundColor = MaterialTheme.colors.surface
    ) {
        NavHost(
            navController = navController,
            startDestination = currentScreen.value
        ) {
            composable(MainScreen.Tracker.route) {
                TrackerFragment()
                scope.launch { drawerState.close() }
            }

            composable(MainScreen.ManagePet.route) {
                ManagePetScreen()
                scope.launch { drawerState.close() }
            }

            composable(MainScreen.Settings.route) {
                SettingsScreen()
                scope.launch { drawerState.close() }
            }
        }
    }
}

@Composable
fun DrawerContent(scope: ColumnScope, currentScreen: MutableState<String>) {
    val context = LocalContext.current
    val currentRoute by remember { currentScreen }

    with(scope) {
        Box(modifier = Modifier
            .background(MaterialTheme.colors.secondary)
            .fillMaxWidth()
            .weight(1f))
        Column(modifier = Modifier
            .padding(top = 16.dp, start = 8.dp, end = 8.dp)
            .weight(4f)) {
            Text("General", fontSize = 16.sp)
            NavDrawerMenuItem(CustomIcons.DogWalk, "dog on leash", "Tracker",
                current = (currentRoute == MainScreen.Tracker.route)
            ) {
                currentScreen.value = MainScreen.Tracker.route
            }
            NavDrawerMenuItem(Icons.Outlined.Edit, "edit icon", "Manage Pet",
                current = (currentRoute == MainScreen.ManagePet.route)
            ) {
                currentScreen.value = MainScreen.ManagePet.route
            }

            Divider()

            Text("Other", fontSize = 16.sp)
            NavDrawerMenuItem(Icons.Outlined.Share, "share icon", "Share",
                current = (currentRoute == MainScreen.Share.route)
            ) {
                sharePet(context)
            }
            NavDrawerMenuItem(Icons.Outlined.Settings, "gear icon", "Settings",
                current = (currentRoute == MainScreen.Settings.route)
            ) {
                currentScreen.value = MainScreen.Settings.route
            }
        }
    }
}

@Composable
fun NavDrawerMenuItem(icon: ImageVector, contentDesc: String, label: String, current: Boolean, onClick: () -> Unit) {
    val btnBackground = if (current) MaterialTheme.colors.primaryVariant else Color.Transparent
    val textColor = if (current) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
    val iconTint = if (current) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground

    Row(modifier = Modifier
        .padding(end = 8.dp)
        .fillMaxWidth()
        .clickable { onClick() }
        .background(color = btnBackground, shape = RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50))
        .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(imageVector = icon, tint = iconTint, contentDescription = contentDesc)
        Text(text = label, color = textColor, modifier = Modifier.padding(start = 16.dp))
    }
}

/*
if (SettingsFragment) {
    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
} else {
    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
}
 */

fun sharePet(context: Context) {
    Log.d("MainFragment", "Share button clicked")
    val pet = Pet()
    pet.name = "Nanook"

    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, if (pet == null) {
        "Here's my pet's ID: ${pet.id}"
    } else {
        "Here's ${pet?.name ?: "my pet"}'s ID: ${pet.id}"
    })
    intent.type = "text/plain"

    val shareIntent = Intent.createChooser(intent, null)
    startActivity(context, shareIntent, null)
}

sealed class MainScreen(var route: String) {
    object Tracker: MainScreen("tracker")
    object ManagePet: MainScreen("manage")
    object Share: MainScreen("share")
    object Settings: MainScreen("settings")
}

@Composable
@Preview("nav_drawer_item", uiMode = UI_MODE_NIGHT_YES)
fun PreviewDrawerItem() {
    PetTrackerTheme {
        Column(Modifier.background(MaterialTheme.colors.surface).fillMaxWidth()) {
            NavDrawerMenuItem(CustomIcons.DogWalk, "dog on leash", "Tracker", true) {}
            NavDrawerMenuItem(
                Icons.Outlined.Edit,
                "edit icon",
                "Manage Pet",
                false
            ) {}
        }
    }
}