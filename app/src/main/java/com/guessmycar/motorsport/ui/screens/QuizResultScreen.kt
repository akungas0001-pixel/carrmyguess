package com.guessmycar.motorsport.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guessmycar.motorsport.R
import com.guessmycar.motorsport.data.Car
import com.guessmycar.motorsport.locale.regionDisplayName
import com.guessmycar.motorsport.ui.theme.*

@Composable
fun QuizResultScreen(
    levelNumber: Int = 26,
    car: Car,
    options: List<String>,
    tireRating: Int,
    pointsEarned: Int,
    onGoToLevels: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val attemptLabel = when (tireRating) {
        3 -> stringResource(R.string.attempt_first)
        2 -> stringResource(R.string.attempt_one_mistake)
        else -> stringResource(R.string.attempt_multiple_mistakes)
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(R.string.brand_name_caps), color = OutlineSteel, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.sp)
                    Text(stringResource(R.string.quiz_result_title), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp, letterSpacing = 1.sp)
                }
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
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            Text(stringResource(R.string.level_number_title, levelNumber), fontWeight = FontWeight.Bold, fontSize = 28.sp, color = TextWhite)
            Spacer(Modifier.height(4.dp))
            Text(
                stringResource(R.string.you_guessed_correctly),
                color = ApexGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(16.dp))

            // Main Car Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceContainerLow)
                    .border(1.dp, OutlineVariant, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceContainerLowest)
                    ) {
                        CarImage(car = car, modifier = Modifier.fillMaxSize())

                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.75f),
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ApexGreen))
                                Spacer(Modifier.width(6.dp))
                                Text(stringResource(R.string.matched_badge), color = ApexGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(car.displayName, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextWhite)
                    Text("${car.productionYear} • ${regionDisplayName(car.regionId)}", color = OutlineSteel, fontSize = 12.sp)

                    Spacer(Modifier.height(12.dp))

                    // 3 Tire rim badges — yellow = earned, gray = missing
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        repeat(3) { tireIndex ->
                            val earned = tireIndex < tireRating
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceContainerLowest)
                                    .border(1.5.dp, if (earned) TachometerAmber else OutlineVariant, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.TireRepair,
                                    contentDescription = null,
                                    tint = if (earned) TachometerAmber else OutlineVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        stringResource(R.string.result_summary_format, attemptLabel, tireRating, pointsEarned),
                        color = TextMuted,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // 4 Option grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                options.chunked(2).forEach { rowOptions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowOptions.forEach { opt ->
                            val isCorrect = opt.equals(car.displayName, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isCorrect) ApexGreenContainer else SurfaceContainerLow)
                                    .border(1.dp, if (isCorrect) ApexGreen else OutlineVariant, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        opt,
                                        color = if (isCorrect) Color.White else OutlineSteel,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    if (isCorrect) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Bottom CTA Buttons: Levels & Next
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onGoToLevels,
                    modifier = Modifier.weight(1f).height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceContainer)
                ) {
                    Icon(Icons.Default.GridView, contentDescription = null, tint = TextWhite)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.levels_button), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Button(
                    onClick = onNext,
                    modifier = Modifier.weight(1f).height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(stringResource(R.string.next_button), color = OnPrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = OnPrimaryBlue)
                }
            }
        }
    }
}
