package org.web3j.aion.abi

// FIXME Remove core dependency
import org.aion.avm.core.util.ABIUtil
import org.aion.avm.userlib.abi.ABIException
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Array
import org.web3j.abi.datatypes.IntType
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.primitive.Boolean
import org.web3j.abi.datatypes.primitive.Byte
import org.web3j.abi.datatypes.primitive.Char
import org.web3j.abi.datatypes.primitive.Double
import org.web3j.abi.datatypes.primitive.Float
import org.web3j.abi.datatypes.primitive.Long
import org.web3j.abi.datatypes.primitive.Short
import org.web3j.utils.Numeric
import java.math.BigInteger

object AbiFunctionDecoder : FunctionReturnDecoder() {

    private val typeSizes = mapOf(
        Boolean::class.java to 1,
        Char::class.java to 2,
        Byte::class.java to 1,
        Short::class.java to 2,
        Int::class.java to 4,
        Long::class.java to 8,
        Float::class.java to 4,
        Double::class.java to 8
    )

    override fun decodeFunctionResult(
        rawInput: String,
        outputParameters: List<TypeReference<Type<*>>>
    ): List<Type<*>> {

        val bytes = Numeric.hexStringToByteArray(rawInput)
        var range = 0..sizeOf(Utf8String::class.java, bytes, 0) + 2

        // Decode and ignore function name
        ABIUtil.decodeOneObject(bytes.sliceArray(range))

        val result = mutableListOf<Type<*>>()

        for (param in outputParameters) {
            val size = sizeOf(param.classType, bytes, range.start)
            range = range.endInclusive + 1..range.endInclusive + size + 1

            ABIUtil.decodeOneObject(bytes.sliceArray(range))
                .toAionValue(param.classType).let { result.add(it) }
        }

        return result
    }

    private fun <T : Type<*>> sizeOf(type: Class<T>, data: ByteArray, index: Int): Int {
        return when (type) {
            Boolean::class.java, Byte::class.java -> 1
            Char::class.java, Short::class.java -> 2
            Int::class.java, Float::class.java -> 4
            Long::class.java, Double::class.java -> 8
            Utf8String::class.java, Array::class.java -> data[index + 1] + data[index + 2]
            else -> throw ABIException("Unsupported ABI type")
        }
    }

    override fun <T : Type<*>> decodeEventParameter(
        rawInput: String,
        typeReference: TypeReference<T>
    ): Type<*> {
        TODO("Events not implemented")
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Type<*>> Any.toAionValue(classType: Class<T>): T {
        return when (this) {
            is kotlin.Boolean -> Boolean(this)
            is kotlin.Byte -> Byte(this)
            is kotlin.Char -> Char(this)
            is kotlin.Long -> Long(this)
            is kotlin.Short -> Short(this)
            is kotlin.Float -> Float(this)
            is kotlin.Double -> Double(this)
            is String -> when (classType) {
                Utf8String::class -> Utf8String(this)
                Address::class -> Address(ADDRESS_BIT_LENGTH, this)
                else -> throw ABIException(javaClass.canonicalName)
            }
            is ByteArray -> when (classType) {
                IntType::class -> {
                    // Conversion from Aion byte[] to Solidity numeric value
                    val constructor = classType.getDeclaredConstructor(BigInteger::class.java)
                    constructor.newInstance(BigInteger(this))
                }
                Array::class -> {
                    // Conversion from Aion byte[] to Solidity static or dynamic array
                    val constructor = classType.getDeclaredConstructor(ByteArray::class.java)
                    constructor.newInstance(this)
                }
                else -> throw ABIException(javaClass.canonicalName)
            }
            is kotlin.Array<*> -> {
                // Conversion from Aion array to Solidity static or dynamic array
                val constructor = classType.getDeclaredConstructor(Class::class.java, kotlin.Array<Any>::class.java)
                constructor.newInstance(
                    classType,
                    *this.map { it!!.toAionValue(Utf8String::class.java) }.toTypedArray()
                )
            }
            else -> throw ABIException(javaClass.canonicalName)
        } as T
    }
}
