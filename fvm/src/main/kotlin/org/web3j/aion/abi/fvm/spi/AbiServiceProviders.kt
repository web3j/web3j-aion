package org.web3j.aion.abi.fvm.spi

import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.spi.FunctionDecoderProvider
import org.web3j.abi.spi.FunctionEncoderProvider
import org.web3j.aion.abi.fvm.AbiFunctionDecoder
import org.web3j.aion.abi.fvm.AbiFunctionEncoder

class AbiFunctionEncoderProvider : FunctionEncoderProvider {
    override fun get(): FunctionEncoder = AbiFunctionEncoder
}

class AbiFunctionDecoderProvider : FunctionDecoderProvider {
    override fun get(): FunctionReturnDecoder = AbiFunctionDecoder
}
