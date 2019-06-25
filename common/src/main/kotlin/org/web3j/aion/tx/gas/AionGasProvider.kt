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
