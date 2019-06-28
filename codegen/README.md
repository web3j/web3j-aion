Aion contract wrapper generation
================================

This module contains a command line interface (CLI) for the generation of 
[web3j contract wrappers](https://docs.web3j.io/smart_contracts.html#deploying-and-interacting-with-smart-contracts)
from the contract ABI and a binary file with the compiled code of the contract.

## AVM - Java contracts

For [Aion Java contracts](https://docs.aion.network/docs/contract-fundamentals), 
the ABI and JAR files can be obtained using the 
[aion4j Maven plugin](https://docs.aion.network/docs/maven-and-aion4j).
After a project build the `.abi` and `.jar` files will be located under the `build` directory.

The ABI file should be a text file containing the contract class, its constructor and other functions:
```
0.0
com.example.dapp.ERC20Token
Clinit: (String, String, int, Address)
public static String name()
public static String symbol()
...
```

The JAR should be a normal JAR file.

## FVM - Solidity contracts

TBD

## Usage

| Flag               | Description |
|:-------------------|-------------|
| `-a`, `--abiFile`  | ABI file in FVM or AVM format with a contract definition. |  
| `-b`, `--binFile`  | BIN or JAR file with the contract compiled code in order to generate deploy methods. |  
| `-o`, `--outputDir`| Destination base directory. |    
| `-p`, `--package`  | Base package name. |  
| `-t`, `--targetVm` | Target Aion virtual machine |   

## Running the CLI 

The code generator can be used in two ways: with a binary distribution that will run an executable file,
or directly calling the CLI class `org.web3j.aion.codegen.AionGeneratorMain`.

### Build a binary distribution

1. In a terminal run the command:
    
    ```bash
    ./graldew distZip
    ```

2. Extract the downloaded Zip or Tar file created in `codegen/build/distributions`:

    ```bash
    unzip web3j-aion.zip
    ``` 

 3. Move into the created folder and run the command:

    ```bash
    bin/aion-web3j \
        --abiFile path/to/contract.abi \
        --binFile path/to/contract.jar \
        --outputDir path/to/generated/source \
        --package com.example.dapp \
        --targetVm AVM
    ```
    This will output the following:

    ```
    Generating org.web3j.aion.samples.generated.HelloAvm 
    ...
    File written to /path/to/web3j-aion-samples/src/main/java
    
    ```
    The generated file will be located in the project structure under `src/main/java`.

### Automate the code generation

To run the code generation within your project 

```groovy
dependencies {
        implementation "org.web3j:web3j-aion-avm:..."
        implementation "org.web3j:web3j-aion-codegen:..."
}

sourceSets {
    main {
        java {
            srcDir {
                // Add the generated code to main source set
                "path/to/generated/source"
            }
        }
    }
}

task generateContractWrappers(type: JavaExec, group: 'aion', dependsOn: 'clean') {
    classpath = sourceSets.main.runtimeClasspath
    main = 'org.web3j.aion.codegen.AionGeneratorMain'
    
    args '--abiFile', "path/to/contract.abi",
            '--binFile', "path/to/contract.jar",
            '--outputDir', "path/to/generated/source",
            '--package', 'com.example.dapp',
            '--targetVm', 'AVM' // By default
}

compileJava {
    source "path/to/generated/source"
}

compileTestJava {
    source "path/to/generated/source"
    dependsOn 'generateContractWrappers'
}
``` 