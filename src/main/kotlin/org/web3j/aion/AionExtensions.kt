package org.web3j.aion

import org.web3j.crypto.RawTransaction
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

var TransactionReceipt.cumulativeNrgUsed: String
    get() = cumulativeGasUsedRaw
    set(value) {
        setCumulativeGasUsed(value)
    }

val RawTransaction.nrg: BigInteger
    get() = gasLimit

val RawTransaction.nrgPrice: BigInteger
    get() = gasPrice
