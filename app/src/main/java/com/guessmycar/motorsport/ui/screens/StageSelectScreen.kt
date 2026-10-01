package com.guessmycar.motorsport.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guessmycar.motorsport.R
import com.guessmycar.motorsport.data.CarRepository
import com.guessmycar.motorsport.data.ProgressRepository
import com.guessmycar.motorsport.locale.regionDisplayName
import com.guessmycar.motorsport.ui.theme.*
import kotlin.math.atan2
import kotlin.math.roundToInt

private val ROW_HEIGHT = 136.dp
private val NODE_RADIUS = 30.dp

private fun xFraction(index: Int): Float = when (index % 4) {
    0 -> 0.5f
    1 -> 0.78f
    2 -> 0.5f
    else -> 0.22f
}

private fun nodeCenter(index: Int, maxWidthPx: Float, rowHeightPx: Float): Offset =
    Offset(x = xFraction(index) * maxWidthPx, y = index * rowHeightPx + rowHeightPx / 2f)

/** Point on the road ~75% of the way from the previous checkpoint into [levelIndex] — "just before" it. */
private fun carMarkerTarget(levelIndex: Int, maxWidthPx: Float, rowHeightPx: Float): Offset {
    val current = nodeCenter(levelIndex, maxWidthPx, rowHeightPx)
    if (levelIndex == 0) return Offset(current.x, current.y - rowHeightPx * 0.4f)
    val previous = nodeCenter(levelIndex - 1, maxWidthPx, rowHeightPx)
    return Offset(
        x = previous.x + (current.x - previous.x) * 0.75f,
        y = previous.y + (current.y - previous.y) * 0.75f
    )
}

/** Rotation (clockwise degrees) so the car marker points along the road's direction of travel. */
private fun carHeadingDegrees(levelIndex: Int, maxWidthPx: Float, rowHeightPx: Float): Float {
    val current = nodeCenter(levelIndex, maxWidthPx, rowHeightPx)
    val previous = if (levelIndex == 0) {
        Offset(current.x, current.y - rowHeightPx)
    } else {
        nodeCenter(levelIndex - 1, maxWidthPx, rowHeightPx)
    }
    val dx = current.x - previous.x
    val dy = current.y - previous.y
    return Math.toDegrees(atan2(dx.toDouble(), -dy.toDouble())).toFloat()
}

@Composable
fun StageSelectScreen(
    regionId: String,
    progress: ProgressRepository,
    onSelectStage: (Int) -> Unit,
    onBack: () -> Unit
) {
    val region = remember(regionId) { CarRepository.regions.first { it.id == regionId } }
    val stages = remember(regionId) { CarRepository.stagesForRegion(regionId) }
    val unlockedLevel = progress.highestUnlockedLevel(regionId)
    val completedCount = remember(regionId, unlockedLevel) {
        (1..CarRepository.LEVELS_PER_REGION).count { progress.rating(regionId, it) > 0 }
    }
    val nextStage = stages.getOrNull((unlockedLevel - 1).coerceIn(0, stages.lastIndex))

    Scaffold(
        containerColor = SurfaceDark,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark.copy(alpha = 0.95f))
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainer)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.cd_back), tint = TextWhite)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(region.flag, fontSize = 16.sp)
                            Spacer(Modifier.width(6.dp))
                            Text(
                                regionDisplayName(region.id).uppercase(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextWhite,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            stringResource(R.string.levels_range, CarRepository.LEVELS_PER_REGION),
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = SurfaceContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceContainerHighest)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("$completedCount/${CarRepository.LEVELS_PER_REGION}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(SurfaceContainerLowest)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(completedCount / CarRepository.LEVELS_PER_REGION.toFloat())
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(PrimaryBlue)
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                color = SurfaceContainer,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Flag, contentDescription = null, tint = PrimaryBlue)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                stringResource(R.string.next_objective),
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                nextStage?.objectiveName ?: stringResource(R.string.region_complete),
                                color = TextWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = { onSelectStage(unlockedLevel) },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(stringResource(R.string.race_button), color = OnPrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = OnPrimaryBlue)
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            PerforatedMeshBackground(modifier = Modifier.fillMaxSize())

            WindingRoadLevelMap(
                regionId = regionId,
                stages = stages,
                unlockedLevel = unlockedLevel,
                ratingFor = { level -> progress.rating(regionId, level) },
                onSelectStage = onSelectStage
            )
        }
    }
}

/** Black metallic perforated mesh / speaker-grill texture, with a soft vignette at the edges. */
@Composable
private fun PerforatedMeshBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val holeSpacing = 15.dp.toPx()
        val holeRadius = 2.4.dp.toPx()

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF221F1F), Color(0xFF0A0A0A)),
                center = center,
                radius = size.maxDimension * 0.8f
            )
        )

        var y = holeSpacing / 2f
        var row = 0
        while (y < size.height) {
            val rowOffset = if (row % 2 == 0) 0f else holeSpacing / 2f
            var x = rowOffset + holeSpacing / 2f
            while (x < size.width) {
                drawCircle(color = Color.Black.copy(alpha = 0.6f), radius = holeRadius, center = Offset(x, y))
                drawCircle(
                    color = Color.White.copy(alpha = 0.035f),
                    radius = holeRadius * 0.45f,
                    center = Offset(x - holeRadius * 0.3f, y - holeRadius * 0.3f)
                )
                x += holeSpacing
            }
            y += holeSpacing * 0.87f
            row++
        }

        // Vignette
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                center = center,
                radius = size.maxDimension * 0.8f
            )
        )
    }
}

@Composable
private fun WindingRoadLevelMap(
    regionId: String,
    stages: List<com.guessmycar.motorsport.data.Stage>,
    unlockedLevel: Int,
    ratingFor: (Int) -> Int,
    onSelectStage: (Int) -> Unit
) {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    val rowHeightPx = with(density) { ROW_HEIGHT.toPx() }
    val contentHeight = ROW_HEIGHT * stages.size

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxWidthPx = with(density) { maxWidth.toPx() }

        var hasScrolledInitially by remember(regionId) { mutableStateOf(false) }
        LaunchedEffect(regionId, maxWidthPx) {
            if (!hasScrolledInitially && maxWidthPx > 0f) {
                val targetY = nodeCenter(unlockedLevel - 1, maxWidthPx, rowHeightPx).y
                val viewportHeightPx = with(density) { maxHeight.toPx() }
                val scrollTarget = (targetY - viewportHeightPx / 2f).roundToInt().coerceAtLeast(0)
                scrollState.scrollTo(scrollTarget)
                hasScrolledInitially = true
            }
        }

        val carOffset = remember(regionId) { Animatable(Offset.Zero, Offset.VectorConverter) }
        var carInitialized by remember(regionId) { mutableStateOf(false) }
        LaunchedEffect(regionId, unlockedLevel, maxWidthPx) {
            if (maxWidthPx <= 0f) return@LaunchedEffect
            val target = carMarkerTarget(unlockedLevel - 1, maxWidthPx, rowHeightPx)
            if (!carInitialized) {
                carOffset.snapTo(target)
                carInitialized = true
            } else {
                carOffset.animateTo(target, animationSpec = tween(700))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(contentHeight)
            ) {
                // Winding asphalt road through every checkpoint
                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (stages.isEmpty()) return@Canvas
                    val roadPath = Path().apply {
                        val first = nodeCenter(0, maxWidthPx, rowHeightPx)
                        moveTo(first.x, first.y)
                        for (i in 0 until stages.size - 1) {
                            val from = nodeCenter(i, maxWidthPx, rowHeightPx)
                            val to = nodeCenter(i + 1, maxWidthPx, rowHeightPx)
                            cubicTo(
                                from.x, from.y + rowHeightPx / 2f,
                                to.x, to.y - rowHeightPx / 2f,
                                to.x, to.y
                            )
                        }
                    }

                    // Drop shadow so the road reads as floating above the mesh background
                    translate(top = 12f) {
                        drawPath(
                            path = roadPath,
                            color = Color.Black.copy(alpha = 0.45f),
                            style = Stroke(width = 98f, cap = StrokeCap.Round)
                        )
                    }

                    // Shoulder
                    drawPath(path = roadPath, color = Color(0xFF2E313A), style = Stroke(width = 90f, cap = StrokeCap.Round))

                    // Asphalt body with a subtle top-to-bottom sheen for a more realistic surface
                    drawPath(
                        path = roadPath,
                        brush = Brush.verticalGradient(listOf(Color(0xFF2C2F38), Color(0xFF15161A))),
                        style = Stroke(width = 75f, cap = StrokeCap.Round)
                    )

                    // Faint specular highlight along the centerline
                    drawPath(
                        path = roadPath,
                        color = Color.White.copy(alpha = 0.035f),
                        style = Stroke(width = 8f, cap = StrokeCap.Round)
                    )

                    // Lane divider — crisp white dashes
                    drawPath(
                        path = roadPath,
                        color = Color(0xFFEFF4FA).copy(alpha = 0.85f),
                        style = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(25f, 25f)))
                    )
                }

                // Checkpoints
                stages.forEachIndexed { index, stage ->
                    val center = nodeCenter(index, maxWidthPx, rowHeightPx)
                    val nodeRadiusPx = with(density) { NODE_RADIUS.toPx() }
                    val rating = ratingFor(stage.levelNumber)
                    val status = when {
                        rating > 0 -> StageStatus.COMPLETED
                        stage.levelNumber == unlockedLevel -> StageStatus.ACTIVE
                        else -> StageStatus.LOCKED
                    }

                    Box(
                        modifier = Modifier.offset {
                            IntOffset(
                                (center.x - nodeRadiusPx).roundToInt(),
                                (center.y - nodeRadiusPx).roundToInt() - with(density) { 18.dp.toPx() }.roundToInt()
                            )
                        }
                    ) {
                        StageNode(
                            number = stage.levelNumber,
                            status = status,
                            tireRating = rating,
                            onClick = { if (status != StageStatus.LOCKED) onSelectStage(stage.levelNumber) }
                        )
                    }
                }

                // Animated player car marker, positioned just before the active checkpoint.
                // Rendered as a tinted vector glyph (no image asset) so there's no background
                // box behind it, and rotated to follow the road's direction of travel.
                if (carInitialized) {
                    val carSizePx = with(density) { 54.dp.toPx() }
                    val heading = carHeadingDegrees((unlockedLevel - 1).coerceAtLeast(0), maxWidthPx, rowHeightPx)
                    Box(
                        modifier = Modifier.offset {
                            IntOffset(
                                (carOffset.value.x - carSizePx / 2f).roundToInt().coerceAtLeast(0),
                                (carOffset.value.y - carSizePx / 2f).roundToInt()
                            )
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        // Soft contact shadow beneath the car
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(y = 14.dp)
                                .size(width = 28.dp, height = 10.dp)
                                .drawBehind {
                                    drawOval(
                                        brush = Brush.radialGradient(
                                            colors = listOf(Color.Black.copy(alpha = 0.45f), Color.Transparent)
                                        )
                                    )
                                }
                        )
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = stringResource(R.string.cd_player_car),
                            tint = PrimaryBlue,
                            modifier = Modifier
                                .size(36.dp)
                                .rotate(heading)
                                .drawBehind {
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            colors = listOf(PrimaryBlue.copy(alpha = 0.35f), Color.Transparent)
                                        )
                                    )
                                }
                        )
                    }
                }
            }
        }
    }
}

enum class StageStatus { COMPLETED, ACTIVE, LOCKED }

@Composable
fun StageNode(number: Int, status: StageStatus, tireRating: Int = 3, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when (status) {
            StageStatus.COMPLETED -> {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHigh)
                        .border(2.dp, PrimaryBlue, CircleShape)
                        .clickable { onClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = stringResource(R.string.cd_completed), tint = PrimaryBlue, modifier = Modifier.size(26.dp))
                }
                Spacer(Modifier.height(4.dp))
                Row {
                    repeat(3) { tireIndex ->
                        Icon(
                            Icons.Default.TireRepair,
                            contentDescription = null,
                            tint = if (tireIndex < tireRating) TachometerAmber else OutlineVariant,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                Text(stringResource(R.string.level_short, number), color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            StageStatus.ACTIVE -> {
                val infiniteTransition = rememberInfiniteTransition(label = "activeGlow")
                val glowAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.3f,
                    targetValue = 0.75f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(900, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "activeGlowAlpha"
                )
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .drawBehind {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(PrimaryBlue.copy(alpha = glowAlpha), Color.Transparent)
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue)
                            .clickable { onClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$number", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = OnPrimaryBlue)
                    }
                }
                Spacer(Modifier.height(4.dp))
                Surface(shape = CircleShape, color = PrimaryBlue) {
                    Text(
                        stringResource(R.string.ready_badge),
                        color = OnPrimaryBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            StageStatus.LOCKED -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.alpha(0.55f)) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerLowest)
                            .border(1.5.dp, OutlineVariant, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Lock, contentDescription = stringResource(R.string.cd_locked), tint = OutlineSteel, modifier = Modifier.size(18.dp))
                            Text("$number", color = OutlineSteel, fontSize = 10.sp)
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(stringResource(R.string.locked_label), color = OutlineVariant, fontSize = 10.sp)
                }
            }
        }
    }
}
