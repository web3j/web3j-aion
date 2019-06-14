package org.web3j.aion

enum class VirtualMachine(val data: Byte) {
    FVM(0x1),
    AVM(0x2)
}
