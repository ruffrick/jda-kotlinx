package dev.ruffrick.jda.kotlinx

import dev.ruffrick.jda.kotlinx.event.SuspendEventListener
import kotlinx.coroutines.delay
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.sharding.ShardManager

inline fun <reified T : GenericEvent> ShardManager.on(
    crossinline consumer: suspend T.() -> Unit
) = object : SuspendEventListener() {
    override suspend fun onEvent(event: GenericEvent) {
        if (event is T) {
            consumer(event)
        }
    }
}.also { addEventListener(it) }

suspend inline fun <reified T : GenericEvent> ShardManager.await(
    crossinline predicate: T.() -> Boolean = { true },
    limit: Int = 1,
    timeout: Long = 30_000,
    crossinline onTimeout: () -> Unit = { },
    crossinline consumer: suspend T.() -> Unit
): SuspendEventListener {
    require(timeout > 0) { "Timeout must be > 0!" }
    require(limit > 0) { "Limit must be > 0!" }

    var count = 0
    return object : SuspendEventListener() {
        override suspend fun onEvent(event: GenericEvent) {
            if (event is T && predicate(event)) {
                if (++count >= limit)
                    removeEventListener(this)
                consumer(event)
            }
        }
    }.also {
        addEventListener(it)
        delay(timeout)
        removeEventListener(it)
        onTimeout()
    }
}

inline fun <reified T : GenericEvent> JDA.on(
    crossinline consumer: suspend T.() -> Unit
) = object : SuspendEventListener() {
    override suspend fun onEvent(event: GenericEvent) {
        if (event is T) {
            consumer(event)
        }
    }
}.also { addEventListener(it) }

suspend inline fun <reified T : GenericEvent> JDA.await(
    crossinline predicate: T.() -> Boolean = { true },
    limit: Int = 1,
    timeout: Long = 30_000,
    crossinline onTimeout: () -> Unit = { },
    crossinline consumer: suspend T.() -> Unit
): SuspendEventListener {
    require(timeout > 0) { "Timeout must be > 0!" }
    require(limit > 0) { "Limit must be > 0!" }

    var count = 0
    return object : SuspendEventListener() {
        override suspend fun onEvent(event: GenericEvent) {
            if (event is T && predicate(event)) {
                if (++count >= limit)
                    removeEventListener(this)
                consumer(event)
            }
        }
    }.also {
        addEventListener(it)
        delay(timeout)
        removeEventListener(it)
        onTimeout()
    }
}

suspend inline fun MessageChannel.awaitMessage(
    crossinline predicate: MessageReceivedEvent.() -> Boolean = { true },
    limit: Int = 1,
    timeout: Long = 30_000,
    crossinline onTimeout: () -> Unit = { },
    crossinline consumer: suspend MessageReceivedEvent.() -> Unit
) = jda.await(
    { channel.idLong == idLong && !author.isBot && !message.isWebhookMessage && predicate(this) },
    limit, timeout, onTimeout, consumer
)

suspend inline fun Message.awaitReaction(
    crossinline predicate: MessageReactionAddEvent.() -> Boolean = { true },
    limit: Int = 1,
    timeout: Long = 30_000,
    crossinline onTimeout: () -> Unit = { },
    crossinline consumer: suspend MessageReactionAddEvent.() -> Unit
) = jda.await({ messageIdLong == idLong && predicate(this) }, limit, timeout, onTimeout, consumer)
