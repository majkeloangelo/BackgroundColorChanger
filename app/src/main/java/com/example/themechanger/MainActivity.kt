package com.example.themechanger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.themechanger.ui.theme.ThemeChangerTheme
import com.example.themechanger.ui.theme.fonts

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThemeChangerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    // color = MaterialTheme.colorScheme.background
                ) {
                    Layout()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Layout() {
    var color by remember { mutableStateOf("") }
    var validColor by remember { mutableStateOf(true) }
    var prevColor = Color.White
    var isRed by remember { mutableStateOf(true) }
    var backgroundColor = if (validColor) {
        parseColor(color) ?: prevColor
    } else {
        prevColor
    }

    val animationColor by animateColorAsState(
        targetValue = if (isRed) backgroundColor else prevColor,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )

    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        disabledTextColor = Color.White,
        disabledBorderColor = if (!validColor) Color.Red else Color.Gray,
        containerColor = Color.White,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(animationColor),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(10.dp)
                )
                .height((40.dp)),
            contentAlignment = Alignment.Center,
        ){
            Text(
                text = "Background color changer",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    fontFamily = fonts,
                    color = Color.White
                )
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .height((120.dp)),
            contentAlignment = Alignment.Center,
        ){
            OutlinedTextField(
                value = color,
                label = {
                    Text(
                        text = "Enter color in HEX or name",
                        style = TextStyle(
                            fontFamily = fonts
                        )
                    )
                },
                onValueChange = {
                    color = it
                    validColor = ValidHEX(color) || parseColor(color) != null
                },
                supportingText = {
                    if (!validColor) {
                        Text(
                            text = "Insert valid HEX color value or color name. Available colors: black, blue, cyan, gray, green, magenta, red, white, yellow.",
                            modifier = Modifier.height(36.dp),
                            fontSize = 12.sp,
                            style = TextStyle(
                                fontFamily = fonts,
                                textAlign = TextAlign.Left
                            )
                        )
                    }
                },
                isError = !validColor,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                colors = textFieldColors
                )
        }
        Box(){}
    }
}

fun ValidHEX(x: String): Boolean {
    val regexPattern = """^#[0-9A-Fa-f]{6}[0-9A-Fa-f]{0,2}${'$'}""".toRegex()
    return regexPattern.matches(x)
}

fun parseColor(colorString: String): Color? {
    return when (colorString.lowercase()) {
        "black" -> Color.Black
        "blue" -> Color.Blue
        "cyan" -> Color.Cyan
        "gray" -> Color.Gray
        "green" -> Color.Green
        "magenta" -> Color.Magenta
        "red" -> Color.Red
        "white" -> Color.White
        "yellow" -> Color.Yellow
        else -> {
            try {
                if (ValidHEX(colorString)) {
                    Color(android.graphics.Color.parseColor(colorString))
                } else {
                    null
                }
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}
