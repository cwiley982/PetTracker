package com.caitlynwiley.pettracker.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.caitlynwiley.pettracker.TextRadioButton

@Composable
fun PetInfoEditor() {
    ConstraintLayout(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()
    ) {
        val (nameRow, ageRow, birthdayRow, breedRow, genderSection, doneBtn) = createRefs()

        Row(modifier = Modifier
            .constrainAs(nameRow) {
                top.linkTo(parent.top)
            }
            .fillMaxWidth()) {
            Text(text = "Name", fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Bottom))
            Divider(modifier = Modifier.width(16.dp), color = Color.Transparent)
            TextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth())
        }

        Row(modifier = Modifier
            .constrainAs(ageRow) {
                top.linkTo(nameRow.bottom, 8.dp)
            }
            .fillMaxWidth()) {
            Text(text = "Age", fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Bottom))
            Divider(modifier = Modifier.width(16.dp), color = Color.Transparent)
            TextField(value = "", onValueChange = {}, label = { Text("Years")},
                modifier = Modifier.weight(1f))
            Divider(modifier = Modifier.width(8.dp), color = Color.Transparent)
            TextField(value = "", onValueChange = {}, label = { Text("Months")},
                modifier = Modifier.weight(1f))
        }

        Row(modifier = Modifier
            .constrainAs(birthdayRow) {
                top.linkTo(ageRow.bottom, 8.dp)
            }
            .fillMaxWidth()) {
            Text(text = "Birthday", fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Bottom)
            )
            Divider(modifier = Modifier.width(16.dp), color = Color.Transparent)
            TextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth())
        }

        Row(modifier = Modifier
            .constrainAs(breedRow) {
                top.linkTo(birthdayRow.bottom, 8.dp)
            }
            .fillMaxWidth()) {
            Text(text = "Breed", fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Bottom))
            Divider(modifier = Modifier.width(16.dp), color = Color.Transparent)
            TextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth())
        }

        GenderOptions(modifier = Modifier.constrainAs(genderSection) {
                top.linkTo(breedRow.bottom, 16.dp)
            centerHorizontallyTo(parent)
        })

        Button(onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            modifier = Modifier.constrainAs(doneBtn) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })
        {
            Text("Save", fontSize = 16.sp, color = MaterialTheme.colors.onSecondary)
        }
    }
}

@Composable
fun GenderOptions(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf<Gender?>(null) }
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

            TextRadioButton(text = "Male", selected = selected == Gender.MALE,
                modifier = Modifier.constrainAs(maleOption) {
                    start.linkTo(barrier)
                }) {
                selected = Gender.MALE
            }

            TextRadioButton(text = "Female", selected = selected == Gender.FEMALE,
                modifier = Modifier.constrainAs(femaleOption) {
                    start.linkTo(maleOption.end, 16.dp)
                    end.linkTo(parent.end)
                }) {
                selected = Gender.FEMALE
            }

            TextRadioButton(text = "Unknown", selected = selected == Gender.UNKNOWN,
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

enum class Gender {
    MALE, FEMALE, UNKNOWN
}

@Preview
@Composable
fun PreviewScreen() {
    PetInfoEditor()
}