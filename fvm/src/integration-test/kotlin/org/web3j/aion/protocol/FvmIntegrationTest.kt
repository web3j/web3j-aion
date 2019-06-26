package org.web3j.aion.protocol

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.aion.greeter.Greeter
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.web3j.aion.VirtualMachine
import org.web3j.aion.tx.gas.AionGasProvider
import org.web3j.tx.ClientTransactionManager

@Disabled
class FvmIntegrationTest : AionIntegrationTest(VirtualMachine.FVM) {

    @Test
    internal fun testContractDeployUnsigned() {
        val manager = ClientTransactionManager(aion, keyPair.address)
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
        val address = "0xa0c34906d069497cC9d33DF73aD36C4349C1d04ac45f2b0Ec357EfD3797e4E3A"
        Greeter.load(address, aion, manager, AionGasProvider).apply {
            assertThat(greet().send()).isEqualTo("Aion test")
        }
    }
}
