# web3j-aion

Integrates [web3j](https://web3j.io/) with the [Aion network](https://aion.network/).
Typically the use case is generating of contract wrappers from Java or Solidity (not supported yet)
and 

## Modules

 * [AVM](avm): Contains the encoder and decoder for interoperability with the Aion Virtual Machine.
 * [FVM](avm): Aims to provide support for Aion Fast VM (FVM) contracts (coming soon).
 * [Code generation](codegen): Provides a CLI for contract wrapper generation from the ABI and binaries.
 * [Common](common): Contains common functionality like transaction signing and JSON-RPC implementation.
  
## Build and test

```bash
./gradlew build
```

## Run integration tests

```bash
./gradlew integrationTest
```
