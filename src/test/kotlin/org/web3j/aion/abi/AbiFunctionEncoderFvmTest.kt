package org.web3j.aion.abi

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.aion.avm.userlib.abi.ABIException
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.web3j.abi.FunctionEncoder.encode
import org.web3j.abi.FunctionEncoder.encodeConstructor
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Int
import org.web3j.abi.datatypes.Uint
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Int128
import org.web3j.abi.datatypes.generated.Int136
import org.web3j.abi.datatypes.generated.Int16
import org.web3j.abi.datatypes.generated.Int24
import org.web3j.abi.datatypes.generated.Int256
import org.web3j.abi.datatypes.generated.Int32
import org.web3j.abi.datatypes.generated.Int40
import org.web3j.abi.datatypes.generated.Int48
import org.web3j.abi.datatypes.generated.Int56
import org.web3j.abi.datatypes.generated.Int64
import org.web3j.abi.datatypes.generated.Int72
import org.web3j.abi.datatypes.generated.Int8
import org.web3j.abi.datatypes.generated.Uint128
import org.web3j.abi.datatypes.generated.Uint136
import org.web3j.abi.datatypes.generated.Uint16
import org.web3j.abi.datatypes.generated.Uint24
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint32
import org.web3j.abi.datatypes.generated.Uint40
import org.web3j.abi.datatypes.generated.Uint48
import org.web3j.abi.datatypes.generated.Uint56
import org.web3j.abi.datatypes.generated.Uint64
import org.web3j.abi.datatypes.generated.Uint8
import java.math.BigInteger

/**
 * TODO Add array and 2D array tests.
 */
class AbiFunctionEncoderFvmTest {

    @Test
    fun `encode empty constructor`() {
        assertThat(encodeConstructor(listOf())).isEqualTo("")
    }

    @Test
    fun `encode constructor`() {
        assertThat(encodeConstructor(listOf(Utf8String("Aion test"))))
            .isEqualTo("000000000000000000000000000000100000000000000000000000000000000941696f6e20746573740000000000000000000000000000000000000000000000")
    }

    @Test
    fun `encode empty parameters`() {
        assertThat(encode(Function("test", listOf(), listOf())))
            .isEqualTo("0x21000474657374")
    }

    @Test
    fun `encode string`() {
        assertThat(encode(Function("test", listOf(Utf8String("")), listOf())))
            .isEqualTo("0x21000474657374210000")

        assertThat(encode(Function("test", listOf(Utf8String("Hello AVM")), listOf())))
            .isEqualTo("0x2100047465737421000948656c6c6f2041564d")
    }

    @Test
    fun `encode bool`() {
        assertThat(encode(Function("test", listOf(Bool(true)), listOf())))
            .isEqualTo("0x210004746573740201")
    }

    @Test
    fun `encode int 136 to int256 throws ABIException`() {
        assertThrows<ABIException> {
            encode(Function("test", listOf(Int(BigInteger.ZERO)), listOf()))
        }
        assertThrows<ABIException> {
            encode(Function("test", listOf(Int136(BigInteger.ZERO)), listOf()))
        }
        assertThrows<ABIException> {
            encode(Function("test", listOf(Int256(BigInteger.ZERO)), listOf()))
        }
    }

    @Test
    fun `encode int8`() {
        assertThat(encode(Function("test", listOf(Int8(Byte.MIN_VALUE.toLong())), listOf())))
            .isEqualTo("0x210004746573740180")

        assertThat(encode(Function("test", listOf(Int8(Byte.MAX_VALUE.toLong())), listOf())))
            .isEqualTo("0x21000474657374017f")
    }

    @Test
    fun `encode int16`() {
        assertThat(encode(Function("test", listOf(Int16(kotlin.Short.MIN_VALUE.toLong())), listOf())))
            .isEqualTo("0x21000474657374048000")

        assertThat(encode(Function("test", listOf(Int16(kotlin.Short.MAX_VALUE.toLong())), listOf())))
            .isEqualTo("0x21000474657374047fff")
    }

    @Test
    fun `encode int24 to int32`() {
        assertThat(encode(Function("test", listOf(Int24(MIN_24_VALUE)), listOf())))
            .isEqualTo("0x2100047465737405ff800000")

        assertThat(encode(Function("test", listOf(Int24(MAX_24_VALUE)), listOf())))
            .isEqualTo("0x2100047465737405007fffff")

        assertThat(encode(Function("test", listOf(Int32(kotlin.Int.MIN_VALUE.toLong())), listOf())))
            .isEqualTo("0x210004746573740580000000")

        assertThat(encode(Function("test", listOf(Int32(kotlin.Int.MAX_VALUE.toLong())), listOf())))
            .isEqualTo("0x21000474657374057fffffff")
    }

    @Test
    fun `encode int40 to int64`() {
        assertThat(encode(Function("test", listOf(Int40(MIN_40_VALUE)), listOf())))
            .isEqualTo("0x2100047465737406ffffff8000000000")

        assertThat(encode(Function("test", listOf(Int40(MAX_40_VALUE)), listOf())))
            .isEqualTo("0x21000474657374060000007fffffffff")

        assertThat(encode(Function("test", listOf(Int48(MIN_48_VALUE)), listOf())))
            .isEqualTo("0x2100047465737406ffff800000000000")

        assertThat(encode(Function("test", listOf(Int48(MAX_48_VALUE)), listOf())))
            .isEqualTo("0x210004746573740600007fffffffffff")

        assertThat(encode(Function("test", listOf(Int56(MIN_56_VALUE)), listOf())))
            .isEqualTo("0x2100047465737406ff80000000000000")

        assertThat(encode(Function("test", listOf(Int56(MAX_56_VALUE)), listOf())))
            .isEqualTo("0x2100047465737406007fffffffffffff")

        assertThat(encode(Function("test", listOf(Int64(Long.MIN_VALUE)), listOf())))
            .isEqualTo("0x21000474657374068000000000000000")

        assertThat(encode(Function("test", listOf(Int64(Long.MAX_VALUE)), listOf())))
            .isEqualTo("0x21000474657374067fffffffffffffff")
    }

    @Test
    fun `encode int72 to int128`() {
        assertThat(encode(Function("test", listOf(Int72(MIN_72_VALUE)), listOf())))
            .isEqualTo("0x21000474657374110009800000000000000001")

        assertThat(encode(Function("test", listOf(Int72(MAX_72_VALUE)), listOf())))
            .isEqualTo("0x2100047465737411000a00800000000000000000")

        assertThat(encode(Function("test", listOf(Int128(MIN_128_VALUE)), listOf())))
            .isEqualTo("0x2100047465737411001080000000000000000000000000000000")

        assertThat(encode(Function("test", listOf(Int128(MAX_128_VALUE)), listOf())))
            .isEqualTo("0x210004746573741100107fffffffffffffffffffffffffffffff")
    }

    @Test
    fun `encode uint136 to uint256 throws ABIException`() {
        assertThrows<ABIException> {
            encode(Function("test", listOf(Uint(BigInteger.ZERO)), listOf()))
        }
        assertThrows<ABIException> {
            encode(Function("test", listOf(Uint136(BigInteger.ZERO)), listOf()))
        }
        assertThrows<ABIException> {
            encode(Function("test", listOf(Uint256(BigInteger.ZERO)), listOf()))
        }
    }

    @Test
    fun `encode uint8`() {
        assertThat(encode(Function("test", listOf(Uint8(0)), listOf())))
            .isEqualTo("0x21000474657374040000")

        assertThat(encode(Function("test", listOf(Uint8(UMAX_8_VALUE)), listOf())))
            .isEqualTo("0x210004746573740400ff")
    }

    @Test
    fun `encode uint16 to uint24`() {
        assertThat(encode(Function("test", listOf(Uint16(0)), listOf())))
            .isEqualTo("0x210004746573740500000000")

        assertThat(encode(Function("test", listOf(Uint16(UMAX_16_VALUE)), listOf())))
            .isEqualTo("0x21000474657374050000ffff")

        assertThat(encode(Function("test", listOf(Uint24(0)), listOf())))
            .isEqualTo("0x210004746573740500000000")

        assertThat(encode(Function("test", listOf(Uint24(UMAX_24_VALUE)), listOf())))
            .isEqualTo("0x210004746573740500ffffff")
    }

    @Test
    fun `encode uint32 to uint56`() {
        assertThat(encode(Function("test", listOf(Uint32(0)), listOf())))
            .isEqualTo("0x21000474657374060000000000000000")

        assertThat(encode(Function("test", listOf(Uint32(UMAX_32_VALUE)), listOf())))
            .isEqualTo("0x210004746573740600000000ffffffff")

        assertThat(encode(Function("test", listOf(Uint40(0)), listOf())))
            .isEqualTo("0x21000474657374060000000000000000")

        assertThat(encode(Function("test", listOf(Uint40(UMAX_40_VALUE)), listOf())))
            .isEqualTo("0x2100047465737406000000ffffffffff")

        assertThat(encode(Function("test", listOf(Uint48(0)), listOf())))
            .isEqualTo("0x21000474657374060000000000000000")

        assertThat(encode(Function("test", listOf(Uint48(UMAX_48_VALUE)), listOf())))
            .isEqualTo("0x21000474657374060000ffffffffffff")

        assertThat(encode(Function("test", listOf(Uint56(0)), listOf())))
            .isEqualTo("0x21000474657374060000000000000000")

        assertThat(encode(Function("test", listOf(Uint56(UMAX_56_VALUE)), listOf())))
            .isEqualTo("0x210004746573740600ffffffffffffff")
    }

    @Test
    fun `encode uint64 to uint128`() {
        assertThat(encode(Function("test", listOf(Uint64(0)), listOf())))
            .isEqualTo("0x2100047465737411000100")

        assertThat(encode(Function("test", listOf(Uint64(UMAX_64_VALUE)), listOf())))
            .isEqualTo("0x2100047465737411000900ffffffffffffffff")

        assertThat(encode(Function("test", listOf(Uint128(0)), listOf())))
            .isEqualTo("0x2100047465737411000100")

        assertThat(encode(Function("test", listOf(Uint128(UMAX_128_VALUE)), listOf())))
            .isEqualTo("0x2100047465737411001100ffffffffffffffffffffffffffffffff")
    }

    @Test
    internal fun `encode bool array`() {
        val empty = DynamicArray(Bool::class.java)
        assertThat(encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0x21000474657374120000")

        val trueFalse = DynamicArray(Bool::class.java, listOf(Bool(true), Bool(false)))
        assertThat(encode(Function("test", listOf(trueFalse), listOf())))
            .isEqualTo("0x210004746573741200020100")
    }
}
