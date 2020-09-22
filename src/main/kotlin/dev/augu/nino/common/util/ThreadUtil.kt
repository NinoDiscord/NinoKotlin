package dev.augu.nino.common.util

/**
 * Initialises a new [java.lang.Thread] easily
 * @param name The name of the Thread
 * @param block The block to execute when the Thread is executed
 * @return A [java.lang.Thread] instance
 */
fun createThread(name: String, block: () -> Unit): Thread = (object: Thread(name) {
    override fun run() {
        block()
    }
})