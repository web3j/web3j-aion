web3j Aion integration
======================

This project integrates [web3j](https://web3j.io/) with the [Aion network](https://aion.network/).
It enables the deployment and invocation of [Aion smart contracts](https://docs.aion.network/docs/contract-fundamentals)
from your Java code using [web3j contract wrappers](https://docs.web3j.io/smart_contracts.html#deploying-and-interacting-with-smart-contracts).

It is composed by the following modules:

 * [AVM](avm): Contains the encoder and decoder for interoperability with the
   [Aion Virtual Machine](https://github.com/aionnetwork/AVM).
 * [Code generation](codegen): Provides a CLI for contract wrapper generation from the ABI and binaries.
 * [Common](common): Contains common functionality like transaction signing and JSON-RPC implementation.
  
## Quick start

The API starting point is the [`org.web3j.aion.protocol.Aion`](common/src/main/kotlin/org/web3j/aion/protocol/Aion.kt) 
class. It implements the standard Ethereum JSON-RPC endpoints (`eth_call`, `ethGetBalance`, ...) with some 
[Aion-specific features](https://github.com/aionnetwork/aion/wiki/JSON-RPC-API-Docs), 
as well as the administration endpoints (`personal_NewAccount`, ...). 

To instantiate and start using it, create a service pointing to a node (http://localhost:8545 by default):

```kotlin
val service = HttpService()
```

then create an Aion instance and you start calling the API endpoints:
```kotlin
val aion = Aion.build(service)
aion.ethGetBalance("0x...", DefaultBlockParameterName.LATEST)
```

### Sending signed transactions

Transactions can be signed locally and sent with the 
[`AionTransactionManager`](common/src/main/kotlin/org/web3j/aion/tx/AionTransactionManager.kt) class.

```kotlin
val manager = AionTransactionManager(
    aion, Ed25519KeyPair("your private key")
)

// Default NRG and value
manager.sendTransaction(
    to = "0x...",
    data = "0x..."
)
```

To deploy a contract override the default values:

```kotlin
manager.sendTransaction(
    to = "0x...",
    data = "0x...",
    constructor = true,
    nrgLimit = AionConstants.NRG_CREATE_CONTRACT_DEFAULT
)
```

### Generating contract wrappers

To learn how to use the CLI to generate contract wrappers, refer to the [code generation](codegen) module.

You can also checkout the [sample repository](https://gitlab.com/web3j/web3j-aion-samples) to start with a configured 
Gradle project.

## Building and testing

To build and run the unit tests:

1. Clone this repository:
    ```bash
    git clone git@github.com:web3j/web3j-aion.git
    ```

2. Change directory to the cloned repository:
    ```bash
    cd web3j-aion
    ```

3. Run the Gradle `build` task:
    ```bash
    ./gradlew build
    ```

### Integration tests

Before running the integration tests, check that your Docker version is at least 1.6.0 and you have more than
2GB of free disk space.  To run the integration tests use the command:
```bash
./gradlew integrationTest
```
<!---
This will start an Aion node in a Docker container and run the integration tests on it:
```
        ℹ︎ Checking the system...
        ✔ Docker version should be at least 1.6.0
        ✔ Docker environment should have more than 2GB free disk space
2019-07-02 15:08:05 [Test worker] DEBUG [aionnetwork/aion:Latest] - Starting container: aionnetwork/aion:Latest
2019-07-02 15:08:05 [Test worker] DEBUG [aionnetwork/aion:Latest] - Trying to start container: aionnetwork/aion:Latest
2019-07-02 15:08:05 [Test worker] DEBUG [aionnetwork/aion:Latest] - Trying to start container: aionnetwork/aion:Latest (attempt 1/1)
2019-07-02 15:08:05 [Test worker] DEBUG [aionnetwork/aion:Latest] - Starting container: aionnetwork/aion:Latest
2019-07-02 15:08:05 [Test worker] INFO  [aionnetwork/aion:Latest] - Creating container for image: aionnetwork/aion:Latest
2019-07-02 15:08:07 [Test worker] INFO  [aionnetwork/aion:Latest] - Starting container with ID: 199b643113b66e3f7c7a5c3cad8c486b6e2d8585f61fff844b696652d04ddc01
2019-07-02 15:08:07 [Test worker] INFO  [aionnetwork/aion:Latest] - Container aionnetwork/aion:Latest is starting: 199b643113b66e3f7c7a5c3cad8c486b6e2d8585f61fff844b696652d04ddc01
2019-07-02 15:08:14 [Test worker] INFO  [aionnetwork/aion:Latest] - Container aionnetwork/aion:Latest started
```
-->

## Work in progress

2-dimensional arrays are not currently supported but we are working in a web3j release to address that.
