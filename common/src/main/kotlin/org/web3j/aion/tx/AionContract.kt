package org.web3j.aion.tx

import org.web3j.aion.AionConstants.ADDRESS_BYTE_LENGTH
import org.web3j.aion.VirtualMachine
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

/**
 * Base class for AVM Aion contract wrappers.
 */
abstract class AvmAionContract protected constructor(
    contractBinary: String,
    contractAddress: String?,
    web3j: Web3j,
    transactionManager: TransactionManager,
    gasProvider: ContractGasProvider
) : AionContract(
    contractBinary, contractAddress, web3j,
    transactionManager,
    gasProvider
) {
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
            Ed25519KeyPair(
                credentials.ecKeyPair.publicKey,
                credentials.ecKeyPair.privateKey
            ),
            VirtualMachine.AVM
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
            Ed25519KeyPair(
                credentials.ecKeyPair.publicKey,
                credentials.ecKeyPair.privateKey
            ),
            VirtualMachine.AVM
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
        contractBinary, contractAddress,
        web3j, transactionManager,
        StaticGasProvider(gasPrice, gasLimit)
    )
}

/**
 * Base class for FVM Aion contract wrappers.
 */
abstract class FvmAionContract protected constructor(
    contractBinary: String,
    contractAddress: String?,
    web3j: Web3j,
    transactionManager: TransactionManager,
    gasProvider: ContractGasProvider
) : AionContract(
    contractBinary, contractAddress, web3j,
    transactionManager,
    gasProvider
) {
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
            Ed25519KeyPair(
                credentials.ecKeyPair.publicKey,
                credentials.ecKeyPair.privateKey
            ),
            VirtualMachine.FVM
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
            Ed25519KeyPair(
                credentials.ecKeyPair.publicKey,
                credentials.ecKeyPair.privateKey
            ),
            VirtualMachine.FVM
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
        contractBinary, contractAddress,
        web3j, transactionManager,
        StaticGasProvider(gasPrice, gasLimit)
    )
}

/**
 * Base class for Aion contract wrappers.
 */
sealed class AionContract constructor(
    contractBinary: String,
    contractAddress: String?,
    web3j: Web3j,
    transactionManager: TransactionManager,
    gasProvider: ContractGasProvider
) : Contract(
    EnsResolver(web3j, EnsResolver.DEFAULT_SYNC_THRESHOLD, ADDRESS_BYTE_LENGTH * 2),
    contractBinary, contractAddress, web3j,
    transactionManager,
    gasProvider
)
