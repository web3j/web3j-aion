package org.web3j.aion.crypto

import org.web3j.utils.Numeric

enum class AionTransactionType(val data: Byte) {
    FVM(Numeric.hexStringToByteArray("1")[0]),
    AVM(Numeric.hexStringToByteArray("f")[0])
}
