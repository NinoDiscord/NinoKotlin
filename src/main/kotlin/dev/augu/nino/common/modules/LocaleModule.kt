package dev.augu.nino.common.modules

import dev.augu.nino.common.entities.Locale
import java.io.File
import org.koin.dsl.module

val localeModule = module {
    factory {
        val directory = File(getPropertyOrNull("localeDirectory") ?: "locales")
        if (!directory.exists() || !directory.isDirectory) {
            return@factory arrayListOf<Locale>()
        }
        (directory
                .listFiles { _, s ->
                    s.endsWith(".yml") || s.endsWith(".yaml")
                }
                ?: arrayOf())
                .map { file -> Locale.fromFile(file) }
    }
}
