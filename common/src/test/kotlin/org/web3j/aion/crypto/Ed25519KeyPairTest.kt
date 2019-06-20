package org.web3j.aion.crypto

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.Test
import org.web3j.utils.Numeric
import org.web3j.utils.Numeric.hexStringToByteArray

class Ed25519KeyPairTest {

    private val keyPair =
        Ed25519KeyPair("0x4776895c43f77676cdec51a6c92d2a1bacdf16ddcc6e7e07ab39104b42e1e52608fe2bf5757b8261d4937f13b5815448f2144f9c1409a3fab4c99ca86fff8a36")

    @Test
    fun `address from private key`() {
        assertThat(keyPair.address).isEqualTo("0xa0c57475b6a30901b2348c1071d7b27a471f43f8bf895d04b73db08e659efe99")
    }

    @Test
    fun `public key from private key`() {
        assertThat(Numeric.toHexStringNoPrefix(keyPair.publicKey))
            .isEqualTo("08fe2bf5757b8261d4937f13b5815448f2144f9c1409a3fab4c99ca86fff8a36")
    }

    @Test
    fun `sign transaction hash`() {
        val hash = hexStringToByteArray("f2499f6f6ab74d250ab266b1c36167e9352904d70ae25ef26383b79eb4b7958b")

        val signature = hexStringToByteArray(
            "a91c55533ae0493dcae07cc47c412a59d8e9b2bf02d12ead1d20ebd2386996fa9a29c8753ae82013b85a8453da3688e4cd436d3f71d4dc450e1d5b6504940804"
        )

        assertThat(keyPair.sign(hash)).isEqualTo(signature)
    }

    @Test
    fun `verify transaction hash`() {
        val hash = hexStringToByteArray("f2499f6f6ab74d250ab266b1c36167e9352904d70ae25ef26383b79eb4b7958b")

        val signature = hexStringToByteArray(
            "a91c55533ae0493dcae07cc47c412a59d8e9b2bf02d12ead1d20ebd2386996fa9a29c8753ae82013b85a8453da3688e4cd436d3f71d4dc450e1d5b6504940804"
        )

        assertThat(keyPair.verify(hash, signature)).isTrue()
    }

    @Test
    fun `verify wrong transaction hash`() {
        val hash = hexStringToByteArray("f2499f6f6ab74d250ab266b1c36167e9352904d70ae25ef26383b79eb4b7958b")

        val signature = hexStringToByteArray(
            "a91c55533ae0493dcae07cc47c412a59d8e9b2bf02d12ead1d20ebd2386996fa9a29c8753ae82013b85a8453da3688e4cd436d3f71d4dc450e1d5b6504940805"
        )

        assertThat(keyPair.verify(hash, signature)).isFalse()
    }
}
