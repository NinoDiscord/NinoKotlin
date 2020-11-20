package dev.augu.nino.common.entities

import com.charleskorn.kaml.Yaml
import dev.augu.nino.butterfly.i18n.I18nLanguage
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class Locale(
    val name: String,
    val code: String,
    val flag: String,
    val coverage: Int,
    val contributors: List<String>,
    val translations: Map<String, String>
) {
    val translateCount: Int = 100
    fun toLanguage(): I18nLanguage = I18nLanguage(name, translations)

    companion object {
        fun fromFile(file: File): Locale = Yaml.default.decodeFromString(serializer(), file.readText())
    }
}
