package com.guessmycar.motorsport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.guessmycar.motorsport.data.CarRepository
import com.guessmycar.motorsport.data.ProgressRepository
import com.guessmycar.motorsport.locale.ProvideAppLocaleLegacy
import com.guessmycar.motorsport.locale.applyAppLocale
import com.guessmycar.motorsport.ui.screens.*
import com.guessmycar.motorsport.ui.theme.GuessMyCarTheme
import com.guessmycar.motorsport.ui.theme.SurfaceDark

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sync the platform's per-app language with the user's persisted choice before the first
        // frame — see Localization.kt / applyAppLocale for the API-33+-vs-legacy mechanism.
        applyAppLocale(applicationContext, ProgressRepository(applicationContext).languageTag)

        setContent {
            GuessMyCarTheme {
                val appContext = LocalContext.current
                val progress = remember { ProgressRepository(appContext.applicationContext) }
                var languageTag by remember { mutableStateOf(progress.languageTag) }

                val content: @Composable () -> Unit = {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = SurfaceDark
                    ) {
                        val navController = rememberNavController()

                        var selectedRegionId by remember {
                            mutableStateOf(progress.selectedRegionId ?: CarRepository.regions.first().id)
                        }
                        var currentLevel by remember { mutableIntStateOf(1) }
                        var selectedCar by remember { mutableStateOf(CarRepository.cars[0]) }
                        val quizOptions = remember(selectedCar.id) { CarRepository.optionsFor(selectedCar) }
                        var lastResult by remember { mutableStateOf(ProgressRepository.LevelResult(0, 0, false)) }

                        NavHost(
                            navController = navController,
                            startDestination = "splash"
                        ) {
                            composable("splash") {
                                SplashScreen(
                                    onFinished = {
                                        navController.navigate("garage") {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable("garage") {
                                GarageHubScreen(
                                    initialRegionId = selectedRegionId,
                                    onStartChallenge = { regionId ->
                                        selectedRegionId = regionId
                                        progress.selectedRegionId = regionId
                                        navController.navigate("stages")
                                    },
                                    onOpenSettings = {
                                        navController.navigate("settings")
                                    }
                                )
                            }

                            composable("stages") {
                                StageSelectScreen(
                                    regionId = selectedRegionId,
                                    progress = progress,
                                    onSelectStage = { levelNumber ->
                                        currentLevel = levelNumber
                                        selectedCar = CarRepository.carForLevel(selectedRegionId, levelNumber)
                                            ?: CarRepository.cars[0]
                                        navController.navigate("quiz")
                                    },
                                    onBack = {
                                        navController.popBackStack()
                                    }
                                )
                            }

                            composable("quiz") {
                                ActiveQuizScreen(
                                    stageNumber = currentLevel,
                                    car = selectedCar,
                                    options = quizOptions,
                                    score = progress.totalPoints(),
                                    soundEnabled = progress.soundEnabled,
                                    hapticsEnabled = progress.hapticsEnabled,
                                    onAnswerSelected = { wrongAttempts ->
                                        lastResult = progress.recordLevelResult(selectedRegionId, currentLevel, wrongAttempts)
                                        navController.navigate("result") {
                                            popUpTo("quiz") { inclusive = true }
                                        }
                                    },
                                    onBack = {
                                        navController.popBackStack()
                                    }
                                )
                            }

                            composable("result") {
                                QuizResultScreen(
                                    levelNumber = currentLevel,
                                    car = selectedCar,
                                    options = quizOptions,
                                    tireRating = lastResult.tireRating,
                                    pointsEarned = lastResult.pointsEarned,
                                    onGoToLevels = {
                                        navController.navigate("stages") {
                                            popUpTo("stages") { inclusive = true }
                                        }
                                    },
                                    onNext = {
                                        if (currentLevel < CarRepository.LEVELS_PER_REGION) {
                                            currentLevel += 1
                                            selectedCar = CarRepository.carForLevel(selectedRegionId, currentLevel)
                                                ?: CarRepository.cars[0]
                                            navController.navigate("quiz") {
                                                popUpTo("result") { inclusive = true }
                                            }
                                        } else {
                                            navController.navigate("stages") {
                                                popUpTo("stages") { inclusive = true }
                                            }
                                        }
                                    },
                                    onBack = {
                                        navController.navigate("stages")
                                    }
                                )
                            }

                            composable("settings") {
                                SettingsScreen(
                                    progress = progress,
                                    currentLanguageTag = languageTag,
                                    onLanguageSelected = { tag ->
                                        languageTag = tag
                                        progress.languageTag = tag
                                        applyAppLocale(appContext.applicationContext, tag)
                                    },
                                    onBack = {
                                        navController.popBackStack()
                                    },
                                    onResetProgress = {
                                        progress.resetAll()
                                        selectedRegionId = CarRepository.regions.first().id
                                        currentLevel = 1
                                        selectedCar = CarRepository.cars[0]
                                    }
                                )
                            }
                        }
                    }
                }

                // applyAppLocale() also registers the choice with the platform LocaleManager on API 33+
                // (so a cold app restart picks the right locale before this code even runs), but on
                // this device the Activity's own ambient Resources doesn't reliably honor that for an
                // already-composed UI — so ProvideAppLocaleLegacy's explicit Resources override is used
                // on every API level to force stringResource() to resolve against languageTag. See
                // Localization.kt.
                ProvideAppLocaleLegacy(languageCode = languageTag) { content() }
            }
        }
    }
}
