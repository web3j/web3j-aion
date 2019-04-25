package org.web3j.protocol.aion;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.ShhFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.DbGetHex;
import org.web3j.protocol.core.methods.response.DbGetString;
import org.web3j.protocol.core.methods.response.DbPutHex;
import org.web3j.protocol.core.methods.response.DbPutString;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthCompileLLL;
import org.web3j.protocol.core.methods.response.EthCompileSerpent;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthGetStorageAt;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetUncleCountByBlockHash;
import org.web3j.protocol.core.methods.response.EthGetUncleCountByBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetWork;
import org.web3j.protocol.core.methods.response.EthSubmitWork;
import org.web3j.protocol.core.methods.response.ShhAddToGroup;
import org.web3j.protocol.core.methods.response.ShhHasIdentity;
import org.web3j.protocol.core.methods.response.ShhMessages;
import org.web3j.protocol.core.methods.response.ShhNewFilter;
import org.web3j.protocol.core.methods.response.ShhNewGroup;
import org.web3j.protocol.core.methods.response.ShhNewIdentity;
import org.web3j.protocol.core.methods.response.ShhPost;
import org.web3j.protocol.core.methods.response.ShhUninstallFilter;
import org.web3j.protocol.core.methods.response.ShhVersion;
import org.web3j.utils.Numeric;

public class JsonRpc2_0Aion extends JsonRpc2_0Web3j implements Aion {

    public JsonRpc2_0Aion(final Web3jService web3jService) {
        super(web3jService);
    }

    public JsonRpc2_0Aion(
            final Web3jService web3jService,
            final long pollingInterval,
            final ScheduledExecutorService scheduledExecutorService) {
        super(web3jService, pollingInterval, scheduledExecutorService);
    }

    // Unsupported Endpoints

    @Override
    public Request<?, EthGetUncleCountByBlockHash> ethGetUncleCountByBlockHash(
            final String blockHash) {
        throw new UnsupportedOperationException("Spec-defined functions that deal with Uncles are not part of the Aion protocol.");
    }

    @Override
    public Request<?, EthGetUncleCountByBlockNumber> ethGetUncleCountByBlockNumber(
            final DefaultBlockParameter defaultBlockParameter) {
        throw new UnsupportedOperationException("Spec-defined functions that deal with Uncles are not part of the Aion protocol.");
    }

    @Override
    public Request<?, EthBlock> ethGetUncleByBlockHashAndIndex(
            final String blockHash, final BigInteger transactionIndex) {
        throw new UnsupportedOperationException("Spec-defined functions that deal with Uncles are not part of the Aion protocol.");
    }

    @Override
    public Request<?, EthBlock> ethGetUncleByBlockNumberAndIndex(
            final DefaultBlockParameter defaultBlockParameter, final BigInteger uncleIndex) {
        throw new UnsupportedOperationException("Spec-defined functions that deal with Uncles are not part of the Aion protocol.");
    }

    @Override
    public Request<?, EthCompileLLL> ethCompileLLL(
            final String sourceCode) {
        throw new UnsupportedOperationException("The LLL compiler is not supported. " +
                "Currently, the only language supported on Aion is Solidity-on-Aion-FVM.");
    }

    @Override
    public Request<?, EthCompileSerpent> ethCompileSerpent(
            final String sourceCode) {
        throw new UnsupportedOperationException("The Serpent compiler is not supported. " +
                "Currently, the only language supported on Aion is Solidity-on-Aion-FVM.");
    }

    @Override
    public Request<?, EthGetWork> ethGetWork() {
        throw new UnsupportedOperationException("Implemented as part of the stratum API submodule.");
    }

    @Override
    public Request<?, EthSubmitWork> ethSubmitWork(
            final String nonce, final String headerPowHash, final String mixDigest) {
        throw new UnsupportedOperationException("Implemented as part of the stratum API submodule.");
    }

    @Override
    public Request<?, ShhPost> shhPost(
            final org.web3j.protocol.core.methods.request.ShhPost shhPost) {
        throw new UnsupportedOperationException("Aion currently does not support the Whisper protocol.");
    }

    @Override
    public Request<?, ShhVersion> shhVersion() {
        throw new UnsupportedOperationException("Aion currently does not support the Whisper protocol.");
    }

    @Override
    public Request<?, ShhNewIdentity> shhNewIdentity() {
        throw new UnsupportedOperationException("Aion currently does not support the Whisper protocol.");
    }

    @Override
    public Request<?, ShhHasIdentity> shhHasIdentity(
            final String identityAddress) {
        throw new UnsupportedOperationException("Aion currently does not support the Whisper protocol.");
    }

    @Override
    public Request<?, ShhNewGroup> shhNewGroup() {
        throw new UnsupportedOperationException("Aion currently does not support the Whisper protocol.");
    }

    @Override
    public Request<?, ShhAddToGroup> shhAddToGroup(
            final String identityAddress) {
        throw new UnsupportedOperationException("Aion currently does not support the Whisper protocol.");
    }

    @Override
    public Request<?, ShhNewFilter> shhNewFilter(
            final ShhFilter shhFilter) {
        throw new UnsupportedOperationException("Aion currently does not support the Whisper protocol.");
    }

    @Override
    public Request<?, ShhUninstallFilter> shhUninstallFilter(
            final BigInteger filterId) {
        throw new UnsupportedOperationException("Aion currently does not support the Whisper protocol.");
    }

    @Override
    public Request<?, ShhMessages> shhGetFilterChanges(
            final BigInteger filterId) {
        throw new UnsupportedOperationException("Aion currently does not support the Whisper protocol.");
    }

    @Override
    public Request<?, ShhMessages> shhGetMessages(
            final BigInteger filterId) {
        throw new UnsupportedOperationException("Aion currently does not support the Whisper protocol.");
    }

    @Override
    public Request<?, DbPutString> dbPutString(
            final String databaseName, final String keyName, final String stringToStore) {
        throw new UnsupportedOperationException("Aion currently does not support the Whisper protocol.");
    }

    @Override
    @Deprecated
    public Request<?, DbGetString> dbGetString(
            final String databaseName, final String keyName) {
        throw new UnsupportedOperationException("The database APIs have been deprecated in the Ethereum Spec.");
    }

    @Override
    @Deprecated
    public Request<?, DbPutHex> dbPutHex(
            final String databaseName, final String keyName, final String dataToStore) {
        throw new UnsupportedOperationException("The database APIs have been deprecated in the Ethereum Spec.");
    }

    @Override
    @Deprecated
    public Request<?, DbGetHex> dbGetHex(
            final String databaseName, final String keyName) {
        throw new UnsupportedOperationException("The database APIs have been deprecated in the Ethereum Spec.");
    }

    // Implementation Deviations

    /**
     * @param defaultBlockParameter <code>pending</code> status is not supported for as a default block parameter.
     */
    @Override
    public Request<?, EthGetBalance> ethGetBalance(
            final String address, final DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getBalance",
                removePendingStatusParameter(defaultBlockParameter, address),
                web3jService,
                EthGetBalance.class);
    }

    /**
     * @param defaultBlockParameter <code>pending</code> status is not supported for as a default block parameter.
     */
    @Override
    public Request<?, EthGetCode> ethGetCode(
            final String address, final DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getCode",
                removePendingStatusParameter(defaultBlockParameter, address),
                web3jService,
                EthGetCode.class);
    }

    /**
     * @param defaultBlockParameter <code>pending</code> status is not supported for as a default block parameter.
     */
    @Override
    public Request<?, EthGetTransactionCount> ethGetTransactionCount(
            final String address, final DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getTransactionCount",
                removePendingStatusParameter(defaultBlockParameter, address),
                web3jService,
                EthGetTransactionCount.class);
    }

    /**
     * @param defaultBlockParameter <code>pending</code> status is not supported for as a default block parameter.
     */
    @Override
    public Request<?, EthGetStorageAt> ethGetStorageAt(
            String address, BigInteger position, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getStorageAt",
                removePendingStatusParameter(defaultBlockParameter, address, Numeric.encodeQuantity(position)),
                web3jService,
                EthGetStorageAt.class);
    }

    /**
     * @param defaultBlockParameter <code>pending</code> status is not supported for as a default block parameter.
     */
    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthCall> ethCall(
            Transaction transaction, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_call",
                removePendingStatusParameter(defaultBlockParameter, transaction),
                web3jService,
                org.web3j.protocol.core.methods.response.EthCall.class);
    }

    private static List<?> removePendingStatusParameter(
            final DefaultBlockParameter defaultBlockParameter, final Object... arguments) {

        final List<Object> result = Arrays.asList(arguments);

        if (!DefaultBlockParameterName.PENDING.getValue()
                .equalsIgnoreCase(defaultBlockParameter.getValue())) {

            result.add(defaultBlockParameter.getValue());
        }

        return result;
    }
}
