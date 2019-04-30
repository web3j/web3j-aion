package org.web3j.protocol.aion

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.web3j.protocol.core.DefaultBlockParameterName.PENDING
import org.web3j.protocol.http.HttpService
import org.web3j.testcontract.TestContract
import org.web3j.tx.ClientTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

@Testcontainers
class AionIT {

    @Test
    fun testGetUncleCountByBlockHash() {
        val hash = "0x34e9d4af6d2bace6a54b6039843e55bdc1d4ff425cd8e8c8f3fa96ca89f58a53"
        val countByBlockHash = aion.ethGetUncleCountByBlockHash(hash).send()

        assertThat(countByBlockHash.result).isNull()
        assertThat(countByBlockHash.error.code).isEqualTo(-32601)
        assertThat(countByBlockHash.error.message).isEqualTo("Method not found")
    }

    @Test
    fun testGetBalance() {
        val balance = aion.ethGetBalance(ADDRESS, PENDING).send()

        assertThat(balance.error).isNull()
        assertThat(balance.balance).isEqualTo(BigInteger("465934586660000000000000000"))
    }

    @Test
    internal fun testContractDeploy() {
        TestContract.deploy(aion, manager, DefaultGasProvider(), "").send()
    }

    companion object {

        private const val ADDRESS = "0x0000000000000000000000000000000000000000000000000000000000000000"

        private lateinit var aion: Aion
        private lateinit var manager: TransactionManager

        @BeforeAll
        @JvmStatic
        fun initClient() {
            val url = "http://${AION.containerIpAddress}:${AION.getMappedPort(8545)}/"
            aion = Aion.build(HttpService(url))
            manager = ClientTransactionManager(aion, ADDRESS)
        }

        @Container
        @JvmStatic
        private val AION = KGenericContainer("aionnetwork/aion:0.3.3")
            .withCommand("/aion/aion.sh --network custom")
            .withExposedPorts(8545)
    }

    class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)
}
