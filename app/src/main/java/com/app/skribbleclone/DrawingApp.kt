package com.app.skribbleclone

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun DrawingBar(onDelete:() -> Unit){
    TopAppBar(title = { Text("Skribble 2.0")},
        actions = {
            IconButton(onClick = onDelete, content = {
                Icon(imageVector = Icons.Default.Delete, null)
            })
        })
}

@Composable//as many color and stroke
fun DrawBody(paths: MutableState<MutableList<PathState>>){

    Box(modifier = Modifier
        .fillMaxSize()
    )
    //need a color and brush
    {
        val drawColor = remember { //default value
            mutableStateOf(Color.Black)
        }
        val drawBrush = remember {
            mutableStateOf(5f)
        }
        val usedColors =  remember{
            mutableStateOf(mutableSetOf(Color.Black, Color.Red, Color.Green, Color.Gray))
        }

        //save old ones
        paths.value.add(PathState(path = Path(), color = drawColor.value, stroke = drawBrush.value))
        DrawingCanvas(drawColor, drawBrush, usedColors, paths.value)
        Tools(drawColor,drawBrush,usedColors.value)
    }
}

//fun to call composable fun
@Composable
fun DrawingScreen(){
    // To construct the screen
    val paths = remember{
        mutableStateOf(mutableListOf<PathState>())
    }
    Scaffold(
        topBar = { DrawingBar {
            paths.value = mutableListOf() //for deletion of canvas

        }}) {
            DrawBody(paths)
        }
}