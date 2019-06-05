package org.web3j.aion.abi

internal enum class AbiDefinitionType {
    CONSTRUCTOR, FUNCTION, EVENT;

    override fun toString() = name.toLowerCase()
}
