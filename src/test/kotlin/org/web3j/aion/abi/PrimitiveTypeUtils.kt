package org.web3j.aion.abi

import java.math.BigInteger
import kotlin.math.pow
import kotlin.reflect.KClass

internal inline fun <reified T : Number> min(): T {
    return when (T::class) {
        Byte::class -> min(Byte.SIZE_BITS)
        Short::class -> min(Short.SIZE_BITS)
        Int::class -> min(Int.SIZE_BITS)
        Long::class -> min(Long.SIZE_BITS)
        Float::class -> Float.MIN_VALUE.convertTo(T::class)
        Double::class -> Double.MIN_VALUE.convertTo(T::class)
        else -> throw ArithmeticException("unknown number type: ${T::class}")
    }
}

internal inline fun <reified T : Number> min(bitSize: Int): T {
    return when (bitSize) {
        Byte.SIZE_BITS -> Byte.MIN_VALUE.convertTo(T::class)
        Short.SIZE_BITS -> Short.MIN_VALUE.convertTo(T::class)
        Int.SIZE_BITS -> Int.MIN_VALUE.convertTo(T::class)
        Long.SIZE_BITS -> Long.MIN_VALUE.convertTo(T::class)
        // Because is test code, this calculation is made for 64-256 bit sizes
        else -> 2.toDouble().pow(bitSize.dec()).unaryMinus().convertTo(T::class)
    }
}

internal inline fun <reified T : Number> max(unsigned: Boolean = false): T {
    val unsignedFactor = if (unsigned) 2 else 1
    return when (T::class) {
        Byte::class -> max(Byte.SIZE_BITS, unsigned)
        Short::class -> max(Short.SIZE_BITS, unsigned)
        Int::class -> max(Int.SIZE_BITS, unsigned)
        Long::class -> max(Long.SIZE_BITS, unsigned)
        Float::class -> Float.MAX_VALUE.times(unsignedFactor).convertTo(T::class)
        Double::class -> Double.MAX_VALUE.times(unsignedFactor).convertTo(T::class)
        else -> throw ArithmeticException("unknown number type: ${T::class}")
    }
}

@ExperimentalUnsignedTypes
internal inline fun <reified T : Number> max(bitSize: Int, unsigned: Boolean = false): T {
    return if (unsigned) {
        when (bitSize) {
            Byte.SIZE_BITS -> UByte.MAX_VALUE.toShort().convertTo(T::class)
            Short.SIZE_BITS -> UShort.MAX_VALUE.toInt().convertTo(T::class)
            Int.SIZE_BITS -> UInt.MAX_VALUE.toLong().convertTo(T::class)
            // Because is test code, this calculation is made for 64-256 bit sizes
            // FIXME Convert to Long to avoid Double ignoring dec operation is needed?
            else -> {
                2.toDouble().pow(bitSize).toLong().dec().convertTo(T::class)
            }
        }
    } else {
        when (bitSize) {
            Byte.SIZE_BITS -> Byte.MAX_VALUE.convertTo(T::class)
            Short.SIZE_BITS -> Short.MAX_VALUE.convertTo(T::class)
            Int.SIZE_BITS -> Int.MAX_VALUE.convertTo(T::class)
            Long.SIZE_BITS -> Long.MAX_VALUE.convertTo(T::class)
            else -> {
                // Because is test code, this calculation is made for 64-256 bit sizes
                // FIXME Convert to Long to avoid Double ignoring dec operation is needed?
                2.toDouble().pow(bitSize.dec()).toLong().dec().convertTo(T::class)
            }
        }
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
        BigInteger::class -> toBigInteger()
        else -> throw ArithmeticException("unknown number type: $clazz")
    } as T
}

internal fun Number.toBigInteger() = BigInteger(toString())
