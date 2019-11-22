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
package org.web3j.aion.abi.avm.spi

import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.spi.FunctionReturnDecoderProvider
import org.web3j.abi.spi.FunctionEncoderProvider
import org.web3j.aion.abi.avm.AbiFunctionDecoder
import org.web3j.aion.abi.avm.AbiFunctionEncoder

class AvmFunctionEncoderProvider : FunctionEncoderProvider {
    override fun get(): FunctionEncoder = AbiFunctionEncoder
}

class AvmFunctionReturnDecoderProvider : FunctionReturnDecoderProvider {
    override fun get(): FunctionReturnDecoder = AbiFunctionDecoder
}
