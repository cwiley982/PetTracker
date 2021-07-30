package com.caitlynwiley.pettracker

import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun TextRadioButton(modifier: Modifier = Modifier, text: String, selected: Boolean, enabled: Boolean, action: () -> Unit) {
    ConstraintLayout(modifier = modifier) {
        val (textField, button) = createRefs()
        RadioButton(selected = selected, onClick = { action() }, enabled = enabled,
            modifier = Modifier.constrainAs(button) {
                start.linkTo(parent.start)
            }
        )
        Text(text, fontSize = 18.sp, modifier = Modifier.constrainAs(textField) {
            start.linkTo(button.end, 8.dp)
            centerVerticallyTo(button)
        })
    }
}