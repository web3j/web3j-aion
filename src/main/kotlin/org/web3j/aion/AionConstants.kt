package org.web3j.aion

object AionConstants {
    const val NRG_CODE_DEPOSIT = 1000

    const val NRG_CREATE_CONTRACT_MIN = 200000

    const val NRG_CREATE_CONTRACT_DEFAULT = 350000

    const val NRG_CREATE_CONTRACT_MAX = 5000000

    const val NRG_TX_DATA_ZERO = 4

    const val NRG_TX_DATA_NONZERO = 64

    const val NRG_TRANSACTION_MIN = 21000

    const val NRG_TRANSACTION_DEFAULT = 90000

    const val NRG_TRANSACTION_MAX = 2000000

    /** Call stack depth limit. Based on EIP-150, the theoretical limit is ~340.  */
    const val MAX_CALL_DEPTH = 128
}
