package org.web3j.aion.abi

import org.aion.avm.userlib.abi.ABIException
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Array
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.FixedPointType
import org.web3j.abi.datatypes.IntType
import org.web3j.abi.datatypes.NumericType
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Uint
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.primitive.Boolean
import org.web3j.abi.datatypes.primitive.Byte
import org.web3j.abi.datatypes.primitive.Char
import org.web3j.abi.datatypes.primitive.Double
import org.web3j.abi.datatypes.primitive.Float
import org.web3j.abi.datatypes.primitive.Int
import org.web3j.abi.datatypes.primitive.Long
import org.web3j.abi.datatypes.primitive.Number
import org.web3j.abi.datatypes.primitive.Short
import org.web3j.protocol.core.methods.response.Transaction
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.utils.Numeric
import java.math.BigInteger

const val ADDRESS_BIT_LENGTH = avm.Address.LENGTH * 8

val org.web3j.protocol.core.methods.request.Transaction.nrg: String
    get() = gas

val org.web3j.protocol.core.methods.request.Transaction.nrgPrice: String
    get() = gasPrice

var Transaction.nrg: BigInteger
    get() = gas
    set(value) {
        setGas(Numeric.encodeQuantity(value))
    }

var Transaction.nrgRaw: String
    get() = gasRaw
    set(value) {
        setGas(value)
    }

var Transaction.nrgPrice: BigInteger
    get() = gasPrice
    set(value) {
        setGasPrice(Numeric.encodeQuantity(value))
    }

var Transaction.nrgPriceRaw: String
    get() = gasPriceRaw
    set(value) {
        setGasPrice(value)
    }

var TransactionReceipt.nrgUsed: BigInteger
    get() = gasUsed
    set(value) {
        setGasUsed(Numeric.encodeQuantity(value))
    }

var TransactionReceipt.nrgRaw: String
    get() = gasUsedRaw
    set(value) {
        setGasUsed(value)
    }

@Suppress("UNCHECKED_CAST")
internal fun <T> Type<T>.toAionValue(): Any {
    return when (this) {
        is Address -> avm.Address(toString().toByteArray())
        is FixedPointType -> throw ABIException("Unsupported fixed point type")
        is IntType -> (this as IntType).toAionValue()
        is Array<*> -> when (this.componentType) {
            Address::class.java ->
                (value as List<Type<avm.Address>>).map { it.toAionValue() }.toTypedArray()
            Utf8String::class.java ->
                (value as List<Type<String>>).map { it.value }.toTypedArray()
            Char::class.java ->
                (value as List<Type<kotlin.Char>>).map { it.value }.toCharArray()
            Bool::class.java, Boolean::class.java ->
                (value as List<Type<kotlin.Boolean>>).map { it.value }.toBooleanArray()
            Byte::class.java ->
                (value as List<Type<kotlin.Byte>>).map { it.value }.toByteArray()
            Short::class.java ->
                (value as List<Type<kotlin.Short>>).map { it.value }.toShortArray()
            Int::class.java ->
                (value as List<Type<kotlin.Int>>).map { it.value }.toIntArray()
            Long::class.java ->
                (value as List<Type<kotlin.Long>>).map { it.value }.toLongArray()
            Float::class.java ->
                (value as List<Type<kotlin.Float>>).map { it.value }.toFloatArray()
            Double::class.java ->
                (value as List<Type<kotlin.Double>>).map { it.value }.toDoubleArray()
            NumericType::class.java -> TODO()
            else -> throw ABIException("Unknown type $javaClass")
        }
        else -> value as Any // Covers primitive types
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T : kotlin.Number> Number<T>.toAionValue(): List<T> {
    return when (this) {
        Byte::class.java ->
            (value as List<Type<kotlin.Byte>>).map { it.value }
        Short::class.java ->
            (value as List<Type<kotlin.Short>>).map { it.value }
        Int::class.java ->
            (value as List<Type<kotlin.Int>>).map { it.value }
        Long::class.java ->
            (value as List<Type<kotlin.Long>>).map { it.value }
        Float::class.java ->
            (value as List<Type<kotlin.Float>>).map { it.value }
        Double::class.java ->
            (value as List<Type<kotlin.Double>>).map { it.value }
        else -> throw ABIException("Unknown type $javaClass")
    } as List<T>
}

internal fun IntType.toAionValue(): Any {
    return when (this) {
        is org.web3j.abi.datatypes.Int -> when (bitSize) {
            8 -> value.byteValueExact()
            16 -> value.shortValueExact()
            24, 32 -> value.intValueExact()
            40, 48, 56, 64 -> value.longValueExact()
            else -> value.toByteArray() // Avoid overflow
        }
        is Uint -> when (bitSize) {
            8 -> value.shortValueExact()
            16, 24 -> value.intValueExact()
            32, 40, 48, 56 -> value.longValueExact()
            else -> value.toByteArray() // Avoid overflow
        }
        else -> throw ABIException("Unknown type ${javaClass.canonicalName}")
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T : Type<*>> Any.toAionValue(classType: Class<T>): T {
    return when (this) {
        is kotlin.Boolean -> Boolean(this)
        is kotlin.Byte -> Byte(this)
        is kotlin.Char -> Char(this)
        is kotlin.Long -> Long(this)
        is kotlin.Short -> Short(this)
        is kotlin.Float -> Float(this)
        is kotlin.Double -> Double(this)
        is String -> when (classType) {
            Utf8String::class -> Utf8String(this)
            Address::class -> Address(ADDRESS_BIT_LENGTH, this)
            else -> throw ABIException(javaClass.canonicalName)
        }
        is ByteArray -> when (classType) {
            IntType::class -> {
                // Conversion from Aion byte[] to Solidity numeric value
                val constructor = classType.getDeclaredConstructor(BigInteger::class.java)
                constructor.newInstance(BigInteger(this))
            }
            Array::class -> {
                // Conversion from Aion byte[] to Solidity static or dynamic array
                val constructor = classType.getDeclaredConstructor(ByteArray::class.java)
                constructor.newInstance(this)
            }
            else -> throw ABIException(javaClass.canonicalName)
        }
        is kotlin.Array<*> -> {
            // Conversion from Aion array to Solidity static or dynamic array
            val constructor = classType.getDeclaredConstructor(Class::class.java, kotlin.Array<Any>::class.java)
            constructor.newInstance(classType, *this.map { it!!.toAionValue(Utf8String::class.java) }.toTypedArray())
        }
        else -> throw ABIException(javaClass.canonicalName)
    } as T
}
