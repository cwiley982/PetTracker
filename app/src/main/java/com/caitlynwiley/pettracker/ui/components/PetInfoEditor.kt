package com.caitlynwiley.pettracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caitlynwiley.pettracker.TextRadioButton
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.models.Pet.Gender
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import com.caitlynwiley.pettracker.viewmodel.PetInfoViewModel

@Composable
fun PetInfoEditor(editing: Boolean = true) {
    val viewModel = viewModel<PetInfoViewModel>(factory = PetInfoViewModel.Factory(
        PetTrackerRepository(), "-ME-Zsu05LZIpdajQJ-3"
    ))

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val pet: Pet by viewModel.pet.observeAsState(Pet())
        val (nameRow, ageRow, birthdayRow, breedRow, genderSection) = createRefs()

        Row(modifier = Modifier
            .constrainAs(nameRow) {
                top.linkTo(parent.top)
            }
            .fillMaxWidth()
            .padding(bottom = 8.dp)) {
            Text(text = "Name",
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Bottom))
            Divider(modifier = Modifier.width(16.dp), color = Color.Transparent)
            TextField(value = pet.name,
                onValueChange = { pet.name = it },
                modifier = Modifier.fillMaxWidth(),
                enabled = editing,
                textStyle = TextStyle(fontSize = 22.sp)
            )
        }

        Row(modifier = Modifier
            .constrainAs(ageRow) {
                top.linkTo(nameRow.bottom, 8.dp)
            }
            .fillMaxWidth()
            .padding(bottom = 8.dp)) {
            Text(text = "DOB",
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Bottom)
            )

            Divider(modifier = Modifier.width(16.dp), color = Color.Transparent)
            TextField(value = pet.birthYear,
                onValueChange = { pet.birthYear = it },
                label = { Text("Year") },
                modifier = Modifier.weight(1f),
                enabled = editing,
                textStyle = TextStyle(fontSize = 22.sp)
            )

            Divider(modifier = Modifier.width(8.dp), color = Color.Transparent)
            TextField(value = pet.birthMonth,
                onValueChange = { pet.birthMonth = it },
                label = { Text("Month") },
                modifier = Modifier.weight(1f),
                enabled = editing,
                textStyle = TextStyle(fontSize = 22.sp)
            )
        }

        Row(modifier = Modifier
            .constrainAs(birthdayRow) {
                top.linkTo(ageRow.bottom, 8.dp)
            }
            .fillMaxWidth()
            .padding(bottom = 8.dp)) {
            Text(text = "Age", fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Bottom)
            )
            Divider(modifier = Modifier.width(16.dp), color = Color.Transparent)
            TextField(value = pet.age,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                enabled = editing,
                textStyle = TextStyle(fontSize = 22.sp)
            )
        }

        Row(modifier = Modifier
            .constrainAs(breedRow) {
                top.linkTo(birthdayRow.bottom, 8.dp)
            }
            .fillMaxWidth()
            .padding(bottom = 8.dp)) {
            Text(text = "Breed", fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Bottom))
            Divider(modifier = Modifier.width(16.dp), color = Color.Transparent)
            TextField(value = pet.breed,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                enabled = editing,
                textStyle = TextStyle(fontSize = 22.sp)
            )
        }

        GenderOptions(modifier = Modifier.constrainAs(genderSection) {
                top.linkTo(breedRow.bottom, 16.dp)
            centerHorizontallyTo(parent)
        }, pet, editing)
    }
}

@Composable
fun GenderOptions(modifier: Modifier = Modifier, pet: Pet, editing: Boolean) {
    var selected: Gender by remember { mutableStateOf(pet.gender) }
    Row(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        ConstraintLayout(modifier = modifier) {
            val (title, maleOption, femaleOption, unknownOption) = createRefs()

            Text(text = "Gender", fontSize = 22.sp, modifier = Modifier.constrainAs(title) {
                start.linkTo(parent.start)
                centerVerticallyTo(parent)
            })

            val barrier = createEndBarrier(title, margin = 16.dp)

            TextRadioButton(text = "Male", selected = selected == Gender.MALE, enabled = editing,
                modifier = Modifier.constrainAs(maleOption) {
                    start.linkTo(barrier)
                }) {
                selected = Gender.MALE
            }

            TextRadioButton(text = "Female", selected = selected == Gender.FEMALE, enabled = editing,
                modifier = Modifier.constrainAs(femaleOption) {
                    start.linkTo(maleOption.end, 16.dp)
                    end.linkTo(parent.end)
                }) {
                selected = Gender.FEMALE
            }

            TextRadioButton(text = "Unknown", selected = selected == Gender.UNKNOWN, enabled = editing,
                modifier = Modifier.constrainAs(unknownOption) {
                    top.linkTo(maleOption.bottom, 8.dp)
                    start.linkTo(barrier)
                    end.linkTo(parent.end)
                }) {
                selected = Gender.UNKNOWN
            }
        }
    }
}

@Preview
@Composable
fun PreviewScreen() {
    PetInfoEditor()
}