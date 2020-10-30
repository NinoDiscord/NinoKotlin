package dev.augu.nino.common.entities

import dev.augu.nino.butterfly.i18n.I18nLanguage
import kotlinx.serialization.Serializable
import com.charleskorn.kaml.Yaml

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
    fun toLanguage(): I18nLanguage = I18nLanguage(name, translations)

    companion object {
        fun fromFile(file: File): Locale = Yaml.default.parse(serializer(), file.readText())
    }
}