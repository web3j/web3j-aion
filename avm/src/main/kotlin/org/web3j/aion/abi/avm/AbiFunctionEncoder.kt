package org.web3j.aion.abi.avm

import org.aion.avm.tooling.ABIUtil
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Array
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.StaticArray
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.primitive.Byte
import org.web3j.abi.datatypes.primitive.Char
import org.web3j.abi.datatypes.primitive.Double
import org.web3j.abi.datatypes.primitive.Float
import org.web3j.abi.datatypes.primitive.Int
import org.web3j.abi.datatypes.primitive.Long
import org.web3j.abi.datatypes.primitive.Short
import org.web3j.aion.abi.UnknownTypeException
import org.web3j.utils.Numeric
import java.nio.ByteBuffer

internal object AbiFunctionEncoder : FunctionEncoder() {

    override fun encodeFunction(function: Function): String {
        val params = function.inputParameters.map { it.toAion() }.toTypedArray()
        return Numeric.toHexString(ABIUtil.encodeMethodArguments(function.name, *params))
    }

    override fun encodeParameters(parameters: List<Type<Any>>): String {
        val params = parameters.map { it.toAion() }.toTypedArray()
        val encodedArgs = ABIUtil.encodeDeploymentArguments(*params)

        val bufferLength = encodedArgs.size + kotlin.Int.SIZE_BYTES
        return ByteBuffer.allocate(bufferLength).apply {
            putInt(encodedArgs.size)
            put(encodedArgs)
        }.run {
            Numeric.toHexStringNoPrefix(array())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> Type<T>.toAion(): Any {
        return when (this) {
            is Address -> avm.Address(Numeric.hexStringToByteArray(value.drop(2)))
            is Array<*> -> (this as Array<Type<*>>).toAion()
            else -> value as Any // Covers primitive types
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Array<Type<*>>.toAion(): Any {
        return when (componentType) {
            Address::class.java ->
                (value as List<Type<Address>>).map { it.toAion() as avm.Address }.toTypedArray()
            Utf8String::class.java ->
                (value as List<Type<String>>).map { it.value }.toTypedArray()
            Char::class.java ->
                (value as List<Type<kotlin.Char>>).map { it.value }.toCharArray()
            Bool::class.java ->
                (value as List<Type<Boolean>>).map { it.value }.toBooleanArray()
            Byte::class.java ->
                (value as List<Type<kotlin.Byte>>).map { it.value }.toByteArray()
            Short::class.java ->
                (value as List<Type<kotlin.Short>>).map { it.value }.toShortArray()
            Int::class.java ->
                (value as List<Type<kotlin.Int>>).map { it.value }.toIntArray()
            Long::class.java ->
                (value as List<Type<kotlin.Long>>).map { it.value }.toLongArray()
            Float::class.java ->
                (value as List<Type<kotlin.Float>>).map { it.value }.toFloatArray()
            Double::class.java ->
                (value as List<Type<kotlin.Double>>).map { it.value }.toDoubleArray()
            DynamicArray::class.java, StaticArray::class.java ->
                TODO("2D arrays not implemented")
            else -> throw UnknownTypeException(this)
        }
    }
}
