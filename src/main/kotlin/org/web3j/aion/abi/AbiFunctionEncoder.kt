package org.web3j.aion.abi

// FIXME Remove core dependency
import org.aion.avm.core.util.ABIUtil
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.utils.Numeric

@Suppress("UNCHECKED_CAST")
object AbiFunctionEncoder : FunctionEncoder() {

    override fun encodeFunction(function: Function): String {
        val params = function.inputParameters.map { it.toAionValue() }.toTypedArray()
        return Numeric.toHexString(ABIUtil.encodeMethodArguments(function.name, *params))
    }

    override fun encodeParameters(parameters: List<Type<Any>>): String {
        val params = parameters.map { it.toAionValue() }.toTypedArray()
        return Numeric.toHexString(ABIUtil.encodeDeploymentArguments(*params))
    }
}
