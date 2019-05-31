package org.web3j.aion.abi

import kotlin.math.pow
import kotlin.reflect.KClass

internal inline fun <reified T : Number> min(unsigned: Boolean = false): T {
    return when (T::class) {
        Byte::class -> Byte.MIN_VALUE.convertTo(T::class)
        Short::class -> Short.MIN_VALUE.convertTo(T::class)
        Int::class -> Int.MIN_VALUE.convertTo(T::class)
        Long::class -> Long.MIN_VALUE.convertTo(T::class)
        Float::class -> Float.MIN_VALUE.convertTo(T::class)
        Double::class -> Double.MIN_VALUE.convertTo(T::class)
        else -> throw ArithmeticException("unknown number type: ${T::class}")
    }
}

internal inline fun <reified T : Number> min(bitSize: Int, unsigned: Boolean = false): T {
    return when (bitSize) {
        Byte.SIZE_BITS -> Byte.MIN_VALUE.convertTo(T::class)
        Short.SIZE_BITS -> Short.MIN_VALUE.convertTo(T::class)
        Int.SIZE_BITS -> Int.MIN_VALUE.convertTo(T::class)
        Long.SIZE_BITS -> Long.MIN_VALUE.convertTo(T::class)
        // Because is test code, this calculation is made for 64-256 bit sizes
        else -> 2.toDouble().pow(bitSize - 1).unaryMinus().convertTo(T::class)
    }
}

internal inline fun <reified T : Number> max(unsigned: Boolean = false): T {
    return when (T::class) {
        Byte::class -> Byte.MAX_VALUE.convertTo(T::class)
        Short::class -> Short.MAX_VALUE.convertTo(T::class)
        Int::class -> Int.MAX_VALUE.convertTo(T::class)
        Long::class -> Long.MAX_VALUE.convertTo(T::class)
        Float::class -> Float.MAX_VALUE.convertTo(T::class)
        Double::class -> Double.MAX_VALUE.convertTo(T::class)
        else -> throw ArithmeticException("unknown number type: ${T::class}")
    }
}

internal inline fun <reified T : Number> max(bitSize: Int, unsigned: Boolean = false): T {
    return when (bitSize) {
        Byte.SIZE_BITS -> Byte.MAX_VALUE.convertTo(T::class)
        Short.SIZE_BITS -> Short.MAX_VALUE.convertTo(T::class)
        Int.SIZE_BITS -> Int.MAX_VALUE.convertTo(T::class)
        Long.SIZE_BITS -> Long.MAX_VALUE.convertTo(T::class)
        // Because is test code, this calculation is made for 64-256 bit sizes
        // FIXME Convert to Long to avoid Double ignoring dec operation is needed?
        else -> 2.toDouble().pow(bitSize - 1).toLong().dec().convertTo(T::class)
    }
}

@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
internal fun <T : Number> Number.convertTo(clazz: KClass<T>): T {
    return when (clazz) {
        Byte::class -> toByte()
        Short::class -> toShort()
        Int::class -> toInt()
        Long::class -> toLong()
        Float::class -> toFloat()
        Double::class -> toDouble()
        else -> throw ArithmeticException("unknown number type: $clazz")
    } as T
}