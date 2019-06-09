package org.web3j.aion.crypto

import org.web3j.aion.abi.nrg
import org.web3j.aion.abi.nrgPrice
import org.web3j.crypto.Sign
import org.web3j.crypto.TransactionEncoder
import org.web3j.rlp.RlpString
import org.web3j.rlp.RlpType
import org.web3j.utils.Bytes
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.time.Clock
import java.util.ArrayList

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
internal class AionTransactionEncoder(private val clock: Clock = Clock.systemUTC()) :
    TransactionEncoder() {

    fun asRlpValues(rawTransaction: AionTransaction, signatureData: Sign.SignatureData?): List<RlpType> {

        val result = ArrayList<RlpType>()
        result.add(RlpString.create(rawTransaction.nonce))

        // an empty to address (contract creation) should not be encoded as a numeric 0 value
        val to = if (rawTransaction.to.isNullOrEmpty()) {
            RlpString.create("")
        } else {
            // addresses that start with zeros should be encoded with the zeros included,
            // not as numeric values
            RlpString.create(Numeric.hexStringToByteArray(rawTransaction.to))
        }

        result.add(to)
        result.add(RlpString.create(rawTransaction.value))

        // value field will already be hex encoded, so we need to convert into binary first
        result.add(RlpString.create(Numeric.hexStringToByteArray(rawTransaction.data)))
        result.add(RlpString.create(BigInteger.valueOf(clock.millis() * 1000).toByteArray()))
        result.add(RlpString.create(rawTransaction.nrg))
        result.add(RlpString.create(rawTransaction.nrgPrice))
        result.add(RlpString.create(rawTransaction.type.data))

        signatureData?.apply {
            result.add(RlpString.create(Bytes.trimLeadingZeroes(v)))
            result.add(RlpString.create(Bytes.trimLeadingZeroes(r)))
            result.add(RlpString.create(Bytes.trimLeadingZeroes(s)))
        }

        return result
    }
}