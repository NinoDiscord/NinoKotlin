package dev.augu.nino.configuration

import com.charleskorn.kaml.Yaml
import java.io.File
import org.koin.dsl.module

val configurationModule = module {
    single {
        val configFile = File(getPropertyOrNull("configurationFile") ?: "config.yml")

        Yaml.default.decodeFromString(Configuration.serializer(), configFile.readText())
    }
}
