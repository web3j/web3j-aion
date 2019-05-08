package org.web3j.protocol.aion

import org.web3j.abi.datatypes.Address
import org.web3j.protocol.core.methods.response.Transaction
import org.web3j.protocol.core.methods.response.TransactionReceipt

fun Transaction.getNrg() = gas
fun Transaction.getNrgPrice() = gasPrice

fun TransactionReceipt.getNrgUsed() = gasUsed

fun Address.toAion(): avm.Address {
    return avm.Address(toString().toByteArray())
}

fun avm.Address.toEthereum(): Address {
    return Address(toString())
}
