package org.web3j.aion.abi

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test
import org.web3j.abi.FunctionReturnDecoder.decode
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Int
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Int8

/**
 * TODO Array tests
 */
class AbiFunctionDecoderFvmTest {

    @Test
    fun `decode empty parameters`() {
        val function = Function("test", listOf(), listOf())
        assertThat(decode("0x21000474657374", function.outputParameters))
            .isEqualTo(listOf<Any>())
    }

    @Test
    fun `decode string`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Utf8String>() {}))

        assertThat(decode("0x21000474657374210000", function.outputParameters))
            .isEqualTo(listOf(Utf8String("")))

        assertThat(decode("0x2100047465737421000948656c6c6f2041564d", function.outputParameters))
            .isEqualTo(listOf(Utf8String("Hello AVM")))
    }

    @Test
    fun `decode boolean`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Bool>() {}))
        assertThat(decode("0x210004746573740201", function.outputParameters))
            .isEqualTo(listOf(Bool(true)))
    }

    @Test
    fun `decode int`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int>() {}))

        assertThat(decode("0x210004746573740180", function.outputParameters))
            .isEqualTo(listOf(Int(MIN_256_VALUE)))

        assertThat(decode("0x21000474657374017f", function.outputParameters))
            .isEqualTo(listOf(Int(MAX_256_VALUE)))
    }

    @Test
    fun `decode int8`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<Int8>() {}))

        assertThat(decode("0x210004746573740180", function.outputParameters))
            .isEqualTo(listOf(Int8(Byte.MIN_VALUE.toLong())))

        assertThat(decode("0x21000474657374017f", function.outputParameters))
            .isEqualTo(listOf(Int8(Byte.MAX_VALUE.toLong())))
    }
//
//    @Test
//    fun `decode int16`() {
//        assertThat(decode(Function("test", listOf(Int16(kotlin.Short.MIN_VALUE.toLong())), listOf())))
//            .isEqualTo("0x21000474657374048000")
//
//        assertThat(decode(Function("test", listOf(Int16(kotlin.Short.MAX_VALUE.toLong())), listOf())))
//            .isEqualTo("0x21000474657374047fff")
//    }
//
//    @Test
//    fun `decode int24 to int32 as int`() {
//        assertThat(decode(Function("test", listOf(Int24(MIN_24_VALUE)), listOf())))
//            .isEqualTo("0x2100047465737405ff800000")
//
//        assertThat(decode(Function("test", listOf(Int24(MAX_24_VALUE)), listOf())))
//            .isEqualTo("0x2100047465737405007fffff")
//
//        assertThat(decode(Function("test", listOf(Int32(kotlin.Int.MIN_VALUE.toLong())), listOf())))
//            .isEqualTo("0x210004746573740580000000")
//
//        assertThat(decode(Function("test", listOf(Int32(kotlin.Int.MAX_VALUE.toLong())), listOf())))
//            .isEqualTo("0x21000474657374057fffffff")
//    }
//
//    @Test
//    fun `decode int40 to int64`() {
//        assertThat(decode(Function("test", listOf(Int40(MIN_40_VALUE)), listOf())))
//            .isEqualTo("0x2100047465737406ffffff8000000000")
//
//        assertThat(decode(Function("test", listOf(Int40(MAX_40_VALUE)), listOf())))
//            .isEqualTo("0x21000474657374060000007fffffffff")
//
//        assertThat(decode(Function("test", listOf(Int48(MIN_48_VALUE)), listOf())))
//            .isEqualTo("0x2100047465737406ffff800000000000")
//
//        assertThat(decode(Function("test", listOf(Int48(MAX_48_VALUE)), listOf())))
//            .isEqualTo("0x210004746573740600007fffffffffff")
//
//        assertThat(decode(Function("test", listOf(Int56(MIN_56_VALUE)), listOf())))
//            .isEqualTo("0x2100047465737406ff80000000000000")
//
//        assertThat(decode(Function("test", listOf(Int56(MAX_56_VALUE)), listOf())))
//            .isEqualTo("0x2100047465737406007fffffffffffff")
//
//        assertThat(decode(Function("test", listOf(Int64(kotlin.Long.MIN_VALUE)), listOf())))
//            .isEqualTo("0x21000474657374068000000000000000")
//
//        assertThat(decode(Function("test", listOf(Int64(kotlin.Long.MAX_VALUE)), listOf())))
//            .isEqualTo("0x21000474657374067fffffffffffffff")
//    }
//
//    @Test
//    fun `decode int72 to int256`() {
//        assertThat(decode(Function("test", listOf(Int72(MIN_72_VALUE)), listOf())))
//            .isEqualTo("0x21000474657374110009800000000000000001")
//
//        assertThat(decode(Function("test", listOf(Int72(MAX_72_VALUE)), listOf())))
//            .isEqualTo("0x2100047465737411000a00800000000000000000")
//
//        assertThat(decode(Function("test", listOf(Int256(MIN_256_VALUE)), listOf())))
//            .isEqualTo("0x210004746573741100208000000000000000000000000000000000000000000000000000000000000001")
//
//        assertThat(decode(Function("test", listOf(Int256(MAX_256_VALUE)), listOf())))
//            .isEqualTo("0x21000474657374110021008000000000000000000000000000000000000000000000000000000000000000")
//    }
//
//    @Test
//    fun `decode uint8`() {
//        assertThat(decode(Function("test", listOf(Uint8(0)), listOf())))
//            .isEqualTo("0x21000474657374040000")
//
//        assertThat(decode(Function("test", listOf(Uint8(UMAX_8_VALUE)), listOf())))
//            .isEqualTo("0x210004746573740400ff")
//    }
//
//    @Test
//    fun `decode uint16 to uint24`() {
//        assertThat(decode(Function("test", listOf(Uint16(0)), listOf())))
//            .isEqualTo("0x210004746573740500000000")
//
//        assertThat(decode(Function("test", listOf(Uint16(UMAX_16_VALUE)), listOf())))
//            .isEqualTo("0x21000474657374050000ffff")
//
//        assertThat(decode(Function("test", listOf(Uint24(0)), listOf())))
//            .isEqualTo("0x210004746573740500000000")
//
//        assertThat(decode(Function("test", listOf(Uint24(UMAX_24_VALUE)), listOf())))
//            .isEqualTo("0x210004746573740500ffffff")
//    }
//
//    @Test
//    fun `decode uint32 to uint56`() {
//        assertThat(decode(Function("test", listOf(Uint32(0)), listOf())))
//            .isEqualTo("0x21000474657374060000000000000000")
//
//        assertThat(decode(Function("test", listOf(Uint32(UMAX_32_VALUE)), listOf())))
//            .isEqualTo("0x210004746573740600000000ffffffff")
//
//        assertThat(decode(Function("test", listOf(Uint40(0)), listOf())))
//            .isEqualTo("0x21000474657374060000000000000000")
//
//        assertThat(decode(Function("test", listOf(Uint40(UMAX_40_VALUE)), listOf())))
//            .isEqualTo("0x2100047465737406000000ffffffffff")
//
//        assertThat(decode(Function("test", listOf(Uint48(0)), listOf())))
//            .isEqualTo("0x21000474657374060000000000000000")
//
//        assertThat(decode(Function("test", listOf(Uint48(UMAX_48_VALUE)), listOf())))
//            .isEqualTo("0x21000474657374060000ffffffffffff")
//
//        assertThat(decode(Function("test", listOf(Uint56(0)), listOf())))
//            .isEqualTo("0x21000474657374060000000000000000")
//
//        assertThat(decode(Function("test", listOf(Uint56(UMAX_56_VALUE)), listOf())))
//            .isEqualTo("0x210004746573740600ffffffffffffff")
//    }
//
//    @Test
//    fun `decode uint64 to uint256`() {
//        assertThat(decode(Function("test", listOf(Uint64(0)), listOf())))
//            .isEqualTo("0x2100047465737411000100")
//
//        assertThat(decode(Function("test", listOf(Uint64(UMAX_64_VALUE)), listOf())))
//            .isEqualTo("0x2100047465737411000900ffffffffffffffff")
//
//        assertThat(decode(Function("test", listOf(Uint256(0)), listOf())))
//            .isEqualTo("0x2100047465737411000100")
//
//        assertThat(decode(Function("test", listOf(Uint256(UMAX_256_VALUE)), listOf())))
//            .isEqualTo("0x2100047465737411002100ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")
//    }

    @Test
    fun `decode string array`() {
        val function = Function("test", listOf(), listOf(object : TypeReference<DynamicArray<Utf8String>>() {}))

        assertThat(decode("0x2100047465737431210000", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Utf8String::class.java)))

        assertThat(decode("0x210004746573743121000121000948656c6c6f2041564d", function.outputParameters))
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

        assertThat(decode("0x21000474657374120000", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Bool::class.java)))

        assertThat(decode("0x210004746573741200020100", function.outputParameters))
            .isEqualTo(listOf(DynamicArray(Bool::class.java, listOf(Bool(true), Bool(false)))))
    }
}
