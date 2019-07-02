web3j-aion Common module  
========================

This module contains the implementation for:

* Aion-specific [JSON-RCP](src/main/kotlin/org/web3j/aion/protocol/JsonRpc2_0Aion.kt) implementation. 
* [Transaction signing](src/main/kotlin/org/web3j/aion/tx/AionTransactionManager.kt) using 
  [ED-25519](src/main/kotlin/org/web3j/aion/crypto/Ed25519KeyPair.kt) elliptic curve signature.
* Aion smart contract wrapper [base class](src/main/kotlin/org/web3j/aion/tx/AionContract.kt) to be extended bu generated wrappers.
* Adding Energy (NRG) [extension fields](src/main/kotlin/org/web3j/aion/AionExtensions.kt) to web3j transaction objects.
* Integration test [base class](src/integration-test/kotlin/org/web3j/aion/protocol/AionIntegrationTest.kt) 
  used for AVM and FVM to start an Aion node.

