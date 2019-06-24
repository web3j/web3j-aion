package org.web3j.aion.abi

import org.web3j.abi.datatypes.Type

sealed class AionAbiException(message: String, cause: Throwable? = null) : Exception(message, cause)

class AionEncoderException(message: String, cause: Throwable? = null) : AionAbiException(message, cause)
class AionDecoderException(message: String, cause: Throwable? = null) : AionAbiException(message, cause)

class UnsupportedTypeException(value: Type<*>) : AionAbiException("Unsupported type ${value.typeAsString}")
class UnknownTypeException(value: Type<*>) : AionAbiException("Unknown type ${value.typeAsString}")
