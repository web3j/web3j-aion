package org.web3j.aion.crypto

import org.web3j.aion.VirtualMachine
import org.web3j.crypto.RawTransaction
import java.math.BigInteger

class AionTransaction(
    nonce: BigInteger? = null,
    nrgPrice: BigInteger? = null,
    nrg: BigInteger,
    to: String? = null,
    value: BigInteger? = null,
    data: String,
    val constructor: Boolean = false,
    val timestamp: Long = System.currentTimeMillis() * 1000,
    val targetVm: VirtualMachine = VirtualMachine.FVM
) : RawTransaction(nonce, nrgPrice, nrg, to, value, data) {

    val nrg: Long
        get() = gasLimit.longValueExact()

    val nrgPrice: Long?
        get() = gasPrice?.longValueExact()
}
