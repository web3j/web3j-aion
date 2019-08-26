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
