package org.web3j.aion.abi

import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Array
import org.web3j.abi.datatypes.Fixed
import org.web3j.abi.datatypes.FixedPointType
import org.web3j.abi.datatypes.Int
import org.web3j.abi.datatypes.IntType
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Ufixed
import org.web3j.abi.datatypes.Uint
import org.web3j.protocol.core.methods.response.Transaction
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.utils.Numeric
import java.math.BigInteger

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
val Type<*>.aionValue: Any
    get() = when (this) {
        is Address -> avm.Address(toString().toByteArray())
        is Array<*> -> (value as List<Type<*>>).map { it.aionValue }
        is FixedPointType -> aionValue
        is IntType -> aionValue
        else -> value
    }

val FixedPointType.aionValue: Number
    get() = when (this) {
        is Fixed -> value.toDouble()
        is Ufixed -> value.toDouble()
        else -> throw AionEncodingException("FixedPointType")
    }

val IntType.aionValue: Number
    get() = when (this) {
        is Int -> value.intValueExact()
        is Uint -> value.intValueExact()
        else -> throw AionEncodingException("IntType")
    }
