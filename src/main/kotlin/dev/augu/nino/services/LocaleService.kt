package dev.augu.nino.services

import dev.augu.nino.common.entities.Locale

class LocaleService(val locales: List<Locale>) {
    init {
        println(locales.size)
    }
}