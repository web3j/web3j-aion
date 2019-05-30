package org.web3j.aion.abi

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test
import org.web3j.abi.FunctionEncoder.encode
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Int
import org.web3j.abi.datatypes.Uint
import org.web3j.abi.datatypes.Utf8String
import java.math.BigInteger

/**
 * TODO Add tests for all web3j types covering precision loss in AVM mapping.
 */
class FunctionEncoderTest {

    @Test
    internal fun noParams() {
        val function = Function("noParams", listOf(), listOf())
        assertThat(encode(function)).isEqualTo("0x2100086e6f506172616d73")
    }

    @Test
    internal fun emptyString() {
        val function = Function("anyString", listOf(Utf8String("")), listOf())
        assertThat(encode(function)).isEqualTo("0x210009616e79537472696e67210000")
    }

    @Test
    internal fun anyString() {
        val function = Function("anyString", listOf(Utf8String("Hello AVM")), listOf())
        assertThat(encode(function)).isEqualTo("0x210009616e79537472696e6721000948656c6c6f2041564d")
    }

    @Test
    internal fun emptyStringArray() {
        val param = DynamicArray(Utf8String::class.java, listOf())
        val function = Function("stringArray", listOf(param), listOf())
        assertThat(encode(function)).isEqualTo("0x21000b737472696e67417272617931210000")
    }

    @Test
    internal fun stringArray() {
        val param = DynamicArray(Utf8String::class.java, listOf(Utf8String("Hello AVM")))
        val function = Function("stringArray", listOf(param), listOf())
        assertThat(encode(function)).isEqualTo("0x21000b737472696e6741727261793121000121000948656c6c6f2041564d")
    }

    @Test
    internal fun integer() {
        val function = Function("integer", listOf(Int(BigInteger.ONE)), listOf())
        assertThat(encode(function)).isEqualTo("0x210007696e74656765720500000001")
    }

    @Test
    internal fun integerArray() {
        val param = DynamicArray(Int::class.java, listOf(Int(BigInteger.ONE)))
        val function = Function("integerArray", listOf(param), listOf())
        assertThat(encode(function)).isEqualTo("0x21000c696e7465676572417272617915000100000001")
    }

    @Test
    internal fun uInteger() {
        val function = Function("uInteger", listOf(Int(BigInteger.ONE)), listOf())
        assertThat(encode(function)).isEqualTo("0x21000875496e74656765720500000001")
    }

    @Test
    internal fun uIntegerArray() {
        val param = DynamicArray(Uint::class.java, listOf(Uint(BigInteger.ONE)))
        val function = Function("uIntegerArray", listOf(param), listOf())
        assertThat(encode(function)).isEqualTo("0x21000d75496e7465676572417272617915000100000001")
    }

    @Test(expected = AbiEncodingException::class)
    internal fun string2DArray() {

        val param = DynamicArray(
            DynamicArray::class.java,
            DynamicArray(Utf8String::class.java, Utf8String("Hello AVM"))
        )

        val function = Function("string2DArray", listOf(param), listOf())
        encode(function)
    }
}
