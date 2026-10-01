package com.guessmycar.motorsport.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.guessmycar.motorsport.R

/**
 * Short audio/haptic cues for the quiz. A single app-wide instance so the "ting" sample is
 * decoded once (SoundPool) rather than reloaded per screen.
 *
 * Correct answer: a short SoundPool "ting" (low latency, no MediaPlayer buffering stutter).
 * Wrong answer: a short one-shot vibration, never a sound — the quiz screen's own click guard
 * (buttons disable themselves while [isCorrect] is true or the wrong-answer banner is showing)
 * is what keeps either cue from double-firing on a fast repeat tap; this class doesn't need its
 * own debounce on top of that.
 */
class GameFeedback private constructor(context: Context) {

    private val appContext = context.applicationContext

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(2)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    @Volatile private var correctSoundId = 0
    @Volatile private var correctSoundLoaded = false

    init {
        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (sampleId == correctSoundId && status == 0) correctSoundLoaded = true
        }
        correctSoundId = soundPool.load(appContext, R.raw.correct_answer, 1)
    }

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    /** Plays the correct-answer "ting". No-op if [soundEnabled] is false or the sample hasn't loaded yet. */
    fun playCorrect(soundEnabled: Boolean) {
        if (!soundEnabled || !correctSoundLoaded) return
        soundPool.play(correctSoundId, 1f, 1f, /* priority = */ 1, /* loop = */ 0, /* rate = */ 1f)
    }

    /** Short wrong-answer buzz (~60ms). No-op if [hapticsEnabled] is false or the device has no vibrator. */
    fun vibrateWrong(hapticsEnabled: Boolean) {
        if (!hapticsEnabled) return
        val v = vibrator ?: return
        if (!v.hasVibrator()) return
        v.vibrate(VibrationEffect.createOneShot(60L, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    companion object {
        @Volatile private var instance: GameFeedback? = null

        fun get(context: Context): GameFeedback =
            instance ?: synchronized(this) {
                instance ?: GameFeedback(context.applicationContext).also { instance = it }
            }
    }
}
