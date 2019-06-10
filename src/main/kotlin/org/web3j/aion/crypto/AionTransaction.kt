package org.web3j.aion.crypto

import org.web3j.aion.AionConstants
import org.web3j.crypto.RawTransaction
import java.math.BigInteger

class AionTransaction(
    nonce: BigInteger? = null,
    gasPrice: BigInteger? = null,
    gasLimit: BigInteger = BigInteger.valueOf(AionConstants.NRG_TRANSACTION_DEFAULT),
    to: String, value: BigInteger, data: String,
    val timestamp: Long? = null,
    val type: AionTransactionType = AionTransactionType.FVM
) : RawTransaction(nonce, gasPrice, gasLimit, to, value, data)
