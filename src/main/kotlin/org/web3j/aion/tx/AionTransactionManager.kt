package org.web3j.aion.tx

import org.aion.rlp.RLP
import org.web3j.aion.crypto.AionTransaction
import org.web3j.aion.crypto.Ed25519KeyPair
import org.web3j.aion.protocol.Aion
import org.web3j.crypto.Hash
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.tx.TransactionManager
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
    private val address: String,
    private val keyPair: Ed25519KeyPair
) : TransactionManager(aion, address) {

    override fun sendTransaction(
        gasPrice: BigInteger?,
        gasLimit: BigInteger?,
        to: String?,
        data: String?,
        value: BigInteger?
    ): EthSendTransaction {
        TODO("not implemented")
    }

    override fun sendCall(to: String?, data: String?, defaultBlockParameter: DefaultBlockParameter?): String {
        TODO("not implemented")
    }

    fun sign(transaction: AionTransaction): String {

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
        return aion.ethGetTransactionCount(address, LATEST).send().transactionCount
    }

    private fun AionTransaction.toRplElements(): Array<ByteArray> {
        val nonce = nonce ?: this@AionTransactionManager.getNonce()
        val value = value?.toByteArray() ?: ByteArray(0)
        val timestamp = BigInteger.valueOf(timestamp)
        val nrgPrice = nrgPrice ?: this@AionTransactionManager.getNrgPrice()

        return arrayOf(
            RLP.encodeElement(nonce.toByteArray()),
            RLP.encodeElement(hexToBytes(to)),
            RLP.encodeElement(value),
            RLP.encodeElement(hexToBytes(data)),
            RLP.encodeElement(timestamp.toByteArray()),
            RLP.encodeLong(nrg),
            RLP.encodeLong(nrgPrice),
            RLP.encodeByte(type.data)
        )
    }

    companion object {
        private fun hexToBytes(data: String?): ByteArray {
            return data?.run {
                val cleanData = Numeric.cleanHexPrefix(data).replace("\\s".toRegex(), "")
                val biBytes = BigInteger("10$cleanData", 16).toByteArray()
                Arrays.copyOfRange(biBytes, 1, biBytes.size)
            } ?: ByteArray(0)
        }
    }
}
