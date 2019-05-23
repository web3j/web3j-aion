package org.web3j.aion.protocol

import assertk.Assert
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import org.junit.Test
import org.web3j.protocol.RequestTester
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.DefaultBlockParameterName.PENDING
import org.web3j.protocol.core.Response
import org.web3j.protocol.core.RpcErrors
import org.web3j.protocol.core.methods.request.ShhFilter
import org.web3j.protocol.core.methods.request.ShhPost
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.http.HttpService
import java.math.BigInteger.ONE
import java.math.BigInteger.TEN
import java.math.BigInteger.ZERO

@Suppress("DEPRECATION")
class JsonRpcAionTest : RequestTester() {

    private lateinit var aion: Aion

    override fun initWeb3Client(service: HttpService) {
        aion = Aion.build(service)
    }

    @Test
    fun testEthGetUncleCountByBlockHash() {
        assertThat(aion.ethGetUncleCountByBlockHash(HASH).send()).isError()
    }

    @Test
    fun testEthGetUncleCountByBlockNumber() {
        assertThat(aion.ethGetUncleCountByBlockNumber(LATEST).send()).isError()
    }

    @Test
    fun testEthGetUncleByBlockHashAndIndex() {
        assertThat(aion.ethGetUncleByBlockHashAndIndex(HASH, ONE).send()).isError()
    }

    @Test
    fun testEthGetUncleByBlockNumberAndIndex() {
        assertThat(aion.ethGetUncleByBlockNumberAndIndex(LATEST, ONE).send()).isError()
    }

    @Test
    fun testEthCompileLLL() {
        assertThat(aion.ethCompileLLL("").send()).isError()
    }

    @Test
    fun testEthCompileSerpent() {
        assertThat(aion.ethCompileSerpent("").send()).isError()
    }

    @Test
    fun testEthGetWork() {
        assertThat(aion.ethGetWork().send()).isError()
    }

    @Test
    fun testEthSubmitWork() {
        assertThat(aion.ethSubmitWork("", "", "").send()).isError()
    }

    @Test
    fun testShhPost() {
        assertThat(aion.shhPost(ShhPost(mutableListOf<String>(), "", ONE, ONE)).send()).isError()
    }

    @Test
    fun testShhVersion() {
        assertThat(aion.shhVersion().send()).isError()
    }

    @Test
    fun testShhNewIdentity() {
        assertThat(aion.shhNewIdentity().send()).isError()
    }

    @Test
    fun testShhHasIdentity() {
        assertThat(aion.shhHasIdentity(ADDRESS).send()).isError()
    }

    @Test
    fun testShhNewGroup() {
        assertThat(aion.shhNewGroup().send()).isError()
    }

    @Test
    fun testShhAddToGroup() {
        assertThat(aion.shhAddToGroup("").send()).isError()
    }

    @Test
    fun testShhNewFilter() {
        assertThat(aion.shhNewFilter(ShhFilter("")).send()).isError()
    }

    @Test
    fun testShhUninstallFilter() {
        assertThat(aion.shhUninstallFilter(ONE).send()).isError()
    }

    @Test
    fun testShhGetFilterChanges() {
        assertThat(aion.shhGetFilterChanges(ONE).send()).isError()
    }

    @Test
    fun testShhGetMessages() {
        assertThat(aion.shhGetMessages(ONE).send()).isError()
    }

    @Test
    fun testDbPutString() {
        assertThat(aion.dbPutString("", "", "").send()).isError()
    }

    @Test
    fun testDbGetString() {
        assertThat(aion.dbGetString("", "").send()).isError()
    }

    @Test
    fun testDbPutHex() {
        assertThat(aion.dbPutHex("", "", "").send()).isError()
    }

    @Test
    fun testDbGetHex() {
        assertThat(aion.dbGetHex("", "").send()).isError()
    }

    @Test(expected = AionProtocolException::class)
    fun testGetBalance() {
        aion.ethGetBalance(ADDRESS, PENDING).send()
    }

    @Test(expected = AionProtocolException::class)
    fun testGetCode() {
        aion.ethGetCode(ADDRESS, PENDING).send()
    }

    @Test(expected = AionProtocolException::class)
    fun testGetTransactionCount() {
        aion.ethGetTransactionCount(ADDRESS, PENDING).send()
    }

    @Test(expected = AionProtocolException::class)
    fun testGetStorageAt() {
        aion.ethGetStorageAt(ADDRESS, ONE, PENDING).send()
    }

    @Test(expected = AionProtocolException::class)
    fun testCall() {
        val transaction = Transaction(
            ADDRESS, ZERO, ONE, ONE,
            ADDRESS, TEN, ""
        )
        aion.ethCall(transaction, PENDING).send()
    }

    private fun <T : Response<*>> Assert<T>.isError() = given { response ->
        assertAll {
            assertThat(response.error.code).isEqualTo(RpcErrors.INVALID_REQUEST)
            assertThat(response.error.message).isEqualTo("Method not found")
            assertThat(response.result).isNull()
        }
    }

    companion object {
        const val ADDRESS = "0xa0b2e70f995df21e7988d645f1fdbaa94324f82b1d162b9b45cdbef7b5b51bd2"
        const val HASH = "0x34e9d4af6d2bace6a54b6039843e55bdc1d4ff425cd8e8c8f3fa96ca89f58a53"
    }
}
