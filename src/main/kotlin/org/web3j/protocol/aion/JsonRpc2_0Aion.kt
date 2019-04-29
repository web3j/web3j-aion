package org.web3j.protocol.aion

import mu.KLogging
import org.web3j.protocol.ObjectMapperFactory
import org.web3j.protocol.Web3jService
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.core.Request
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthCall
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.EthGetCode
import org.web3j.protocol.core.methods.response.EthGetStorageAt
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.util.Arrays
import java.util.concurrent.ScheduledExecutorService

@Suppress("ClassName")
internal class JsonRpc2_0Aion : JsonRpc2_0Web3j, Aion {

    init {
        ObjectMapperFactory.getObjectMapper()
            .addMixIn(Request::class.java, AionRequestMixIn::class.java)
    }

    constructor(web3jService: Web3jService) : super(web3jService)

    constructor(
        web3jService: Web3jService,
        pollingInterval: Long,
        scheduledExecutorService: ScheduledExecutorService
    ) : super(web3jService, pollingInterval, scheduledExecutorService)

    // Unsupported Endpoints
/*
    override fun ethGetUncleCountByBlockHash(blockHash: String)
            : Request<*, EthGetUncleCountByBlockHash> {
        throw UnsupportedOperationException("Spec-defined functions that deal with Uncles are not part of the Aion protocol.")
    }

    override fun ethGetUncleCountByBlockNumber(defaultBlockParameter: DefaultBlockParameter)
            : Request<*, EthGetUncleCountByBlockNumber> {
        throw UnsupportedOperationException("Spec-defined functions that deal with Uncles are not part of the Aion protocol.")
    }

    override fun ethGetUncleByBlockHashAndIndex(blockHash: String, transactionIndex: BigInteger)
            : Request<*, EthBlock> {
        throw UnsupportedOperationException("Spec-defined functions that deal with Uncles are not part of the Aion protocol.")
    }

    override fun ethGetUncleByBlockNumberAndIndex(
            defaultBlockParameter: DefaultBlockParameter,
            uncleIndex: BigInteger): Request<*, EthBlock> {
        throw UnsupportedOperationException("Spec-defined functions that deal with Uncles are not part of the Aion protocol.")
    }

    override fun ethCompileLLL(sourceCode: String): Request<*, EthCompileLLL> {
        throw UnsupportedOperationException("The LLL compiler is not supported. "
                + "Currently, the only language supported on Aion is Solidity-on-Aion-FVM.")
    }

    override fun ethCompileSerpent(sourceCode: String): Request<*, EthCompileSerpent> {
        throw UnsupportedOperationException("The Serpent compiler is not supported. "
                + "Currently, the only language supported on Aion is Solidity-on-Aion-FVM.")
    }

    override fun ethGetWork(): Request<*, EthGetWork> {
        throw UnsupportedOperationException("Implemented as part of the stratum API submodule.")
    }

    override fun ethSubmitWork(nonce: String, headerPowHash: String, mixDigest: String): Request<*, EthSubmitWork> {
        throw UnsupportedOperationException("Implemented as part of the stratum API submodule.")
    }

    override fun shhPost(shhPost: org.web3j.protocol.core.methods.request.ShhPost): Request<*, ShhPost> {
        throw UnsupportedOperationException("Aion currently does not support the Whisper protocol.")
    }

    override fun shhVersion(): Request<*, ShhVersion> {
        throw UnsupportedOperationException("Aion currently does not support the Whisper protocol.")
    }

    override fun shhNewIdentity(): Request<*, ShhNewIdentity> {
        throw UnsupportedOperationException("Aion currently does not support the Whisper protocol.")
    }

    override fun shhHasIdentity(identityAddress: String): Request<*, ShhHasIdentity> {
        throw UnsupportedOperationException("Aion currently does not support the Whisper protocol.")
    }

    override fun shhNewGroup(): Request<*, ShhNewGroup> {
        throw UnsupportedOperationException("Aion currently does not support the Whisper protocol.")
    }

    override fun shhAddToGroup(identityAddress: String): Request<*, ShhAddToGroup> {
        throw UnsupportedOperationException("Aion currently does not support the Whisper protocol.")
    }

    override fun shhNewFilter(shhFilter: ShhFilter): Request<*, ShhNewFilter> {
        throw UnsupportedOperationException("Aion currently does not support the Whisper protocol.")
    }

    override fun shhUninstallFilter(filterId: BigInteger): Request<*, ShhUninstallFilter> {
        throw UnsupportedOperationException("Aion currently does not support the Whisper protocol.")
    }

    override fun shhGetFilterChanges(filterId: BigInteger): Request<*, ShhMessages> {
        throw UnsupportedOperationException("Aion currently does not support the Whisper protocol.")
    }

    override fun shhGetMessages(filterId: BigInteger): Request<*, ShhMessages> {
        throw UnsupportedOperationException("Aion currently does not support the Whisper protocol.")
    }

    override fun dbPutString(databaseName: String, keyName: String, stringToStore: String): Request<*, DbPutString> {
        throw UnsupportedOperationException("Aion currently does not support the Whisper protocol.")
    }

    @Deprecated("")
    override fun dbGetString(databaseName: String, keyName: String): Request<*, DbGetString> {
        throw UnsupportedOperationException("The database APIs have been deprecated in the Ethereum Spec.")
    }

    @Deprecated("")
    override fun dbPutHex(databaseName: String, keyName: String, dataToStore: String): Request<*, DbPutHex> {
        throw UnsupportedOperationException("The database APIs have been deprecated in the Ethereum Spec.")
    }

    @Deprecated("")
    override fun dbGetHex(databaseName: String, keyName: String): Request<*, DbGetHex> {
        throw UnsupportedOperationException("The database APIs have been deprecated in the Ethereum Spec.")
    }*/

    // Implementation Deviations

    /**
     * @param defaultBlockParameter `pending` status is not supported for as a default block parameter.
     */
    override fun ethGetBalance(
        address: String,
        defaultBlockParameter: DefaultBlockParameter
    ):
        Request<*, EthGetBalance> {

        return Request<Any, EthGetBalance>(
            "eth_getBalance",
            removePendingStatusParameter(defaultBlockParameter, address),
            web3jService,
            EthGetBalance::class.java
        )
    }

    /**
     * @param defaultBlockParameter `pending` status is not supported for as a default block parameter.
     */
    override fun ethGetCode(
        address: String,
        defaultBlockParameter: DefaultBlockParameter
    ):
        Request<*, EthGetCode> {

        return Request<Any, EthGetCode>(
            "eth_getCode",
            removePendingStatusParameter(defaultBlockParameter, address),
            web3jService,
            EthGetCode::class.java
        )
    }

    /**
     * @param defaultBlockParameter `pending` status is not supported for as a default block parameter.
     */
    override fun ethGetTransactionCount(
        address: String,
        defaultBlockParameter: DefaultBlockParameter
    ):
        Request<*, EthGetTransactionCount> {

        return Request<Any, EthGetTransactionCount>(
            "eth_getTransactionCount",
            removePendingStatusParameter(defaultBlockParameter, address),
            web3jService,
            EthGetTransactionCount::class.java
        )
    }

    /**
     * @param defaultBlockParameter `pending` status is not supported for as a default block parameter.
     */
    override fun ethGetStorageAt(
        address: String,
        position: BigInteger,
        defaultBlockParameter: DefaultBlockParameter
    ):
        Request<*, EthGetStorageAt> {

        return Request<Any, EthGetStorageAt>(
            "eth_getStorageAt",
            removePendingStatusParameter(defaultBlockParameter, address, Numeric.encodeQuantity(position)),
            web3jService,
            EthGetStorageAt::class.java
        )
    }

    /**
     * @param defaultBlockParameter `pending` status is not supported for as a default block parameter.
     */
    override fun ethCall(
        transaction: Transaction,
        defaultBlockParameter: DefaultBlockParameter
    ):
        Request<*, EthCall> {

        return Request<Any, EthCall>(
            "eth_call",
            removePendingStatusParameter(defaultBlockParameter, transaction),
            web3jService,
            org.web3j.protocol.core.methods.response.EthCall::class.java
        )
    }

    private fun removePendingStatusParameter(
        defaultBlockParameter: DefaultBlockParameter,
        vararg arguments: Any
    ):
        List<*> {

        val result = Arrays.asList(*arguments)

        if (!DefaultBlockParameterName.PENDING.value
                .equals(defaultBlockParameter.value, ignoreCase = true)
        ) {

            result.add(defaultBlockParameter.value)
        }

        return result
    }

    companion object : KLogging()
}
