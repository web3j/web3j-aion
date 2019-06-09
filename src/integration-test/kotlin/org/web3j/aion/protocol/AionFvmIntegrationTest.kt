package org.web3j.aion.protocol

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.web3j.aion.tx.gas.AionGasProvider
import org.web3j.greeter.Greeter
import org.web3j.tx.ClientTransactionManager

class AionFvmIntegrationTest : AionIntegrationTest() {

    @Test
    internal fun testContractDeployUnsigned() {
        val manager = ClientTransactionManager(aion, ACCOUNT[Network.LOCALHOST])
        Greeter.deploy(aion, manager, AionGasProvider, "Aion test").send().apply {
            assertThat(greet().send()).isEqualTo("Aion test")
        }
    }

    @Test
    internal fun testContractDeploy() {
        Greeter.deploy(aion, manager, AionGasProvider, "Aion test").send().apply {
            assertThat(greet().send()).isEqualTo("Aion test")
        }
    }

    @Test
    internal fun testContractLoad() {
        val address = "0xa08f5C149f4Ba76582D23da202266554Ffa2d613cFD40C5F6816b897ccFd5978"
        Greeter.load(address, aion, manager, AionGasProvider).apply {
            assertThat(greet().send()).isEqualTo("Aion test")
        }
    }
}
