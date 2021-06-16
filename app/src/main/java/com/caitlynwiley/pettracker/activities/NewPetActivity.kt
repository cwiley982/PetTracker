package com.caitlynwiley.pettracker.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caitlynwiley.pettracker.PetTrackerTheme
import com.caitlynwiley.pettracker.screens.AddByIdScreen
import com.caitlynwiley.pettracker.screens.PetInfoEntryScreen
import com.caitlynwiley.pettracker.screens.SpeciesSelectorScreen
import com.caitlynwiley.pettracker.viewmodel.PetTrackerViewModel

class NewPetActivity: ComponentActivity() {

    val viewModel by viewModels<PetTrackerViewModel>()

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                PetTrackerTheme {
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

    @ExperimentalComposeUiApi
    @Composable
    fun ActivityContent() {
        val navController = rememberNavController()
        NavHost(navController, startDestination = Screen.SelectSpecies.route) {
            composable(Screen.SelectSpecies.route) {
                SpeciesSelectorScreen(viewModel = viewModel, navController)
            }
            composable(Screen.EnterPetInfo.route) {
                PetInfoEntryScreen(viewModel = viewModel, navController)
            }
            composable(Screen.ConfirmNewPet.route) {
            }
            composable(Screen.AddPetById.route) {
                AddByIdScreen(navigator = navController)
            }
        }
    }
}

sealed class Screen(val route: String) {
    object SelectSpecies: Screen("selectSpecies")
    object EnterPetInfo: Screen("enterPetInfo")
    object ConfirmNewPet: Screen("confirmNewPet")
    object AddPetById: Screen("addPetById")
}