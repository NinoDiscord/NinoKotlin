package dev.augu.nino.testutils

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.koin.KoinListener
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.mock.Provider

abstract class KoinDescribeSpec(private val module: List<Module> = testModule, private val mockProvider: Provider<*>? = null) : DescribeSpec(), KoinTest {
    override fun listeners(): List<TestListener> = listOf(KoinListener(module, mockProvider))
}
