package org.web3j.protocol.aion

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.isZero
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.web3j.greeter.Greeter
import org.web3j.protocol.core.DefaultBlockParameterName.PENDING
import org.web3j.protocol.http.HttpService
import org.web3j.tx.ClientTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.security.Security

@Testcontainers
class AionIT {

    init {
        Security.addProvider(BouncyCastleProvider())
//        Security.addProvider(EdDSASecurityProvider())
    }

    @Test
    fun testGetUncleCountByBlockHash() {
        val hash = "0x34e9d4af6d2bace6a54b6039843e55bdc1d4ff425cd8e8c8f3fa96ca89f58a53"

        aion.ethGetUncleCountByBlockHash(hash).send().apply {
            assertThat(result).isNull()
            assertThat(error.code).isEqualTo(-32601)
            assertThat(error.message).isEqualTo("Method not found")
        }
    }

    @Test
    fun testGetBalance() {
        aion.ethGetBalance(ADDRESS, PENDING).send().apply {
            assertThat(error).isNull()
            assertThat(balance).isZero()
        }
    }

    @Test
    internal fun testContractDeployUnsigned() {
        Greeter.deploy(aion, manager, DefaultGasProvider(), "0x0").send()
    }

    companion object {

        private const val ACCOUNT = "a0d5c14a9a2f84a1a8b20fbc329f27e8cb2d2dc0752bb4411b9cd77814355ce6"
        private const val ADDRESS = "0x$ACCOUNT"

        private lateinit var aion: Aion
        private lateinit var manager: TransactionManager

        @BeforeAll
        @JvmStatic
        fun initClient() {
            "http://${AION.containerIpAddress}:${AION.getMappedPort(8545)}/".apply {
                aion = Aion.build(HttpService(this))
                aion.personalUnlockAccount(ADDRESS, "410n").send()
            }
            manager = ClientTransactionManager(aion, ADDRESS)
        }

        @Container
        @JvmStatic
        private val AION = KGenericContainer("aionnetwork/aion:0.3.3")
            .withClasspathResourceMapping("aion/config", "/aion/custom/config", BindMode.READ_ONLY)
            .withClasspathResourceMapping("aion/keystore", "/aion/custom/keystore", BindMode.READ_ONLY)
            .withCommand("/aion/aion.sh --network custom")
            .withExposedPorts(8545)
    }

    class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)
}
