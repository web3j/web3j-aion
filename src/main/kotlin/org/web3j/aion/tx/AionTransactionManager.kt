package org.web3j.aion.tx

import org.web3j.aion.crypto.AionTransaction
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.tx.RawTransactionManager

class AionTransactionManager(web3j: Web3j, credentials: Credentials) :
    RawTransactionManager(web3j, credentials) {

    override fun sign(rawTransaction: RawTransaction): String {
        if (rawTransaction !is AionTransaction) {
            error("Invalid transaction type: ${rawTransaction.javaClass}")
        }

        TODO()
    }

    override fun signAndSend(rawTransaction: RawTransaction): EthSendTransaction {
        if (rawTransaction !is AionTransaction) {
            error("Invalid transaction type: ${rawTransaction.javaClass}")
        }

        TODO()
    }
}
