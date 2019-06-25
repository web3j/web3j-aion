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

            send_setString("Hello test").send()
            assertThat(call_getString().send()).isEqualTo("Hello test")
        }
    }

    @Test
    internal fun `deploy and call ERC20Token contract`() {
        ERC20Token.deploy(
            aion, manager, AionGasProvider,
            "Test", "TEST", 2, keyPair.address
        ).send().apply {
            assertThat(call_name().send()).isEqualTo("Test")
            assertThat(call_symbol().send()).isEqualTo("TEST")
            assertThat(call_decimals().send()).isEqualTo(2)
        }

        // TODO Send a transfer to a new account on Docker test
//        val newAccount = aion.personalNewAccount("test").send()
//        contract.send_transfer(newAccount.accountId, 1L).send().apply {
//            assertThat(aion.ethGetBalance(newAccount.accountId, LATEST)).isEqualTo(1L)
//        }
    }
}
