package org.web3j.protocol.aion

import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jService

interface Aion : Web3j {
    companion object {

        @JvmStatic
        fun build(web3jService: Web3jService): Aion {
            return JsonRpc2_0Aion(web3jService)
        }
    }
}
