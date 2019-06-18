package org.web3j.aion.crypto

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test
import org.web3j.utils.Numeric

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
}