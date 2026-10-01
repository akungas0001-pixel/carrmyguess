package com.guessmycar.motorsport.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.guessmycar.motorsport.R
import com.guessmycar.motorsport.data.Car
import com.guessmycar.motorsport.data.CarImageRepository
import com.guessmycar.motorsport.ui.theme.OutlineSteel
import com.guessmycar.motorsport.ui.theme.OutlineVariant
import com.guessmycar.motorsport.ui.theme.SurfaceContainerHigh
import com.guessmycar.motorsport.ui.theme.TextMuted

/**
 * Menampilkan foto [car]:
 *  1. File lokal res/drawable/<imageResource tanpa ekstensi>, jika ada (selalu diprioritaskan).
 *  2. Jika tidak ada, foto dari Wikimedia Commons (lihat CarImageSources / CarImageRepository).
 *     Coil menyimpan foto di cache disk, jadi setelah dimuat sekali tetap tampil saat offline.
 *  3. Kartu placeholder saat memuat atau bila foto tidak tersedia.
 */
@Composable
fun CarImage(
    car: Car,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val resId = remember(car.imageResource) {
        val resName = car.imageResource.substringBeforeLast('.')
        context.resources.getIdentifier(resName, "drawable", context.packageName)
    }

    if (resId != 0) {
        SubcomposeAsyncImage(
            model = resId,
            contentDescription = car.displayName,
            contentScale = contentScale,
            modifier = modifier
        )
        return
    }

    var url by remember(car.id) { mutableStateOf<String?>(null) }
    var lookupDone by remember(car.id) { mutableStateOf(false) }

    LaunchedEffect(car.id) {
        url = CarImageRepository.get(context).imageUrlFor(car)
        lookupDone = true
    }

    val loadingText = stringResource(R.string.label_loading)
    val unavailableText = stringResource(R.string.image_unavailable)

    val currentUrl = url
    when {
        currentUrl != null -> SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(currentUrl)
                .setHeader("User-Agent", CarImageRepository.USER_AGENT)
                .crossfade(true)
                .build(),
            contentDescription = car.displayName,
            contentScale = contentScale,
            modifier = modifier,
            loading = { CarImagePlaceholder(loadingText, Modifier.fillMaxSize()) },
            error = { CarImagePlaceholder(unavailableText, Modifier.fillMaxSize()) }
        )
        !lookupDone -> CarImagePlaceholder(loadingText, modifier)
        else -> CarImagePlaceholder(unavailableText, modifier)
    }
}

@Composable
private fun CarImagePlaceholder(status: String, modifier: Modifier) {
    Box(modifier.background(SurfaceContainerHigh), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.DirectionsCar,
                contentDescription = null,
                tint = OutlineSteel,
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                // Nama mobil sengaja tidak ditampilkan agar jawaban kuis tidak bocor
                stringResource(R.string.brand_name_caps),
                color = TextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                status,
                color = OutlineVariant,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}
