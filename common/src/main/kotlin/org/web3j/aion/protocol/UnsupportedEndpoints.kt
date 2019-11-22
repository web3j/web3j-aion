/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
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
