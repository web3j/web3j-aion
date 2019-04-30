package org.web3j.protocol.aion

import org.web3j.abi.datatypes.Address

fun Address.toAion(): avm.Address {
    return avm.Address(toString().toByteArray())
}

fun avm.Address.toEthereum(): Address {
    return Address(toString())
}

