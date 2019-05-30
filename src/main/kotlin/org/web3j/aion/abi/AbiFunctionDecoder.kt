package org.web3j.aion.abi

// FIXME Remove core dependency
import org.aion.avm.core.util.ABIUtil
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Type
import org.web3j.utils.Numeric

object AionFunctionReturnDecoder : FunctionReturnDecoder() {

    override fun decodeFunctionResult(
        rawInput: String,
        outputParameters: List<TypeReference<Type<*>>>
    ): List<Type<*>> {
        val classType = outputParameters.first().classType
        val bytes = Numeric.hexStringToByteArray(rawInput)
        return listOf(ABIUtil.decodeOneObject(bytes).toAionValue(classType))
    }

    override fun <T : Type<*>> decodeEventParameter(
        rawInput: String,
        typeReference: TypeReference<T>
    ): Type<*> {
        TODO("Events not implemented")
    }
}

class AbiDecodingException(message: String) : RuntimeException(message)
