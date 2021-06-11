package com.caitlynwiley.pettracker.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caitlynwiley.pettracker.fragments.PetInfoEntryScreen
import com.caitlynwiley.pettracker.fragments.SpeciesSelectorScreen
import com.caitlynwiley.pettracker.kotlincompose.PetTrackerViewModel

class NewPetActivity: ComponentActivity() {

    val viewModel by viewModels<PetTrackerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                MaterialTheme {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Add a Pet") }
                            )
                        }
                    ) {
                       ActivityContent()
                    }
                }
            }
        }
    }

    @Composable
    fun ActivityContent() {
        val navController = rememberNavController()
        NavHost(navController, startDestination = "speciesSelector") {
            composable(Screen.SelectSpecies.route) {
                SpeciesSelectorScreen(viewModel = viewModel)
            }
            composable(Screen.EnterPetInfo.route) {
                PetInfoEntryScreen(viewModel = viewModel)
            }
        }
    }
}

sealed class Screen(val route: String) {
    object SelectSpecies: Screen("selectSpecies")
    object EnterPetInfo: Screen("enterPetInfo")
}