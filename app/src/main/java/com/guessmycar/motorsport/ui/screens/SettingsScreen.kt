package com.guessmycar.motorsport.ui.screens

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guessmycar.motorsport.R
import com.guessmycar.motorsport.data.ProgressRepository
import com.guessmycar.motorsport.locale.languageFor
import com.guessmycar.motorsport.locale.supportedLanguages
import com.guessmycar.motorsport.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    progress: ProgressRepository,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
    onBack: () -> Unit,
    onResetProgress: () -> Unit
) {
    var soundEnabled by remember { mutableStateOf(progress.soundEnabled) }
    var hapticsEnabled by remember { mutableStateOf(progress.hapticsEnabled) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    val currentLanguage = languageFor(currentLanguageTag)

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
                Text(stringResource(R.string.settings_title), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp, letterSpacing = 1.sp)
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
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(10.dp))

            // Sound, Haptics and Language Card
            Surface(
                color = SurfaceContainerLow,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = TextMuted)
                            Spacer(Modifier.width(12.dp))
                            Text(stringResource(R.string.sound_label), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 1.sp)
                        }
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = {
                                soundEnabled = it
                                progress.soundEnabled = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SurfaceDark,
                                checkedTrackColor = PrimaryBlue,
                                uncheckedTrackColor = SurfaceContainerHighest
                            )
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SurfaceContainerHighest)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Vibration, contentDescription = null, tint = TextMuted)
                            Spacer(Modifier.width(12.dp))
                            Text(stringResource(R.string.haptics_label), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 1.sp)
                        }
                        Switch(
                            checked = hapticsEnabled,
                            onCheckedChange = {
                                hapticsEnabled = it
                                progress.hapticsEnabled = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SurfaceDark,
                                checkedTrackColor = PrimaryBlue,
                                uncheckedTrackColor = SurfaceContainerHighest
                            )
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SurfaceContainerHighest)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showLanguageSheet = true },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = TextMuted)
                            Spacer(Modifier.width(12.dp))
                            Text(stringResource(R.string.language_label), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 1.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(currentLanguage.flag, fontSize = 16.sp)
                            Spacer(Modifier.width(6.dp))
                            Text(currentLanguage.nativeName, color = TextMuted, fontSize = 13.sp)
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(stringResource(R.string.how_to_play), color = TachometerAmber, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.5.sp)

            Spacer(Modifier.height(10.dp))

            // How to Play Card
            Surface(
                color = SurfaceContainerLow,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    StepRow(number = 1, text = stringResource(R.string.how_to_play_step1))
                    StepRow(number = 2, text = stringResource(R.string.how_to_play_step2))
                    StepRow(number = 3, text = stringResource(R.string.how_to_play_step3))
                }
            }

            Spacer(Modifier.weight(1f))

            // Reset Button
            Button(
                onClick = { showResetDialog = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerLow),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, RedlineCrimson.copy(alpha = 0.4f))
            ) {
                Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = RedlineCrimson)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.reset_all_progress), color = RedlineCrimson, fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 1.sp)
            }

            Spacer(Modifier.height(12.dp))

            Text(
                stringResource(R.string.app_footer_version),
                color = OutlineSteel,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.reset_dialog_title), fontWeight = FontWeight.Bold, color = TextWhite) },
            text = { Text(stringResource(R.string.reset_dialog_message), color = TextMuted) },
            confirmButton = {
                Button(
                    onClick = {
                        onResetProgress()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedlineCrimson)
                ) {
                    Text(stringResource(R.string.confirm_reset), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(stringResource(R.string.cancel), color = TextWhite)
                }
            },
            containerColor = SurfaceContainerHigh
        )
    }

    if (showLanguageSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLanguageSheet = false },
            containerColor = SurfaceContainerHigh
        ) {
            Column(modifier = Modifier.padding(bottom = 24.dp)) {
                Text(
                    stringResource(R.string.choose_language),
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
                supportedLanguages.forEach { language ->
                    val isActive = language.code == currentLanguageTag
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageSelected(language.code)
                                showLanguageSheet = false
                            }
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(language.flag, fontSize = 20.sp)
                            Spacer(Modifier.width(12.dp))
                            Text(
                                language.nativeName,
                                color = if (isActive) TextWhite else TextMuted,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 15.sp
                            )
                        }
                        if (isActive) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = PrimaryBlue)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepRow(number: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHighest),
            contentAlignment = Alignment.Center
        ) {
            Text("$number", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Text(text, color = TextWhite, fontSize = 13.sp)
    }
}
