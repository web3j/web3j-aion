package org.web3j.aion.crypto

import assertk.assertThat
import assertk.assertions.containsExactly
import org.junit.Test
import org.web3j.aion.abi.nrg
import org.web3j.aion.abi.nrgPrice
import org.web3j.crypto.Sign
import org.web3j.rlp.RlpString
import org.web3j.utils.Bytes
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class AionTransactionAdapterTest {

    private val clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)
    private val adapter = AionTransactionAdapter(clock)

    @Test
    fun `transaction RLP values`() {

        val aionTransaction = AionTransaction(
            BigInteger.ZERO,
            BigInteger.TEN,
            BigInteger.valueOf(21000),
            "0xa0c57475b6a30901b2348c1071d7b27a471f43f8bf895d04b73db08e659efe99",
            BigInteger.TEN,
            "",
            AionTransactionType.FVM
        )

        val signatureData = Sign.SignatureData(ByteArray(0), ByteArray(0), ByteArray(0))
        val rlpValues = adapter.asRlpValues(aionTransaction, signatureData)

        assertThat(rlpValues).containsExactly(
            RlpString.create(aionTransaction.nonce),
            RlpString.create(Numeric.hexStringToByteArray(aionTransaction.to)),
            RlpString.create(aionTransaction.value),
            RlpString.create(Numeric.hexStringToByteArray(aionTransaction.data)),
            RlpString.create(BigInteger.valueOf(clock.millis() * 1000).toByteArray()),
            RlpString.create(aionTransaction.nrg),
            RlpString.create(aionTransaction.nrgPrice),
            RlpString.create(aionTransaction.type.data),
            RlpString.create(Bytes.trimLeadingZeroes(signatureData.v)),
            RlpString.create(Bytes.trimLeadingZeroes(signatureData.r)),
            RlpString.create(Bytes.trimLeadingZeroes(signatureData.s))
        )
    }
}