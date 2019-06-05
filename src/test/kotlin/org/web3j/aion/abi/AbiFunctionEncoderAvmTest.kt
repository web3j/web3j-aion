package org.web3j.aion.abi

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.aion.avm.userlib.abi.ABIException
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.web3j.abi.FunctionEncoder.encode
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.primitive.Byte
import org.web3j.abi.datatypes.primitive.Char
import org.web3j.abi.datatypes.primitive.Double
import org.web3j.abi.datatypes.primitive.Float
import org.web3j.abi.datatypes.primitive.Int
import org.web3j.abi.datatypes.primitive.Long
import org.web3j.abi.datatypes.primitive.Short

/**
 * TODO 2 dimensional array tests.
 */
class AbiFunctionEncoderAvmTest {

    @Test
    internal fun `encode char`() {
        assertThat(encode(Function("test", listOf(Char('a')), listOf())))
            .isEqualTo("0x21000474657374030061")
    }

    @Test
    internal fun `encode byte`() {
        assertThat(encode(Function("test", listOf(Byte(kotlin.Byte.MIN_VALUE)), listOf())))
            .isEqualTo("0x210004746573740180")

        assertThat(encode(Function("test", listOf(Byte(kotlin.Byte.MAX_VALUE)), listOf())))
            .isEqualTo("0x21000474657374017f")
    }

    @Test
    internal fun `encode short`() {
        assertThat(encode(Function("test", listOf(Short(kotlin.Short.MIN_VALUE)), listOf())))
            .isEqualTo("0x21000474657374048000")

        assertThat(encode(Function("test", listOf(Short(kotlin.Short.MAX_VALUE)), listOf())))
            .isEqualTo("0x21000474657374047fff")
    }

    @Test
    internal fun `encode int`() {
        assertThat(encode(Function("test", listOf(Int(kotlin.Int.MIN_VALUE)), listOf())))
            .isEqualTo("0x210004746573740580000000")

        assertThat(encode(Function("test", listOf(Int(kotlin.Int.MAX_VALUE)), listOf())))
            .isEqualTo("0x21000474657374057fffffff")
    }

    @Test
    internal fun `encode long`() {
        assertThat(encode(Function("test", listOf(Long(kotlin.Long.MIN_VALUE)), listOf())))
            .isEqualTo("0x21000474657374068000000000000000")

        assertThat(encode(Function("test", listOf(Long(kotlin.Long.MAX_VALUE)), listOf())))
            .isEqualTo("0x21000474657374067fffffffffffffff")
    }

    @Test
    internal fun `encode float`() {
        assertThat(encode(Function("test", listOf(Float(kotlin.Float.MIN_VALUE)), listOf())))
            .isEqualTo("0x210004746573740700000001")

        assertThat(encode(Function("test", listOf(Float(kotlin.Float.MAX_VALUE)), listOf())))
            .isEqualTo("0x21000474657374077f7fffff")
    }

    @Test
    internal fun `encode double`() {
        assertThat(encode(Function("test", listOf(Double(kotlin.Double.MIN_VALUE)), listOf())))
            .isEqualTo("0x21000474657374080000000000000001")

        assertThat(encode(Function("test", listOf(Double(kotlin.Double.MAX_VALUE)), listOf())))
            .isEqualTo("0x21000474657374087fefffffffffffff")
    }

    @Test
    internal fun `encode string array`() {
        val empty = DynamicArray(Utf8String::class.java)
        assertThat(encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x2100047465737431210000")

        val single = DynamicArray(Utf8String::class.java, listOf(Utf8String("Hello AVM")))
        assertThat(encode(Function("test", listOf(single), listOf())))
            .isEqualTo("0x210004746573743121000121000948656c6c6f2041564d")
    }

    @Test
    internal fun `encode 2D string array`() {
        assertThrows<ABIException> {
            val param = DynamicArray(
                DynamicArray::class.java,
                DynamicArray(Utf8String::class.java, Utf8String("Hello AVM"))
            )
            encode(Function("test", listOf(param), listOf()))
        }
    }

    @Test
    internal fun `encode char array`() {
        val empty = DynamicArray(Char::class.java)
        assertThat(encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374130000")

        val abc = DynamicArray(Char::class.java, listOf(Char('a'), Char('b'), Char('c')))
        assertThat(encode(Function("test", listOf(abc), listOf())))
            .isEqualTo("0x21000474657374130003006100620063")
    }

    @Test
    fun `encode byte array`() {
        val empty = DynamicArray(Byte::class.java)
        assertThat(encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374110000")

        val minMax = DynamicArray(Byte::class.java, listOf(Byte(kotlin.Byte.MIN_VALUE), Byte(kotlin.Byte.MAX_VALUE)))
        assertThat(encode(Function("test", listOf(minMax), listOf())))
            .isEqualTo("0x21000474657374110002807f")
    }

    @Test
    fun `encode short array`() {
        val empty = DynamicArray(Short::class.java)
        assertThat(encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374140000")

        val minMax =
            DynamicArray(Short::class.java, listOf(Short(kotlin.Short.MIN_VALUE), Short(kotlin.Short.MAX_VALUE)))
        assertThat(encode(Function("test", listOf(minMax), listOf())))
            .isEqualTo("0x2100047465737414000280007fff")
    }

    @Test
    fun `encode int array`() {
        val empty = DynamicArray(Int::class.java)
        assertThat(encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374150000")

        val minMax = DynamicArray(Int::class.java, listOf(Int(kotlin.Int.MIN_VALUE), Int(kotlin.Int.MAX_VALUE)))
        assertThat(encode(Function("test", listOf(minMax), listOf())))
            .isEqualTo("0x21000474657374150002800000007fffffff")
    }

    @Test
    fun `encode long array`() {
        val empty = DynamicArray(Long::class.java)
        assertThat(encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374160000")

        val minMax = DynamicArray(Long::class.java, listOf(Long(kotlin.Long.MIN_VALUE), Long(kotlin.Long.MAX_VALUE)))
        assertThat(encode(Function("test", listOf(minMax), listOf())))
            .isEqualTo("0x2100047465737416000280000000000000007fffffffffffffff")
    }

    @Test
    fun `encode float array`() {
        val empty = DynamicArray(Float::class.java)
        assertThat(encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374170000")

        val minMax = DynamicArray(
            Float::class.java,
            listOf(
                Float(kotlin.Float.MIN_VALUE),
                Float(kotlin.Float.MAX_VALUE)
            )
        )
        assertThat(encode(Function("test", listOf(minMax), listOf())))
            .isEqualTo("0x21000474657374170002000000017f7fffff")
    }

    @Test
    fun `encode double array`() {
        val empty = DynamicArray(Double::class.java)
        assertThat(encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374180000")

        val minMax = DynamicArray(
            Double::class.java, listOf(
                Double(kotlin.Double.MIN_VALUE),
                Double(kotlin.Double.MAX_VALUE)
            )
        )
        assertThat(encode(Function("test", listOf(minMax), listOf())))
            .isEqualTo("0x2100047465737418000200000000000000017fefffffffffffff")
    }
}