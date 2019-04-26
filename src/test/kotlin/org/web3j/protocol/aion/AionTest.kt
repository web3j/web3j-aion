package org.web3j.protocol.aion

import org.junit.Test
import org.web3j.protocol.RequestTester
import org.web3j.protocol.core.DefaultBlockParameterName.PENDING
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.http.HttpService
import java.math.BigInteger.*

class AionTest : RequestTester() {

    private lateinit var web3j: Aion

    override fun initWeb3Client(service: HttpService) {
        web3j = Aion.build(service)
    }

    @Test
    fun testGetBalance() {
        val address = "0xa0b2e70f995df21e7988d645f1fdbaa94324f82b1d162b9b45cdbef7b5b51bd2"
        web3j.ethGetBalance(address, PENDING).send()

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBalance\",\"params\":[\"0xa0b2e70f995df21e7988d645f1fdbaa94324f82b1d162b9b45cdbef7b5b51bd2\"],\"id\":0}")
    }

    @Test
    fun testGetCode() {
        val address = "0xa0b2e70f995df21e7988d645f1fdbaa94324f82b1d162b9b45cdbef7b5b51bd2"
        web3j.ethGetCode(address, PENDING).send()

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getCode\",\"params\":[\"0xa0b2e70f995df21e7988d645f1fdbaa94324f82b1d162b9b45cdbef7b5b51bd2\"],\"id\":0}")
    }

    @Test
    fun testGetTransactionCount() {
        val address = "0xa0b2e70f995df21e7988d645f1fdbaa94324f82b1d162b9b45cdbef7b5b51bd2"
        web3j.ethGetTransactionCount(address, PENDING).send()

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getTransactionCount\",\"params\":[\"0xa0b2e70f995df21e7988d645f1fdbaa94324f82b1d162b9b45cdbef7b5b51bd2\"],\"id\":0}")
    }

    @Test
    fun testGetStorageAt() {
        val address = "0xa0b2e70f995df21e7988d645f1fdbaa94324f82b1d162b9b45cdbef7b5b51bd2"
        web3j.ethGetStorageAt(address, ONE, PENDING).send()

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getStorageAt\",\"params\":[\"0xa0b2e70f995df21e7988d645f1fdbaa94324f82b1d162b9b45cdbef7b5b51bd2\",\"0x1\"],\"id\":0}")
    }

    @Test
    fun testCall() {
        val from = "0xa0b2e70f995df21e7988d645f1fdbaa94324f82b1d162b9b45cdbef7b5b51bd2"
        val to = "0xa0c6ed9486e9137802d0acdcd9a0499241872f648b51a5ab49a534a0d440f62c"

        val data = """f0a147ad
            a069071db28d1f8676766a82272e433e
            46bd80773e69bbbe92dccf2306e042b1
            000000000000000104295b07fb4fa600
            00000000000000000000000000000040
            00000000000000000000000000000000
        """.trimIndent()

        val transaction = Transaction(from, ZERO, ONE, ONE, to, TEN, data)
        web3j.ethCall(transaction, PENDING).send()

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_call\",\"params\":[{\"from\":\"0xa0b2e70f995df21e7988d645f1fdbaa94324f82b1d162b9b45cdbef7b5b51bd2\",\"to\":\"0xa0c6ed9486e9137802d0acdcd9a0499241872f648b51a5ab49a534a0d440f62c\",\"gas\":\"0x1\",\"gasPrice\":\"0x1\",\"value\":\"0xa\",\"data\":\"0xf0a147ad\n            a069071db28d1f8676766a82272e433e\n            46bd80773e69bbbe92dccf2306e042b1\n            000000000000000104295b07fb4fa600\n            00000000000000000000000000000040\n            00000000000000000000000000000000\",\"nonce\":\"0x0\"}],\"id\":<generatedValue>}")
    }

}
