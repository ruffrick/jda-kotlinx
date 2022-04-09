@file:Suppress("unused")

package dev.ruffrick.jda.kotlinx

import kotlinx.coroutines.CompletableDeferred
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.utils.concurrent.Task

suspend inline fun <T> RestAction<T>.await() = async().await()

fun <T> RestAction<T>.async() = CompletableDeferred<T?>().apply {
    queue(
        { complete(it) },
        { complete(null) }
    )
}

suspend inline fun <T> Task<T>.await() = async().await()

fun <T> Task<T>.async() = CompletableDeferred<T?>().apply {
    onSuccess { complete(it) }
    onError { complete(null) }
}
