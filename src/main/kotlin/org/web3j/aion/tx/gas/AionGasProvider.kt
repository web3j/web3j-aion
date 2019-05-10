package org.web3j.aion.tx.gas

import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

object AionGasProvider : StaticGasProvider(
    BigInteger.valueOf(10_000_000_000),
    BigInteger.valueOf(5_000_000)
)