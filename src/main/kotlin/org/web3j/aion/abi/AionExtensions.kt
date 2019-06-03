package org.web3j.aion.abi

import org.aion.avm.userlib.abi.ABIException
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Array
import org.web3j.abi.datatypes.FixedPointType
import org.web3j.abi.datatypes.Int
import org.web3j.abi.datatypes.IntType
import org.web3j.abi.datatypes.Type
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
internal val Type<Any>.aionValue: Any
    get() = when (this) {
        is Address -> avm.Address(toString().toByteArray())
        is Array<*> -> (value as List<Type<Any>>).map { it.aionValue }.toTypedArray()
        is FixedPointType -> throw ABIException("Unsupported fixed point type")
        is IntType -> (this as IntType).aionValue
        else -> value // Covers primitive types
    }

internal val IntType.aionValue: Any
    get() = when (this) {
        is Int -> when (bitSize) {
            8, 16 -> value.shortValueExact()
            24, 32 -> value.intValueExact()
            40, 48, 56, 64 -> value.longValueExact()
            else -> value.toByteArray() // Avoid overflow
        }
        is Uint -> when (bitSize) {
            8, 16, 24, 32 -> value.shortValueExact()
            40, 48, 56, 64 -> value.intValueExact()
            72, 80, 88, 96, 104, 112, 120, 128 -> value.longValueExact()
            else -> value.toByteArray() // Avoid overflow
        }
        else -> throw ABIException("Unexpected error")
    }

@Suppress("UNCHECKED_CAST")
internal fun <T : Type<*>> Any.toAionValue(classType: Class<T>): T {
    return when (this) {
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
