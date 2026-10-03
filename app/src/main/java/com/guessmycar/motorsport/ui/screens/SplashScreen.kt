package com.guessmycar.motorsport.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.guessmycar.motorsport.R
import com.guessmycar.motorsport.ui.theme.BrandNavy
import kotlinx.coroutines.delay

private const val SPLASH_LOGO_ASPECT_RATIO = 747f / 514f
private const val SPLASH_ANIMATION_MILLIS = 700
private const val SPLASH_HOLD_MILLIS = 900L

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = SPLASH_ANIMATION_MILLIS, easing = FastOutSlowInEasing)
        )
        delay(SPLASH_HOLD_MILLIS)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandNavy),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_guess_my_car),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.55f)
                .aspectRatio(SPLASH_LOGO_ASPECT_RATIO)
                .graphicsLayer {
                    val scale = 0.85f + 0.15f * progress.value
                    scaleX = scale
                    scaleY = scale
                    alpha = progress.value
                }
        )
    }
}
