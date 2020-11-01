package dev.augu.nino.services

import dev.augu.nino.common.entities.Locale
import dev.augu.nino.configuration.Configuration

class LocaleService(
        val locales: List<Locale>,
        val config: Configuration
) {
    /**
     * Get coverage of a single language
     * @param locale The locale to get coverage from
     */
    fun coverage(locale: Locale): Int {
        val language = locales.find { it.code == config.base.defaultLanguage }!!
        return (locale.translations.size / language.translations.size) * 100
    }
}
