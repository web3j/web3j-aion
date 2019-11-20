/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
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
