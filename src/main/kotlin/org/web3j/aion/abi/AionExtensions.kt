package org.web3j.aion.abi

import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Array
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Fixed
import org.web3j.abi.datatypes.FixedPointType
import org.web3j.abi.datatypes.Int
import org.web3j.abi.datatypes.IntType
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Ufixed
import org.web3j.abi.datatypes.Uint
import org.web3j.abi.datatypes.Utf8String
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
internal val Type<*>.aionValue: Any
    get() = when (this) {
        is Address -> avm.Address(toString().toByteArray())
        is Array<*> -> (value as List<Type<*>>).map { it.aionValue }
        is FixedPointType -> aionValue
        is IntType -> aionValue
        else -> value
    }

internal val FixedPointType.aionValue: Number
    get() = when (this) {
        is Fixed -> value.toDouble()
        is Ufixed -> value.toDouble()
        else -> throw AbiEncodingException("FixedPointType")
    }

internal val IntType.aionValue: Number
    get() = when (this) {
        is Int -> value.intValueExact()
        is Uint -> value.intValueExact()
        else -> throw AbiEncodingException("IntType")
    }

@Suppress("UNCHECKED_CAST")
internal fun <T : Type<*>> Any.toAionValue(classType: Class<T>): T {
    return when (this) {
        is kotlin.Array<*> -> DynamicArray(*this.map { it!!.toAionValue(Utf8String::class.java) }.toTypedArray())
        is Boolean -> org.web3j.abi.datatypes.primitive.Boolean(this)
        is Byte -> org.web3j.abi.datatypes.primitive.Byte(this)
        is Char -> org.web3j.abi.datatypes.primitive.Char(this)
        is Long -> org.web3j.abi.datatypes.primitive.Long(this)
        is Short -> org.web3j.abi.datatypes.primitive.Short(this)
        is Float -> org.web3j.abi.datatypes.primitive.Float(this)
        is Double -> org.web3j.abi.datatypes.primitive.Double(this)
        is String -> when (classType) {
            Utf8String::class -> Utf8String(this)
            Address::class -> Address(ADDRESS_BIT_LENGTH, this)
            else -> throw AbiDecodingException(javaClass.canonicalName)
        }
        else -> throw AbiDecodingException(javaClass.canonicalName)
    } as T
}
