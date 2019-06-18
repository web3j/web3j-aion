package org.web3j.aion

import org.web3j.utils.Numeric

object AionConstants {
    val ADDRESS_ID: ByteArray = Numeric.hexStringToByteArray("a0")

    const val NRG_CREATE_CONTRACT_DEFAULT = 350000L
    const val NRG_TRANSACTION_DEFAULT = 90000L

    const val MAX_BIT_LENGTH = 128
    const val MAX_BYTE_LENGTH = MAX_BIT_LENGTH / 8

    const val ADDRESS_BYTE_LENGTH = 32
    const val ADDRESS_BIT_LENGTH = ADDRESS_BYTE_LENGTH * Byte.SIZE_BITS
}
