package org.web3j.protocol.aion

import net.i2p.crypto.eddsa.EdDSASecurityProvider
import org.assertj.core.api.Assertions.assertThat
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.protocol.core.DefaultBlockParameterName.PENDING
import org.web3j.protocol.http.HttpService
import org.web3j.testcontract.TestContract
import org.web3j.tx.gas.DefaultGasProvider
import java.io.File
import java.io.IOException
import java.io.StringReader
import java.security.Key
import java.security.KeyPair
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Security

@Testcontainers
class AionIT {

    init {
        Security.addProvider(BouncyCastleProvider())
        Security.addProvider(EdDSASecurityProvider())
    }

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
        assertThat(balance.balance).isZero()
    }

    @Test
    internal fun testContractDeploy() {
        val file = File(javaClass.classLoader.getResource(ACCOUNT).file)

//        aion.personalUnlockAccount(ACCOUNT, "410n")
        val keys = getKeys(file)

//        val data = FileUtils.readFileToString(file, Charsets.UTF_8)
//        val ecKeyPair = ECKeyPair.create(getKeyPair(data))
//        val credentials = Credentials.create(ecKeyPair)
//        val credentials = WalletUtils.loadCredentials("410n", file)
        val keyPair = KeyPair(keys[0] as PublicKey, keys[1] as PrivateKey)
        val credentials = Credentials.create(ECKeyPair.create(keyPair))

        TestContract.deploy(aion, credentials, DefaultGasProvider(), "").send()
    }

    companion object {

        private const val ADDRESS = "0xa0d5c14a9a2f84a1a8b20fbc329f27e8cb2d2dc0752bb4411b9cd77814355ce6"
        private const val ACCOUNT = "a0d5c14a9a2f84a1a8b20fbc329f27e8cb2d2dc0752bb4411b9cd77814355ce6"

        private lateinit var aion: Aion

        @BeforeAll
        @JvmStatic
        fun initClient() {
            val url = "http://${AION.containerIpAddress}:${AION.getMappedPort(8545)}/"
            aion = Aion.build(HttpService(url))
        }

        private fun getKeys(file: File): List<Key> {
            val keyStore = KeyStore.getInstance("PKCS12")
            val password = "410n".toCharArray()

            keyStore.load(file.inputStream(), password)

            return keyStore.aliases().toList().map {
                keyStore.getKey(it, password)
            }
        }

        private fun getKeyPair(data: String): KeyPair {
            PEMParser(StringReader(data)).use {

                val pemKeyPair = when (val pemPair = it.readObject()) {
                    is PEMKeyPair -> pemPair
                    else -> throw IOException("Unexpected PEM object from $pemPair")
                }

                val converter = JcaPEMKeyConverter().setProvider(EdDSASecurityProvider.PROVIDER_NAME)
                val privateKey = converter.getPrivateKey(pemKeyPair.privateKeyInfo)
                val publicKey = converter.getPublicKey(pemKeyPair.publicKeyInfo)

                return KeyPair(publicKey, privateKey)
            }
        }

        @Container
        @JvmStatic
        private val AION = KGenericContainer("aionnetwork/aion:0.3.3")
            .withCommand("/aion/aion.sh --network custom")
            .withExposedPorts(8545)
    }

    class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)
}
