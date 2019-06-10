package org.web3j.aion.tx

import org.web3j.aion.crypto.AionTransaction
import org.web3j.aion.crypto.Ed25519KeyPair
import org.web3j.aion.protocol.Aion
import org.web3j.crypto.Hash
import org.web3j.crypto.RawTransaction
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.rlp.RlpDecoder
import org.web3j.rlp.RlpEncoder
import org.web3j.rlp.RlpList
import org.web3j.rlp.RlpString
import org.web3j.rlp.RlpType
import org.web3j.tx.TransactionManager
import org.web3j.utils.Numeric
import java.math.BigInteger

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

    fun sign(transaction: RawTransaction): String {
        if (transaction !is AionTransaction) {
            error("Invalid transaction type: ${transaction.javaClass}")
        }

        // hash and sign encoded message
        val rlpValues = transaction.rlpEncode()
        val rlpEncoded = RlpEncoder.encode(RlpList(rlpValues))

        val hash = Hash.blake2b256(rlpEncoded)
        val signature = keyPair.sign(hash)

        if (!keyPair.verify(hash, signature)) {
            error("Invalid signature")
        }

        // aion-specific signature scheme
        val pubSig = RlpDecoder.decode(rlpEncoded).values.apply {
            add(RlpString.create(keyPair.publicKey + signature))
        }

        // re-encode with signature included
        val rawTransaction = RlpEncoder.encode(RlpList(pubSig))
        return Numeric.toHexString(rawTransaction)
    }

    private fun getGasPrice(): BigInteger {
        return aion.ethGasPrice().send().gasPrice
    }

    private fun getNonce(): BigInteger {
        return aion.ethGetTransactionCount(address, LATEST).send().transactionCount
    }

    private fun AionTransaction.rlpEncode(): List<RlpType> {
        return mutableListOf<RlpType>().apply {

            add(RlpString.create(this@rlpEncode.nonce ?: this@AionTransactionManager.getNonce()))

            // an empty to address (contract creation) should not be encoded as a numeric 0 value
            if (to != null && to.isNotEmpty()) {
                // addresses that start with zeros should be encoded with the zeros included, not
                // as numeric values
                add(RlpString.create(Numeric.hexStringToByteArray(to)))
            } else {
                add(RlpString.create(""))
            }

            add(RlpString.create(value))

            // data field will already be hex encoded, so we need to convert into binary first
            add(RlpString.create(Numeric.hexStringToByteArray(data)))

            // Timestamp
            val timestamp = timestamp ?: System.nanoTime()
            add(RlpString.create(timestamp))

            add(RlpString.create(gasLimit))
            add(RlpString.create(gasPrice ?: this@AionTransactionManager.getGasPrice()))
            add(RlpString.create(type.data))

//        if (signatureData != null) {
//            add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.v)))
//            add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.r)))
//            add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.s)))
//        }
        }
    }

    companion object {
        const val AION_PUB_SIG_LEN = 96
    }
}
