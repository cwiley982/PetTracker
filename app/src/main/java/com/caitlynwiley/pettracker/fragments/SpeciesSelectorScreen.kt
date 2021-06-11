package com.caitlynwiley.pettracker.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.kotlincompose.PetTrackerViewModel

@Composable
fun SpeciesSelectorScreen (viewModel: PetTrackerViewModel) {
    Box(modifier = Modifier
        .fillMaxHeight(1f)
        .fillMaxWidth(1f)) {
        Text(modifier = Modifier.padding(end = 8.dp, top = 16.dp),
            text = "Select which type of pet you'd like to add:",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            softWrap = true,
            overflow = TextOverflow.Visible)
        PetTypeOptions(modifier = Modifier.align(Alignment.Center))
        Button(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp),
            onClick = { /* TODO */ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan)
        ) {
            Text(text = "Next", fontSize = 16.sp)
        }
    }
}

@Composable
fun PetTypeOptions(modifier: Modifier? = Modifier) {
    var selected by remember { mutableStateOf(PetType.OTHER) }
    val updateSelection = fun (type: PetType) {
        selected = type
    }

    Column(modifier = modifier!!) {
        Row (horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(1f)) {
            PetTypeOptionItem(PetType.DOG, updateSelection, selected)
            PetTypeOptionItem(PetType.CAT, updateSelection, selected)
        }
        Row (horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(1f)) {
            PetTypeOptionItem(PetType.SNAKE, updateSelection, selected)
            //SpeciesOption(PetType.OTHER)
        }
    }
}

@Composable
fun PetTypeOptionItem(type: PetType, onClick: (PetType) -> Unit, currentSelection: PetType?) {
    val squareShape = RoundedCornerShape(12.dp)
    val squareSize = 108.dp
    val iconSize = squareSize - 20.dp
    IconButton(onClick = { onClick(type) }, modifier = Modifier
        .padding(8.dp) // outside of box
        .border(2.dp, Color.Black, squareShape)
        .background(shape = squareShape,
            color = if (currentSelection!! == type) Color.Cyan else Color.Transparent)
        .width(squareSize)
        .height(squareSize)) {
        Box(modifier = Modifier.fillMaxSize(1f)) {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .width(iconSize)
                    .height(iconSize)
                    .padding(4.dp),
                painter = painterResource(id = mapTypeToResource(type)),
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = 4.dp, bottom = 4.dp),
                text = type.name, fontSize = 18.sp
            )
        }
    }
}

fun mapTypeToResource(type: PetType): Int {
    return when (type) {
        PetType.DOG -> R.drawable.dog_face
        PetType.CAT -> R.drawable.cat_face
        PetType.SNAKE -> R.drawable.snake
        PetType.OTHER -> R.drawable.other_animals
    }
}

enum class PetType {
    DOG, CAT, SNAKE, OTHER
}

@Preview
@Composable
fun PetChooserScreen() {
    SpeciesSelectorScreen(PetTrackerViewModel())
}

@Preview
@Composable
fun PreviewSpeciesPicker() {
    PetTypeOptions()
}

@Preview
@Composable
fun PreviewSnakeOption() {
    PetTypeOptionItem(type = PetType.SNAKE, {}, PetType.OTHER)
}

@Preview
@Composable
fun PreviewOtherOption() {
    PetTypeOptionItem(type = PetType.OTHER, {}, PetType.OTHER)
}