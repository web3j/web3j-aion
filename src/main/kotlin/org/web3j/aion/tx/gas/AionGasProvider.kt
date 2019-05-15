package org.web3j.aion.tx.gas

import org.web3j.aion.AionConstants
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

object AionGasProvider : StaticGasProvider(
    BigInteger.valueOf(10_000_000_000),
    BigInteger.ZERO // Overriden here
) {
    private val NRG_CREATE_CONTRACT_DEFAULT = BigInteger.valueOf(AionConstants.NRG_CREATE_CONTRACT_DEFAULT.toLong())
    private val NRG_TRANSACTION_DEFAULT = BigInteger.valueOf(AionConstants.NRG_TRANSACTION_DEFAULT.toLong())

    override fun getGasLimit(contractFunc: String): BigInteger {
        return when (contractFunc) {
            // Aion makes a difference for contract creation
            "deploy" -> NRG_CREATE_CONTRACT_DEFAULT
            else -> NRG_TRANSACTION_DEFAULT
        }
    }
}
