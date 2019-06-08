package org.web3j.aion.crypto

import org.web3j.crypto.RawTransaction
import java.math.BigInteger

class AionTransaction(
    nonce: BigInteger,
    gasPrice: BigInteger,
    gasLimit: BigInteger,
    to: String,
    value: BigInteger,
    data: String,
    val type: AionTransactionType
) : RawTransaction(nonce, gasPrice, gasLimit, to, value, data)
