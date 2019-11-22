/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
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
