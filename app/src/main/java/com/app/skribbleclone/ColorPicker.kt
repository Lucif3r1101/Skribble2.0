package com.app.skribbleclone

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.Random.Default.nextFloat

@Composable
fun ColorPicker(onColorSelected:(Color)-> Unit){

    Text(text = "Drag to change color",
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(8.dp))

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenInPx = with(LocalDensity.current){
        screenWidth.toPx()
    }
    var activeColor by remember{
        mutableStateOf(Color.Black)
    }

    //drag
    val dragOffset= remember {
        mutableStateOf(0f)
    }

    //OPtions to select
    Box(modifier = Modifier
        .padding(8.dp)
    )
    {
        Spacer(modifier = Modifier
            .height(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(
                brush = colorMap(screenInPx)
            )
            .align(Alignment.Center)
            .pointerInput("paint") {
                detectTapGestures { offset ->
                    dragOffset.value = offset.x
                    activeColor = getActiveColor(dragOffset.value, screenInPx)
                    onColorSelected.invoke(activeColor) // same function return
                }
            }
        )
        
        //drag icon on color picker
        
        val min = 0.dp
        val max = screenWidth - 32.dp
        val (minPx, maxPx) = with(LocalDensity.current){
            min.toPx() to max.toPx()
        }
        
        Icon(imageVector = Icons.Default.FiberManualRecord, null, tint=activeColor,
        modifier = Modifier
            .offset { IntOffset(dragOffset.value.roundToInt(), 0) }
            .border(
                BorderStroke(4.dp, MaterialTheme.colors.onSurface),
                shape = CircleShape
            )
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState{delta ->
                    //to form the value of mov of icon
                    val newValue = dragOffset.value + delta
                    dragOffset.value = newValue.coerceIn(minPx, maxPx) //tp form the range as a condition
                    activeColor = getActiveColor(dragOffset.value, screenInPx)
                    }
                )) //construct offset from x and y value
    }
}
//slider values
fun colorMap(screenWidthInPx:Float) = Brush.horizontalGradient(
    colors = createColorMap(),
    startX = 0f,
    endX = screenWidthInPx
)

fun createColorMap(): List<Color>{
    val colorList = mutableListOf<Color>()
    for (i in 0..360 step (2)){
     //sat and spectrum
        val randomSat = 90 + Random.nextFloat()* 10
        val randomLight = 50 + Random.nextFloat() * 10
        val hsv = android.graphics.Color.HSVToColor(
            floatArrayOf(
                i.toFloat(),
                randomSat, randomLight
            )
        )
        colorList.add(Color(hsv))
    }
    return colorList
}

fun getActiveColor(dragPosition: Float, screenWidth: Float): Color{
    val hue = (dragPosition/screenWidth) * 360f
    val randomSat = 90 + Random.nextFloat()* 10
    val randomLight = 50 + Random.nextFloat() * 10

    return Color(
        android.graphics.Color.HSVToColor(
            floatArrayOf(
                hue,
                randomSat, randomLight
            )
    ))

}