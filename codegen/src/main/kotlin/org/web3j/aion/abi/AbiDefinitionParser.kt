package org.web3j.aion.abi

import org.web3j.protocol.core.methods.response.AbiDefinition
import org.web3j.protocol.core.methods.response.AbiDefinition.NamedType
import java.io.File
import java.nio.file.Files

internal object AbiDefinitionParser {

    /**
     * Regular expression matching function definitions in Aion ABI format, e.g.:
     *
     *    public static boolean transferFrom(Address, Address, long)
     */
    private val FUNCTION_REGEX = "^public\\s+static\\s+([\\w\\[\\]]+)\\s+(\\w+)\\s*\\(\\s*(.+)*\\s*\\)\$".toRegex()

    /**
     * Parses an ABI string in Aion VM format into a list of [AbiDefinition]s.
     */
    fun parse(input: String): List<AbiDefinition> {
        return parse(input.lines())
    }

    /**
     * Parses an ABI file in Aion VM format into a list of [AbiDefinition]s.
     */
    fun parse(input: File): List<AbiDefinition> {
        return parse(Files.readAllLines(input.toPath()))
    }

    private fun parse(lines: List<String>): List<AbiDefinition> {
        return lines.drop(2) // Drop first line and contract name
            .map { it.toAbiDefinition() }
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
        val parameters = substring(indexOf('(').inc()).trim().dropLast(1)

        type = AbiDefinitionType.CONSTRUCTOR.toString()
        inputs = parameters.toInputs()
        outputs = listOf()
        isConstant = false
        isPayable = false
    }

    private fun String.toFunction(): AbiDefinition {
        val result = FUNCTION_REGEX.matchEntire(this)
            ?: throw AbiDefinitionException("Invalid function definition: $this")

        return AbiDefinition().apply {
            type = AbiDefinitionType.FUNCTION.toString()
            outputs = result.groupValues[1].toOutputs()
            inputs = result.groupValues[3].toInputs()
            name = result.groupValues[2]
            isConstant = true
            isPayable = false
        }
    }

    /**
     * Parses a return type or an empty list if the type is `void`.
     */
    private fun String.toOutputs(): List<NamedType> {
        return if (this != "void") {
            listOf(toNamedType())
        } else {
            listOf()
        }
    }

    /**
     * Parses the function parameters from a string `param1, param2, ...`.
     */
    private fun String.toInputs(): List<NamedType> {
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

class AbiDefinitionException(message: String, cause: Throwable? = null) : Exception(message, cause)
