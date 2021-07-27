package com.caitlynwiley.pettracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import com.caitlynwiley.pettracker.theme.PetTrackerTheme
import com.caitlynwiley.pettracker.viewmodel.PTViewModelFactory
import com.caitlynwiley.pettracker.viewmodel.PetInfoViewModel
import com.caitlynwiley.pettracker.viewmodel.PetTrackerViewModel
import com.caitlynwiley.pettracker.views.fragments.ChooseAdditionTypeScreen
import com.caitlynwiley.pettracker.views.screens.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class NewPetActivity: ComponentActivity() {

    private val viewModel by viewModels<PetTrackerViewModel>(
        factoryProducer = { PTViewModelFactory(PetTrackerRepository()) }
    )

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                PetTrackerTheme {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                navigationIcon = {
                                    IconButton(onClick = {
                                        Firebase.auth.signOut()
                                        val i = Intent(this@NewPetActivity, LoginActivity::class.java)
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(i)
                                        finish()
                                    }) {
                                        Icon(painter = painterResource(R.drawable.ic_arrow_back_white_24dp),
                                            contentDescription = "back button")
                                    }
                                },
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
        NavHost(navController, startDestination = Screen.ChooseAdditionType.route) {
            composable(Screen.ChooseAdditionType.route) {
                ChooseAdditionTypeScreen(navController)
            }
            composable(Screen.SelectSpecies.route) {
                SpeciesSelectorScreen(viewModel = viewModel, navController)
            }
            composable(Screen.EnterPetInfo.route) {
                PetInfoEntryScreen(viewModel = PetInfoViewModel(PetTrackerRepository(), ""), navController)
            }
            composable(route = Screen.ConfirmNewPet.route,
                arguments = listOf(navArgument("id") {type = NavType.StringType})) {
                it.arguments?.getString("id")?.let { id ->
                    ConfirmPetScreen(id, navController)
                }
            }
            composable(Screen.AddPetById.route) {
                AddByIdScreen(navigator = navController)
            }
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