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
package org.web3j.aion.tx.gas

import org.web3j.aion.AionConstants.NRG_CREATE_CONTRACT_MAX
import org.web3j.aion.AionConstants.NRG_TRANSACTION_MAX
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

object AionGasProvider : StaticGasProvider(
    BigInteger.valueOf(10_000_000_000),
    BigInteger.ZERO // Overriden here
) {
    private val CREATE_CONTRACT_DEFAULT = BigInteger.valueOf(NRG_CREATE_CONTRACT_MAX)
    private val TRANSACTION_DEFAULT = BigInteger.valueOf(NRG_TRANSACTION_MAX)

    override fun getGasLimit(): BigInteger {
        return TRANSACTION_DEFAULT
    }

    override fun getGasLimit(contractFunc: String): BigInteger {
        return when (contractFunc) {
            // Aion makes a difference for contract creation
            "deploy" -> CREATE_CONTRACT_DEFAULT
            else -> TRANSACTION_DEFAULT
        }
    }
}
