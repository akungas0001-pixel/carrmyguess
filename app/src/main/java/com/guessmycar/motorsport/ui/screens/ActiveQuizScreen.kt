package com.guessmycar.motorsport.ui.screens

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guessmycar.motorsport.R
import com.guessmycar.motorsport.audio.GameFeedback
import com.guessmycar.motorsport.data.Car
import com.guessmycar.motorsport.locale.regionDisplayName
import com.guessmycar.motorsport.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ActiveQuizScreen(
    stageNumber: Int = 12,
    car: Car,
    options: List<String>,
    score: Int = 1240,
    soundEnabled: Boolean = true,
    hapticsEnabled: Boolean = true,
    onAnswerSelected: (wrongAttempts: Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val feedback = remember { GameFeedback.get(context) }

    var wrongAttempts by remember(car.id) { mutableIntStateOf(0) }
    var selectedWrongOption by remember(car.id) { mutableStateOf<String?>(null) }
    var isCorrect by remember(car.id) { mutableStateOf(false) }
    var showWrongBanner by remember(car.id) { mutableStateOf(false) }

    LaunchedEffect(showWrongBanner) {
        if (showWrongBanner) {
            delay(1200)
            showWrongBanner = false
            selectedWrongOption = null
        }
    }

    LaunchedEffect(isCorrect) {
        if (isCorrect) {
            delay(500)
            onAnswerSelected(wrongAttempts)
        }
    }

    Scaffold(
        containerColor = SurfaceDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.cd_back), tint = TextWhite)
                }
                Text(stringResource(R.string.active_quiz_title), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp, letterSpacing = 1.sp)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = stringResource(R.string.cd_profile), tint = OnPrimaryBlue)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Stage and PTS row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Text(stringResource(R.string.stage_progress, stageNumber, 20), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🪙", fontSize = 12.sp)
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.points_suffix, score), color = TachometerAmber, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Main Car Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceContainerLowest)
                    .border(1.dp, OutlineVariant, RoundedCornerShape(18.dp))
            ) {
                CarImage(car = car, modifier = Modifier.fillMaxSize())

                // Difficulty pill
                val difficultyColor = when (car.difficulty) {
                    "Easy" -> ApexGreen
                    "Hard" -> RedlineCrimson
                    else -> TachometerAmber
                }
                val difficultyText = when (car.difficulty) {
                    "Easy" -> stringResource(R.string.difficulty_easy)
                    "Hard" -> stringResource(R.string.difficulty_hard)
                    else -> stringResource(R.string.difficulty_medium)
                }
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.75f),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(difficultyColor))
                        Spacer(Modifier.width(6.dp))
                        Text(difficultyText, color = difficultyColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Region pill
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.75f),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Flag, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("${regionDisplayName(car.regionId)} • ${car.productionYear}", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Wrong answer banner
                androidx.compose.animation.AnimatedVisibility(
                    visible = showWrongBanner,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = RedlineCrimsonDark,
                        border = androidx.compose.foundation.BorderStroke(1.dp, RedlineCrimson)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(stringResource(R.string.wrong_answer_banner), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Select vehicle prompt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.select_matching_vehicle), color = TextMuted, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                Text(
                    if (wrongAttempts > 0) stringResource(R.string.attempts_label, wrongAttempts) else stringResource(R.string.plus_fifty_pts),
                    color = if (wrongAttempts > 0) RedlineCrimson else PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            // 2x2 Options
            val labels = listOf("A", "B", "C", "D")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                options.chunked(2).forEachIndexed { rowIndex, pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        pair.forEachIndexed { colIndex, opt ->
                            val index = rowIndex * 2 + colIndex
                            val isThisCorrect = opt.equals(car.displayName, ignoreCase = true)
                            val isFlashingWrong = selectedWrongOption == opt

                            val bgColor = when {
                                isCorrect && isThisCorrect -> ApexGreenContainer
                                isFlashingWrong -> RedlineCrimsonDark
                                else -> SurfaceContainerHigh
                            }

                            val borderColor = when {
                                isCorrect && isThisCorrect -> ApexGreen
                                isFlashingWrong -> RedlineCrimson
                                else -> OutlineVariant
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(68.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(bgColor)
                                    .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                                    .clickable(!isCorrect && !showWrongBanner) {
                                        if (isThisCorrect) {
                                            isCorrect = true
                                            feedback.playCorrect(soundEnabled)
                                        } else {
                                            selectedWrongOption = opt
                                            showWrongBanner = true
                                            wrongAttempts++
                                            feedback.vibrateWrong(hapticsEnabled)
                                        }
                                    }
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(labels.getOrElse(index) { "" }, color = OutlineSteel, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        if (isCorrect && isThisCorrect) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        } else if (isFlashingWrong) {
                                            Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                    Text(opt, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Bottom Buttons: Hint & Skip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Hint action */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceContainer)
                ) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = TachometerAmber, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.hint_label, 1), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { /* Skip action */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceContainer)
                ) {
                    Text(stringResource(R.string.skip_vehicle), color = TextMuted, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(Modifier.width(6.dp))
                    Icon(Icons.Default.SkipNext, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
