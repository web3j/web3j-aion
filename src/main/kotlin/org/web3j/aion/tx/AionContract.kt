package org.web3j.aion.tx

import avm.Address
import org.web3j.aion.crypto.Ed25519KeyPair
import org.web3j.aion.protocol.Aion
import org.web3j.crypto.Credentials
import org.web3j.ens.EnsResolver
import org.web3j.protocol.Web3j
import org.web3j.tx.Contract
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

abstract class AionContract : Contract {

    protected constructor(
        contractBinary: String,
        contractAddress: String?,
        web3j: Web3j,
        transactionManager: TransactionManager,
        gasProvider: ContractGasProvider
    ) : super(
        EnsResolver(web3j, EnsResolver.DEFAULT_SYNC_THRESHOLD, Address.LENGTH),
        contractBinary, contractAddress, web3j,
        transactionManager as AionTransactionManager,
        gasProvider
    )

    protected constructor(
        contractBinary: String,
        contractAddress: String?,
        web3j: Web3j,
        credentials: Credentials,
        gasProvider: ContractGasProvider
    ) : this(
        contractBinary, contractAddress, web3j,
        AionTransactionManager(
            web3j as Aion,
            credentials.address,
            Ed25519KeyPair(
                credentials.ecKeyPair.publicKey,
                credentials.ecKeyPair.privateKey
            )
        ),
        gasProvider
    )

    @Deprecated(message = "Removed in v5.0")
    protected constructor(
        contractBinary: String,
        contractAddress: String?,
        web3j: Web3j,
        credentials: Credentials,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ) : this(
        contractBinary, contractAddress, web3j,
        AionTransactionManager(
            web3j as Aion,
            credentials.address,
            Ed25519KeyPair(
                credentials.ecKeyPair.publicKey,
                credentials.ecKeyPair.privateKey
            )
        ),
        StaticGasProvider(gasPrice, gasLimit)
    )

    @Deprecated(message = "Removed in v5.0")
    protected constructor(
        contractBinary: String,
        contractAddress: String?,
        web3j: Web3j,
        transactionManager: TransactionManager,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ) : this(
        contractBinary, contractAddress, web3j,
        transactionManager as AionTransactionManager,
        StaticGasProvider(gasPrice, gasLimit)
    )
}
