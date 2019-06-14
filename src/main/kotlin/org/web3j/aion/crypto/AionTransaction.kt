package org.web3j.aion.crypto

import org.web3j.aion.VirtualMachine
import org.web3j.crypto.RawTransaction
import java.math.BigInteger

class AionTransaction(
    nonce: Long? = null,
    nrgPrice: Long? = null,
    nrg: Long,
    to: String? = null,
    value: Long? = null,
    data: String,
    val timestamp: Long = System.currentTimeMillis() * 1000,
    val targetVm: VirtualMachine = VirtualMachine.FVM
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
