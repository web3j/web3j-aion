package org.web3j.aion.abi.avm

import org.aion.avm.userlib.abi.ABIDecoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.primitive.Byte
import org.web3j.abi.datatypes.primitive.Char
import org.web3j.abi.datatypes.primitive.Double
import org.web3j.abi.datatypes.primitive.Float
import org.web3j.abi.datatypes.primitive.Int
import org.web3j.abi.datatypes.primitive.Long
import org.web3j.abi.datatypes.primitive.Short
import org.web3j.aion.AionConstants.ADDRESS_BIT_LENGTH
import org.web3j.aion.abi.UnsupportedTypeException
import org.web3j.utils.Numeric
import java.lang.reflect.ParameterizedType

internal object AbiFunctionDecoder : FunctionReturnDecoder() {

    override fun decodeFunctionResult(
        rawInput: String,
        outputParameters: List<TypeReference<Type<*>>>
    ): List<Type<*>> {
        return ABIDecoder(Numeric.hexStringToByteArray(rawInput)).run {
            outputParameters.map { decodeResult(it, this) }
        }
    }

    private fun decodeResult(typeReference: TypeReference<Type<*>>, decoder: ABIDecoder): Type<*> {
        val type: Class<Type<*>> = typeReference.classType
        return decoder.run {
            when {
                Address::class.java.isAssignableFrom(type) -> decodeOneAddress().toWeb3j()
                Char::class.java.isAssignableFrom(type) -> decodeOneCharacter().toWeb3j()
                Byte::class.java.isAssignableFrom(type) -> decodeOneByte().toWeb3j()
                Short::class.java.isAssignableFrom(type) -> decodeOneShort().toWeb3j()
                Int::class.java.isAssignableFrom(type) -> decodeOneInteger().toWeb3j()
                Float::class.java.isAssignableFrom(type) -> decodeOneFloat().toWeb3j()
                Long::class.java.isAssignableFrom(type) -> decodeOneLong().toWeb3j()
                Double::class.java.isAssignableFrom(type) -> decodeOneDouble().toWeb3j()
                Utf8String::class.java.isAssignableFrom(type) -> decodeOneString().toWeb3j()
                Bool::class.java.isAssignableFrom(type) -> decodeOneBoolean().toWeb3j()
                DynamicArray::class.java.isAssignableFrom(type) -> decodeDynamicArray(typeReference, decoder)
                else -> throw UnsupportedTypeException(type)
            }
        }
    }

    override fun <T : Type<*>> decodeEventParameter(
        rawInput: String,
        typeReference: TypeReference<T>
    ): Type<*> {
        TODO("AVM events not implemented")
    }

    @Suppress("UNCHECKED_CAST")
    private fun decodeDynamicArray(typeReference: TypeReference<*>, decoder: ABIDecoder): DynamicArray<Type<*>> {
        val type: Class<Type<*>> = getParameterizedType(typeReference)

        return decoder.run {
            when (type) {
                Address::class.java -> DynamicArray(
                    Address::class.java,
                    decodeOneAddressArray().map { it.toWeb3j() } as List<Address>)
                Utf8String::class.java -> DynamicArray(
                    Utf8String::class.java,
                    decodeOneStringArray().map { it.toWeb3j() } as List<Utf8String>)
                Bool::class.java -> DynamicArray(
                    Bool::class.java,
                    decodeOneBooleanArray().map { it.toWeb3j() } as List<Bool>)
                Char::class.java -> DynamicArray(
                    Char::class.java,
                    decodeOneCharacterArray().map { it.toWeb3j() } as List<Char>)
                Byte::class.java -> DynamicArray(
                    Byte::class.java,
                    decodeOneByteArray().map { it.toWeb3j() } as List<Byte>)
                Short::class.java -> DynamicArray(
                    Short::class.java,
                    decodeOneShortArray().map { it.toWeb3j() } as List<Short>)
                Int::class.java -> DynamicArray(
                    Int::class.java,
                    decodeOneIntegerArray().map { it.toWeb3j() } as List<Int>)
                Float::class.java -> DynamicArray(
                    Float::class.java,
                    decodeOneFloatArray().map { it.toWeb3j() } as List<Float>)
                Long::class.java -> DynamicArray(
                    Long::class.java,
                    decodeOneLongArray().map { it.toWeb3j() } as List<Long>)
                Double::class.java -> DynamicArray(
                    Double::class.java,
                    decodeOneDoubleArray().map { it.toWeb3j() } as List<Double>)
                DynamicArray::class.java -> TODO("2D arrays not implemented")
                else -> throw UnsupportedTypeException(type)
            } as DynamicArray<Type<*>>
        }
    }

//    private fun decodeStaticArray(typeReference: TypeReference<Type<*>>, decoder: ABIDecoder): StaticArray<Type<*>> {
//        TODO("not implemented")
//    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> T.toWeb3j(): Type<*> {
        return when (this) {
            is avm.Address -> Address(ADDRESS_BIT_LENGTH, Numeric.toHexString(toByteArray()))
            is Boolean -> Bool(this)
            is kotlin.Char -> Char(this)
            is kotlin.Byte -> Byte(this)
            is kotlin.Short -> Short(this)
            is kotlin.Int -> Int(this)
            is kotlin.Float -> Float(this)
            is kotlin.Long -> Long(this)
            is kotlin.Double -> Double(this)
            is String -> Utf8String(this)
            else -> throw UnsupportedTypeException(javaClass)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Type<*>> getParameterizedType(typeReference: TypeReference<*>): Class<T> {
        val typeArguments = (typeReference.type as ParameterizedType).actualTypeArguments
        val parameterizedTypeName = typeArguments[0].typeName
        return Class.forName(parameterizedTypeName) as Class<T>
    }
}
