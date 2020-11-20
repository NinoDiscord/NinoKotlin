package dev.augu.nino.services.locale

import dev.augu.nino.common.entities.Locale

interface ILocaleService {
    val locales: List<Locale>
    val defaultLocale: Locale

    fun coverage(locale: Locale): Int
}
