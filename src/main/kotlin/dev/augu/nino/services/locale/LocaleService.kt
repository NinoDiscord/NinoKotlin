package dev.augu.nino.services.locale

import dev.augu.nino.common.entities.Locale
import dev.augu.nino.configuration.Configuration

class LocaleService(
        override val locales: List<Locale>,
        config: Configuration
) : ILocaleService {
    private val defaultLanguage: String = config.base.defaultLanguage

    /**
     * Get coverage of a single language
     * @param locale The locale to get coverage from
     */
    override fun coverage(locale: Locale): Int {
        val language = locales.find { it.code == defaultLanguage }!!
        return (locale.translations.size / language.translations.size) * 100
    }
}
