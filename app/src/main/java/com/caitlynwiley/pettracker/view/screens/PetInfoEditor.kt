package com.caitlynwiley.pettracker.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.caitlynwiley.pettracker.models.Pet.Gender
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import com.caitlynwiley.pettracker.viewmodel.PetInfoViewModel

@Composable
fun PetInfoEditor(ActionButton: @Composable () -> Unit, editing: Boolean = true, onBtnClick: () -> Unit) {
    val viewModel = viewModel<PetInfoViewModel>(factory = PetInfoViewModel.Factory(
        PetTrackerRepository(), "-ME-Zsu05LZIpdajQJ-3"
    ))

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (nameRow, ageRow, birthdayRow, breedRow, genderSection) = createRefs()

        Row(modifier = Modifier
            .constrainAs(nameRow) {
                top.linkTo(parent.top)
            }
            .fillMaxWidth()
            .padding(bottom = 8.dp)) {
            val name: String by viewModel.name.observeAsState("")

            Text(text = "Name",
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Bottom))
            Divider(modifier = Modifier.width(16.dp), color = Color.Transparent)
            TextField(value = name,
                onValueChange = { viewModel.setName(it) },
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
            val birthMonth: String by viewModel.birthMonth.observeAsState("")
            val birthYear: String by viewModel.birthYear.observeAsState("")

            Text(text = "DOB",
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Bottom)
            )

            Divider(modifier = Modifier.width(16.dp), color = Color.Transparent)
            TextField(value = birthYear,
                onValueChange = { viewModel.setBirthYear(it) },
                label = { Text("Year") },
                modifier = Modifier.weight(1f),
                enabled = editing,
                textStyle = TextStyle(fontSize = 22.sp)
            )

            Divider(modifier = Modifier.width(8.dp), color = Color.Transparent)
            TextField(value = birthMonth,
                onValueChange = { viewModel.setBirthMonth(it) },
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
            val age: String by viewModel.age.observeAsState("")

            Text(text = "Age", fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Bottom)
            )
            Divider(modifier = Modifier.width(16.dp), color = Color.Transparent)
            TextField(value = age,
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
            val breed: String by viewModel.breed.observeAsState("")

            Text(text = "Breed", fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Bottom))
            Divider(modifier = Modifier.width(16.dp), color = Color.Transparent)
            TextField(value = breed,
                onValueChange = { viewModel.setBreed(it) },
                modifier = Modifier.fillMaxWidth(),
                enabled = editing,
                textStyle = TextStyle(fontSize = 22.sp)
            )
        }

        val gender: Gender by viewModel.gender.observeAsState(Gender.UNKNOWN)
        GenderOptions(modifier = Modifier.constrainAs(genderSection) {
                top.linkTo(breedRow.bottom, 16.dp)
            centerHorizontallyTo(parent)
        }, gender, editing, viewModel::updateGender)

        Box(modifier = Modifier.wrapContentSize(Alignment.BottomEnd).clickable { onBtnClick() }) {
            ActionButton()
        }
    }
}

@Composable
private fun GenderOptions(modifier: Modifier = Modifier, current: Gender, editing: Boolean,
                          updateSelection: (Gender) -> Unit) {
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

            TextRadioButton(text = "Male", selected = current == Gender.MALE, enabled = editing,
                modifier = Modifier.constrainAs(maleOption) {
                    start.linkTo(barrier)
                }) {
                updateSelection(Gender.MALE)
            }

            TextRadioButton(text = "Female", selected = current == Gender.FEMALE, enabled = editing,
                modifier = Modifier.constrainAs(femaleOption) {
                    start.linkTo(maleOption.end, 16.dp)
                    end.linkTo(parent.end)
                }) {
                updateSelection(Gender.FEMALE)
            }

            TextRadioButton(text = "Unknown", selected = current == Gender.UNKNOWN, enabled = editing,
                modifier = Modifier.constrainAs(unknownOption) {
                    top.linkTo(maleOption.bottom, 8.dp)
                    start.linkTo(barrier)
                    end.linkTo(parent.end)
                }) {
                updateSelection(Gender.UNKNOWN)
            }
        }
    }
}

@Preview
@Composable
fun PreviewScreen() {
    PetInfoEditor(ActionButton = {
        Button(onClick = {}) {
            Text(text = "test button")
        }
    }) { }
}