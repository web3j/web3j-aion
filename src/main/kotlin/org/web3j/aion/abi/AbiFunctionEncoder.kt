package org.web3j.aion.abi

// FIXME Remove core dependency
import org.aion.avm.core.util.ABIUtil
import org.aion.avm.userlib.abi.ABIException
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Array
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Bytes
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.FixedPointType
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.IntType
import org.web3j.abi.datatypes.StaticArray
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Uint
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.primitive.Byte
import org.web3j.abi.datatypes.primitive.Char
import org.web3j.abi.datatypes.primitive.Double
import org.web3j.abi.datatypes.primitive.Float
import org.web3j.abi.datatypes.primitive.Int
import org.web3j.abi.datatypes.primitive.Long
import org.web3j.abi.datatypes.primitive.Short
import org.web3j.utils.Numeric
import java.math.BigInteger

internal object AbiFunctionEncoder : FunctionEncoder() {

    override fun encodeFunction(function: Function): String {
        val params = function.inputParameters.map { it.toAion() }.toTypedArray()
        return Numeric.toHexString(ABIUtil.encodeMethodArguments(function.name, *params))
    }

    override fun encodeParameters(parameters: List<Type<Any>>): String {
        val params = parameters.map { it.toAion() }.toTypedArray()
        return Numeric.toHexString(ABIUtil.encodeDeploymentArguments(*params))
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> Type<T>.toAion(): Any {
        return when (this) {
            is Address -> avm.Address(Numeric.hexStringToByteArray(value.drop(2)))
            is FixedPointType -> throw ABIException("Unsupported fixed point type")
            is IntType -> (this as IntType).toAion()
            is Array<*> -> when (this.componentType) {
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
                IntType::class.java ->
                    (value as List<Type<BigInteger>>).map { (it as IntType).toAion() }.toTypedArray()
                Bytes::class.java ->
                    (value as List<Type<ByteArray>>).map { it.value }.toTypedArray()
                DynamicArray::class.java, StaticArray::class.java ->
                    TODO("2D arrays not implemented")
//                      (value as List<DynamicArray<Type<Char>>>)
//                      .map { it.toAion() as CharArray }.toTypedArray()
                else -> throw ABIException("Unknown type $javaClass")
            }
            else -> value as Any // Covers primitive types
        }
    }

    private fun IntType.toAion(): Any {
        return when (this) {
            is org.web3j.abi.datatypes.Int -> when (bitSize) {
                8 -> value.byteValueExact()
                16 -> value.shortValueExact()
                24, 32 -> value.intValueExact()
                40, 48, 56, 64 -> value.longValueExact()
                in 136..256 -> throw ABIException("Unsupported type: int$bitSize")
                else -> value.toByteArray() // Avoid overflow
            }
            is Uint -> when (bitSize) {
                8 -> value.shortValueExact()
                16, 24 -> value.intValueExact()
                32, 40, 48, 56 -> value.longValueExact()
                in 136..256 -> throw ABIException("Unsupported type: uint$bitSize")
                else -> value.toByteArray() // Avoid overflow
            }
            else -> throw ABIException("Unknown type ${javaClass.canonicalName}")
        }
    }
}
