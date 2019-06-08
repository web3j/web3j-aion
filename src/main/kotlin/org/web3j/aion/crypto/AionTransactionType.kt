package org.web3j.aion.crypto

enum class AionTransactionType(val data: Byte) {
    FVM(0x1),
    AVM(0x2)
}
