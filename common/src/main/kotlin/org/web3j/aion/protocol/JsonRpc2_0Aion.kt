package org.web3j.aion.protocol

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.web3j.protocol.ObjectMapperFactory
import org.web3j.protocol.Web3jService
import org.web3j.protocol.admin.JsonRpc2_0Admin
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
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
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.security.Security
import java.util.concurrent.ScheduledExecutorService

/**
 * Jackson mix-in for JSON-RPC [org.web3j.protocol.core.Request] as per
 * [JSON RPC 2.0 (Google) Spec Deviations](https://github.com/aionnetwork/aion/wiki/JSON-RPC-API-Docs#3-json-rpc-20-google-spec-deviations).
 *
 * @see ObjectMapper.addMixIn
 */
internal class AionRequestMixIn(
    @field:JsonIgnore
    var jsonrpc: String
)

/**
 * Aion JSON-RPC implementation following the
 * [Aion docs](https://github.com/aionnetwork/aion/wiki/JSON-RPC-API-Docs).
 */
@Suppress("ClassName")
internal class JsonRpc2_0Aion : JsonRpc2_0Admin, Aion {

    init {
        Security.addProvider(BouncyCastleProvider())

        ObjectMapperFactory.getObjectMapper()
            .addMixIn(Request::class.java, AionRequestMixIn::class.java)
    }

    constructor(web3jService: Web3jService) : super(web3jService)

    constructor(
        web3jService: Web3jService,
        pollingInterval: Long,
        scheduledExecutorService: ScheduledExecutorService
    ) : super(web3jService, pollingInterval, scheduledExecutorService)

    override fun ethGetUncleCountByBlockHash(blockHash: String):
        Request<*, EthGetUncleCountByBlockHash> {
        return UnsupportedRequest(
            "eth_getUncleCountByBlockHash",
            listOf(blockHash),
            EthGetUncleCountByBlockHash::class.java
        )
    }

    override fun ethGetUncleCountByBlockNumber(defaultBlockParameter: DefaultBlockParameter):
        Request<*, EthGetUncleCountByBlockNumber> {
        return UnsupportedRequest(
            "eth_getUncleCountByBlockNumber",
            listOf(defaultBlockParameter.value),
            EthGetUncleCountByBlockNumber::class.java
        )
    }

    override fun ethGetUncleByBlockHashAndIndex(blockHash: String, transactionIndex: BigInteger):
        Request<*, EthBlock> {
        return UnsupportedRequest(
            "eth_getUncleByBlockHashAndIndex",
            listOf(blockHash, Numeric.encodeQuantity(transactionIndex)),
            EthBlock::class.java
        )
    }

    override fun ethGetUncleByBlockNumberAndIndex(
        defaultBlockParameter: DefaultBlockParameter,
        uncleIndex: BigInteger
    ): Request<*, EthBlock> {
        return UnsupportedRequest(
            "eth_getUncleByBlockNumberAndIndex",
            listOf(defaultBlockParameter.value, Numeric.encodeQuantity(uncleIndex)),
            EthBlock::class.java
        )
    }

    override fun ethCompileLLL(sourceCode: String): Request<*, EthCompileLLL> {
        return UnsupportedRequest(
            "eth_compileLLL",
            listOf(sourceCode),
            EthCompileLLL::class.java
        )
    }

    override fun ethCompileSerpent(sourceCode: String): Request<*, EthCompileSerpent> {
        return UnsupportedRequest(
            "eth_compileSerpent",
            listOf(sourceCode),
            EthCompileSerpent::class.java
        )
    }

    override fun ethGetWork(): Request<*, EthGetWork> {
        return UnsupportedRequest(
            "eth_getWork",
            listOf<Any>(),
            EthGetWork::class.java
        )
    }

    override fun ethSubmitWork(nonce: String, headerPowHash: String, mixDigest: String): Request<*, EthSubmitWork> {
        return UnsupportedRequest(
            "eth_submitWork",
            listOf(nonce, headerPowHash, mixDigest),
            EthSubmitWork::class.java
        )
    }

    override fun shhPost(shhPost: org.web3j.protocol.core.methods.request.ShhPost): Request<*, ShhPost> {
        return UnsupportedRequest(
            "shh_post",
            listOf(shhPost),
            ShhPost::class.java
        )
    }

    override fun shhVersion(): Request<*, ShhVersion> {
        return UnsupportedRequest(
            "shh_version",
            listOf<Any>(),
            ShhVersion::class.java
        )
    }

    override fun shhNewIdentity(): Request<*, ShhNewIdentity> {
        return UnsupportedRequest(
            "shh_newIdentity",
            listOf<Any>(),
            ShhNewIdentity::class.java
        )
    }

    override fun shhHasIdentity(identityAddress: String): Request<*, ShhHasIdentity> {
        return UnsupportedRequest(
            "shh_hasIdentity",
            listOf(identityAddress),
            ShhHasIdentity::class.java
        )
    }

    override fun shhNewGroup(): Request<*, ShhNewGroup> {
        return UnsupportedRequest(
            "shh_newGroup",
            listOf<Any>(),
            ShhNewGroup::class.java
        )
    }

    override fun shhAddToGroup(identityAddress: String): Request<*, ShhAddToGroup> {
        return UnsupportedRequest(
            "shh_addToGroup",
            listOf(identityAddress),
            ShhAddToGroup::class.java
        )
    }

    override fun shhNewFilter(shhFilter: ShhFilter): Request<*, ShhNewFilter> {
        return UnsupportedRequest(
            "shh_newFilter",
            listOf(shhFilter),
            ShhNewFilter::class.java
        )
    }

    override fun shhUninstallFilter(filterId: BigInteger): Request<*, ShhUninstallFilter> {
        return UnsupportedRequest(
            "shh_uninstallFilter",
            listOf(Numeric.toHexStringWithPrefixSafe(filterId)),
            ShhUninstallFilter::class.java
        )
    }

    override fun shhGetFilterChanges(filterId: BigInteger): Request<*, ShhMessages> {
        return UnsupportedRequest(
            "shh_getFilterChanges",
            listOf(Numeric.toHexStringWithPrefixSafe(filterId)),
            ShhMessages::class.java
        )
    }

    override fun shhGetMessages(filterId: BigInteger): Request<*, ShhMessages> {
        return UnsupportedRequest(
            "shh_getMessages",
            listOf(Numeric.toHexStringWithPrefixSafe(filterId)),
            ShhMessages::class.java
        )
    }

    override fun dbPutString(databaseName: String, keyName: String, stringToStore: String): Request<*, DbPutString> {
        return UnsupportedRequest(
            "db_putString",
            listOf(databaseName, keyName, stringToStore),
            DbPutString::class.java
        )
    }

    override fun dbGetString(databaseName: String, keyName: String): Request<*, DbGetString> {
        return UnsupportedRequest(
            "db_getString",
            listOf(databaseName, keyName),
            DbGetString::class.java
        )
    }

    override fun dbPutHex(databaseName: String, keyName: String, dataToStore: String): Request<*, DbPutHex> {
        return UnsupportedRequest(
            "db_putHex",
            listOf(databaseName, keyName, dataToStore),
            DbPutHex::class.java
        )
    }

    override fun dbGetHex(databaseName: String, keyName: String): Request<*, DbGetHex> {
        return UnsupportedRequest(
            "db_getHex",
            listOf(databaseName, keyName),
            DbGetHex::class.java
        )
    }

    override fun ethGetBalance(
        address: String,
        defaultBlockParameter: DefaultBlockParameter
    ): Request<*, EthGetBalance> {
        checkPendingStatusParameter(defaultBlockParameter)
        return super.ethGetBalance(address, defaultBlockParameter)
    }

    override fun ethGetCode(
        address: String,
        defaultBlockParameter: DefaultBlockParameter
    ): Request<*, EthGetCode> {
        checkPendingStatusParameter(defaultBlockParameter)
        return super.ethGetCode(address, defaultBlockParameter)
    }

    override fun ethGetTransactionCount(
        address: String,
        defaultBlockParameter: DefaultBlockParameter
    ): Request<*, EthGetTransactionCount> {
        checkPendingStatusParameter(defaultBlockParameter)
        return super.ethGetTransactionCount(address, defaultBlockParameter)
    }

    override fun ethGetStorageAt(
        address: String,
        position: BigInteger,
        defaultBlockParameter: DefaultBlockParameter
    ): Request<*, EthGetStorageAt> {
        checkPendingStatusParameter(defaultBlockParameter)
        return super.ethGetStorageAt(address, position, defaultBlockParameter)
    }

    override fun ethCall(
        transaction: Transaction,
        defaultBlockParameter: DefaultBlockParameter
    ): Request<*, EthCall> {
        checkPendingStatusParameter(defaultBlockParameter)
        return super.ethCall(transaction, defaultBlockParameter)
    }

    companion object : KLogging() {

        private fun checkPendingStatusParameter(defaultBlockParameter: DefaultBlockParameter) {
            if (DefaultBlockParameterName.PENDING.value.equals(defaultBlockParameter.value, ignoreCase = true)) {
                throw AionProtocolException("'pending' status is not supported as a default block parameter.")
            }
        }
    }
}
