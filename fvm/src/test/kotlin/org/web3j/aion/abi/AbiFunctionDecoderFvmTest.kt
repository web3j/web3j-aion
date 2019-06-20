package org.web3j.aion.abi

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Ignore
import org.junit.Test
import org.web3j.abi.FunctionReturnDecoder.decode
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Int128
import org.web3j.abi.datatypes.generated.Int16
import org.web3j.abi.datatypes.generated.Int24
import org.web3j.abi.datatypes.generated.Int32
import org.web3j.abi.datatypes.generated.Int40
import org.web3j.abi.datatypes.generated.Int48
import org.web3j.abi.datatypes.generated.Int56
import org.web3j.abi.datatypes.generated.Int64
import org.web3j.abi.datatypes.generated.Int72
import org.web3j.abi.datatypes.generated.Int8
import org.web3j.abi.datatypes.generated.Uint128
import org.web3j.abi.datatypes.generated.Uint16
import org.web3j.abi.datatypes.generated.Uint24
import org.web3j.abi.datatypes.generated.Uint32
import org.web3j.abi.datatypes.generated.Uint40
import org.web3j.abi.datatypes.generated.Uint48
import org.web3j.abi.datatypes.generated.Uint56
import org.web3j.abi.datatypes.generated.Uint64
import org.web3j.abi.datatypes.generated.Uint8

/**
 * TODO Add array and 2D array tests.
 */
@Ignore
class AbiFunctionDecoderFvmTest {

    @Test
    fun `decode empty parameters`() {
        val function = Function("test", listOf(), listOf())
        assertThat(decode("0x", function.outputParameters))
            .isEqualTo(listOf<Any>())
    }

    @Test
    fun `decode string`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Utf8String>() {}))

//        assertThat(decode("0x210000", function.outputParameters))
//            .isEqualTo(listOf(Utf8String("")))
//
//        assertThat(decode("0x21000948656c6c6f2041564d", function.outputParameters))
//            .isEqualTo(listOf(Utf8String("Hello AVM")))

        assertThat(
            decode(
                "0x000000000000000000000000000000100000000000000000000000000000000e48657920796f7520616761696e210000",
                function.outputParameters
            )
        ).isEqualTo(listOf(Utf8String("Hey you again!")))
    }

    @Test
    fun `decode boolean`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Bool>() {}))
        assertThat(decode("0x0201", function.outputParameters))
            .isEqualTo(listOf(Bool(true)))
    }

    @Test
    fun `decode int8`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int8>() {}))

        assertThat(decode("0x0180", function.outputParameters))
            .isEqualTo(listOf(Int8(Byte.MIN_VALUE.toLong())))

        assertThat(decode("0x017f", function.outputParameters))
            .isEqualTo(listOf(Int8(Byte.MAX_VALUE.toLong())))
    }

    @Test
    fun `decode int16`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int16>() {}))

        assertThat(decode("0x048000", function.outputParameters))
            .isEqualTo(listOf(Int16(Short.MIN_VALUE.toLong())))

        assertThat(decode("0x047fff", function.outputParameters))
            .isEqualTo(listOf(Int16(Short.MAX_VALUE.toLong())))
    }

    @Test
    fun `decode int24`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int24>() {}))

        assertThat(decode("0x05ff800000", function.outputParameters))
            .isEqualTo(listOf(Int24(MIN_24_VALUE)))

        assertThat(decode("0x05007fffff", function.outputParameters))
            .isEqualTo(listOf(Int24(MAX_24_VALUE)))
    }

    @Test
    fun `decode int32`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int32>() {}))

        assertThat(decode("0x0580000000", function.outputParameters))
            .isEqualTo(listOf(Int32(kotlin.Int.MIN_VALUE.toLong())))

        assertThat(decode("0x057fffffff", function.outputParameters))
            .isEqualTo(listOf(Int32(kotlin.Int.MAX_VALUE.toLong())))
    }

    @Test
    fun `decode int40`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int40>() {}))

        assertThat(decode("0x06ffffff8000000000", function.outputParameters))
            .isEqualTo(listOf(Int40(MIN_40_VALUE)))

        assertThat(decode("0x060000007fffffffff", function.outputParameters))
            .isEqualTo(listOf(Int40(MAX_40_VALUE)))
    }

    @Test
    fun `decode int48`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int48>() {}))

        assertThat(decode("0x06ffff800000000000", function.outputParameters))
            .isEqualTo(listOf(Int48(MIN_48_VALUE)))

        assertThat(decode("0x0600007fffffffffff", function.outputParameters))
            .isEqualTo(listOf(Int48(MAX_48_VALUE)))
    }

    @Test
    fun `decode int56`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int56>() {}))

        assertThat(decode("0x06ff80000000000000", function.outputParameters))
            .isEqualTo(listOf(Int56(MIN_56_VALUE)))

        assertThat(decode("0x06007fffffffffffff", function.outputParameters))
            .isEqualTo(listOf(Int56(MAX_56_VALUE)))
    }

    @Test
    fun `decode int64`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int64>() {}))

        assertThat(decode("0x068000000000000000", function.outputParameters))
            .isEqualTo(listOf(Int64(Long.MIN_VALUE)))

        assertThat(decode("0x067fffffffffffffff", function.outputParameters))
            .isEqualTo(listOf(Int64(Long.MAX_VALUE)))
    }

    @Test
    fun `decode int72`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int72>() {}))

        assertThat(decode("0x110009800000000000000001", function.outputParameters))
            .isEqualTo(listOf(Int72(MIN_72_VALUE)))

        assertThat(decode("0x11000a00800000000000000000", function.outputParameters))
            .isEqualTo(listOf(Int72(MAX_72_VALUE)))
    }

    @Test
    fun `decode int128`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int128>() {}))

        assertThat(
            decode(
                "0x11001080000000000000000000000000000000",
                function.outputParameters
            )
        ).isEqualTo(listOf(Int128(MIN_128_VALUE)))

        assertThat(
            decode(
                "0x1100107fffffffffffffffffffffffffffffff",
                function.outputParameters
            )
        ).isEqualTo(listOf(Int128(MAX_128_VALUE)))
    }

    @Test
    fun `decode uint8`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Uint8>() {}))

        assertThat(decode("0x040000", function.outputParameters))
            .isEqualTo(listOf(Uint8(0)))

        assertThat(decode("0x0400ff", function.outputParameters))
            .isEqualTo(listOf(Uint8(UMAX_8_VALUE)))
    }

    @Test
    fun `decode uint16`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Uint16>() {}))

        assertThat(decode("0x0500000000", function.outputParameters))
            .isEqualTo(listOf(Uint16(0)))

        assertThat(decode("0x050000ffff", function.outputParameters))
            .isEqualTo(listOf(Uint16(UMAX_16_VALUE)))
    }

    @Test
    fun `decode uint24`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Uint24>() {}))

        assertThat(decode("0x0500000000", function.outputParameters))
            .isEqualTo(listOf(Uint24(0)))

        assertThat(decode("0x0500ffffff", function.outputParameters))
            .isEqualTo(listOf(Uint24(UMAX_24_VALUE)))
    }

    @Test
    fun `decode uint32`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Uint32>() {}))

        assertThat(decode("0x060000000000000000", function.outputParameters))
            .isEqualTo(listOf(Uint32(0)))

        assertThat(decode("0x0600000000ffffffff", function.outputParameters))
            .isEqualTo(listOf(Uint32(UMAX_32_VALUE)))
    }

    @Test
    fun `decode uint40`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Uint40>() {}))

        assertThat(decode("0x060000000000000000", function.outputParameters))
            .isEqualTo(listOf(Uint40(0)))

        assertThat(decode("0x06000000ffffffffff", function.outputParameters))
            .isEqualTo(listOf(Uint40(UMAX_40_VALUE)))
    }

    @Test
    fun `decode uint48`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Uint48>() {}))

        assertThat(decode("0x060000000000000000", function.outputParameters))
            .isEqualTo(listOf(Uint48(0)))

        assertThat(decode("0x060000ffffffffffff", function.outputParameters))
            .isEqualTo(listOf(Uint48(UMAX_48_VALUE)))
    }

    @Test
    fun `decode uint56`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Uint56>() {}))

        assertThat(decode("0x060000000000000000", function.outputParameters))
            .isEqualTo(listOf(Uint56(0)))

        assertThat(decode("0x0600ffffffffffffff", function.outputParameters))
            .isEqualTo(listOf(Uint56(UMAX_56_VALUE)))
    }

    @Test
    fun `decode uint64`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Uint64>() {}))

        assertThat(decode("0x11000100", function.outputParameters))
            .isEqualTo(listOf(Uint64(0)))

        assertThat(decode("0x11000900ffffffffffffffff", function.outputParameters))
            .isEqualTo(listOf(Uint64(UMAX_64_VALUE)))
    }

    @Test
    fun `decode uint128`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Uint128>() {}))

        assertThat(decode("0x11000100", function.outputParameters))
            .isEqualTo(listOf(Uint128(0)))

        assertThat(
            decode(
                "0x11001100ffffffffffffffffffffffffffffffff",
                function.outputParameters
            )
        ).isEqualTo(listOf(Uint128(UMAX_128_VALUE)))
    }

    @Test
    fun `decode string array`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<DynamicArray<Utf8String>>() {}))

        assertThat(decode("0x31210000", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Utf8String::class.java)))

        assertThat(decode("0x3121000121000948656c6c6f2041564d", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Utf8String::class.java, listOf(Utf8String("Hello AVM")))))
    }

//    @Test
//    fun `decode 2D string array`() {
//        assertThrows<ABIException> {
//            val param = DynamicArray(
//                DynamicArray::class.java,
//                DynamicArray(Utf8String::class.java, Utf8String("Hello AVM"))
//            )
//            decode(Function("test", listOf(param), listOf()))
//        }
//    }

    @Test
    fun `decode boolean array`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<DynamicArray<Bool>>() {}))

        assertThat(decode("0x120000", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Bool::class.java)))

        assertThat(decode("0x1200020100", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Bool::class.java, listOf(Bool(true), Bool(false)))))
    }
}
