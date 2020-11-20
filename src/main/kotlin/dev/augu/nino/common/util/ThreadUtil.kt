package dev.augu.nino.common.util

/**
 * Initialises a new [java.lang.Thread] easily, using a simple inline function
 *
 * @param name The name of the Thread
 * @param block The block to execute when the Thread is executed
 * @return A [java.lang.Thread] instance that will run when called.
 */
fun createThread(name: String, block: () -> Unit): Thread = Thread(block, name)
