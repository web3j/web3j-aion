package org.web3j.protocol.aion

import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jService
import org.web3j.protocol.core.Request
import org.web3j.protocol.core.methods.response.EthSign
import org.web3j.protocol.core.methods.response.Web3Sha3

interface Aion : Web3j {

    @Deprecated("The eth_sign method should not be used to sign Aion transactions.")
    override fun ethSign(address: String, sha3HashOfDataToSign: String?): Request<*, EthSign>

    @Deprecated("You should not be sending data to get hashed to the kernel API server.")
    override fun web3Sha3(data: String): Request<*, Web3Sha3>

    companion object {

        @JvmStatic
        fun build(web3jService: Web3jService): Aion {
            return JsonRpc2_0Aion(web3jService)
        }
    }
}
