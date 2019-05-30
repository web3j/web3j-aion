package org.web3j.aion.abi

import org.web3j.protocol.core.methods.response.AbiDefinition
import org.web3j.protocol.core.methods.response.AbiDefinition.NamedType
import java.io.File
import java.nio.file.Files

internal object AbiDefinitionParser {

    private val FUNCTION_REGEX = "^public\\s+static\\s+(\\w+)\\s+(\\w+)\\s*\\(\\s*(.+)*\\s*\\)\$".toRegex()

    /**
     * Parses an ABI file in Aion VM format into a list of [AbiDefinition]s.
     */
    fun parse(input: String): Array<AbiDefinition> {
        return parse(input.lines())
    }

    /**
     * Parses an ABI file in Aion VM format into a list of [AbiDefinition]s.
     */
    fun parse(input: File): Array<AbiDefinition> {
        return parse(Files.readAllLines(input.toPath()))
    }

    private fun parse(lines: List<String>): Array<AbiDefinition> {
        return lines.drop(2) // Drop first line and contract name
            .map { it.toAbiDefinition() }
            .toTypedArray()
    }

    private fun String.toAbiDefinition(): AbiDefinition {
        return if (isConstructor()) {
            toConstructor()
        } else {
            toFunction()
        }
    }

    private fun String.isConstructor() = startsWith("Clinit: ")

    /**
     * Parses the named from a string `param`.
     */
    private fun String.toNamedType() = NamedType(null, toLowerCase())

    private fun String.toConstructor() = AbiDefinition().apply {

        // Remove parentheses before parsing parameters
        val parameters = substring(indexOf('('))
            .trim().drop(1).dropLast(1).toParams()

        type = AbiDefinitionType.CONSTRUCTOR.toString()
        inputs = parameters
        outputs = listOf()
        isConstant = false
        isPayable = false
    }

    private fun String.toFunction(): AbiDefinition {
        val result = FUNCTION_REGEX.matchEntire(this)!!
        return AbiDefinition().apply {
            type = AbiDefinitionType.FUNCTION.toString()
            outputs = result.groupValues[1].toReturnType()
            inputs = result.groupValues[3].toParams()
            name = result.groupValues[2]
            isConstant = false
            isPayable = false
        }
    }

    /**
     * Parses a return type or `null` if the type is `void`.
     */
    private fun String.toReturnType(): List<NamedType> {
        return if (this != "void") {
            toParams()
        } else {
            listOf()
        }
    }

    /**
     * Parses the function parameters from a string `param1, param2, ...`.
     */
    private fun String.toParams(): List<NamedType> {
        return if (isNotEmpty()) {
            split(',')
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.toNamedType() }
        } else {
            listOf()
        }
    }
}
