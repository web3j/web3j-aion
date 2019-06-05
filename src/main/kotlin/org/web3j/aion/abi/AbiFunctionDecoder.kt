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
import org.web3j.abi.datatypes.Uint
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Int16
import org.web3j.abi.datatypes.generated.Int24
import org.web3j.abi.datatypes.generated.Int32
import org.web3j.abi.datatypes.generated.Int40
import org.web3j.abi.datatypes.generated.Int48
import org.web3j.abi.datatypes.generated.Int56
import org.web3j.abi.datatypes.generated.Int64
import org.web3j.abi.datatypes.generated.Int8
import org.web3j.abi.datatypes.generated.Uint16
import org.web3j.abi.datatypes.generated.Uint24
import org.web3j.abi.datatypes.generated.Uint32
import org.web3j.abi.datatypes.generated.Uint40
import org.web3j.abi.datatypes.generated.Uint48
import org.web3j.abi.datatypes.generated.Uint56
import org.web3j.abi.datatypes.generated.Uint8
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

    /**
     * Regex to match the bit size of an [org.web3j.abi.datatypes.Int].
     */
    private val INT_REGEX = "^Int(\\d*)\$".toRegex()

    /**
     * Regex to match the bit size of an unsigned [Uint].
     */
    private val UINT_REGEX = "^Uint(\\d*)\$".toRegex()

    /**
     * Regex to match the bit size of an unsigned [Uint].
     */
    private val BYTES_REGEX = "^Bytes(\\d*)\$".toRegex()

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
                BytesType::class.java.isAssignableFrom(type) -> decodeBytesType(type, decoder)
                IntType::class.java.isAssignableFrom(type) -> decodeIntType(type, decoder)
                DynamicArray::class.java.isAssignableFrom(type) -> decodeDynamicArray(typeReference, decoder)
                StaticArray::class.java.isAssignableFrom(type) -> decodeStaticArray(typeReference, decoder)
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

    private fun decodeIntType(type: Class<Type<*>>, decoder: ABIDecoder): IntType {
        return when {
            org.web3j.abi.datatypes.Int::class.java.isAssignableFrom(type) ->
                when (type.bitSize) {
                    8 -> Int8(decoder.decodeOneByte().toLong())
                    16 -> Int16(decoder.decodeOneShort().toLong())
                    24 -> Int24(decoder.decodeOneInteger().toLong())
                    32 -> Int32(decoder.decodeOneInteger().toLong())
                    40 -> Int40(decoder.decodeOneLong())
                    48 -> Int48(decoder.decodeOneLong())
                    56 -> Int56(decoder.decodeOneLong())
                    64 -> Int64(decoder.decodeOneLong())
                    else -> newNumericInstance(type.bitSize, decoder, false)
                }
            Uint::class.java.isAssignableFrom(type) ->
                when (type.bitSize) {
                    8 -> Uint8(decoder.decodeOneShort().toLong())
                    16 -> Uint16(decoder.decodeOneInteger().toLong())
                    24 -> Uint24(decoder.decodeOneInteger().toLong())
                    32 -> Uint32(decoder.decodeOneLong())
                    40 -> Uint40(decoder.decodeOneLong())
                    48 -> Uint48(decoder.decodeOneLong())
                    56 -> Uint56(decoder.decodeOneLong())
                    else -> newNumericInstance(type.bitSize, decoder, true)
                }
            else -> throw ABIException("Unknown type ${javaClass.canonicalName}")
        }
    }

    private val Class<Type<*>>.bitSize: kotlin.Int?
        get() = when {
            INT_REGEX.matches(simpleName) -> bitSize(simpleName, INT_REGEX)
            UINT_REGEX.matches(simpleName) -> bitSize(simpleName, UINT_REGEX)
            BYTES_REGEX.matches(simpleName) -> bitSize(simpleName, BYTES_REGEX)
            else -> throw ABIException("Unknown type ${javaClass.canonicalName}")
        }

    private fun bitSize(input: String, regex: Regex): kotlin.Int? {
        return regex.matchEntire(input)?.groupValues?.get(1)?.toIntOrNull()
    }

    private fun newNumericInstance(bitSize: kotlin.Int?, decoder: ABIDecoder, unsigned: Boolean): IntType {

        val className = "${if (unsigned) "Uint" else "Int"}${bitSize ?: ""}"
        val packageName = "org.web3j.abi.datatypes${if (bitSize != null) ".generated" else ""}"

        val constructor = Class.forName("$packageName.$className")
            .getDeclaredConstructor(BigInteger::class.java)

        return constructor.newInstance(BigInteger(decoder.decodeOneByteArray())) as IntType
    }

    private fun decodeBytesType(type: Class<Type<*>>, decoder: ABIDecoder): BytesType {
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
            is Boolean -> Bool(this)
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