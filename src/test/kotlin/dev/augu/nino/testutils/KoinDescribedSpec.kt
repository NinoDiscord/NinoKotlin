package dev.augu.nino.testutils

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.koin.KoinListener
import org.koin.test.KoinTest
import org.koin.test.mock.Provider

abstract class KoinDescribeSpec(private val mockProvider: Provider<*>? = null) : DescribeSpec(), KoinTest {
    override fun listeners(): List<TestListener> = listOf(KoinListener(testModule, mockProvider))
}
