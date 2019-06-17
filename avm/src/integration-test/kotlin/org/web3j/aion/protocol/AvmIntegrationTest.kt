package org.web3j.aion.protocol

import assertk.assertThat
import assertk.assertions.isEqualTo
import helloavm.HelloAvm
import org.junit.jupiter.api.Test
import org.web3j.aion.VirtualMachine
import org.web3j.aion.tx.gas.AionGasProvider

class AvmIntegrationTest : AionIntegrationTest(VirtualMachine.AVM) {

    @Test
    internal fun `deploy and call contract`() {
        HelloAvm.deploy(aion, manager, AionGasProvider).send().apply {
            assertThat(string.send()).isEqualTo("Hello AVM")
        }
    }

    @Test
    internal fun `load and call contract`() {
        HelloAvm.load(
            "0xa01af11dc05cc9aedcafdc80c8b94301e11540cf7fbdcff477b6bd964a208adc",
            aion, manager, AionGasProvider
        ).apply {
            assertThat(string.send()).isEqualTo("Hello AVM")
        }
    }

    @Test
    internal fun `load and execute transaction`() {
        HelloAvm.load(
            "0xa01af11dc05cc9aedcafdc80c8b94301e11540cf7fbdcff477b6bd964a208adc",
            aion, manager, AionGasProvider
        ).apply {
            setString("Hello test").sendAsync().thenAccept {
                assertThat(string.send()).isEqualTo("Hello test")
            }.join()
        }
    }
}
