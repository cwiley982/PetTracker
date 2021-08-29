package com.caitlynwiley.pettracker.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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

@Composable
fun ChooseSpecies(goToDetailsScreen: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(modifier = Modifier.padding(top = 32.dp),
            text = "Select which type of pet you'd like to add:",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            softWrap = true,
            overflow = TextOverflow.Visible)
        PetTypeOptions(modifier = Modifier.align(Alignment.Center))

        Button(modifier = Modifier.align(Alignment.BottomEnd),
            onClick = goToDetailsScreen,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
        ) {
            Text(text = "Next", fontSize = 16.sp, color = MaterialTheme.colors.onSecondary)
        }
    }
}

@Composable
private fun PetTypeOptions(modifier: Modifier? = Modifier) {
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
private fun PetTypeOptionItem(type: PetType, onClick: (PetType) -> Unit, currentSelection: PetType?) {
    val squareShape = RoundedCornerShape(12.dp)
    val squareSize = 108.dp
    val iconSize = squareSize - 20.dp
    IconButton(onClick = { onClick(type) }, modifier = Modifier
        .padding(8.dp) // outside of box
        .border(
            2.dp,
            color = if (currentSelection!! == type) MaterialTheme.colors.secondary else Color.Black,
            squareShape
        )
        .background(shape = squareShape, color = Color.Transparent)
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
                contentDescription = type.name
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

private fun mapTypeToResource(type: PetType): Int {
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
fun PreviewSpeciesPicker() {
    PetTypeOptions()
}