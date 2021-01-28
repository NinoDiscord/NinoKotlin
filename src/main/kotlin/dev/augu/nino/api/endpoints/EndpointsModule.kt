package dev.augu.nino.api.endpoints

import dev.augu.nino.api.Endpoint
import org.koin.dsl.bind
import org.koin.dsl.module

val endpointsModule = module {
    single { MainEndpoint() } bind Endpoint::class
}
