package com.caitlynwiley.pettracker.activities

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import com.caitlynwiley.pettracker.ui.screens.PetInfoEditor
import com.caitlynwiley.pettracker.ui.screens.SettingsScreen
import com.caitlynwiley.pettracker.ui.screens.TrackerScreen
import com.caitlynwiley.pettracker.viewmodel.PetInfoViewModel
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
                })
        },
        drawerContent = { DrawerContent(scope = this, navController = navController) }
    ) {
        NavHost(
            navController = navController,
            startDestination = "TrackerFragment"
        ) {
            composable("TrackerFragment") {
                TrackerScreen(items = listOf())
                scope.launch { drawerState.close() }
            }

            composable("ManageFragment") {
//                    ManagePetsFragment()
                PetInfoEditor(
                    viewModel = PetInfoViewModel(repository = PetTrackerRepository(), petId = "")
                )
                scope.launch { drawerState.close() }
            }

            composable("SettingsFragment") {
                SettingsScreen()
            }
        }
    }
}

@Composable
fun DrawerContent(scope: ColumnScope, navController: NavController) {
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
                sharePet()
            }
            NavDrawerMenuItem(R.drawable.ic_settings_black_24dp, "gear icon", "Settings", false) {
                navController.navigate("SettingsFragment")
            }
        }
    }
}

@Composable
fun NavDrawerMenuItem(resId: Int, contentDesc: String, label: String, current: Boolean, onClick: () -> Unit) {
    Button(onClick = { onClick() },
        modifier = Modifier.fillMaxWidth(),
        elevation = ButtonDefaults.elevation(defaultElevation = if (current) 2.dp else 0.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = if (current) MaterialTheme.colors.background else Color.Transparent)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(painter = painterResource(id = resId), contentDescription = contentDesc)
            Text(label, modifier = Modifier.padding(start = 16.dp))
        }
    }
}

/*
if (SettingsFragment) {
    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
} else {
    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
}
 */

fun sharePet() {
    Log.d("MainFragment", "Share button clicked")
    /*
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, if (mPet == null) {
        "Here's my pet's ID: $petID"
    } else {
        "Here's ${mPet?.name ?: "my pet"}'s ID: $petID"
    })
    intent.type = "text/plain"

    val shareIntent = Intent.createChooser(intent, null)
    startActivity(shareIntent)
     */
}