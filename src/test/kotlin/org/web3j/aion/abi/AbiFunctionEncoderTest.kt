package org.web3j.aion.abi

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test
import org.web3j.abi.FunctionEncoder.encode
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Int16
import org.web3j.abi.datatypes.generated.Int24
import org.web3j.abi.datatypes.generated.Int256
import org.web3j.abi.datatypes.generated.Int32
import org.web3j.abi.datatypes.generated.Int40
import org.web3j.abi.datatypes.generated.Int48
import org.web3j.abi.datatypes.generated.Int56
import org.web3j.abi.datatypes.generated.Int64
import org.web3j.abi.datatypes.generated.Int72
import org.web3j.abi.datatypes.generated.Uint16
import org.web3j.abi.datatypes.generated.Uint24
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint32
import org.web3j.abi.datatypes.generated.Uint40
import org.web3j.abi.datatypes.generated.Uint48
import org.web3j.abi.datatypes.generated.Uint56
import org.web3j.abi.datatypes.generated.Uint64
import org.web3j.abi.datatypes.generated.Uint8
import org.web3j.abi.datatypes.primitive.Boolean
import org.web3j.abi.datatypes.primitive.Char
import org.web3j.abi.datatypes.primitive.Int
import org.web3j.abi.datatypes.primitive.Long
import org.web3j.abi.datatypes.primitive.Short
import java.math.BigInteger

@ExperimentalUnsignedTypes
class AbiFunctionEncoderTest {

    @Test
    internal fun `encode empty parameters`() {
        assertThat(encode(Function("test", listOf(), listOf())))
            .isEqualTo("0x21000474657374")
    }

    @Test
    internal fun `encode string`() {
        assertThat(encode(Function("test", listOf(Utf8String("")), listOf())))
            .isEqualTo("0x21000474657374210000")

        assertThat(encode(Function("test", listOf(Utf8String("Hello AVM")), listOf())))
            .isEqualTo("0x2100047465737421000948656c6c6f2041564d")
    }

    @Test
    internal fun `encode char`() {
        assertThat(encode(Function("test", listOf(Char('a')), listOf())))
            .isEqualTo("0x21000474657374030061")
    }

    @Test
    internal fun `encode boolean`() {
        assertThat(encode(Function("test", listOf(Boolean(true)), listOf())))
            .isEqualTo("0x210004746573740201")

        assertThat(encode(Function("test", listOf(Bool(true)), listOf())))
            .isEqualTo("0x210004746573740201")
    }

    @Test
    internal fun `encode short`() {
        assertThat(encode(Function("test", listOf(Short(min())), listOf())))
            .isEqualTo("0x21000474657374048000")

        assertThat(encode(Function("test", listOf(Short(max())), listOf())))
            .isEqualTo("0x21000474657374047fff")
    }

    @Test
    internal fun `encode int16 as short`() {
        assertThat(encode(Function("test", listOf(Int16(min<kotlin.Short>().toLong())), listOf())))
            .isEqualTo("0x21000474657374048000")

        assertThat(encode(Function("test", listOf(Int16(max<kotlin.Short>().toLong())), listOf())))
            .isEqualTo("0x21000474657374047fff")
    }

    @Test
    internal fun `encode int as int`() {
        assertThat(encode(Function("test", listOf(Int(min())), listOf())))
            .isEqualTo("0x210004746573740580000000")

        assertThat(encode(Function("test", listOf(Int(max())), listOf())))
            .isEqualTo("0x21000474657374057fffffff")
    }

    @Test
    internal fun `encode int24 to int32 as int`() {
        assertThat(encode(Function("test", listOf(Int24(min<kotlin.Long>(24))), listOf())))
            .isEqualTo("0x2100047465737405ff800000")

        assertThat(encode(Function("test", listOf(Int24(max<kotlin.Long>(24))), listOf())))
            .isEqualTo("0x2100047465737405007fffff")

        assertThat(encode(Function("test", listOf(Int32(min<kotlin.Int>().toLong())), listOf())))
            .isEqualTo("0x210004746573740580000000")

        assertThat(encode(Function("test", listOf(Int32(max<kotlin.Int>().toLong())), listOf())))
            .isEqualTo("0x21000474657374057fffffff")
    }

    @Test
    internal fun `encode long`() {
        assertThat(encode(Function("test", listOf(Long(1)), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")
    }

    @Test
    internal fun `encode int40 to int64 as long`() {
        assertThat(encode(Function("test", listOf(Int40(min<kotlin.Long>(40))), listOf())))
            .isEqualTo("0x2100047465737406ffffff8000000000")

        assertThat(encode(Function("test", listOf(Int40(max<kotlin.Long>(40))), listOf())))
            .isEqualTo("0x21000474657374060000007fffffffff")

        assertThat(encode(Function("test", listOf(Int48(min<kotlin.Long>(48))), listOf())))
            .isEqualTo("0x2100047465737406ffff800000000000")

        assertThat(encode(Function("test", listOf(Int48(max<kotlin.Long>(48))), listOf())))
            .isEqualTo("0x210004746573740600007fffffffffff")

        assertThat(encode(Function("test", listOf(Int56(min<kotlin.Long>(56))), listOf())))
            .isEqualTo("0x2100047465737406ff80000000000000")

        assertThat(encode(Function("test", listOf(Int56(max<kotlin.Long>(56))), listOf())))
            .isEqualTo("0x2100047465737406007fffffffffffff")

        assertThat(encode(Function("test", listOf(Int64(min<kotlin.Long>())), listOf())))
            .isEqualTo("0x21000474657374068000000000000000")

        assertThat(encode(Function("test", listOf(Int64(max<kotlin.Long>())), listOf())))
            .isEqualTo("0x21000474657374067fffffffffffffff")
    }

    @Test
    internal fun `encode int72 to int256 as byte array`() {
        assertThat(encode(Function("test", listOf(Int72(min<kotlin.Long>(72))), listOf())))
            .isEqualTo("0x210004746573741100088000000000000000")

        assertThat(encode(Function("test", listOf(Int72(max<kotlin.Long>(72))), listOf())))
            .isEqualTo("0x210004746573741100087ffffffffffffffe")

        assertThat(encode(Function("test", listOf(Int256(min<kotlin.Long>(256))), listOf())))
            .isEqualTo("0x210004746573741100088000000000000000")

        assertThat(encode(Function("test", listOf(Int72(max<kotlin.Long>(256))), listOf())))
            .isEqualTo("0x210004746573741100087ffffffffffffffe")
    }

    @Test
    internal fun `encode uint8 short`() {
        assertThat(encode(Function("test", listOf(Uint8(0)), listOf())))
            .isEqualTo("0x21000474657374040000")

        assertThat(encode(Function("test", listOf(Uint8(max<kotlin.Short>(8, true).toLong())), listOf())))
            .isEqualTo("0x210004746573740400ff")
    }

    @Test
    internal fun `encode uint16 to uint24 as int`() {
        assertThat(encode(Function("test", listOf(Uint16(0)), listOf())))
            .isEqualTo("0x210004746573740500000000")

        assertThat(encode(Function("test", listOf(Uint16(max<kotlin.Int>(16, true).toLong())), listOf())))
            .isEqualTo("0x21000474657374050000ffff")

        assertThat(encode(Function("test", listOf(Uint24(0)), listOf())))
            .isEqualTo("0x210004746573740500000000")

        assertThat(encode(Function("test", listOf(Uint24(max<kotlin.Int>(24, true).toLong())), listOf())))
            .isEqualTo("0x210004746573740500ffffff")
    }

    @Test
    internal fun `encode uint32 to uint56 as long`() {
        assertThat(encode(Function("test", listOf(Uint32(0)), listOf())))
            .isEqualTo("0x21000474657374060000000000000000")

        assertThat(encode(Function("test", listOf(Uint32(max<kotlin.Long>(32, true))), listOf())))
            .isEqualTo("0x210004746573740600000000ffffffff")

        assertThat(encode(Function("test", listOf(Uint40(0)), listOf())))
            .isEqualTo("0x21000474657374060000000000000000")

        assertThat(encode(Function("test", listOf(Uint40(max<kotlin.Long>(40, true))), listOf())))
            .isEqualTo("0x2100047465737406000000ffffffffff")

        assertThat(encode(Function("test", listOf(Uint48(0)), listOf())))
            .isEqualTo("0x21000474657374060000000000000000")

        assertThat(encode(Function("test", listOf(Uint48(max<kotlin.Long>(48, true))), listOf())))
            .isEqualTo("0x21000474657374060000ffffffffffff")

        assertThat(encode(Function("test", listOf(Uint56(0)), listOf())))
            .isEqualTo("0x21000474657374060000000000000000")

        assertThat(encode(Function("test", listOf(Uint56(max<kotlin.Long>(56, true))), listOf())))
            .isEqualTo("0x210004746573740600ffffffffffffff")
    }

    @Test
    internal fun `encode uint64 to uint256 as byte array`() {
        assertThat(encode(Function("test", listOf(Uint64(0)), listOf())))
            .isEqualTo("0x2100047465737411000100")

        assertThat(encode(Function("test", listOf(Uint64(max<BigInteger>(64, true))), listOf())))
            .isEqualTo("0x210004746573741100087ffffffffffffffe")

        assertThat(encode(Function("test", listOf(Uint256(0)), listOf())))
            .isEqualTo("0x2100047465737411000100")

        assertThat(encode(Function("test", listOf(Uint256(max<BigInteger>(256, true))), listOf())))
            .isEqualTo("0x210004746573741100087ffffffffffffffe")
    }
}
