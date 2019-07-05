package org.web3j.aion.protocol

import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.BindMode.READ_WRITE
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.web3j.aion.VirtualMachine
import org.web3j.aion.crypto.Ed25519KeyPair
import org.web3j.aion.protocol.AionIntegrationTest.Network.LOCALHOST
import org.web3j.aion.protocol.AionIntegrationTest.Network.MASTERY
import org.web3j.aion.tx.AionTransactionManager
import org.web3j.protocol.http.HttpService
import org.web3j.tx.TransactionManager

@Testcontainers
abstract class AionIntegrationTest(private val targetVm: VirtualMachine) {

    protected lateinit var manager: TransactionManager

    enum class Network {
        LOCALHOST,
        MASTERY
    }

    @BeforeEach
    fun setUp() {
        manager = AionTransactionManager(aion, keyPair, targetVm, 20, 3000)
    }

    companion object {

        @Container
        @JvmStatic
        private val AION = KGenericContainer("aionnetwork/aion:Latest")
            .withClasspathResourceMapping("aion/config", "/aion/custom/config", READ_WRITE)
            .withClasspathResourceMapping("aion/log", "/aion/custom/log", READ_WRITE)
            .withCommand("/aion/aion.sh --network custom")
            .withExposedPorts(8545)

        @JvmStatic
        protected val NETWORK = MASTERY

        @JvmStatic
        private val RPC_URL: Map<Network, String> by lazy {
            mapOf(
                LOCALHOST to "http://${AION.containerIpAddress}:${AION.getMappedPort(8545)}/",
                MASTERY to "https://aion.api.nodesmith.io/v1/mastery/jsonrpc?apiKey=7b1d449f07774200bc1000a8b0eb1a9e"
            )
        }

        @JvmStatic
        protected val PRIVATE_KEY = mapOf(
            LOCALHOST to "0x183759fc5cfd01a893ce417c2dde2f3f94026f96276043ddc98ab95a62dc3583a13df70b0ccc362e94c02e7bcd514523add9edcbb20412f00544a462f00d63e4",
            MASTERY to "0x4776895c43f77676cdec51a6c92d2a1bacdf16ddcc6e7e07ab39104b42e1e52608fe2bf5757b8261d4937f13b5815448f2144f9c1409a3fab4c99ca86fff8a36"
        )

        @JvmStatic
        protected val aion: Aion by lazy {
            Aion.build(HttpService(RPC_URL[NETWORK]))
        }

        @JvmStatic
        protected val keyPair: Ed25519KeyPair by lazy {
            Ed25519KeyPair(PRIVATE_KEY.getValue(NETWORK))
        }

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)
    }
}
