package com.guessmycar.motorsport.locale

import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.guessmycar.motorsport.R
import java.util.Locale

/**
 * One entry in the Language picker. [code] is the value persisted to [com.guessmycar.motorsport.data.ProgressRepository.languageTag]
 * and MUST be one of the standard Android locale qualifiers below — never a display name, never "in" for Indonesian.
 * [nativeName] is never translated — it's always shown in its own language regardless of the active app language.
 */
data class AppLanguage(
    val code: String,
    val flag: String,
    val nativeName: String
)

val supportedLanguages = listOf(
    AppLanguage(code = "id", flag = "🇮🇩", nativeName = "Bahasa Indonesia"),
    AppLanguage(code = "en", flag = "🇺🇸", nativeName = "English (US)"),
    AppLanguage(code = "en-GB", flag = "🇬🇧", nativeName = "English (UK)"),
    AppLanguage(code = "ja", flag = "🇯🇵", nativeName = "日本語"),
    AppLanguage(code = "de", flag = "🇩🇪", nativeName = "Deutsch"),
    AppLanguage(code = "fr", flag = "🇫🇷", nativeName = "Français"),
    AppLanguage(code = "it", flag = "🇮🇹", nativeName = "Italiano"),
    AppLanguage(code = "ko", flag = "🇰🇷", nativeName = "한국어"),
    AppLanguage(code = "zh-CN", flag = "🇨🇳", nativeName = "简体中文")
)

fun languageFor(code: String): AppLanguage =
    supportedLanguages.firstOrNull { it.code == code } ?: supportedLanguages.first { it.code == "en" }

/**
 * Explicit, unambiguous locale per supported code.
 *
 * On-device testing found the opposite of what's usually assumed: for every language EXCEPT
 * Indonesian, the two-arg [Locale] constructor (`Locale("de", "DE")` etc.) correctly resolves its
 * `values-xx` resource folder (verified live on a physical Android 15 device — German switched
 * correctly). Only Indonesian failed with that constructor. The two-arg constructor is a legacy Java
 * API that, for a handful of languages (Indonesian "id"/"in", Hebrew "he"/"iw", Yiddish "yi"/"ji"),
 * canonicalizes the language code to its old ISO 639 alias internally — so `Locale("id", "ID")` can
 * resolve to language code "in" at runtime, which no longer matches the `values-id` folder (compiled
 * with the modern "id" qualifier), silently falling back to default (English) strings.
 * [Locale.forLanguageTag] parses the BCP-47 tag directly and does not go through that legacy alias
 * table, so it's used here specifically for Indonesian; the two-arg constructor is kept for every
 * other language since it was verified working as-is.
 */
fun localeForCode(code: String): Locale = when (code) {
    "id" -> Locale.forLanguageTag("id-ID")
    "en" -> Locale("en", "US")
    "en-GB" -> Locale("en", "GB")
    "ja" -> Locale("ja", "JP")
    "de" -> Locale("de", "DE")
    "fr" -> Locale("fr", "FR")
    "it" -> Locale("it", "IT")
    "ko" -> Locale("ko", "KR")
    "zh-CN" -> Locale("zh", "CN")
    else -> Locale("en", "US")
}

/**
 * Applies [languageCode] as the app's active locale, immediately and without an Activity restart.
 *
 * API 33+ (the device this was verified on): calls the platform `LocaleManager` directly — this is
 * Android's own per-app-language system, the authoritative owner of the app's locale on these versions.
 * Going straight to this API needs nothing more than a plain `Context`; it does NOT require
 * `AppCompatActivity`. (Two earlier approaches were tried and rejected: (1) a manual
 * `Context.createConfigurationContext` / `Resources` override — on-device testing proved Android 13+
 * silently overrides that, even a direct `Resources.updateConfiguration()` call kept resolving English
 * despite a verified-correct `Configuration(locale=id_ID)`; (2) `AppCompatDelegate.setApplicationLocales()`
 * — this needs an `AppCompatDelegate` instance, which only gets created automatically by
 * `AppCompatActivity`, and switching `MainActivity` to `AppCompatActivity` crashed on startup, then again
 * inside `NavHost` — see git history / prior session notes. Neither problem applies to calling
 * `LocaleManager` directly from a plain `ComponentActivity`.)
 *
 * Below API 33, there's no platform per-app-language system to go through, so [ProvideAppLocaleLegacy]
 * (a manual Configuration/Resources override) is used instead — and unlike on API 33+, there's no
 * competing OS-level authority to silently override it there.
 */
fun applyAppLocale(context: Context, languageCode: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val locale = localeForCode(languageCode)
        context.getSystemService(LocaleManager::class.java)?.applicationLocales = LocaleList(locale)
    }
    // Below API 33: handled reactively by ProvideAppLocaleLegacy wrapping MainActivity's content.
}

/**
 * API < 33 fallback only — see [applyAppLocale]. Overrides [LocalContext]/[LocalConfiguration] for
 * everything composed inside [content] so [stringResource] picks up [languageCode] immediately.
 */
@Composable
fun ProvideAppLocaleLegacy(languageCode: String, content: @Composable () -> Unit) {
    val baseContext = LocalContext.current
    val localizedContext = remember(languageCode, baseContext) {
        val locale = localeForCode(languageCode)
        val config = Configuration(baseContext.resources.configuration)
        config.setLocale(locale)
        config.setLocales(LocaleList(locale))
        baseContext.createConfigurationContext(config)
    }
    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides localizedContext.resources.configuration
    ) {
        content()
    }
}

/** Localized display name for one of [com.guessmycar.motorsport.data.CarRepository.regions] (country/region names only — not the marketing taglines). */
@Composable
fun regionDisplayName(regionId: String): String {
    val resId = when (regionId) {
        "cn" -> R.string.region_name_cn
        "us" -> R.string.region_name_us
        "jp" -> R.string.region_name_jp
        "de" -> R.string.region_name_de
        "kr" -> R.string.region_name_kr
        "it" -> R.string.region_name_it
        "fr" -> R.string.region_name_fr
        "gb" -> R.string.region_name_gb
        else -> null
    }
    return resId?.let { stringResource(it) } ?: regionId
}
