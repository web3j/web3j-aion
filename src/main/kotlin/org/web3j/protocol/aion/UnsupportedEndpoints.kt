package org.web3j.protocol.aion

import org.web3j.protocol.core.Request
import org.web3j.protocol.core.Response
import java.util.concurrent.CompletableFuture

internal class UnsupportedRequest<S, T : Response<*>>(
    method: String, params: List<S>,
    private val responseType: Class<T>
) : Request<S, T>(method, params, null, responseType) {

    override fun sendAsync(): CompletableFuture<T> {
        return CompletableFuture.completedFuture(send())
    }

    override fun send(): T {
        return responseType.newInstance().apply {
            error = MethodNotFound
            jsonrpc = "2.0"
            id = 1
        }
    }
}

private object MethodNotFound : Response.Error(-32601, "Method not found")