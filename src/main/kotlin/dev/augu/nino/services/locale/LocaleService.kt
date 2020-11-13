package dev.augu.nino.services.locale

import dev.augu.nino.common.entities.Locale
import dev.augu.nino.configuration.Configuration
import kotlin.math.roundToInt

class LocaleService(
        override val locales: List<Locale>,
        config: Configuration
) : ILocaleService {
    override val defaultLocale: Locale = locales.first { it.code == config.base.defaultLanguage }

    /**
     * Get coverage of a single language
     * @param locale The locale to get coverage from
     */
    override fun coverage(locale: Locale): Int {
        return ((locale.translations.size.toDouble() / defaultLocale.translations.size.toDouble()) * 100).roundToInt()
    }
}
