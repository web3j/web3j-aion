package org.web3j.aion.abi

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.AssignExpr
import com.github.javaparser.ast.expr.Expression
import com.github.javaparser.ast.expr.MarkerAnnotationExpr
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.NameExpr
import com.github.javaparser.ast.expr.UnaryExpr
import com.github.javaparser.ast.nodeTypes.NodeWithParameters
import com.github.javaparser.ast.nodeTypes.NodeWithType
import com.github.javaparser.ast.stmt.BlockStmt
import com.github.javaparser.ast.stmt.ExpressionStmt
import com.github.javaparser.ast.stmt.ReturnStmt
import com.github.javaparser.ast.stmt.Statement
import com.github.javaparser.ast.type.VoidType
import org.aion.avm.tooling.abi.Callable
import org.web3j.aion.abi.AbiDefinitionType.FUNCTION
import org.web3j.protocol.core.methods.response.AbiDefinition
import org.web3j.protocol.core.methods.response.AbiDefinition.NamedType
import java.io.File
import java.io.InputStream
import java.nio.file.Path

/**
 *  Parses Aion Java smart contracts into Solidity ABI definitions.
 *
 * TODO Parse static block initializers for contract constructor.
 */
class AbiDefinitionParser {

    fun parse(input: String): List<AbiDefinition> {
        return parse(StaticJavaParser.parse(input))
    }

    fun parse(input: InputStream): List<AbiDefinition> {
        return parse(StaticJavaParser.parse(input))
    }

    fun parse(input: File): List<AbiDefinition> {
        return parse(StaticJavaParser.parse(input))
    }

    fun parse(input: Path): List<AbiDefinition> {
        return parse(StaticJavaParser.parse(input))
    }

    private fun parse(unit: CompilationUnit): List<AbiDefinition> {
        return unit.findAll(ClassOrInterfaceDeclaration::class.java)
            .filter { it.isSmartContract }
            .flatMap { it.callables.map { m -> m.toAbiDefinition() } }
    }

    private val ClassOrInterfaceDeclaration.isSmartContract: Boolean
        get() = !isInterface && isPublic && !isAbstract

    private val ClassOrInterfaceDeclaration.callables: List<MethodDeclaration>
        get() = methods.filter {
            it.isPublic && it.isStatic && it.annotations.contains(callableAnnotation)
        }

    private fun MethodDeclaration.toAbiDefinition() =
        AbiDefinition().let {
            it.type = FUNCTION.toString()
            it.isConstant = isConstant
            it.name = nameAsString
            it.outputs = outputs
            it.inputs = inputs
            it.isPayable = false
            it
        }

    private val NodeWithParameters<*>.inputs: List<NamedType>
        get() = parameters.map {
            NamedType(it.nameAsString, it.type.toString().toLowerCase())
        }

    private val NodeWithType<*, *>.outputs: List<NamedType>
        get() = when (type) {
            is VoidType -> listOf()
            else -> listOf(NamedType(null, type.toString().toLowerCase()))
        }

    private val MethodDeclaration.isConstant: Boolean
        get() = !body.isPresent || declaringClass.staticFields.let { fields ->
            body.get().findAll(Expression::class.java) {
                it.isAssignExpr || it.isUnaryExpr
            }.all {
                fields.none { field -> it.modifies(field) }
            } && body.get().findAll(Statement::class.java) {
                true
            }.all {
                fields.none { field -> it.modifies(field) }
            }
        }

    private val MethodDeclaration.declaringClass: ClassOrInterfaceDeclaration
        get() = parentNode.get() as ClassOrInterfaceDeclaration

    private fun Expression.modifies(field: FieldDeclaration): Boolean {
        return field.variables.first().nameAsString.let {
            when (this) {
                is AssignExpr -> it == target.asNameExpr().nameAsString
                is MethodCallExpr -> modifies(field)
                is UnaryExpr -> modifies(field)
                else -> false
            }
        }
    }

    private fun Statement.modifies(field: FieldDeclaration): Boolean {
        return when (this) {
            is ExpressionStmt -> expression.modifies(field)
            is BlockStmt -> asBlockStmt().statements.any { it.modifies(field) }
            is ReturnStmt -> expression.map { it.modifies(field) }.orElse(false)
            else -> false
        }
    }

    private val ClassOrInterfaceDeclaration.staticFields: List<FieldDeclaration>
        get() = fields.filter { it.isStatic }

    private fun UnaryExpr.modifies(field: FieldDeclaration): Boolean {
        return field.variables.first().nameAsString.let {
            when (expression) {
                is NameExpr -> it == expression.asNameExpr().nameAsString
                is MethodCallExpr -> expression.asMethodCallExpr().modifies(field)
                else -> false
            }
        }
    }

    private fun MethodCallExpr.modifies(field: FieldDeclaration): Boolean {
        return field.variables.first().nameAsString == scopeName && name.asString() in modifyMethods
    }

    private val MethodCallExpr.scopeName: String?
        get() = asMethodCallExpr().scope.map {
            when (it) {
                is NameExpr -> it.asNameExpr().nameAsString
                is MethodCallExpr -> it.scopeName // Chained method call
                else -> null
            }
        }.orElse(null)

    private class NamedTypeMixin(@field:JsonIgnore var indexed: Boolean)

    companion object {

        private val callableAnnotation = MarkerAnnotationExpr(Callable::class.java.simpleName)

        private val mapper = ObjectMapper()
            .addMixIn(NamedType::class.java, NamedTypeMixin::class.java)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)

        private val modifyMethods = listOf(
            "compute", "computeIfPresent", "clear",
            "computeIfAbsent", "forEach", "merge",
            "put", "putAll", "putIfAbsent", "remove",
            "replace", "replaceAll"
        )

        fun serialize(definitions: List<AbiDefinition>, indent: Boolean = false): String {
            return mapper.configure(SerializationFeature.INDENT_OUTPUT, indent)
                .writeValueAsString(definitions)
        }

        @JvmStatic
        fun main(args: Array<String>) {
        }
    }
}
