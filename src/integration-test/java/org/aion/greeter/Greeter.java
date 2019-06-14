package org.aion.greeter;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.aion.tx.FvmAionContract;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.4.0-SNAPSHOT.
 */
public class Greeter extends FvmAionContract {
    private static final String BINARY = "605060405234156100105760006000fd5b604051610469380380610469833981016040528080518201919060100150505b5b3360006000508282909180600101839055555050505b8060026000509080519060100190610060929190610068565b505b5061011a565b8280546001816001161561010002031660029004906000526010600020905090600f016010900481019282600f106100ab57805160ff19168380011785556100de565b828001600101855582156100de579182015b828111156100dd57825182600050909055916010019190600101906100bd565b5b5090506100eb91906100ef565b5090565b61011791906100f9565b8082111561011357600081815060009055506001016100f9565b5090565b90565b610340806101296000396000f30060506040526000356c01000000000000000000000000900463ffffffff16806341c0e1b5146100495780634ac0d66e1461005f578063cfae3217146100c157610043565b60006000fd5b34156100555760006000fd5b61005d610151565b005b341561006b5760006000fd5b6100bf6004808035906010019082018035906010019191908080600f01601080910402601001604051908101604052809392919081815260100183838082843782019150505050505090909190505061017b565b005b34156100cd5760006000fd5b6100d5610199565b6040518080601001828103825283818151815260100191508051906010019080838360005b838110156101165780820151818401525b6010810190506100fa565b50505050905090810190600f1680156101435780820380516001836010036101000a031916815260100191505b509250505060405180910390f35b60006000508060010154905433909114919014161561017857600060005080600101549054ff5b5b565b806002600050908051906010019061019492919061024b565b505b50565b6101a16102d2565b60026000508054600181600116156101000203166002900480600f01601080910402601001604051908101604052809291908181526010018280546001816001161561010002031660029004801561023c5780600f1061020f5761010080835404028352916010019161023c565b8201919060005260106000209050905b81548152906001019060100180831161021f57829003600f168201915b50505050509050610248565b90565b8280546001816001161561010002031660029004906000526010600020905090600f016010900481019282600f1061028e57805160ff19168380011785556102c1565b828001600101855582156102c1579182015b828111156102c057825182600050909055916010019190600101906102a0565b5b5090506102ce91906102e9565b5090565b601060405190810160405280600081526010015090565b61031191906102f3565b8082111561030d57600081815060009055506001016102f3565b5090565b905600a165627a7a7230582010c49411e467ff996841c65bb3ae4244ecff67c33ff42a4eccf3c27d5e09a1ba0029";

    public static final String FUNC_KILL = "kill";

    public static final String FUNC_NEWGREETING = "newGreeting";

    public static final String FUNC_GREET = "greet";

    @Deprecated
    protected Greeter(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Greeter(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Greeter(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Greeter(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> kill() {
        final Function function = new Function(
                FUNC_KILL,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> newGreeting(String _greeting) {
        final Function function = new Function(
                FUNC_NEWGREETING,
                Arrays.<Type>asList(new Utf8String(_greeting)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> greet() {
        final Function function = new Function(FUNC_GREET,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static Greeter load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Greeter(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Greeter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Greeter(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Greeter load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Greeter(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Greeter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Greeter(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Greeter> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _greeting) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_greeting)));
        return deployRemoteCall(Greeter.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Greeter> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _greeting) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_greeting)));
        return deployRemoteCall(Greeter.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Greeter> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _greeting) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_greeting)));
        return deployRemoteCall(Greeter.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Greeter> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _greeting) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_greeting)));
        return deployRemoteCall(Greeter.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
