package org.web3j.aion.protocol

import assertk.assertThat
import assertk.assertions.isEqualTo
import helloavm.HelloAvm
import org.junit.jupiter.api.Test
import org.web3j.aion.tx.gas.AionGasProvider
import org.web3j.tx.ClientTransactionManager

class AionAvmIntegrationTest : AionIntegrationTest() {

    @Test
    internal fun testAvmLoad() {
        HelloAvm.load(
            "0xa040f6b14cd0a7158d6b120cdd0994a5dbc11b19f3e8914154938088ac1bc041",
            aion, ClientTransactionManager(aion, ACCOUNT[NETWORK]), AionGasProvider
        ).apply {
            assertThat(string.send()).isEqualTo("Hello AVM")
        }
    }
}
