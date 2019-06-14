package org.web3j.aion.codegen

import org.web3j.aion.VirtualMachine
import org.web3j.aion.VirtualMachine.AVM
import org.web3j.aion.VirtualMachine.FVM
import org.web3j.aion.abi.AbiDefinitionParser
import org.web3j.aion.codegen.AionFunctionWrapperGenerator.CommandLineRunner
import org.web3j.aion.tx.AvmAionContract
import org.web3j.aion.tx.FvmAionContract
import org.web3j.codegen.Console.exitError
import org.web3j.codegen.SolidityFunctionWrapperGenerator
import org.web3j.protocol.core.methods.response.AbiDefinition
import org.web3j.utils.Collection.tail
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.run
import java.io.File
import java.io.IOException

private class AionFunctionWrapperGenerator constructor(
    binFile: File,
    abiFile: File,
    destinationDir: File,
    basePackageName: String,
    private val targetVm: VirtualMachine
) : SolidityFunctionWrapperGenerator(
    binFile,
    abiFile,
    destinationDir,
    basePackageName,
    true,
    when (targetVm) {
        AVM -> AvmAionContract::class.java
        FVM -> FvmAionContract::class.java
    },
    ADDRESS_BIT_LENGTH
) {
    @Throws(IOException::class)
    override fun loadContractDefinition(absFile: File): List<AbiDefinition> {
        return if (targetVm == FVM) {
            super.loadContractDefinition(absFile)
        } else {
            AbiDefinitionParser.parse(absFile)
        }
    }

    /**
     * Custom CLI interpreter to support target virtual machine and remove unneeded options.
     */
    @Command(name = "aion generate", mixinStandardHelpOptions = true, version = ["1.0"], sortOptions = false)
    internal class CommandLineRunner : Runnable {

        @Option(
            names = ["-a", "--abiFile"],
            description = ["abi file with contract definition."],
            required = true
        )
        private lateinit var abiFile: File

        @Option(
            names = ["-b", "--binFile"],
            description = ["bin file with contract compiled code " + "in order to generate deploy methods."],
            required = true
        )
        private lateinit var binFile: File

        @Option(
            names = ["-o", "--outputDir"],
            description = ["destination base directory."],
            required = true
        )
        private lateinit var destinationFileDir: File

        @Option(
            names = ["-p", "--package"],
            description = ["base package name."],
            required = true
        )
        private lateinit var packageName: String

        @Option(
            names = ["-vm", "--targetVm"],
            description = ["target Aion virtual machine."]
        )
        private val targetVm = AVM

        override fun run() {
            try {
                AionFunctionWrapperGenerator(
                    binFile, abiFile,
                    destinationFileDir, packageName, targetVm
                ).generate()
            } catch (e: Exception) {
                exitError(e)
            }
        }
    }

    companion object {
        const val ADDRESS_BIT_LENGTH = avm.Address.LENGTH * 8
    }
}

class AionFunctionWrapperMain {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val finalArgs = if (args.isNotEmpty()) {
                when (args[0]) {
                    "generate" -> tail(args)
                    "aion" -> tail(args)
                    else -> args
                }
            } else {
                args
            }
            run(CommandLineRunner(), *finalArgs)
        }
    }
}
