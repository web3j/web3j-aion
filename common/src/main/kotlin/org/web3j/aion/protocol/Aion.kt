package org.web3j.aion.protocol

import org.web3j.protocol.Web3jService
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.Request
import org.web3j.protocol.core.methods.request.ShhFilter
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.DbGetHex
import org.web3j.protocol.core.methods.response.DbGetString
import org.web3j.protocol.core.methods.response.DbPutHex
import org.web3j.protocol.core.methods.response.DbPutString
import org.web3j.protocol.core.methods.response.EthBlock
import org.web3j.protocol.core.methods.response.EthCall
import org.web3j.protocol.core.methods.response.EthCompileLLL
import org.web3j.protocol.core.methods.response.EthCompileSerpent
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.EthGetCode
import org.web3j.protocol.core.methods.response.EthGetStorageAt
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.protocol.core.methods.response.EthGetUncleCountByBlockHash
import org.web3j.protocol.core.methods.response.EthGetUncleCountByBlockNumber
import org.web3j.protocol.core.methods.response.EthGetWork
import org.web3j.protocol.core.methods.response.EthSign
import org.web3j.protocol.core.methods.response.EthSubmitWork
import org.web3j.protocol.core.methods.response.ShhAddToGroup
import org.web3j.protocol.core.methods.response.ShhHasIdentity
import org.web3j.protocol.core.methods.response.ShhMessages
import org.web3j.protocol.core.methods.response.ShhNewFilter
import org.web3j.protocol.core.methods.response.ShhNewGroup
import org.web3j.protocol.core.methods.response.ShhNewIdentity
import org.web3j.protocol.core.methods.response.ShhPost
import org.web3j.protocol.core.methods.response.ShhUninstallFilter
import org.web3j.protocol.core.methods.response.ShhVersion
import org.web3j.protocol.core.methods.response.Web3Sha3
import java.math.BigInteger

class AionProtocolException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

interface Aion : Admin {

    @Deprecated("Spec-defined functions that deal with Uncles are not part of the Aion protocol.")
    override fun ethGetUncleCountByBlockHash(blockHash: String):
        Request<*, EthGetUncleCountByBlockHash>

    @Deprecated("Spec-defined functions that deal with Uncles are not part of the Aion protocol.")
    override fun ethGetUncleCountByBlockNumber(defaultBlockParameter: DefaultBlockParameter):
        Request<*, EthGetUncleCountByBlockNumber>

    @Deprecated("Spec-defined functions that deal with Uncles are not part of the Aion protocol.")
    override fun ethGetUncleByBlockHashAndIndex(blockHash: String, transactionIndex: BigInteger):
        Request<*, EthBlock>

    @Deprecated("Spec-defined functions that deal with Uncles are not part of the Aion protocol.")
    override fun ethGetUncleByBlockNumberAndIndex(
        defaultBlockParameter: DefaultBlockParameter,
        uncleIndex: BigInteger
    ): Request<*, EthBlock>

    @Deprecated("The LLL compiler is not supported by Aion.")
    override fun ethCompileLLL(sourceCode: String): Request<*, EthCompileLLL>

    @Deprecated("The Serpent compiler is not supported by Aion.")
    override fun ethCompileSerpent(sourceCode: String): Request<*, EthCompileSerpent>

    @Deprecated("Implemented as part of the stratum API submodule.")
    override fun ethGetWork(): Request<*, EthGetWork>

    @Deprecated("Implemented as part of the stratum API submodule.")
    override fun ethSubmitWork(nonce: String, headerPowHash: String, mixDigest: String): Request<*, EthSubmitWork>

    @Deprecated("Aion currently does not support the Whisper protocol.")
    override fun shhPost(shhPost: org.web3j.protocol.core.methods.request.ShhPost): Request<*, ShhPost>

    @Deprecated("Aion currently does not support the Whisper protocol.")
    override fun shhVersion(): Request<*, ShhVersion>

    @Deprecated("Aion currently does not support the Whisper protocol.")
    override fun shhNewIdentity(): Request<*, ShhNewIdentity>

    @Deprecated("Aion currently does not support the Whisper protocol.")
    override fun shhHasIdentity(identityAddress: String): Request<*, ShhHasIdentity>

    @Deprecated("Aion currently does not support the Whisper protocol.")
    override fun shhNewGroup(): Request<*, ShhNewGroup>

    @Deprecated("Aion currently does not support the Whisper protocol.")
    override fun shhAddToGroup(identityAddress: String): Request<*, ShhAddToGroup>

    @Deprecated("Aion currently does not support the Whisper protocol.")
    override fun shhNewFilter(shhFilter: ShhFilter): Request<*, ShhNewFilter>

    @Deprecated("Aion currently does not support the Whisper protocol.")
    override fun shhUninstallFilter(filterId: BigInteger): Request<*, ShhUninstallFilter>

    @Deprecated("Aion currently does not support the Whisper protocol.")
    override fun shhGetFilterChanges(filterId: BigInteger): Request<*, ShhMessages>

    @Deprecated("Aion currently does not support the Whisper protocol.")
    override fun shhGetMessages(filterId: BigInteger): Request<*, ShhMessages>

    @Deprecated("The database APIs have been deprecated in the Ethereum Spec.")
    override fun dbPutString(databaseName: String, keyName: String, stringToStore: String): Request<*, DbPutString>

    @Deprecated("The database APIs have been deprecated in the Ethereum Spec.")
    override fun dbGetString(databaseName: String, keyName: String): Request<*, DbGetString>

    @Deprecated("The database APIs have been deprecated in the Ethereum Spec.")
    override fun dbPutHex(databaseName: String, keyName: String, dataToStore: String): Request<*, DbPutHex>

    @Deprecated("The database APIs have been deprecated in the Ethereum Spec.")
    override fun dbGetHex(databaseName: String, keyName: String): Request<*, DbGetHex>

    @Deprecated("The eth_sign method should not be used to sign Aion transactions.")
    override fun ethSign(address: String, sha3HashOfDataToSign: String?): Request<*, EthSign>

    @Deprecated("You should not be sending data to get hashed to the kernel API server.")
    override fun web3Sha3(data: String): Request<*, Web3Sha3>

    /**
     * @param defaultBlockParameter `pending` status is not supported for as a default block parameter.
     * @throws AionProtocolException if `defaultBlockParameter` is equal to `pending`.
     */
    @Throws(AionProtocolException::class)
    override fun ethGetBalance(
        address: String,
        defaultBlockParameter: DefaultBlockParameter
    ): Request<*, EthGetBalance>

    /**
     * @param defaultBlockParameter `pending` status is not supported for as a default block parameter.
     * @throws AionProtocolException if `defaultBlockParameter` is equal to `pending`.
     */
    @Throws(AionProtocolException::class)
    override fun ethGetCode(
        address: String,
        defaultBlockParameter: DefaultBlockParameter
    ): Request<*, EthGetCode>

    /**
     * @param defaultBlockParameter `pending` status is not supported for as a default block parameter.
     * @throws AionProtocolException if `defaultBlockParameter` is equal to `pending`.
     */
    @Throws(AionProtocolException::class)
    override fun ethGetTransactionCount(
        address: String,
        defaultBlockParameter: DefaultBlockParameter
    ): Request<*, EthGetTransactionCount>

    /**
     * @param defaultBlockParameter `pending` status is not supported for as a default block parameter.
     * @throws AionProtocolException if `defaultBlockParameter` is equal to `pending`.
     */
    @Throws(AionProtocolException::class)
    override fun ethGetStorageAt(
        address: String,
        position: BigInteger,
        defaultBlockParameter: DefaultBlockParameter
    ): Request<*, EthGetStorageAt>

    /**
     * @param defaultBlockParameter `pending` status is not supported for as a default block parameter.
     * @throws AionProtocolException if `defaultBlockParameter` is equal to `pending`.
     */
    @Throws(AionProtocolException::class)
    override fun ethCall(
        transaction: Transaction,
        defaultBlockParameter: DefaultBlockParameter
    ): Request<*, EthCall>

    companion object {

        @JvmStatic
        fun build(web3jService: Web3jService): Aion {
            return JsonRpc2_0Aion(web3jService)
        }
    }
}
