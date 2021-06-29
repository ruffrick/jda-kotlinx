package dev.ruffrick.jda.kotlinx.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

object EventScope : CoroutineScope {

    override val coroutineContext = Executors.newWorkStealingPool().asCoroutineDispatcher()

}
