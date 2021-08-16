package com.caitlynwiley.pettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@ExperimentalComposeUiApi
@Composable
fun AddByIdScreen(submit: (String) -> Unit) {
    var id by remember { mutableStateOf("") }

    ConstraintLayout {
        val (prompt, text, button) = createRefs()

        Text(text = "Enter Pet ID:", fontSize = 32.sp, modifier = Modifier.constrainAs(prompt) {
            top.linkTo(parent.top)
            centerHorizontallyTo(parent)
        })

        val keyboardController = LocalSoftwareKeyboardController.current
        TextField(
            value = id,
            onValueChange = { id = it },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                submit(id)
                keyboardController?.hide()
            }),
            modifier = Modifier.constrainAs(text) {
                top.linkTo(prompt.bottom, margin = 16.dp)
                centerHorizontallyTo(parent)
            }
        )

        Button(onClick = { submit(id) },
            modifier = Modifier.constrainAs(button) {
                top.linkTo(text.bottom, margin = 16.dp)
                centerHorizontallyTo(parent)
            }.background(MaterialTheme.colors.primary)) {
            Text("Add")
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun PreviewAddById() {
    AddByIdScreen({})
}