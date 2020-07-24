package dev.augu.nino.configuration

import com.charleskorn.kaml.Yaml
import org.koin.dsl.module
import java.io.File

val configurationModule = module {
    single {
        val configFile = File(getPropertyOrNull("configurationFile") ?: "config.yml")

        Yaml.default.parse(Configuration.serializer(), configFile.readText())
    }
}