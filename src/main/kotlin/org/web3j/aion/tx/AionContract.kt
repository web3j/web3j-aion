package org.web3j.aion.tx

import org.web3j.aion.ens.AionEnsResolver
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.Contract
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider

open class AionContract : Contract {

    init {
        ensResolver = AionEnsResolver(web3j)
    }

    protected constructor(
        contractBinary: String,
        contractAddress: String?,
        web3j: Web3j,
        transactionManager: TransactionManager,
        gasProvider: ContractGasProvider
    ) : super(contractBinary, contractAddress, web3j, transactionManager, gasProvider)

    protected constructor(
        contractBinary: String,
        contractAddress: String?,
        web3j: Web3j,
        credentials: Credentials,
        gasProvider: ContractGasProvider
    ) : this(contractBinary, contractAddress, web3j, RawTransactionManager(web3j, credentials), gasProvider)

    override fun resolveContractAddress(contractAddress: String?) = contractAddress
}
