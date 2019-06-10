package org.web3j.aion.tx

import org.web3j.aion.crypto.AionTransaction
import org.web3j.aion.protocol.Aion
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.tx.RawTransactionManager

class AionTransactionManager(aion: Aion, credentials: Credentials) :
    RawTransactionManager(aion, credentials) {

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
