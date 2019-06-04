package org.web3j.aion.abi

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.aion.avm.userlib.abi.ABIException
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.primitive.Boolean
import org.web3j.abi.datatypes.primitive.Byte
import org.web3j.abi.datatypes.primitive.Char
import org.web3j.abi.datatypes.primitive.Double
import org.web3j.abi.datatypes.primitive.Float
import org.web3j.abi.datatypes.primitive.Int
import org.web3j.abi.datatypes.primitive.Long
import org.web3j.abi.datatypes.primitive.Short

/**
 * TODO Arrays of Solidity numeric types and 2D arrays.
 */
class AbiFunctionEncoderArrayTest {

    @Test
    internal fun `encode string array`() {
        val empty = DynamicArray(Utf8String::class.java)
        assertThat(FunctionEncoder.encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x2100047465737431210000")

        val single = DynamicArray(Utf8String::class.java, listOf(Utf8String("Hello AVM")))
        assertThat(FunctionEncoder.encode(Function("test", listOf(single), listOf())))
            .isEqualTo("0x210004746573743121000121000948656c6c6f2041564d")
    }

    @Test
    internal fun `encode 2D string array`() {
        assertThrows<ABIException> {
            val param = DynamicArray(
                DynamicArray::class.java,
                DynamicArray(Utf8String::class.java, Utf8String("Hello AVM"))
            )
            FunctionEncoder.encode(Function("test", listOf(param), listOf()))
        }
    }

    @Test
    internal fun `encode char array`() {
        val empty = DynamicArray(Char::class.java)
        assertThat(FunctionEncoder.encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374130000")

        val abc = DynamicArray(Char::class.java, listOf(Char('a'), Char('b'), Char('c')))
        assertThat(FunctionEncoder.encode(Function("test", listOf(abc), listOf())))
            .isEqualTo("0x21000474657374130003006100620063")
    }

    @Test
    internal fun `encode boolean array`() {
        val empty = DynamicArray(Boolean::class.java)
        assertThat(FunctionEncoder.encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374120000")

        val trueFalse = DynamicArray(Boolean::class.java, listOf(Boolean(true), Boolean(false)))
        assertThat(FunctionEncoder.encode(Function("test", listOf(trueFalse), listOf())))
            .isEqualTo("0x210004746573741200020100")
    }

    @Test
    fun `encode byte array`() {
        val empty = DynamicArray(Byte::class.java)
        assertThat(FunctionEncoder.encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374110000")

        val single = DynamicArray(Byte::class.java, listOf(Byte(kotlin.Byte.MAX_VALUE)))
        assertThat(FunctionEncoder.encode(Function("test", listOf(single), listOf())))
            .isEqualTo("0x210004746573741100017f")
    }

    @Test
    fun `encode short array`() {
        val empty = DynamicArray(Short::class.java)
        assertThat(FunctionEncoder.encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374140000")

        val single = DynamicArray(Short::class.java, listOf(Short(kotlin.Short.MAX_VALUE)))
        assertThat(FunctionEncoder.encode(Function("test", listOf(single), listOf())))
            .isEqualTo("0x210004746573741400017fff")
    }

    @Test
    fun `encode int array`() {
        val empty = DynamicArray(Int::class.java)
        assertThat(FunctionEncoder.encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374150000")

        val single = DynamicArray(Int::class.java, listOf(Int(kotlin.Int.MAX_VALUE)))
        assertThat(FunctionEncoder.encode(Function("test", listOf(single), listOf())))
            .isEqualTo("0x210004746573741500017fffffff")
    }

    @Test
    fun `encode long array`() {
        val empty = DynamicArray(Long::class.java)
        assertThat(FunctionEncoder.encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374160000")

        val single = DynamicArray(Long::class.java, listOf(Long(kotlin.Long.MAX_VALUE)))
        assertThat(FunctionEncoder.encode(Function("test", listOf(single), listOf())))
            .isEqualTo("0x210004746573741600017fffffffffffffff")
    }

    @Test
    fun `encode float array`() {
        val empty = DynamicArray(Float::class.java)
        assertThat(FunctionEncoder.encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374170000")

        val single = DynamicArray(Float::class.java, listOf(Float(kotlin.Float.MAX_VALUE)))
        assertThat(FunctionEncoder.encode(Function("test", listOf(single), listOf())))
            .isEqualTo("0x210004746573741700017f7fffff")
    }

    @Test
    fun `encode double array`() {
        val empty = DynamicArray(Double::class.java)
        assertThat(FunctionEncoder.encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374180000")

        val single = DynamicArray(Double::class.java, listOf(Double(kotlin.Double.MAX_VALUE)))
        assertThat(FunctionEncoder.encode(Function("test", listOf(single), listOf())))
            .isEqualTo("0x210004746573741800017fefffffffffffff")
    }
}