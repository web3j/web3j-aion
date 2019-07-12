package org.web3j.aion.abi

import org.web3j.abi.datatypes.Type

sealed class AbiException(message: String, cause: Throwable? = null) : Exception(message, cause)

class UnsupportedTypeException(value: String) : AbiException("Unsupported type $value") {
    constructor(value: Type<*>) : this(value.typeAsString)
    constructor(type: Class<*>) : this(type.canonicalName)
}

class UnknownTypeException(value: String) : AbiException("Unknown type $value") {
    constructor(value: Type<*>) : this(value.typeAsString)
    constructor(type: Class<*>) : this(type.canonicalName)
}
