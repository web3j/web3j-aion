package org.web3j.aion.ens

import org.web3j.ens.EnsResolver
import org.web3j.protocol.Web3j

class AionEnsResolver(
    web3j: Web3j,
    syncThreshold: Long = 1000 * 60 * 3
) : EnsResolver(web3j, syncThreshold) {

    override fun resolve(contractId: String?): String? {
        return if (isValidEnsName(contractId)) {
            super.resolve(contractId)
        } else {
            contractId
        }
    }

    companion object {
        private fun isValidEnsName(input: String?): Boolean {
            return false // FIXME Validate ENS address compared to Aion
        }
    }
}
