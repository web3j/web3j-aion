package org.web3j.aion.abi

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Ignore
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
@Ignore
class AbiFunctionEncoderFvmTest {

    @Test
    fun `encode empty constructor`() {
        assertThat(encodeConstructor(listOf())).isEqualTo("")
    }

    @Test
    fun `encode constructor`() {
        assertThat(encodeConstructor(listOf(Utf8String("Aion test"))))
            .isEqualTo("0000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000941696f6e20746573740000000000000000000000000000000000000000000000")
    }

    @Test
    fun `encode empty parameters`() {
        assertThat(encode(Function("test", listOf(), listOf())))
            .isEqualTo("0xf8a8fd6d")
    }

    @Test
    fun `encode string`() {
        assertThat(encode(Function("test", listOf(Utf8String("")), listOf())))
            .isEqualTo("0xf9fbd55400000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000000")

        assertThat(encode(Function("test", listOf(Utf8String("Hello AVM")), listOf())))
            .isEqualTo("0xf9fbd5540000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000948656c6c6f2041564d0000000000000000000000000000000000000000000000")
    }

    @Test
    fun `encode bool`() {
        assertThat(encode(Function("test", listOf(Bool(true)), listOf())))
            .isEqualTo("0x36091dff0000000000000000000000000000000000000000000000000000000000000001")
    }

    @Test
    fun `encode int 136 to int256 throws UnsupportedTypeException`() {
        assertThrows<UnsupportedTypeException> {
            encode(Function("test", listOf(Int(BigInteger.ZERO)), listOf()))
        }
        assertThrows<UnsupportedTypeException> {
            encode(Function("test", listOf(Int136(BigInteger.ZERO)), listOf()))
        }
        assertThrows<UnsupportedTypeException> {
            encode(Function("test", listOf(Int256(BigInteger.ZERO)), listOf()))
        }
    }

    @Test
    fun `encode int8`() {
        assertThat(encode(Function("test", listOf(Int8(Byte.MIN_VALUE.toLong())), listOf())))
            .isEqualTo("0x2b58697effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80")

        assertThat(encode(Function("test", listOf(Int8(Byte.MAX_VALUE.toLong())), listOf())))
            .isEqualTo("0x2b58697e000000000000000000000000000000000000000000000000000000000000007f")
    }

    @Test
    fun `encode int16`() {
        assertThat(encode(Function("test", listOf(Int16(kotlin.Short.MIN_VALUE.toLong())), listOf())))
            .isEqualTo("0x87d12c1bffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8000")

        assertThat(encode(Function("test", listOf(Int16(kotlin.Short.MAX_VALUE.toLong())), listOf())))
            .isEqualTo("0x87d12c1b0000000000000000000000000000000000000000000000000000000000007fff")
    }

    @Test
    fun `encode int24 to int32`() {
        assertThat(encode(Function("test", listOf(Int24(MIN_24_VALUE)), listOf())))
            .isEqualTo("0x63eae2cdffffffffffffffffffffffffffffffffffffffffffffffffffffffffff800000")

        assertThat(encode(Function("test", listOf(Int24(MAX_24_VALUE)), listOf())))
            .isEqualTo("0x63eae2cd00000000000000000000000000000000000000000000000000000000007fffff")

        assertThat(encode(Function("test", listOf(Int32(kotlin.Int.MIN_VALUE.toLong())), listOf())))
            .isEqualTo("0x7747cc75ffffffffffffffffffffffffffffffffffffffffffffffffffffffff80000000")

        assertThat(encode(Function("test", listOf(Int32(kotlin.Int.MAX_VALUE.toLong())), listOf())))
            .isEqualTo("0x7747cc75000000000000000000000000000000000000000000000000000000007fffffff")
    }

    @Test
    fun `encode int40 to int64`() {
        assertThat(encode(Function("test", listOf(Int40(MIN_40_VALUE)), listOf())))
            .isEqualTo("0x3929934cffffffffffffffffffffffffffffffffffffffffffffffffffffff8000000000")

        assertThat(encode(Function("test", listOf(Int40(MAX_40_VALUE)), listOf())))
            .isEqualTo("0x3929934c0000000000000000000000000000000000000000000000000000007fffffffff")

        assertThat(encode(Function("test", listOf(Int48(MIN_48_VALUE)), listOf())))
            .isEqualTo("0xd664ab99ffffffffffffffffffffffffffffffffffffffffffffffffffff800000000000")

        assertThat(encode(Function("test", listOf(Int48(MAX_48_VALUE)), listOf())))
            .isEqualTo("0xd664ab9900000000000000000000000000000000000000000000000000007fffffffffff")

        assertThat(encode(Function("test", listOf(Int56(MIN_56_VALUE)), listOf())))
            .isEqualTo("0xf163f0b2ffffffffffffffffffffffffffffffffffffffffffffffffff80000000000000")

        assertThat(encode(Function("test", listOf(Int56(MAX_56_VALUE)), listOf())))
            .isEqualTo("0xf163f0b2000000000000000000000000000000000000000000000000007fffffffffffff")

        assertThat(encode(Function("test", listOf(Int64(Long.MIN_VALUE)), listOf())))
            .isEqualTo("0xb3a5eb29ffffffffffffffffffffffffffffffffffffffffffffffff8000000000000000")

        assertThat(encode(Function("test", listOf(Int64(Long.MAX_VALUE)), listOf())))
            .isEqualTo("0xb3a5eb290000000000000000000000000000000000000000000000007fffffffffffffff")
    }

    @Test
    fun `encode int72 to int128`() {
        assertThat(encode(Function("test", listOf(Int72(MIN_72_VALUE)), listOf())))
            .isEqualTo("0x3795df92ffffffffffffffffffffffffffffffffffffffffffffff800000000000000001")

        assertThat(encode(Function("test", listOf(Int72(MAX_72_VALUE)), listOf())))
            .isEqualTo("0x3795df920000000000000000000000000000000000000000000000800000000000000000")

        assertThat(encode(Function("test", listOf(Int128(MIN_128_VALUE)), listOf())))
            .isEqualTo("0xa10c1284ffffffffffffffffffffffffffffffff80000000000000000000000000000000")

        assertThat(encode(Function("test", listOf(Int128(MAX_128_VALUE)), listOf())))
            .isEqualTo("0xa10c1284000000000000000000000000000000007fffffffffffffffffffffffffffffff")
    }

    @Test
    fun `encode uint136 to uint256 throws UnsupportedTypeException`() {
        assertThrows<UnsupportedTypeException> {
            encode(Function("test", listOf(Uint(BigInteger.ZERO)), listOf()))
        }
        assertThrows<UnsupportedTypeException> {
            encode(Function("test", listOf(Uint136(BigInteger.ZERO)), listOf()))
        }
        assertThrows<UnsupportedTypeException> {
            encode(Function("test", listOf(Uint256(BigInteger.ZERO)), listOf()))
        }
    }

    @Test
    fun `encode uint8`() {
        assertThat(encode(Function("test", listOf(Uint8(0)), listOf())))
            .isEqualTo("0xf1820bdc0000000000000000000000000000000000000000000000000000000000000000")

        assertThat(encode(Function("test", listOf(Uint8(UMAX_8_VALUE)), listOf())))
            .isEqualTo("0xf1820bdc00000000000000000000000000000000000000000000000000000000000000ff")
    }

    @Test
    fun `encode uint16 to uint24`() {
        assertThat(encode(Function("test", listOf(Uint16(0)), listOf())))
            .isEqualTo("0x1891002e0000000000000000000000000000000000000000000000000000000000000000")

        assertThat(encode(Function("test", listOf(Uint16(UMAX_16_VALUE)), listOf())))
            .isEqualTo("0x1891002e000000000000000000000000000000000000000000000000000000000000ffff")

        assertThat(encode(Function("test", listOf(Uint24(0)), listOf())))
            .isEqualTo("0xbf7092c40000000000000000000000000000000000000000000000000000000000000000")

        assertThat(encode(Function("test", listOf(Uint24(UMAX_24_VALUE)), listOf())))
            .isEqualTo("0xbf7092c40000000000000000000000000000000000000000000000000000000000ffffff")
    }

    @Test
    fun `encode uint32 to uint56`() {
        assertThat(encode(Function("test", listOf(Uint32(0)), listOf())))
            .isEqualTo("0xe3cff6340000000000000000000000000000000000000000000000000000000000000000")

        assertThat(encode(Function("test", listOf(Uint32(UMAX_32_VALUE)), listOf())))
            .isEqualTo("0xe3cff63400000000000000000000000000000000000000000000000000000000ffffffff")

        assertThat(encode(Function("test", listOf(Uint40(0)), listOf())))
            .isEqualTo("0xaa766a3e0000000000000000000000000000000000000000000000000000000000000000")

        assertThat(encode(Function("test", listOf(Uint40(UMAX_40_VALUE)), listOf())))
            .isEqualTo("0xaa766a3e000000000000000000000000000000000000000000000000000000ffffffffff")

        assertThat(encode(Function("test", listOf(Uint48(0)), listOf())))
            .isEqualTo("0xc129e61f0000000000000000000000000000000000000000000000000000000000000000")

        assertThat(encode(Function("test", listOf(Uint48(UMAX_48_VALUE)), listOf())))
            .isEqualTo("0xc129e61f0000000000000000000000000000000000000000000000000000ffffffffffff")

        assertThat(encode(Function("test", listOf(Uint56(0)), listOf())))
            .isEqualTo("0xa3d80f0f0000000000000000000000000000000000000000000000000000000000000000")

        assertThat(encode(Function("test", listOf(Uint56(UMAX_56_VALUE)), listOf())))
            .isEqualTo("0xa3d80f0f00000000000000000000000000000000000000000000000000ffffffffffffff")
    }

    @Test
    fun `encode uint64 to uint128`() {
        assertThat(encode(Function("test", listOf(Uint64(0)), listOf())))
            .isEqualTo("0xb0e0b9ed0000000000000000000000000000000000000000000000000000000000000000")

        assertThat(encode(Function("test", listOf(Uint64(UMAX_64_VALUE)), listOf())))
            .isEqualTo("0xb0e0b9ed000000000000000000000000000000000000000000000000ffffffffffffffff")

        assertThat(encode(Function("test", listOf(Uint128(0)), listOf())))
            .isEqualTo("0xce57f1e20000000000000000000000000000000000000000000000000000000000000000")

        assertThat(encode(Function("test", listOf(Uint128(UMAX_128_VALUE)), listOf())))
            .isEqualTo("0xce57f1e200000000000000000000000000000000ffffffffffffffffffffffffffffffff")
    }

    @Test
    internal fun `encode bool array`() {
        val empty = DynamicArray(Bool::class.java)
        assertThat(encode(Function("test", listOf(empty), listOf())))
            .isEqualTo("0xac5d4f6200000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000000")

        val trueFalse = DynamicArray(Bool::class.java, listOf(Bool(true), Bool(false)))
        assertThat(encode(Function("test", listOf(trueFalse), listOf())))
            .isEqualTo("0xac5d4f620000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000000")
    }
}
