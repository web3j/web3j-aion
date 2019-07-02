web3j Aion integration
======================

This project integrates [web3j](https://web3j.io/) with the [Aion network](https://aion.network/).
It allows generating Aion contract wrappers from Java or Solidity (not supported yet) and
calling them  

It is composed by the following modules:

 * [AVM](avm): Contains the encoder and decoder for interoperability with the Aion Virtual Machine.
 * [FVM](fvm): Aims to provide support for Aion Fast VM (FVM) contracts (coming soon).
 * [Code generation](codegen): Provides a CLI for contract wrapper generation from the ABI and binaries.
 * [Common](common): Contains common functionality like transaction signing and JSON-RPC implementation.
  
## Building and testing

To build and run the tests use the command:

```bash
./gradlew build
```


:whale:

## Running the integration tests

Before running the integration tests, check that your Docker version is at least 1.6.0 and you have more than
2GB of free disk space. 

To run the integration tests use the command:

```bash
./gradlew integrationTest
```

This will start an Aion node in a Docker container and run the integration tests on it:

```
ℹ︎ Checking the system...
        ✔ Docker version should be at least 1.6.0
        ✔ Docker environment should have more than 2GB free disk space
2019-07-02 15:08:05 [Test worker] DEBUG :whale: [aionnetwork/aion:Latest] - Starting container: aionnetwork/aion:Latest
2019-07-02 15:08:05 [Test worker] DEBUG :whale: [aionnetwork/aion:Latest] - Trying to start container: aionnetwork/aion:Latest
2019-07-02 15:08:05 [Test worker] DEBUG :whale: [aionnetwork/aion:Latest] - Trying to start container: aionnetwork/aion:Latest (attempt 1/1)
2019-07-02 15:08:05 [Test worker] DEBUG :whale: [aionnetwork/aion:Latest] - Starting container: aionnetwork/aion:Latest
2019-07-02 15:08:05 [Test worker] INFO  :whale: [aionnetwork/aion:Latest] - Creating container for image: aionnetwork/aion:Latest
2019-07-02 15:08:07 [Test worker] INFO  :whale: [aionnetwork/aion:Latest] - Starting container with ID: 199b643113b66e3f7c7a5c3cad8c486b6e2d8585f61fff844b696652d04ddc01
2019-07-02 15:08:07 [Test worker] INFO  :whale: [aionnetwork/aion:Latest] - Container aionnetwork/aion:Latest is starting: 199b643113b66e3f7c7a5c3cad8c486b6e2d8585f61fff844b696652d04ddc01
2019-07-02 15:08:14 [Test worker] INFO  :whale: [aionnetwork/aion:Latest] - Container aionnetwork/aion:Latest started
```
