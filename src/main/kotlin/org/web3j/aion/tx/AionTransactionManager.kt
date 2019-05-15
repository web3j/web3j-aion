package org.web3j.aion.tx

import org.web3j.crypto.RawTransaction
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.tx.TransactionManager
import java.math.BigInteger

class AionTransactionManager(web3j: Web3j, fromAddress: String) : TransactionManager(web3j, fromAddress) {

    override fun sendCall(to: String?, data: String?, defaultBlockParameter: DefaultBlockParameter?): String {
        TODO("not implemented")
    }

    override fun sendTransaction(
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        to: String,
        data: String,
        value: BigInteger
    ): EthSendTransaction {
        val rawTransaction = RawTransaction.createTransaction(BigInteger.ZERO, gasPrice, gasLimit, to, value, data)
        return signAndSend(rawTransaction)
    }

    private fun signAndSend(rawTransaction: RawTransaction): EthSendTransaction {
        TODO("not implemented")
    }
}
