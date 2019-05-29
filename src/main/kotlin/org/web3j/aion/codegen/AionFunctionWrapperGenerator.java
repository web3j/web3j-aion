package org.web3j.aion.codegen;

import avm.Address;
import org.web3j.codegen.SolidityFunctionWrapperGenerator;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.utils.Collection.tail;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

public class AionFunctionWrapperGenerator extends SolidityFunctionWrapperGenerator {

    private static final int ADDRESS_BIT_LENGTH = Address.LENGTH * 8;

    private enum VirtualMachine {
        AVM, FVM
    }

    private final VirtualMachine targetVm;

    private AionFunctionWrapperGenerator(
            File binFile,
            File abiFile,
            File destinationDir,
            String basePackageName,
            VirtualMachine targetVm) {

        super(binFile, abiFile, destinationDir, basePackageName,
                true, ADDRESS_BIT_LENGTH);

        this.targetVm = targetVm;
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("aion")) {
            args = tail(args);
        }

        if (args.length > 0 && args[0].equals("generate")) {
            args = tail(args);
        }

        CommandLine.run(new CommandLineRunner(), args);
    }

    @Override
    protected List<AbiDefinition> loadContractDefinition(File absFile) throws IOException {
        if (targetVm == VirtualMachine.FVM) {
            return super.loadContractDefinition(absFile);
        } else {
            // TODO Aion ABI format parsing
            return Collections.emptyList();
        }
    }

    /**
     * Custom CLI interpreter to support target virtual machine and remove unneeded options.
     */
    @Command(name = "aion generate", mixinStandardHelpOptions = true, version = "1.0", sortOptions = false)
    private static class CommandLineRunner implements Runnable {

        @Option(names = {"-a", "--abiFile"},
                description = "abi file with contract definition.",
                required = true)
        private File abiFile;

        @Option(names = {"-b", "--binFile"},
                description = "bin file with contract compiled code "
                        + "in order to generate deploy methods.",
                required = true)
        private File binFile;

        @Option(names = {"-o", "--outputDir"},
                description = "destination base directory.",
                required = true)
        private File destinationFileDir;

        @Option(names = {"-p", "--package"},
                description = "base package name.",
                required = true)
        private String packageName;

        @Option(names = {"-vm", "--targetVm"},
                description = "target Aion virtual machine.")
        private VirtualMachine targetVm = VirtualMachine.AVM;

        @Override
        public void run() {
            try {
                new AionFunctionWrapperGenerator(binFile, abiFile,
                        destinationFileDir, packageName, targetVm).generate();
            } catch (Exception e) {
                exitError(e);
            }
        }
    }
}
