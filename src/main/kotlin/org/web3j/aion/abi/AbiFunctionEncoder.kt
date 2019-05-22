package org.web3j.aion.abi

import org.aion.avm.userlib.abi.ABIStreamingEncoder
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Array
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Bytes
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.DynamicBytes
import org.web3j.abi.datatypes.Fixed
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Int
import org.web3j.abi.datatypes.StaticArray
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Ufixed
import org.web3j.abi.datatypes.Uint
import org.web3j.abi.datatypes.Utf8String
import org.web3j.utils.Numeric

@Suppress("UNCHECKED_CAST")
object AbiFunctionEncoder : FunctionEncoder() {

    override fun encodeFunction(function: Function): String {
        val encoder = ABIStreamingEncoder()
        encoder.encodeOneString(function.name)
        encodeParameters(function.inputParameters, encoder)
        return Numeric.toHexString(encoder.toBytes())
    }

    override fun encodeParameters(parameters: List<Type<*>>): String {
        val encoder = ABIStreamingEncoder()
        encodeParameters(parameters, encoder)
        return Numeric.toHexString(encoder.toBytes())
    }

    private fun encodeParameters(
        parameters: List<Type<*>>,
        encoder: ABIStreamingEncoder
    ) {
        for (param in parameters) {
            when (param) {
                is Address -> encoder.encodeOneAddress(param.aionValue)
                is Array<*> -> encodeArrayParameter(param, encoder)
                is Bool -> encoder.encodeOneBoolean(param.value)
                is Bytes -> encoder.encodeOneByteArray(param.value)
                is DynamicArray<*> -> encodeArrayParameter(param, encoder)
                is DynamicBytes -> encoder.encodeOneByteArray(param.value)
                is Fixed -> encoder.encodeOneDouble(param.value.toDouble())
                is Int -> encoder.encodeOneInteger(param.value.intValueExact())
                is StaticArray<*> -> encodeArrayParameter(param, encoder)
                is Ufixed -> encoder.encodeOneDouble(param.value.toDouble())
                is Uint -> encoder.encodeOneInteger(param.value.intValueExact())
                is Utf8String -> encoder.encodeOneString(param.value)
            }
        }
    }

    private fun encodeArrayParameter(
        param: Array<*>,
        encoder: ABIStreamingEncoder
    ) {
        when (param.componentType) {
            is Address -> encoder.encodeOneAddressArray(param.value.map { (it as Address).aionValue }.toTypedArray())
            is Array<*> -> encode2DArrayParameter(param, encoder)
            is Bool -> encoder.encodeOneBooleanArray((param.value as List<Bool>).map { it.value }.toBooleanArray())
            is Bytes -> encoder.encodeOne2DByteArray((param.value as List<Bytes>).map { it.value }.toTypedArray())
            is DynamicArray<*> -> encode2DArrayParameter(param, encoder)
            is DynamicBytes -> encoder.encodeOne2DByteArray((param.value as List<Bytes>).map { it.value }.toTypedArray())
            is Fixed -> encoder.encodeOneDoubleArray((param.value as List<Fixed>).map { it.value.toDouble() }.toDoubleArray())
            is Int -> encoder.encodeOneIntegerArray((param.value as List<Int>).map { it.value.intValueExact() }.toIntArray())
            is StaticArray<*> -> encode2DArrayParameter(param, encoder)
            is Ufixed -> encoder.encodeOneDoubleArray((param.value as List<Fixed>).map { it.value.toDouble() }.toDoubleArray())
            is Uint -> encoder.encodeOneIntegerArray((param.value as List<Int>).map { it.value.intValueExact() }.toIntArray())
            is Utf8String -> encoder.encodeOneStringArray((param.value as List<Utf8String>).map { it.value }.toTypedArray())
        }
    }

    private fun encode2DArrayParameter(
        param: Array<*>,
        encoder: ABIStreamingEncoder
    ) {
        when (param.componentType) {
            is Address -> throw AionEncodingException("unsupported 2-dimensional array of address")
            is Array<*> -> throw AionEncodingException("unsupported 3-dimensional array")
            is Bool -> encoder.encodeOne2DBooleanArray((param.value as List<List<Boolean>>).map { it.toBooleanArray() }.toTypedArray())
            is Bytes -> throw AionEncodingException("unsupported 2-dimensional array of bytes")
            is DynamicArray<*> -> throw AionEncodingException("unsupported 3-dimensional array")
            is DynamicBytes -> throw AionEncodingException("unsupported 2-dimensional array of bytes")
            is Fixed -> encoder.encodeOne2DDoubleArray((param.value as List<List<Fixed>>).map { fixedList ->
                fixedList.toTypedArray()
            }.map { fixedArray ->
                fixedArray.map { it.value.toDouble() }.toDoubleArray()
            }.toTypedArray())
            is Int -> encoder.encodeOne2DIntegerArray((param.value as List<List<Int>>).map { intList ->
                intList.toTypedArray()
            }.map { intArray ->
                intArray.map { it.value.toInt() }.toIntArray()
            }.toTypedArray())
            is StaticArray<*> -> throw AionEncodingException("unsupported 3-dimensional array")
            is Ufixed -> encoder.encodeOne2DDoubleArray((param.value as List<List<Fixed>>).map { fixedList ->
                fixedList.toTypedArray()
            }.map { fixedArray ->
                fixedArray.map { it.value.toDouble() }.toDoubleArray()
            }.toTypedArray())
            is Uint -> encoder.encodeOne2DIntegerArray((param.value as List<List<Uint>>).map { uintList ->
                uintList.toTypedArray()
            }.map { uintArray ->
                uintArray.map { it.value.toInt() }.toIntArray()
            }.toTypedArray())
            is Utf8String -> throw AionEncodingException("unsupported 2-dimensional array of string")
        }
    }
}

class AionEncodingException(message: String) : RuntimeException(message)