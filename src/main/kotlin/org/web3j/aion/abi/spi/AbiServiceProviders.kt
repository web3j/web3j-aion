package org.web3j.aion.abi.spi

import org.web3j.abi.FunctionEncoder
import org.web3j.abi.spi.FunctionEncoderProvider
import org.web3j.aion.abi.AbiFunctionEncoder

class AbiFunctionEncoderProvider : FunctionEncoderProvider {
    override fun get(): FunctionEncoder = AbiFunctionEncoder
}

// class AionFunctionDecoderProvider : FunctionDecoderProvider {
// //    override fun get(): FunctionReturnDecoder = AionFunctionCodec
// //}
