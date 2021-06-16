package com.caitlynwiley.pettracker.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun PetInfoEditor() {
    ConstraintLayout(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize(1f)) {
        val (nameLabel, nameField, ageLabel, yearsField, monthsField,
            birthdayLabel, birthdayField, breedLabel, breedField,
            genderSection, doneBtn) = createRefs()

        Text(text = "Name", fontSize = 22.sp,
            modifier = Modifier.constrainAs(nameLabel) {
                bottom.linkTo(nameField.bottom, 8.dp)
                start.linkTo(parent.start)
            }
        )
        TextField(value = "", onValueChange = {},
            modifier = Modifier.constrainAs(nameField) {
                start.linkTo(nameLabel.end, 16.dp)
                end.linkTo(parent.end)
        })

        val ageBarrier = createBottomBarrier(nameLabel, nameField, margin = 8.dp)
        Text(text = "Age", fontSize = 22.sp,
            modifier = Modifier.constrainAs(ageLabel) {
                bottom.linkTo(yearsField.bottom, 8.dp)
                start.linkTo(parent.start)
        })
        TextField(value = "", onValueChange = {},
            modifier = Modifier
                .constrainAs(yearsField) {
                    top.linkTo(ageBarrier)
                    end.linkTo(monthsField.start, 8.dp)
                }
                .fillMaxWidth(.4f))
        TextField(value = "", onValueChange = {},
            modifier = Modifier
                .constrainAs(monthsField) {
                    top.linkTo(ageBarrier)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(.4f))

        val birthdayBarrier = createBottomBarrier(ageLabel, yearsField, monthsField, margin = 8.dp)
        Text(text = "Birthday", fontSize = 22.sp,
            modifier = Modifier.constrainAs(birthdayLabel) {
                bottom.linkTo(birthdayField.bottom, 8.dp)
                start.linkTo(parent.start)
            }
        )
        TextField(value = "", onValueChange = {},
            modifier = Modifier.constrainAs(birthdayField) {
                top.linkTo(birthdayBarrier)
                start.linkTo(birthdayLabel.end, 16.dp)
                end.linkTo(parent.end)
            }
        )

        val breedBarrier = createBottomBarrier(birthdayLabel, birthdayField, margin = 8.dp)
        Text(text = "Breed", fontSize = 22.sp,
            modifier = Modifier.constrainAs(breedLabel) {
                bottom.linkTo(breedField.bottom, 8.dp)
                start.linkTo(parent.start)
        })
        TextField(value = "", onValueChange = {},
            modifier = Modifier.constrainAs(breedField) {
                top.linkTo(breedBarrier)
                end.linkTo(parent.end)
            })

        val genderBarrier = createBottomBarrier(breedLabel, breedField, margin = 8.dp)
        GenderOptions(modifier = Modifier.constrainAs(genderSection) {
                top.linkTo(genderBarrier)
            centerHorizontallyTo(parent)
        })

        Button(onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            modifier = Modifier.constrainAs(doneBtn) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })
        {
            Text("Save")
        }
    }
}

@Composable
fun GenderOptions(modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier) {
        val (title, maleBtn, maleText, femaleBtn, femaleText, unknownBtn, unknownText) = createRefs()

        Text(text = "Gender", fontSize = 22.sp, modifier = Modifier.constrainAs(title) {
            centerHorizontallyTo(parent)
        })

        val barrier = createBottomBarrier(title, margin = 8.dp)

        RadioButton(selected = false, onClick = {},
            modifier = Modifier.constrainAs(maleBtn) {
                start.linkTo(parent.start, 8.dp)
                top.linkTo(barrier)
            }
        )
        Text(text = "Male", fontSize = 18.sp, modifier = Modifier.constrainAs(maleText) {
            start.linkTo(maleBtn.end)
            top.linkTo(barrier)
            centerVerticallyTo(maleBtn)
        })

        RadioButton(selected = false, onClick = {},
            modifier = Modifier.constrainAs(femaleBtn) {
                start.linkTo(maleText.end, 8.dp)
                top.linkTo(barrier)
            }
        )
        Text(text = "Female", fontSize = 18.sp, modifier = Modifier.constrainAs(femaleText) {
            start.linkTo(femaleBtn.end)
            top.linkTo(barrier)
            centerVerticallyTo(femaleBtn)
        })

        RadioButton(selected = false, onClick = {},
            modifier = Modifier.constrainAs(unknownBtn) {
                start.linkTo(femaleText.end, 8.dp)
                top.linkTo(barrier)
            }
        )
        Text(text = "Unknown", fontSize = 18.sp, modifier = Modifier.constrainAs(unknownText) {
            start.linkTo(unknownBtn.end)
            top.linkTo(barrier)
            centerVerticallyTo(unknownBtn)
        })
    }
}

@Preview
@Composable
fun PreviewScreen() {
    PetInfoEditor()
}