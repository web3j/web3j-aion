package org.web3j.aion.protocol

import assertk.assertThat
import assertk.assertions.isEqualTo
import erc20.ERC20Token
import helloavm.HelloAvm
import org.junit.jupiter.api.Test
import org.web3j.aion.VirtualMachine
import org.web3j.aion.tx.gas.AionGasProvider

class AvmIntegrationTest : AionIntegrationTest(VirtualMachine.AVM) {

    @Test
    internal fun `deploy and call contract`() {
        HelloAvm.deploy(aion, manager, AionGasProvider).send().apply {
            assertThat(call_getString().send()).isEqualTo("Hello AVM")
        }
    }

    @Test
    internal fun `deploy and call ERC20Token contract`() {
        ERC20Token.deploy(
            aion, manager, AionGasProvider,
            "testcoin", "TEST", 0, keyPair.address
        ).send().apply {
            assertThat(call_decimals().send()).isEqualTo(0)
        }
    }

    @Test
    internal fun `load and execute transaction`() {
        HelloAvm.load(
            "0xa01af11dc05cc9aedcafdc80c8b94301e11540cf7fbdcff477b6bd964a208adc",
            aion, manager, AionGasProvider
        ).apply {
            send_setString("Hello test").sendAsync().thenAccept {
                assertThat(call_getString().send()).isEqualTo("Hello test")
            }.join()
        }
    }
}
