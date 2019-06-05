package org.web3j.aion.abi

// FIXME Remove core dependency
import org.aion.avm.userlib.abi.ABIDecoder
import org.aion.avm.userlib.abi.ABIException
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.BytesType
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.IntType
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
import org.web3j.utils.Numeric
import java.lang.reflect.ParameterizedType
import java.math.BigInteger

internal object AbiFunctionDecoder : FunctionReturnDecoder() {

    override fun decodeFunctionResult(
        rawInput: String,
        outputParameters: List<TypeReference<Type<*>>>
    ): List<Type<*>> {
        return ABIDecoder(Numeric.hexStringToByteArray(rawInput)).run {
            decodeMethodName() // FIXME Remove as method name won't be returned
            outputParameters.map { decodeResult(it, this) }
        }
    }

    private fun decodeResult(typeReference: TypeReference<Type<*>>, decoder: ABIDecoder): Type<*> {
        return decoder.run {
            when (typeReference.classType) {
                Address::class.java -> decodeOneAddress().toWeb3j()
                Char::class.java -> decodeOneCharacter().toWeb3j()
                Byte::class.java -> decodeOneByte().toWeb3j()
                Short::class.java -> decodeOneShort().toWeb3j()
                Int::class.java -> decodeOneInteger().toWeb3j()
                Float::class.java -> decodeOneFloat().toWeb3j()
                Long::class.java -> decodeOneLong().toWeb3j()
                Double::class.java -> decodeOneDouble().toWeb3j()
                Utf8String::class.java -> decodeOneString().toWeb3j()
                Bool::class.java -> decodeOneBoolean().toWeb3j()
                BytesType::class.java -> decodeBytesType(typeReference, decoder)
                IntType::class.java -> decodeIntType(typeReference, decoder)
                DynamicArray::class.java -> decodeDynamicArray(typeReference, decoder)
                StaticArray::class.java -> decodeStaticArray(typeReference, decoder)
                else -> throw ABIException("Unsupported ABI type")
            }
        }
    }

    override fun <T : Type<*>> decodeEventParameter(
        rawInput: String,
        typeReference: TypeReference<T>
    ): Type<*> {
        TODO("Solidity events not implemented")
    }

    private fun decodeIntType(typeReference: TypeReference<Type<*>>, decoder: ABIDecoder): IntType {
        TODO("not implemented")
    }

    private fun decodeBytesType(typeReference: TypeReference<Type<*>>, decoder: ABIDecoder): BytesType {
        TODO("not implemented")
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
                BytesType::class.java -> TODO()
                IntType::class.java -> TODO()
                DynamicArray::class.java -> TODO("2D arrays not implemented")
                StaticArray::class.java -> TODO("2D arrays not implemented")
                else -> throw ABIException("Unsupported ABI type")
            } as DynamicArray<Type<*>>
        }
    }

    private fun decodeStaticArray(typeReference: TypeReference<Type<*>>, decoder: ABIDecoder): StaticArray<Type<*>> {
        TODO("not implemented")
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> T.toWeb3j(): Type<*> {
        return when (this) {
            is avm.Address -> Address(BigInteger(unwrap()))
            is kotlin.Boolean -> Bool(this)
            is kotlin.Char -> Char(this)
            is kotlin.Byte -> Byte(this)
            is kotlin.Short -> Short(this)
            is kotlin.Int -> Int(this)
            is kotlin.Float -> Float(this)
            is kotlin.Long -> Long(this)
            is kotlin.Double -> Double(this)
            is String -> Utf8String(this)
            else -> throw ABIException("Unsupported ABI type")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Type<*>> getParameterizedType(
        typeReference: TypeReference<*>
    ): Class<T> {
        val typeArguments = (typeReference.type as ParameterizedType).actualTypeArguments
        val parameterizedTypeName = typeArguments[0].typeName
        return Class.forName(parameterizedTypeName) as Class<T>
    }
}