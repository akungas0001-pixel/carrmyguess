package com.guessmycar.motorsport.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guessmycar.motorsport.R
import com.guessmycar.motorsport.data.CarRepository
import com.guessmycar.motorsport.locale.regionDisplayName
import com.guessmycar.motorsport.ui.theme.*

@Composable
fun GarageHubScreen(
    initialRegionId: String? = null,
    onStartChallenge: (regionId: String) -> Unit,
    onOpenSettings: () -> Unit
) {
    val regions = CarRepository.regions
    val initialPage = remember(initialRegionId) {
        regions.indexOfFirst { it.id == initialRegionId }.takeIf { it >= 0 } ?: 0
    }
    val pagerState = rememberPagerState(initialPage = initialPage) { regions.size }
    var isSilhouetteMode by remember { mutableStateOf(false) }

    val currentRegion = regions[pagerState.currentPage]

    Scaffold(
        containerColor = SurfaceDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainer)
                            .border(1.dp, SurfaceContainerHighest, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_guess_my_car_home),
                            contentDescription = stringResource(R.string.cd_car_logo),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(33.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        stringResource(R.string.nav_garage_hub),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextWhite,
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.cd_settings), tint = TextMuted)
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

            // Main Title & Motorsport Edition
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = TextWhite
                    )
                    Text(
                        stringResource(R.string.brand_tagline),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = OutlineSteel,
                        letterSpacing = 2.sp
                    )
                }

                IconButton(
                    onClick = { isSilhouetteMode = !isSilhouetteMode },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainer)
                        .border(1.dp, SurfaceContainerHighest, RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.Tune, contentDescription = stringResource(R.string.cd_filter), tint = TextWhite)
                }
            }

            Spacer(Modifier.height(14.dp))

            // Automotive Visual Trivia Pill
            Surface(
                shape = CircleShape,
                color = SurfaceContainerLow,
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ApexGreen))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.brand_trivia_badge),
                        color = ApexGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.brand_subtitle), fontSize = 12.sp, color = TextMuted)

            Spacer(Modifier.height(14.dp))

            // Region Switcher Badge
            Surface(
                shape = CircleShape,
                color = SurfaceContainer,
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(currentRegion.flag, fontSize = 16.sp)
                    Spacer(Modifier.width(6.dp))
                    Surface(shape = RoundedCornerShape(4.dp), color = SurfaceDark) {
                        Text(
                            currentRegion.code,
                            color = OutlineSteel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        currentRegion.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = TextWhite,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Car Showcase Card — region is changed ONLY by swiping this pager, never by buttons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceContainerLow)
                    .border(1.dp, OutlineVariant, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(R.string.page_indicator, pagerState.currentPage + 1, regions.size),
                            color = TextMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Icon(Icons.Default.AutoAwesome, contentDescription = stringResource(R.string.cd_sparkles), tint = PrimaryBlue)
                    }

                    Spacer(Modifier.height(10.dp))

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val region = regions[page]
                        val car = CarRepository.cars.firstOrNull { it.regionId == region.id }
                            ?: CarRepository.cars[0]

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Vehicle Image Viewport
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(SurfaceContainerLowest)
                            ) {
                                CarImage(car = car, modifier = Modifier.fillMaxSize())
                            }

                            Spacer(Modifier.height(14.dp))

                            Text(stringResource(R.string.region_label), color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp)
                            Text("${region.flag} ${regionDisplayName(region.id)}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(stringResource(R.string.cars_available, region.totalCars), color = OutlineSteel, fontSize = 12.sp)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // Dots indicator — updates automatically as the pager settles on a page
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        regions.indices.forEach { idx ->
                            Box(
                                modifier = Modifier
                                    .width(if (idx == pagerState.currentPage) 20.dp else 6.dp)
                                    .height(6.dp)
                                    .clip(CircleShape)
                                    .background(if (idx == pagerState.currentPage) PrimaryBlue else SurfaceContainerHighest)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Start Challenge Button — always targets whichever region is currently visible in the pager
            Button(
                onClick = { onStartChallenge(regions[pagerState.currentPage].id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text(
                    stringResource(R.string.start_challenge),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = OnPrimaryBlue,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = OnPrimaryBlue)
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Speed, contentDescription = null, tint = OutlineSteel, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    stringResource(R.string.stage_timed_mode),
                    color = OutlineSteel,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
