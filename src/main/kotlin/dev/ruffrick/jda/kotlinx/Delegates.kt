package dev.ruffrick.jda.kotlinx

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

object Logger {

    operator fun getValue(thisRef: Any, property: KProperty<*>): Logger = LoggerFactory.getLogger(thisRef::class.java)

}
