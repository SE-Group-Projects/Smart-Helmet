package com.example.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import kotlinx.coroutines.delay
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween

@Composable
fun LoadingScreen(onLoadingFinished: () -> Unit) {
    var logoVisible by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var showLoadingBar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        logoVisible = true
        delay(2000)
        showLoadingBar = true
        for (i in 1..100) {
            progress = i / 100f
            delay(30)
        }
        onLoadingFinished() // <-- Tell MainActivity loading is done
    }

    val logoAlpha by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF3C86B9), Color(0xFF1B3C53)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    Box(
        modifier = Modifier.fillMaxSize().background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(500.dp).alpha(logoAlpha)
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (showLoadingBar) {
                LinearProgressIndicator(
                    progress = progress,
                    color = Color.White,
                    trackColor = Color(0x66FFFFFF),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
            }
        }
    }
}
