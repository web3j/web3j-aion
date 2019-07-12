package org.web3j.aion.abi.fvm

import org.web3j.abi.DefaultFunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Array
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.FixedPointType
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Int
import org.web3j.abi.datatypes.IntType
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Uint
import org.web3j.aion.abi.UnknownTypeException
import org.web3j.aion.abi.UnsupportedTypeException

internal object AbiFunctionEncoder : DefaultFunctionEncoder() {

    override fun encodeFunction(function: Function): String {
        val params = function.inputParameters.map { it.toAion() }
        return super.encodeFunction(
            Function(
                function.name, params,
                function.outputParameters as List<TypeReference<*>>?
            )
        )
    }

    override fun encodeParameters(parameters: List<Type<Any>>): String {
        val params = parameters.map { it.toAion() }
        return super.encodeParameters(params)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> Type<T>.toAion(): Type<*> {
        return when (this) {
            is FixedPointType -> throw UnsupportedTypeException(this)
            is IntType -> (this as IntType).toAion()
            is Array<*> -> (this as Array<Type<*>>).toAion()
            else -> this
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Array<Type<*>>.toAion(): Array<Type<*>> {
        return when (componentType) {
            IntType::class.java -> {
                val values = (value as List<IntType>).map { it.toAion() }
                DynamicArray(componentType as Class<IntType>, values) as Array<Type<*>>
            }
//                DynamicArray::class.java, StaticArray::class.java ->
//                TODO("2D arrays not implemented")
            else -> this
        }
    }

    private fun IntType.toAion(): IntType {
        return when (this) {
            is Int -> when (bitSize) {
                in 8..128 -> this
                in 136..256 -> throw UnsupportedTypeException(this)
                else -> throw UnknownTypeException(this)
            }
            is Uint -> when (bitSize) {
                in 8..128 -> this
                in 136..256 -> throw UnsupportedTypeException(this)
                else -> throw UnknownTypeException(this)
            }
            else -> throw UnknownTypeException(this)
        }
    }
}
