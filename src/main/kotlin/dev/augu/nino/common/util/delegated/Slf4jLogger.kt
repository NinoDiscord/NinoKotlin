package dev.augu.nino.common.util.delegated

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Provides a wrapper to created a delegated property for [org.slf4j.Logger],
 * which is read-only.
 *
 * @param baseCls The base class to delegate the property from
 */
class DelegatedSlf4jLogger(private val baseCls: Class<*>): ReadOnlyProperty<Any?, Logger> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Logger =
        LoggerFactory.getLogger(baseCls)
}

/**
 * Inline function to create a [DelegatedSlf4jLogger] instance
 * @param baseCls The base class to delegate the property from
 * @returns The instance of [DelegatedSlf4jLogger]
 */
fun logging(baseCls: Class<*>): DelegatedSlf4jLogger =
    DelegatedSlf4jLogger(baseCls)
