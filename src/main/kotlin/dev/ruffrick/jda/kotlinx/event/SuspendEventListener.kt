package dev.ruffrick.jda.kotlinx.event

import dev.ruffrick.jda.kotlinx.Logger
import net.dv8tion.jda.api.events.GenericEvent

abstract class SuspendEventListener {
    protected val log by Logger

    abstract suspend fun onEvent(event: GenericEvent)
}
