package org.web3j.protocol.aion

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Jackson mix-in for JSON-RPC [org.web3j.protocol.core.Request] as per
 * [JSON RPC 2.0 (Google) Spec Deviations](https://github.com/aionnetwork/aion/wiki/JSON-RPC-API-Docs#3-json-rpc-20-google-spec-deviations).
 *
 * @see ObjectMapper.addMixIn
 */
internal class AionRequestMixIn(
    @field:JsonIgnore
    var jsonrpc: String
)
