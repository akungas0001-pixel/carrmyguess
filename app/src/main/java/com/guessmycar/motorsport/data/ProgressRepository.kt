package com.guessmycar.motorsport.data

import android.content.Context

class ProgressRepository(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var selectedRegionId: String?
        get() = prefs.getString(KEY_SELECTED_REGION, null)
        set(value) {
            prefs.edit().putString(KEY_SELECTED_REGION, value).apply()
        }

    var soundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND_ENABLED, true)
        set(value) {
            prefs.edit().putBoolean(KEY_SOUND_ENABLED, value).apply()
        }

    var hapticsEnabled: Boolean
        get() = prefs.getBoolean(KEY_HAPTICS_ENABLED, true)
        set(value) {
            prefs.edit().putBoolean(KEY_HAPTICS_ENABLED, value).apply()
        }

    var languageTag: String
        get() = prefs.getString(KEY_LANGUAGE_TAG, DEFAULT_LANGUAGE_TAG) ?: DEFAULT_LANGUAGE_TAG
        set(value) {
            prefs.edit().putString(KEY_LANGUAGE_TAG, value).apply()
        }

    fun highestUnlockedLevel(regionId: String): Int =
        prefs.getInt(unlockedKey(regionId), 1)

    fun rating(regionId: String, level: Int): Int =
        prefs.getInt(ratingKey(regionId, level), 0)

    fun isCompleted(regionId: String, level: Int): Boolean = rating(regionId, level) > 0

    fun isUnlocked(regionId: String, level: Int): Boolean = level <= highestUnlockedLevel(regionId)

    fun totalPoints(): Int = prefs.getInt(KEY_POINTS, 0)

    fun completedCount(regionIds: List<String> = CarRepository.regions.map { it.id }): Int =
        regionIds.sumOf { regionId -> (1..CarRepository.LEVELS_PER_REGION).count { rating(regionId, it) > 0 } }

    fun highestRatingAchieved(regionIds: List<String> = CarRepository.regions.map { it.id }): Int {
        var highest = 0
        for (regionId in regionIds) {
            for (level in 1..CarRepository.LEVELS_PER_REGION) {
                highest = maxOf(highest, rating(regionId, level))
            }
        }
        return highest
    }

    data class LevelResult(
        val tireRating: Int,
        val pointsEarned: Int,
        val isNewUnlock: Boolean
    )

    fun recordLevelResult(regionId: String, level: Int, wrongAttempts: Int): LevelResult {
        val newRating = tireRatingFor(wrongAttempts)
        val prevRating = rating(regionId, level)

        var pointsEarned = 0
        if (newRating > prevRating) {
            prefs.edit().putInt(ratingKey(regionId, level), newRating).apply()
            pointsEarned = pointsFor(newRating) - pointsFor(prevRating)
            if (pointsEarned != 0) {
                prefs.edit().putInt(KEY_POINTS, totalPoints() + pointsEarned).apply()
            }
        }

        var isNewUnlock = false
        val unlocked = highestUnlockedLevel(regionId)
        if (level == unlocked && level < CarRepository.LEVELS_PER_REGION) {
            prefs.edit().putInt(unlockedKey(regionId), level + 1).apply()
            isNewUnlock = true
        }

        return LevelResult(
            tireRating = maxOf(newRating, prevRating),
            pointsEarned = pointsEarned,
            isNewUnlock = isNewUnlock
        )
    }

    fun resetAll() {
        prefs.edit().clear().apply()
    }

    private fun unlockedKey(regionId: String) = "unlocked_$regionId"
    private fun ratingKey(regionId: String, level: Int) = "rating_${regionId}_$level"

    companion object {
        private const val PREFS_NAME = "guess_my_car_progress"
        private const val KEY_SELECTED_REGION = "selected_region"
        private const val KEY_POINTS = "points"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_HAPTICS_ENABLED = "haptics_enabled"
        private const val KEY_LANGUAGE_TAG = "language_tag"
        const val DEFAULT_LANGUAGE_TAG = "en"

        fun tireRatingFor(wrongAttempts: Int): Int = when {
            wrongAttempts <= 0 -> 3
            wrongAttempts == 1 -> 2
            else -> 1
        }

        fun pointsFor(tireRating: Int): Int = when (tireRating) {
            3 -> 150
            2 -> 100
            1 -> 50
            else -> 0
        }
    }
}
