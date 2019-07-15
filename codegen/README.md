Aion contract wrapper generation
================================

This module contains a command line interface (CLI) for the generation of 
[contract wrappers](https://docs.web3j.io/smart_contracts.html#deploying-and-interacting-with-smart-contracts)
for the Aion network. 

The CLI tool generates the Java stub to deploy and call an Aion smart contract using its ABI and a binary file.

## Usage

To generate Aion contract wrappers, run the command `web3j-aion` using the following options:

|        Flag        | Required | Description |
|:-------------------|:--------:|-------------|
| `-a`, `--abiFile`  |     ✔    | ABI file in AVM format with a contract definition. |
| `-b`, `--binFile`  |          | BIN or JAR file with the contract compiled code in order to generate deploy methods. |  
| `-o`, `--outputDir`|     ✔    | Destination base directory. |
| `-p`, `--package`  |     ✔    | Base package name. |
| `-t`, `--targetVm` |          | Target Aion virtual machine (`AVM` by default). |

### Java contracts (AVM)

The ABI and JAR for [Aion Java contracts](https://docs.aion.network/docs/contract-fundamentals), 
files can be obtained using the [aion4j Maven plugin](https://docs.aion.network/docs/maven-and-aion4j).
After a project build the `.abi` and `.jar` files will be located under the `build` directory.

The ABI file should be a text file containing the contract class name, its constructor and other function definitions:
```
0.0
com.example.dapp.ERC20Token
Clinit: (String, String, int, Address)
public static String name()
public static String symbol()
...
```

## Building the CLI

The code generator can be used with a binary distribution, running a Docker image, or calling the generator main class.

### Build a binary distribution

Run the Gradle task `distZip` to obtain a binary distribution of the `web3j-aion` executable:

```bash
./graldew distZip
```

After successful run the ZIP and TAR files will be available on `codegen/build/distributions`.  

### Using the Docker image

The [`web3labs/web3j-aion`](https://hub.docker.com/r/web3labs/web3j-aion) Docker image contains a pre-built distribution
and can be used with the following command: 

```bash
docker run web3labs/web3j-aion:latest web3j-aion --abiFile ...
```

### Using the main class

To run the code generation within your project using the `org.web3j.aion.codegen.AionGeneratorMain`,
the easiest way is to create a Gradle task of type `JavaExec` and configure the generated code source sets: 

```groovy
dependencies {
    implementation "org.web3j:core:4.4.0-SNAPSHOT"
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

Checkout the [sample repository](https://gitlab.com/web3j/web3j-aion-samples) for a fully configured Gradle project.