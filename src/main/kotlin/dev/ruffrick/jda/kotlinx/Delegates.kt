package dev.ruffrick.jda.kotlinx

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

object Logger {
    operator fun getValue(thisRef: Any, property: KProperty<*>): Logger = LoggerFactory.getLogger(thisRef::class.java)
}

object Env {
    private val regex0 = Regex("\\W+")
    private val regex1 = Regex("\\s+|\\B(?=[A-Z])")

    private fun getEnv(thisRef: Any, property: KProperty<*>): String? {
        val prefix = thisRef::class.simpleName!!.replace(regex0, " ").split(regex1).joinToString("_")
        val name = property.name.replace(regex0, " ").split(regex1).joinToString("_")
        return System.getenv("${prefix.uppercase()}_${name.uppercase()}")
    }

    operator fun getValue(thisRef: Any, property: KProperty<*>) = getEnv(thisRef, property)

    object Byte {
        operator fun getValue(thisRef: Any, property: KProperty<*>) = getEnv(thisRef, property)?.toByte()
    }

    object Short {
        operator fun getValue(thisRef: Any, property: KProperty<*>) = getEnv(thisRef, property)?.toShort()
    }

    object Int {
        operator fun getValue(thisRef: Any, property: KProperty<*>) = getEnv(thisRef, property)?.toInt()
    }

    object Long {
        operator fun getValue(thisRef: Any, property: KProperty<*>) = getEnv(thisRef, property)?.toLong()
    }

    object Float {
        operator fun getValue(thisRef: Any, property: KProperty<*>) = getEnv(thisRef, property)?.toFloat()
    }

    object Double {
        operator fun getValue(thisRef: Any, property: KProperty<*>) = getEnv(thisRef, property)?.toDouble()
    }

    object Boolean {
        operator fun getValue(thisRef: Any, property: KProperty<*>) = getEnv(thisRef, property)?.toBoolean()
    }
}
