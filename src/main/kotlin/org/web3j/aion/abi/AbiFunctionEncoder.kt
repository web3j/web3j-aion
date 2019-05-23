package org.web3j.aion.abi

import org.aion.avm.userlib.abi.ABIStreamingEncoder
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Array
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Bytes
import org.web3j.abi.datatypes.BytesType
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
                is BytesType -> encoder.encodeOneByteArray(param.value)
                is Fixed -> encoder.encodeOneDouble(param.value.toDouble())
                is Int -> encoder.encodeOneInteger(param.value.intValueExact())
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
            Address::class.java -> encoder.encodeOneAddressArray(param.value.map { (it as Address).aionValue }.toTypedArray())
            Array::class.java -> encode2DArrayParameter(param, encoder)
            DynamicArray::class.java -> encode2DArrayParameter(param, encoder)
            StaticArray::class.java -> encode2DArrayParameter(param, encoder)
            Bool::class.java -> encoder.encodeOneBooleanArray((param.value as List<Bool>).map { it.value }.toBooleanArray())
            Bytes::class.java -> encoder.encodeOne2DByteArray((param.value as List<Bytes>).map { it.value }.toTypedArray())
            DynamicBytes::class.java -> encoder.encodeOne2DByteArray((param.value as List<Bytes>).map { it.value }.toTypedArray())
            Fixed::class.java -> encoder.encodeOneDoubleArray((param.value as List<Fixed>).map { it.value.toDouble() }.toDoubleArray())
            Int::class.java -> encoder.encodeOneIntegerArray((param.value as List<Int>).map { it.value.intValueExact() }.toIntArray())
            Ufixed::class.java -> encoder.encodeOneDoubleArray((param.value as List<Fixed>).map { it.value.toDouble() }.toDoubleArray())
            Uint::class.java -> encoder.encodeOneIntegerArray((param.value as List<Uint>).map { it.value.intValueExact() }.toIntArray())
            Utf8String::class.java -> encoder.encodeOneStringArray((param.value as List<Utf8String>).map { it.value }.toTypedArray())
        }
    }

    private fun encode2DArrayParameter(
        param: Array<*>,
        encoder: ABIStreamingEncoder
    ) {
        // FIXME What to do when 2nd array dimension is empty? We cannot know its component type
        when ((param.value as List<Array<*>>).firstOrNull()?.componentType ?: Any::class.java) {
            Address::class.java -> throw AionEncodingException("unsupported 2-dimensional array of address")
            Array::class.java -> throw AionEncodingException("unsupported 3-dimensional array")
            DynamicArray::class.java -> throw AionEncodingException("unsupported 3-dimensional array")
            StaticArray::class.java -> throw AionEncodingException("unsupported 3-dimensional array")
            Bool::class.java -> encoder.encodeOne2DBooleanArray((param.value as List<List<Boolean>>).map { it.toBooleanArray() }.toTypedArray())
            Bytes::class.java -> throw AionEncodingException("unsupported 2-dimensional array of bytes")
            DynamicBytes::class.java -> throw AionEncodingException("unsupported 2-dimensional array of bytes")
            Fixed::class.java -> encoder.encodeOne2DDoubleArray((param.value as List<List<Fixed>>).map { fixedList ->
                fixedList.toTypedArray()
            }.map { fixedArray ->
                fixedArray.map { it.value.toDouble() }.toDoubleArray()
            }.toTypedArray())
            Int::class.java -> encoder.encodeOne2DIntegerArray((param.value as List<List<Int>>).map { intList ->
                intList.toTypedArray()
            }.map { intArray ->
                intArray.map { it.value.toInt() }.toIntArray()
            }.toTypedArray())
            Ufixed::class.java -> encoder.encodeOne2DDoubleArray((param.value as List<List<Fixed>>).map { fixedList ->
                fixedList.toTypedArray()
            }.map { fixedArray ->
                fixedArray.map { it.value.toDouble() }.toDoubleArray()
            }.toTypedArray())
            Uint::class.java -> encoder.encodeOne2DIntegerArray((param.value as List<List<Uint>>).map { uintList ->
                uintList.toTypedArray()
            }.map { uintArray ->
                uintArray.map { it.value.toInt() }.toIntArray()
            }.toTypedArray())
            Utf8String::class.java -> throw AionEncodingException("unsupported 2-dimensional array of string")
        }
    }
}

class AionEncodingException(message: String) : RuntimeException(message)