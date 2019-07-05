package org.web3j.aion.abi

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.containsOnly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import org.junit.Test
import org.web3j.protocol.core.methods.response.AbiDefinition.NamedType
import java.io.File

class AbiDefinitionParserTest {

    @Test
    fun `parse empty constructor`() {
        AbiDefinitionParser.parse(
            """
            0.0
            org.aion.TestContract
            Clinit: ()
        """.trimIndent()
        ).first().apply {
            assertThat(name).isNull()
            assertThat(inputs).isEmpty()
            assertThat(outputs).isEmpty()
        }
    }

    @Test
    fun `parse non-empty constructor`() {
        AbiDefinitionParser.parse(
            """
            0.0
            org.aion.TestContract
            Clinit: (String, Address, long)
        """.trimIndent()
        ).first().apply {
            assertThat(name).isNull()
            assertThat(outputs).isEmpty()
            assertThat(inputs).containsExactly(
                NamedType(null, "string"),
                NamedType(null, "address"),
                NamedType(null, "long")
            )
        }
    }

    @Test
    fun `parse void function`() {
        AbiDefinitionParser.parse(
            """
            0.0
            org.aion.TestContract
            public static void test()
        """.trimIndent()
        ).first().apply {
            assertThat(name).isEqualTo("test")
            assertThat(inputs).isEmpty()
            assertThat(outputs).isEmpty()
        }
    }

    @Test
    fun `parse return array function`() {
        AbiDefinitionParser.parse(
            """
            0.0
            org.aion.TestContract
            public static byte[] test()
        """.trimIndent()
        ).first().apply {
            assertThat(name).isEqualTo("test")
            assertThat(inputs).isEmpty()
            assertThat(outputs).containsExactly(
                NamedType(null, "byte[]")
            )
        }
    }

    @Test
    fun `parse array parameter function`() {
        AbiDefinitionParser.parse(
            """
            0.0
            org.aion.TestContract
            public static void test(byte[])
        """.trimIndent()
        ).first().apply {
            assertThat(name).isEqualTo("test")
            assertThat(outputs).isEmpty()
            assertThat(inputs).containsExactly(
                NamedType(null, "byte[]")
            )
        }
    }

    @Test
    fun `parse non-void function`() {
        AbiDefinitionParser.parse(
            """
            0.0
            org.aion.TestContract
            public static boolean test(Address, long)
        """.trimIndent()
        ).first().apply {
            assertThat(name).isEqualTo("test")
            assertThat(inputs).containsExactly(
                NamedType(null, "address"),
                NamedType(null, "long")
            )
            assertThat(outputs).containsExactly(
                NamedType(null, "boolean")
            )
        }
    }

    @Test
    fun `parse ERC20Token ABI file`() {
        val abiFile = File(javaClass.classLoader.getResource("erc20/ERC20Token.abi")?.file ?: "")

        AbiDefinitionParser.parse(abiFile).apply {
            assertAll {
                // Constructor
                assertThat(get(0).name).isNull()
                assertThat(get(0).outputs).isEmpty()
                assertThat(get(0).inputs).containsExactly(
                    NamedType(null, "string"),
                    NamedType(null, "string"),
                    NamedType(null, "int"),
                    NamedType(null, "address")
                )

                // Function name
                assertThat(get(1).name).isEqualTo("name")
                assertThat(get(1).inputs).isEmpty()
                assertThat(get(1).outputs).containsExactly(NamedType(null, "string"))

                // Function balanceOf
                assertThat(get(5).name).isEqualTo("balanceOf")
                assertThat(get(5).inputs).containsOnly(NamedType(null, "address"))
                assertThat(get(5).outputs).containsExactly(NamedType(null, "long"))

                // Function transfer
                assertThat(get(7).name).isEqualTo("transfer")
                assertThat(get(7).inputs).containsExactly(
                    NamedType(null, "address"),
                    NamedType(null, "long")
                )
                assertThat(get(7).outputs).containsExactly(NamedType(null, "boolean"))
            }
        }
    }
}
