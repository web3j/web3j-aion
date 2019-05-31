package org.web3j.aion.abi

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.aion.avm.userlib.abi.ABIException
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.web3j.abi.FunctionEncoder.encode
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Uint
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
import org.web3j.abi.datatypes.generated.Uint104
import org.web3j.abi.datatypes.generated.Uint112
import org.web3j.abi.datatypes.generated.Uint120
import org.web3j.abi.datatypes.generated.Uint128
import org.web3j.abi.datatypes.generated.Uint136
import org.web3j.abi.datatypes.generated.Uint16
import org.web3j.abi.datatypes.generated.Uint24
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint32
import org.web3j.abi.datatypes.generated.Uint72
import org.web3j.abi.datatypes.generated.Uint8
import org.web3j.abi.datatypes.generated.Uint80
import org.web3j.abi.datatypes.generated.Uint88
import org.web3j.abi.datatypes.generated.Uint96
import org.web3j.abi.datatypes.primitive.Int
import org.web3j.abi.datatypes.primitive.Long
import org.web3j.abi.datatypes.primitive.Short
import java.math.BigInteger.ONE

class FunctionEncoderTest {

    @Test
    internal fun `encode empty parameters`() {
        assertThat(encode(Function("test", listOf(), listOf())))
            .isEqualTo("0x21000474657374")
    }

    @Test
    internal fun `encode empty string`() {
        assertThat(encode(Function("test", listOf(Utf8String("")), listOf())))
            .isEqualTo("0x21000474657374210000")
    }

    @Test
    internal fun `encode string`() {
        assertThat(encode(Function("test", listOf(Utf8String("Hello AVM")), listOf())))
            .isEqualTo("0x2100047465737421000948656c6c6f2041564d")
    }

    @Test
    internal fun `encode empty string array`() {
        val param = DynamicArray(Utf8String::class.java, listOf())

        assertThat(encode(Function("test", listOf(param), listOf())))
            .isEqualTo("0x21000b737472696e67417272617931210000")
    }

    @Test
    internal fun `encode string array`() {
        val param = DynamicArray(Utf8String::class.java, listOf(Utf8String("Hello AVM")))

        assertThat(encode(Function("test", listOf(param), listOf())))
            .isEqualTo("0x21000b737472696e6741727261793121000121000948656c6c6f2041564d")
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
    fun `encode short as short`() {
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
    fun `encode int as int`() {
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
    fun `encode long as long`() {
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
    internal fun `encode int72 to int256 throws ABI exception`() {
        assertThrows<ABIException> {
            encode(Function("test", listOf(Int72(min<kotlin.Long>(72))), listOf()))
        }
        assertThrows<ABIException> {
            encode(Function("test", listOf(Int72(max<kotlin.Long>(72))), listOf()))
        }
        assertThrows<ABIException> {
            encode(Function("test", listOf(Int256(min<kotlin.Long>(256))), listOf()))
        }
        assertThrows<ABIException> {
            encode(Function("test", listOf(Int72(max<kotlin.Long>(256))), listOf()))
        }
    }

    @Test
    internal fun `encode uint8 to uint32 as short`() {
        assertThat(encode(Function("test", listOf(Uint8(min<Byte>(true).toLong())), listOf())))
            .isEqualTo("0x21000474657374040001")

        assertThat(encode(Function("test", listOf(Uint8(max<Byte>(true).toLong())), listOf())))
            .isEqualTo("0x21000474657374040001")

        assertThat(encode(Function("test", listOf(Uint16(min<kotlin.Short>(true).toLong())), listOf())))
            .isEqualTo("0x21000474657374040001")

        assertThat(encode(Function("test", listOf(Uint16(max<kotlin.Short>(true).toLong())), listOf())))
            .isEqualTo("0x21000474657374040001")

        assertThat(encode(Function("test", listOf(Uint24(min<kotlin.Long>(24, true))), listOf())))
            .isEqualTo("0x21000474657374040001")

        assertThat(encode(Function("test", listOf(Uint24(max<kotlin.Long>(24, true))), listOf())))
            .isEqualTo("0x21000474657374040001")

        assertThat(encode(Function("test", listOf(Uint32(min<kotlin.Int>(true).toLong())), listOf())))
            .isEqualTo("0x21000474657374040001")

        assertThat(encode(Function("test", listOf(Uint32(max<kotlin.Int>(true).toLong())), listOf())))
            .isEqualTo("0x21000474657374040001")
    }

    @Test
    internal fun `encode uint40 to uint64 as int`() {
        assertThat(encode(Function("test", listOf(Int24(min<kotlin.Long>(24))), listOf())))
            .isEqualTo("0x2100047465737405ff800000")

        assertThat(encode(Function("test", listOf(Int24(max<kotlin.Long>(24))), listOf())))
            .isEqualTo("0x210004746573740500000001")

        assertThat(encode(Function("test", listOf(Int32(min<kotlin.Int>().toLong())), listOf())))
            .isEqualTo("0x210004746573740500000001")

        assertThat(encode(Function("test", listOf(Int32(max<kotlin.Int>().toLong())), listOf())))
            .isEqualTo("0x210004746573740500000001")
    }

    @Test
    internal fun `encode uint72 to uint128 as long`() {
        assertThat(encode(Function("test", listOf(Uint72(min<kotlin.Long>(72, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint72(max<kotlin.Long>(72, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint80(min<kotlin.Long>(80, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint80(max<kotlin.Long>(80, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint88(min<kotlin.Long>(88, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint88(max<kotlin.Long>(88, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint96(min<kotlin.Long>(96, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint96(max<kotlin.Long>(96, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint104(min<kotlin.Long>(104, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint104(max<kotlin.Long>(104, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint112(min<kotlin.Long>(112, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint112(max<kotlin.Long>(112, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint120(min<kotlin.Long>(120, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint120(max<kotlin.Long>(120, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint128(min<kotlin.Long>(128, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")

        assertThat(encode(Function("test", listOf(Uint128(max<kotlin.Long>(128, true))), listOf())))
            .isEqualTo("0x21000474657374060000000000000001")
    }

    @Test
    internal fun `encode uint136 to uint256 throws ABI exception`() {
        assertThrows<ABIException> {
            encode(Function("test", listOf(Uint136(min<kotlin.Long>(136))), listOf()))
        }
        assertThrows<ABIException> {
            encode(Function("test", listOf(Uint136(max<kotlin.Long>(136))), listOf()))
        }
        assertThrows<ABIException> {
            encode(Function("test", listOf(Uint256(min<kotlin.Long>(256))), listOf()))
        }
        assertThrows<ABIException> {
            encode(Function("test", listOf(Uint256(max<kotlin.Long>(256))), listOf()))
        }
    }

    @Test
    internal fun intArray() {
        val param = DynamicArray(Int32::class.java, listOf(Int32(ONE)))
        val function = Function("test", listOf(param), listOf())
        assertThat(encode(function)).isEqualTo("0x21000c696e7465676572417272617915000100000001")
    }

    @Test
    internal fun uIntArray() {
        val param = DynamicArray(Uint::class.java, listOf(Uint32(ONE)))
        val function = Function("test", listOf(param), listOf())
        assertThat(encode(function)).isEqualTo("0x21000d75496e7465676572417272617915000100000001")
    }
}
