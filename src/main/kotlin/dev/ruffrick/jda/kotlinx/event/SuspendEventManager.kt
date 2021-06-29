package dev.ruffrick.jda.kotlinx.event

import dev.ruffrick.jda.kotlinx.Logger
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.IEventManager

class SuspendEventManager : IEventManager {

    private var listeners = emptyArray<SuspendEventListener>()
    private val log by Logger

    override fun register(listener: Any) {
        val listeners = listeners.copyOf(listeners.size + 1)
        listeners[listeners.lastIndex] = listener as? SuspendEventListener
            ?: throw IllegalArgumentException("Listener must extend SuspendEventListener")
        @Suppress("UNCHECKED_CAST")
        this.listeners = listeners as Array<SuspendEventListener>
    }

    override fun unregister(listener: Any) {
        for (index in listeners.indices) {
            if (listener == listeners[index]) {
                val listeners = arrayOfNulls<SuspendEventListener>(listeners.size - 1)
                this.listeners.copyInto(listeners, 0, 0, index)
                if (index < listeners.size)
                    this.listeners.copyInto(listeners, index, index + 1, this.listeners.size)
                @Suppress("UNCHECKED_CAST")
                this.listeners = listeners as Array<SuspendEventListener>
                return
            }
        }
    }

    override fun handle(event: GenericEvent) {
        EventScope.launch {
            listeners.forEach {
                try {
                    it.onEvent(event)
                } catch (t: Throwable) {
                    log.error(t.message)
                    t.printStackTrace()
                }
            }
        }
    }

    override fun getRegisteredListeners(): MutableList<Any> {
        return listeners.toMutableList()
    }

}
