package org.web3j.aion.abi.avm.spi

import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.spi.FunctionReturnDecoderProvider
import org.web3j.abi.spi.FunctionEncoderProvider
import org.web3j.aion.abi.avm.AbiFunctionDecoder
import org.web3j.aion.abi.avm.AbiFunctionEncoder

class AbiFunctionEncoderProvider : FunctionEncoderProvider {
    override fun get(): FunctionEncoder = AbiFunctionEncoder
}

class AbiFunctionDecoderProvider : FunctionReturnDecoderProvider {
    override fun get(): FunctionReturnDecoder = AbiFunctionDecoder
}
