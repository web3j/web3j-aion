package org.web3j.aion.crypto

import org.web3j.aion.AionConstants.NRG_TRANSACTION_DEFAULT
import org.web3j.crypto.RawTransaction
import java.math.BigInteger

class AionTransaction(
    nonce: Long? = null,
    nrgPrice: Long? = null,
    nrg: Long = NRG_TRANSACTION_DEFAULT,
    to: String? = null,
    value: Long? = null,
    data: String,
    val timestamp: Long = System.nanoTime(),
    val type: AionTransactionType = AionTransactionType.FVM
) : RawTransaction(
    nonce?.let { BigInteger.valueOf(it) },
    nrgPrice?.let { BigInteger.valueOf(it) },
    BigInteger.valueOf(nrg),
    to,
    value?.let { BigInteger.valueOf(it) },
    data
) {
    val nrg: Long
        get() = gasLimit.longValueExact()

    val nrgPrice: Long?
        get() = gasPrice?.longValueExact()
}
