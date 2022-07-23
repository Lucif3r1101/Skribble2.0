package com.app.skribbleclone

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Tools(drawColor: MutableState<Color>, drawBrush:MutableState<Float>,usedColors: MutableSet<Color>){
    Column(modifier = Modifier.padding(horizontal = 8.dp)){
        ColorPicker(onColorSelected = {
            color ->
            drawColor.value = color
        })

        //show few color in begin
        Row(modifier = Modifier
            .horizontalGradientBackground(listOf(Color.Gray, Color.Black))
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .horizontalScroll(rememberScrollState())
            .animateContentSize())
        {
            //uing useColor to run thourgh loop to display diff color
            usedColors.forEach{
                Icon(imageVector = Icons.Default.Star, null, tint = it,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { //to slect colors
                        drawColor.value = it
                    })

            }
        }
        //show and hide brush
        var showBrushes by remember{
            mutableStateOf(false)
        }
        val strokes = remember {
            (1..50 step 5).toList()
        }
        FloatingActionButton(onClick = { showBrushes = !showBrushes },
        modifier = Modifier.padding(vertical = 4.dp)
            ) {
            Icon(imageVector = Icons.Default.Brush, null, tint = drawColor.value)
        }
        AnimatedVisibility(visible = showBrushes) {
            LazyColumn{
                items(strokes){
                    IconButton(onClick = {drawBrush.value = it.toFloat()
                    showBrushes = false // all the icons will disappear
                    },
                    modifier = Modifier.padding(8.dp)
                        .border(border = BorderStroke(width = with(LocalDensity.current){ it.toDp()},
                        color = Color.Gray),
                            shape = CircleShape)

                        ) {

                        }
                }
            }
        }
    }

}

//hor grad bg

fun Modifier.horizontalGradientBackground(
    colors:List<Color>
)= gradientBackground(colors){
    gradientColors, size ->
    Brush.horizontalGradient(
        colors=gradientColors,
        startX = 0f,
        endX = size.width
    )
}


//using for hor grad bg

fun Modifier.gradientBackground(
    colors: List<Color>,
    brushProvider: (List<Color>, Size) -> Brush
): Modifier= composed{    //JIT time composition, allowing hoisted state
    var size by remember{ mutableStateOf(Size.Zero)}
    val gradient =  remember(colors, size){
        brushProvider(colors, size)
    }
    drawWithContent {
        size = this.size
        drawRect(brush= gradient)
        drawContent()
    }

}

