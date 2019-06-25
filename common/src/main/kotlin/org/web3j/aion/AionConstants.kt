package org.web3j.aion

import org.web3j.utils.Numeric

object AionConstants {
    val ADDRESS_ID: ByteArray = Numeric.hexStringToByteArray("a0")

    const val NRG_CODE_DEPOSIT = 1000L

    const val NRG_CREATE_CONTRACT_MIN = 200000L

    const val NRG_CREATE_CONTRACT_DEFAULT = 350000L

    const val NRG_CREATE_CONTRACT_MAX = 5000000L

    const val NRG_TX_DATA_ZERO = 4L

    const val NRG_TX_DATA_NONZERO = 64L

    const val NRG_TRANSACTION_MIN = 21000L

    const val NRG_TRANSACTION_DEFAULT = 90000L

    const val NRG_TRANSACTION_MAX = 2000000L

    /** Call stack depth limit. Based on EIP-150, the theoretical limit is ~340.  */
    val MAX_CALL_DEPTH = 128

    const val MAX_BIT_LENGTH = 128
    const val MAX_BYTE_LENGTH = MAX_BIT_LENGTH / 8

    const val ADDRESS_BYTE_LENGTH = 32
    const val ADDRESS_BIT_LENGTH = ADDRESS_BYTE_LENGTH * Byte.SIZE_BITS
}
