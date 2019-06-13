package org.web3j.aion.protocol

import org.junit.jupiter.api.BeforeAll
import org.web3j.aion.crypto.Ed25519KeyPair
import org.web3j.aion.protocol.AionIntegrationTest.Network.LOCALHOST
import org.web3j.aion.protocol.AionIntegrationTest.Network.MASTERY
import org.web3j.aion.tx.AionTransactionManager
import org.web3j.protocol.http.HttpService
import org.web3j.tx.TransactionManager

// @Testcontainers
abstract class AionIntegrationTest {

    enum class Network {
        LOCALHOST,
        MASTERY
    }

    companion object {

        @JvmStatic
        protected val NETWORK = MASTERY

        private val RPC_URL = mapOf(
            LOCALHOST to "http://localhost:8545",
//            LOCALHOST to "http://${AION.containerIpAddress}:${AION.getMappedPort(8545)}/",
            MASTERY to "https://aion.api.nodesmith.io/v1/mastery/jsonrpc?apiKey=7b1d449f07774200bc1000a8b0eb1a9e"
        )

        @JvmStatic
        protected val ACCOUNT = mapOf(
            LOCALHOST to "0xa0d5c14a9a2f84a1a8b20fbc329f27e8cb2d2dc0752bb4411b9cd77814355ce6",
            MASTERY to "0xa0c57475b6a30901b2348c1071d7b27a471f43f8bf895d04b73db08e659efe99"
        )

        @JvmStatic
        protected val PRIVATE_KEY = mapOf(
            LOCALHOST to "0x183759fc5cfd01a893ce417c2dde2f3f94026f96276043ddc98ab95a62dc3583a13df70b0ccc362e94c02e7bcd514523add9edcbb20412f00544a462f00d63e4",
            MASTERY to "0x4776895c43f77676cdec51a6c92d2a1bacdf16ddcc6e7e07ab39104b42e1e52608fe2bf5757b8261d4937f13b5815448f2144f9c1409a3fab4c99ca86fff8a36"
        )

        @JvmStatic
        protected val PUBLIC_KEY = mapOf(
            LOCALHOST to "",
            MASTERY to "08fe2bf5757b8261d4937f13b5815448f2144f9c1409a3fab4c99ca86fff8a36"
        )

        @JvmStatic
        protected lateinit var aion: Aion

        @JvmStatic
        protected lateinit var manager: TransactionManager

        @BeforeAll
        @JvmStatic
        fun initClient() {
            aion = Aion.build(HttpService(RPC_URL[NETWORK]))

            val keyPair = Ed25519KeyPair(PUBLIC_KEY.getValue(NETWORK), PRIVATE_KEY.getValue(NETWORK))
            manager = AionTransactionManager(aion, ACCOUNT.getValue(NETWORK), keyPair)

            if (NETWORK != MASTERY) { // Unlock account not supported in Nodesmith
                aion.personalUnlockAccount(ACCOUNT[NETWORK], "410n").send()
            }
        }

//        @Container
//        @JvmStatic
//        private val AION = KGenericContainer("aionnetwork/aion:0.4.0.1")
//            .withClasspathResourceMapping("aion/config", "/aion/custom/config", BindMode.READ_ONLY)
//            .withClasspathResourceMapping("aion/keystore", "/aion/custom/keystore", BindMode.READ_ONLY)
//            .withClasspathResourceMapping("aion/log", "/aion/custom/log", BindMode.READ_WRITE)
//            .withCommand("/aion/aion.sh --network custom")
//            .withExposedPorts(8545)

//        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)
    }
}
