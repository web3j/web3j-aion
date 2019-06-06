package org.web3j.aion.protocol

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.web3j.aion.crypto.Ed25519KeyPair
import org.web3j.aion.tx.gas.AionGasProvider
import org.web3j.crypto.Credentials
import org.web3j.greeter.Greeter
import org.web3j.tx.ClientTransactionManager
import org.web3j.tx.RawTransactionManager
import java.math.BigInteger

class AionFvmIntegrationTest : AionIntegrationTest() {

    @Test
    internal fun testContractDeployUnsigned() {
        val manager = ClientTransactionManager(aion, ACCOUNT[NETWORK])
        Greeter.deploy(
            aion, manager,
            AionGasProvider, "Aion test"
        ).send().apply {
            assertThat(greet().send()).isEqualTo("Aion test")
        }
    }

    @Test
    internal fun testContractDeploySigned() {
        val keyPair = Ed25519KeyPair.create(BigInteger(PRIVATE_KEY[NETWORK], 16))
        val manager = RawTransactionManager(aion, Credentials.create(keyPair))
        Greeter.deploy(
            aion, manager,
            AionGasProvider, "Aion test"
        ).send().apply {
            assertThat(greet().send()).isEqualTo("Aion test")
        }
    }
}
