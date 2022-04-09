@file:Suppress("unused")

package dev.ruffrick.jda.kotlinx

import dev.ruffrick.jda.kotlinx.event.SuspendEventManager
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder

fun JDABuilder.useSuspendEventManager() = setEventManager(SuspendEventManager())

fun JDABuilder.buildSuspend() = useSuspendEventManager().build()

fun DefaultShardManagerBuilder.useSuspendEventManager() = setEventManagerProvider { SuspendEventManager() }

fun DefaultShardManagerBuilder.buildSuspend() = useSuspendEventManager().build()
