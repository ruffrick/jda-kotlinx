@file:Suppress("unused")

package dev.ruffrick.jda.kotlinx.event

import dev.ruffrick.jda.kotlinx.LogFactory
import net.dv8tion.jda.api.events.GenericEvent

abstract class SuspendEventListener {
    protected val log by LogFactory

    abstract suspend fun onEvent(event: GenericEvent)
}
