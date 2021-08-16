package com.caitlynwiley.pettracker.activities

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.ui.fragments.ChooseAdditionTypeScreen
import com.caitlynwiley.pettracker.ui.screens.AddByIdScreen
import com.caitlynwiley.pettracker.ui.screens.ConfirmPetScreen
import com.caitlynwiley.pettracker.ui.screens.NewPetDetailsScreen
import com.caitlynwiley.pettracker.ui.screens.SpeciesSelectorScreen


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
                            painter = painterResource(R.drawable.ic_arrow_back_white_24dp),
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
            ChooseAdditionTypeScreen(
                addById = {navController.navigate(Screen.AddPetById.route)},
                createNewPet = {navController.navigate(Screen.SelectSpecies.route)}
            )
        }
        composable(Screen.SelectSpecies.route) {
            SpeciesSelectorScreen(
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
                ConfirmPetScreen(id, navController)
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

sealed class Screen(val route: String) {
    object ChooseAdditionType: Screen("chooseAddMethod")
    object SelectSpecies: Screen("selectSpecies")
    object EnterPetInfo: Screen("enterPetInfo")
    object ConfirmNewPet: Screen("confirmNewPet/{id}") {
        fun createRoute(petId: String) = "confirmNewPet/$petId"
    }
    object AddPetById: Screen("addPetById")
}