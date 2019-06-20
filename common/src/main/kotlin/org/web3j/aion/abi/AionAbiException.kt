package org.web3j.aion.abi

sealed class AionAbiException(message: String, cause: Throwable? = null) : Exception(message, cause)

class AionEncoderException(message: String, cause: Throwable? = null) : AionAbiException(message, cause)

class AionDecoderException(message: String, cause: Throwable? = null) : AionAbiException(message, cause)
