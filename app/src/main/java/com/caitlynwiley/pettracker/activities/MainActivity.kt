package com.caitlynwiley.pettracker.activities

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.theme.PetTrackerTheme
import com.caitlynwiley.pettracker.ui.fragments.ManagePetsFragment
import com.caitlynwiley.pettracker.ui.screens.SettingsScreen
import com.caitlynwiley.pettracker.ui.screens.TrackerFragment
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun MainActivity() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(drawerState = drawerState)

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar (
                title = { Text("Pet Tracker") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(painter = painterResource(id = R.drawable.ic_menu), contentDescription = "menu icon")
                    }
                }
            )
        },
        drawerContent = { DrawerContent(scope = this, navController = navController) },
        drawerBackgroundColor = MaterialTheme.colors.surface,
        floatingActionButton = {

        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "TrackerFragment"
        ) {
            composable("TrackerFragment") {
                TrackerFragment()
                scope.launch { drawerState.close() }
            }

            composable("ManageFragment") {
                ManagePetsFragment()
                scope.launch { drawerState.close() }
            }

            composable("SettingsFragment") {
                SettingsScreen()
                scope.launch { drawerState.close() }
            }
        }
    }
}

@Composable
fun DrawerContent(scope: ColumnScope, navController: NavController) {
    val context = LocalContext.current

    with(scope) {
        Box(modifier = Modifier
            .background(MaterialTheme.colors.secondary)
            .fillMaxWidth()
            .weight(1f))
        Column(modifier = Modifier
            .padding(top = 16.dp, start = 8.dp, end = 8.dp)
            .weight(4f)) {
            Text("General", fontSize = 16.sp)
            NavDrawerMenuItem(R.drawable.ic_dog_walk, "dog on leash", "Tracker", true) {
                navController.navigate("TrackerFragment")
            }
            NavDrawerMenuItem(R.drawable.ic_edit_black_24dp, "edit icon", "Manage Pet", false) {
                navController.navigate("ManageFragment")
            }

            Divider()

            Text("Other", fontSize = 16.sp)
            NavDrawerMenuItem(R.drawable.ic_share_black_24dp, "share icon", "Share", false) {
                sharePet(context)
            }
            NavDrawerMenuItem(R.drawable.ic_settings_black_24dp, "gear icon", "Settings", false) {
                navController.navigate("SettingsFragment")
            }
        }
    }
}

@Composable
fun NavDrawerMenuItem(resId: Int, contentDesc: String, label: String, current: Boolean, onClick: () -> Unit) {
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
        Icon(painter = painterResource(id = resId), tint = iconTint, contentDescription = contentDesc)
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

@Composable
@Preview("nav_drawer_item", uiMode = UI_MODE_NIGHT_YES)
fun PreviewDrawerItem() {
    PetTrackerTheme {
        Column(Modifier.background(MaterialTheme.colors.surface).fillMaxWidth()) {
            NavDrawerMenuItem(R.drawable.ic_dog_walk, "dog on leash", "Tracker", true) {}
            NavDrawerMenuItem(
                R.drawable.ic_edit_black_24dp,
                "edit icon",
                "Manage Pet",
                false
            ) {}
        }
    }
}