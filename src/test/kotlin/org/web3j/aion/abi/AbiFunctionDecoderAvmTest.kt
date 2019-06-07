package org.web3j.aion.abi

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test
import org.web3j.abi.FunctionReturnDecoder.decode
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.primitive.Byte
import org.web3j.abi.datatypes.primitive.Char
import org.web3j.abi.datatypes.primitive.Double
import org.web3j.abi.datatypes.primitive.Float
import org.web3j.abi.datatypes.primitive.Int
import org.web3j.abi.datatypes.primitive.Long
import org.web3j.abi.datatypes.primitive.Short
import org.web3j.aion.AionConstants.ADDRESS_BIT_LENGTH

/**
 * TODO 2 dimensional array tests.
 */
class AbiFunctionDecoderAvmTest {

    @Test
    fun `decode address`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Address>() {}))
        assertThat(
            decode(
                "0x22a04462684b510796c186d19abfa6929742f79394583d6efb1243bbb473f21d9f",
                function.outputParameters
            )
        ).isEqualTo(
            listOf(
                Address(
                    ADDRESS_BIT_LENGTH,
                    "0xa04462684b510796c186d19abfa6929742f79394583d6efb1243bbb473f21d9f"
                )
            )
        )
    }

    @Test
    fun `decode char`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Char>() {}))
        assertThat(decode("0x030061", function.outputParameters))
            .isEqualTo(listOf(Char('a')))
    }

    @Test
    fun `decode byte`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Byte>() {}))

        assertThat(decode("0x0180", function.outputParameters))
            .isEqualTo(listOf(Byte(kotlin.Byte.MIN_VALUE)))

        assertThat(decode("0x017f", function.outputParameters))
            .isEqualTo(listOf(Byte(kotlin.Byte.MAX_VALUE)))
    }

    @Test
    fun `decode short`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Short>() {}))

        assertThat(decode("0x048000", function.outputParameters))
            .isEqualTo(listOf(Short(kotlin.Short.MIN_VALUE)))

        assertThat(decode("0x047fff", function.outputParameters))
            .isEqualTo(listOf(Short(kotlin.Short.MAX_VALUE)))
    }

    @Test
    fun `decode int`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int>() {}))

        assertThat(decode("0x0580000000", function.outputParameters))
            .isEqualTo(listOf(Int(kotlin.Int.MIN_VALUE)))

        assertThat(decode("0x057fffffff", function.outputParameters))
            .isEqualTo(listOf(Int(kotlin.Int.MAX_VALUE)))
    }

    @Test
    fun `decode long`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Long>() {}))

        assertThat(decode("0x068000000000000000", function.outputParameters))
            .isEqualTo(listOf(Long(kotlin.Long.MIN_VALUE)))

        assertThat(decode("0x067fffffffffffffff", function.outputParameters))
            .isEqualTo(listOf(Long(kotlin.Long.MAX_VALUE)))
    }

    @Test
    fun `decode float`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Float>() {}))

        assertThat(decode("0x0700000001", function.outputParameters))
            .isEqualTo(listOf(Float(kotlin.Float.MIN_VALUE)))

        assertThat(decode("0x077f7fffff", function.outputParameters))
            .isEqualTo(listOf(Float(kotlin.Float.MAX_VALUE)))
    }

    @Test
    fun `decode double`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Double>() {}))

        assertThat(decode("0x080000000000000001", function.outputParameters))
            .isEqualTo(listOf(Double(kotlin.Double.MIN_VALUE)))

        assertThat(decode("0x087fefffffffffffff", function.outputParameters))
            .isEqualTo(listOf(Double(kotlin.Double.MAX_VALUE)))
    }

    @Test
    fun `decode address array`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<DynamicArray<Address>>() {}))

        assertThat(decode("0x31220000", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Address::class.java)))

        assertThat(
            decode(
                "0x3122000222a04462684b510796c186d19abfa6929742f79394583d6efb1243bbb47" +
                    "3f21d9f22a02e5aad91bed3a1c6bbd1958cea6f0ecedd31ac8801a435913b7ada136dcdfa",
                function.outputParameters
            )
        ).isEqualTo(
            listOf(
                DynamicArray(
                    Address::class.java,
                    listOf(
                        Address(
                            ADDRESS_BIT_LENGTH,
                            "0xa04462684b510796c186d19abfa6929742f79394583d6efb1243bbb473f21d9f"
                        ), Address(
                            ADDRESS_BIT_LENGTH,
                            "0xa02e5aad91bed3a1c6bbd1958cea6f0ecedd31ac8801a435913b7ada136dcdfa"
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `decode char array`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<DynamicArray<Char>>() {}))

        assertThat(decode("0x130000", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Char::class.java)))

        assertThat(decode("0x130003006100620063", function.outputParameters)).isEqualTo(
            listOf(
                DynamicArray(
                    Char::class.java,
                    listOf(Char('a'), Char('b'), Char('c'))
                )
            )
        )
    }

    @Test
    fun `decode byte array`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<DynamicArray<Byte>>() {}))

        assertThat(decode("0x110000", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Byte::class.java)))

        assertThat(decode("0x110002807f", function.outputParameters)).isEqualTo(
            listOf(
                DynamicArray(
                    Byte::class.java, listOf(
                        Byte(kotlin.Byte.MIN_VALUE),
                        Byte(kotlin.Byte.MAX_VALUE)
                    )
                )
            )
        )
    }

    @Test
    fun `decode short array`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<DynamicArray<Short>>() {}))

        assertThat(decode("0x140000", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Short::class.java)))

        assertThat(decode("0x14000280007fff", function.outputParameters)).isEqualTo(
            listOf(
                DynamicArray(
                    Short::class.java,
                    listOf(Short(kotlin.Short.MIN_VALUE), Short(kotlin.Short.MAX_VALUE))
                )
            )
        )
    }

    @Test
    fun `decode int array`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<DynamicArray<Int>>() {}))

        assertThat(decode("0x150000", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Int::class.java)))

        assertThat(decode("0x150002800000007fffffff", function.outputParameters)).isEqualTo(
            listOf(
                DynamicArray(
                    Int::class.java,
                    listOf(Int(kotlin.Int.MIN_VALUE), Int(kotlin.Int.MAX_VALUE))
                )
            )
        )
    }

    @Test
    fun `decode long array`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<DynamicArray<Long>>() {}))

        assertThat(decode("0x160000", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Long::class.java)))

        assertThat(
            decode(
                "0x16000280000000000000007fffffffffffffff",
                function.outputParameters
            )
        ).isEqualTo(
            listOf(
                DynamicArray(
                    Long::class.java,
                    listOf(Long(kotlin.Long.MIN_VALUE), Long(kotlin.Long.MAX_VALUE))
                )
            )
        )
    }

    @Test
    fun `decode float array`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<DynamicArray<Float>>() {}))

        assertThat(decode("0x170000", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Float::class.java)))

        assertThat(decode("0x170002000000017f7fffff", function.outputParameters)).isEqualTo(
            listOf(
                DynamicArray(
                    Float::class.java,
                    listOf(Float(kotlin.Float.MIN_VALUE), Float(kotlin.Float.MAX_VALUE))
                )
            )
        )
    }

    @Test
    fun `decode double array`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<DynamicArray<Double>>() {}))

        assertThat(decode("0x180000", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Double::class.java)))

        assertThat(
            decode(
                "0x18000200000000000000017fefffffffffffff",
                function.outputParameters
            )
        ).isEqualTo(
            listOf(
                DynamicArray(
                    Double::class.java,
                    listOf(Double(kotlin.Double.MIN_VALUE), Double(kotlin.Double.MAX_VALUE))
                )
            )
        )
    }
}
