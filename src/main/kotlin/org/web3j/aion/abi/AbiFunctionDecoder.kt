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
