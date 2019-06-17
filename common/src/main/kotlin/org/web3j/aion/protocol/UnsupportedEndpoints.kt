package org.web3j.aion.protocol

import org.web3j.protocol.core.Request
import org.web3j.protocol.core.Response
import org.web3j.protocol.core.RpcErrors
import java.util.concurrent.CompletableFuture

internal class UnsupportedRequest<S, T : Response<*>>(
    method: String,
    params: List<S>,
    private val responseType: Class<T>
) : Request<S, T>(method, params, null, responseType) {

    override fun sendAsync(): CompletableFuture<T> {
        return CompletableFuture.completedFuture(send())
    }

    override fun send(): T {
        return responseType.getDeclaredConstructor().newInstance().apply {
            error = MethodNotFound
            jsonrpc = "2.0"
            id = 1
        }
    }
}

private object MethodNotFound : Response.Error(RpcErrors.INVALID_REQUEST, "Method not found")
