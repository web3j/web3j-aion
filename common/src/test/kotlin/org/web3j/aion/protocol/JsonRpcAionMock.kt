package org.web3j.aion.protocol

import org.web3j.protocol.core.Request
import org.web3j.protocol.core.Response
import java.util.concurrent.CompletableFuture

internal inline fun <reified T : Response<V>, V> mock(value: V): Request<String, T> {
    return object : Request<String, T>("", listOf(), null, T::class.java) {

        override fun sendAsync(): CompletableFuture<T> {
            return CompletableFuture.completedFuture(send())
        }

        override fun send(): T {
            return T::class.java.getDeclaredConstructor().newInstance().apply {
                jsonrpc = "2.0"
                result = value
                id = 1
            }
        }
    }
}
