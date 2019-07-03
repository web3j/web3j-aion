package org.web3j.aion.tx

import org.aion.rlp.RLP
import org.web3j.aion.AionConstants
import org.web3j.aion.VirtualMachine
import org.web3j.aion.VirtualMachine.AVM
import org.web3j.aion.crypto.AionTransaction
import org.web3j.aion.crypto.Ed25519KeyPair
import org.web3j.aion.protocol.Aion
import org.web3j.crypto.Hash
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.tx.TransactionManager
import org.web3j.tx.exceptions.TxHashMismatchException
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.util.Arrays

/**
 *  Notes for serialization:
 *
 *  1) NONCE (byte[])
 *  2) TO (byte[])
 *  3) VALUE (byte[])
 *  4) DATA (byte[])
 *  5) TIMESTAMP in nanos (byte[])
 *  6) NRG (long)
 *  7) NRG PRICE (long)
 *  8) TYPE (byte)
 *  9) SIGNATURE (byte[]) (HashUtil.h256(encodeList(1-8)) (optional)
 *
 *  FINAL: encode either (1-8) or (1-9) as list
 *
 *  Based on the code from [TxTool](https://github.com/arajasek/TxTool/blob/master/src/main/java/org/aion/offline/TxTool.java).
 */
class AionTransactionManager(
    private val aion: Aion,
    private val keyPair: Ed25519KeyPair,
    private val targetVm: VirtualMachine = AVM,
    attempts: Int,
    sleepDuration: Long
) : TransactionManager(aion, attempts, sleepDuration, keyPair.address) {

    constructor(
        aion: Aion,
        keyPair: Ed25519KeyPair,
        targetVm: VirtualMachine
    ) : this(
        aion, keyPair, targetVm,
        DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH,
        DEFAULT_POLLING_FREQUENCY
    )

    fun sendTransaction(
        nrgPrice: Long? = null,
        nrgLimit: Long = AionConstants.NRG_TRANSACTION_DEFAULT,
        to: String,
        data: String,
        value: Long? = null,
        constructor: Boolean = false
    ): EthSendTransaction {
        return sendTransaction(
            nrgPrice?.toBigInteger(),
            nrgLimit.toBigInteger(),
            to,
            data,
            value?.toBigInteger(),
            constructor
        )
    }

    override fun sendTransaction(
        gasPrice: BigInteger?,
        gasLimit: BigInteger,
        to: String?,
        data: String,
        value: BigInteger?,
        constructor: Boolean
    ): EthSendTransaction {

        val transaction = AionTransaction(
            to = to,
            data = data,
            value = value,
            targetVm = targetVm,
            nrgPrice = gasPrice,
            nrg = gasLimit,
            constructor = constructor
        )

        val rawTx = sign(transaction)
        val result = aion.ethSendRawTransaction(rawTx).send()

        if (result != null && !result.hasError()) {
            val txHashLocal = Numeric.toHexString(Hash.blake2b256(Numeric.hexStringToByteArray(rawTx)))
            val txHashRemote = result.transactionHash

            if (txHashLocal != txHashRemote) {
                throw TxHashMismatchException(txHashLocal, txHashRemote)
            }
        }

        return result
    }

    override fun sendCall(to: String, data: String, defaultBlockParameter: DefaultBlockParameter): String {
        val transaction = Transaction.createEthCallTransaction(fromAddress, to, data)
        return aion.ethCall(transaction, defaultBlockParameter).send().value
    }

    internal fun sign(transaction: AionTransaction): String {

        // hash and sign encoded message
        val rlpTransaction = transaction.toRplElements()
        val rplEncoded = RLP.encodeList(*rlpTransaction)
        val hash = Hash.blake2b256(rplEncoded)

        // Sign and verify hash
        val signature = keyPair.sign(hash)
        if (!keyPair.verify(hash, signature)) {
            error("Invalid signature")
        }

        // Encode signature with Aion-specific signature scheme
        val rlpSignature = RLP.encodeElement(keyPair.publicKey + signature)
        val encodedWithPayload = RLP.encodeList(*rlpTransaction, rlpSignature)

        return Numeric.toHexString(encodedWithPayload)
    }

    private fun getNrgPrice(): Long {
        return aion.ethGasPrice().send().gasPrice.longValueExact()
    }

    private fun getNonce(): BigInteger {
        return aion.ethGetTransactionCount(keyPair.address, LATEST).send().transactionCount
    }

    private fun AionTransaction.toRplElements(): Array<ByteArray> {
        val nonce = nonce ?: this@AionTransactionManager.getNonce()
        val timestamp = BigInteger.valueOf(timestamp)
        val nrgPrice = nrgPrice ?: this@AionTransactionManager.getNrgPrice()
        val data = Numeric.prependHexPrefix(data)

        val value = if (value == BigInteger.ZERO) {
            null
        } else {
            value
        }

        val type: Byte = when {
            targetVm == AVM && constructor -> TRANSACTION_TYPE_AVM_CONSTRUCTOR
            else -> TRANSACTION_TYPE_OTHER
        }

        return arrayOf(
            RLP.encodeElement(nonce.toByteArray()),
            RLP.encodeElement(to.toByteArray()),
            RLP.encodeElement(value?.toByteArray()),
            RLP.encodeElement(data.toByteArray()),
            RLP.encodeElement(timestamp.toByteArray()),
            RLP.encodeLong(nrg),
            RLP.encodeLong(nrgPrice),
            RLP.encodeByte(type)
        )
    }

    companion object {
        const val TRANSACTION_TYPE_OTHER: Byte = 0x1
        const val TRANSACTION_TYPE_AVM_CONSTRUCTOR: Byte = 0x2

        private fun String?.toByteArray(): ByteArray {
            return this?.run {
                val cleanData = Numeric.cleanHexPrefix(this).replace("\\s".toRegex(), "")
                val biBytes = BigInteger("10$cleanData", 16).toByteArray()
                Arrays.copyOfRange(biBytes, 1, biBytes.size)
            } ?: ByteArray(0)
        }
    }
}
