package com.caitlynwiley.pettracker.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController

@Composable
@ExperimentalComposeUiApi
fun NewPetScreen(goToLoginScreen: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        goToLoginScreen()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                },
                title = { Text("Add a Pet") }
            )
        }
    ) {
        ActivityContent()
    }
}

@ExperimentalComposeUiApi
@Composable
fun ActivityContent() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Screen.ChooseAdditionType.route) {
        composable(Screen.ChooseAdditionType.route) {
            ChooseHowToAddPet(
                addById = {navController.navigate(Screen.AddPetById.route)},
                createNewPet = {navController.navigate(Screen.SelectSpecies.route)}
            )
        }
        composable(Screen.SelectSpecies.route) {
            ChooseSpecies(
                goToDetailsScreen = { navController.navigate(Screen.EnterPetInfo.route) }
            )
        }
        composable(Screen.EnterPetInfo.route) {
            NewPetDetailsScreen(
                goToConfirmDetails = { navController.navigate(Screen.ConfirmNewPet.route) }
            )
        }
        composable(route = Screen.ConfirmNewPet.route,
            arguments = listOf(navArgument("id") {type = NavType.StringType})) {
            it.arguments?.getString("id")?.let { id ->
                ConfirmPetScreen(id)
            }
        }
        composable(Screen.AddPetById.route) {
            AddByIdScreen(
                submit = { id ->
                    navController.navigate(Screen.ConfirmNewPet.createRoute(petId = id))
                }
            )
        }
    }
}

@Composable
fun ChooseHowToAddPet(addById: () -> Unit, createNewPet: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Button(onClick = createNewPet ) {
            Text("Create new pet")
        }

        Text("OR")

        Button(onClick = addById ) {
            Text("Enter a pet ID")
        }
    }
}

sealed class Screen(val route: String) {
    object ChooseAdditionType: Screen("chooseAddMethod")
    object SelectSpecies: Screen("selectSpecies")
    object EnterPetInfo: Screen("enterPetInfo")
    object ConfirmNewPet: Screen("confirmNewPet/{id}") {
        fun createRoute(petId: String) = "confirmNewPet/$petId"
    }
    object AddPetById: Screen("addPetById")
}