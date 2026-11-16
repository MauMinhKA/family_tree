package com.example.presentation.splash

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Intercept back gesture during Splash to prevent exiting prematurely
    BackHandler(enabled = true) {
        // Consumed to prevent premature app exit during initial load
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle navigation side-effects
    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                SplashEffect.NavigateToHome -> onNavigateToHome()
                SplashEffect.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    // Load Lottie composition safely from RawRes
    val compositionResult = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.tree_animation)
    )
    val composition = compositionResult.value

    // Animate Lottie composition once from 0% to 100%
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = composition != null,
        iterations = 1,
        restartOnPlay = false
    )

    // Notify ViewModel when animation finishes
    LaunchedEffect(progress) {
        if (progress >= 1f) {
            viewModel.processIntent(SplashIntent.AnimationFinished)
        }
    }

    // Handle potential Lottie load failure gracefully so app is never stuck
    LaunchedEffect(compositionResult.isFailure) {
        if (compositionResult.isFailure) {
            viewModel.processIntent(SplashIntent.AnimationFinished)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .widthIn(max = 420.dp)
        ) {
            // Animation or Fallback Icon container
            Box(
                modifier = Modifier
                    .sizeIn(maxWidth = 260.dp, maxHeight = 260.dp)
                    .fillMaxWidth(0.65f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                when {
                    composition != null -> {
                        LottieAnimation(
                            composition = composition,
                            progress = { progress },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    compositionResult.isFailure -> {
                        Icon(
                            imageVector = Icons.Default.AccountTree,
                            contentDescription = stringResource(R.string.splash_app_title),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(120.dp)
                        )
                    }
                    else -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Name / Branding
            Text(
                text = stringResource(R.string.splash_app_title),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // App Tagline
            Text(
                text = stringResource(R.string.splash_tagline),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Subtle Loading indicator while initializing
            AnimatedVisibility(
                visible = state.isLoading,
                enter = fadeIn()
            ) {
                Text(
                    text = stringResource(R.string.splash_loading),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
