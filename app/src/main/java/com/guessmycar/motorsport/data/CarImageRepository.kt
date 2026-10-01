package com.guessmycar.motorsport.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.ConcurrentHashMap

/**
 * Mencari URL foto mobil dari Wikipedia / Wikimedia Commons.
 *
 * Urutan pencarian:
 *  1. Cache (SharedPreferences) — setiap mobil cukup dicari sekali seumur instalasi.
 *  2. Foto utama artikel Wikipedia sesuai [CarImageSources].
 *  3. Kalau tidak ada, pencarian Wikipedia berdasarkan nama mobil.
 *
 * Wikimedia mewajibkan header User-Agent yang jelas; tanpa itu permintaan bisa ditolak (HTTP 403).
 */
class CarImageRepository private constructor(context: Context) {

    private val prefs = context.getSharedPreferences("car_image_urls", Context.MODE_PRIVATE)
    private val memory = ConcurrentHashMap<Int, String>()

    suspend fun imageUrlFor(car: Car): String? {
        memory[car.id]?.let { return it }
        prefs.getString(key(car), null)?.let { memory[car.id] = it; return it }

        val url = withContext(Dispatchers.IO) {
            runCatching { byTitle(CarImageSources.titleFor(car)) }.getOrNull()
                ?: runCatching { bySearch("${car.displayName} car") }.getOrNull()
                ?: runCatching { bySearch(car.displayName) }.getOrNull()
        }
        if (url != null) {
            memory[car.id] = url
            prefs.edit().putString(key(car), url).apply()
        }
        return url
    }

    /** Hapus cache satu mobil (misalnya setelah judul di CarImageSources diganti). */
    fun clear(car: Car) {
        memory.remove(car.id)
        prefs.edit().remove(key(car)).apply()
    }

    private fun key(car: Car) = "${car.id}|${CarImageSources.titleFor(car)}"

    private fun byTitle(title: String): String? =
        firstThumbnail("$API&redirects=1&titles=${enc(title)}")

    private fun bySearch(query: String): String? =
        firstThumbnail("$API&generator=search&gsrnamespace=0&gsrlimit=1&gsrsearch=${enc(query)}")

    private fun firstThumbnail(url: String): String? {
        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            setRequestProperty("User-Agent", USER_AGENT)
            setRequestProperty("Accept", "application/json")
            connectTimeout = 15_000
            readTimeout = 20_000
        }
        try {
            if (conn.responseCode != 200) return null
            val body = conn.inputStream.bufferedReader().use { it.readText() }
            val pages = JSONObject(body).optJSONObject("query")?.optJSONArray("pages") ?: return null
            for (i in 0 until pages.length()) {
                val src = pages.getJSONObject(i).optJSONObject("thumbnail")?.optString("source")
                if (!src.isNullOrBlank() && !src.endsWith(".svg.png")) return src
            }
            return null
        } finally {
            conn.disconnect()
        }
    }

    private fun enc(s: String) = URLEncoder.encode(s, "UTF-8")

    companion object {
        /** Ganti email dengan email Anda — ini syarat dari Wikimedia. */
        const val USER_AGENT = "akungas.0001@gmail.com"

        private const val API = "https://en.wikipedia.org/w/api.php?action=query&format=json" +
            "&formatversion=2&prop=pageimages&piprop=thumbnail&pithumbsize=800"

        @Volatile private var instance: CarImageRepository? = null

        fun get(context: Context): CarImageRepository =
            instance ?: synchronized(this) {
                instance ?: CarImageRepository(context.applicationContext).also { instance = it }
            }
    }
}
