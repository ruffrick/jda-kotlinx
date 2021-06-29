package dev.ruffrick.jda.kotlinx

import dev.ruffrick.jda.kotlinx.event.SuspendEventManager
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder

fun JDABuilder.useSuspendEventManager() = setEventManager(SuspendEventManager())

fun DefaultShardManagerBuilder.useSuspendEventManager() = setEventManagerProvider { SuspendEventManager() }
