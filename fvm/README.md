Integration with the Aion Fast VM
=================================

This module contains the Service Provider Interface (SPI) implementation of the web3j 
[encoder](src/main/kotlin/org/web3j/aion/abi/fvm/AbiFunctionEncoder.kt) and 
[decoder](src/main/kotlin/org/web3j/aion/abi/fvm/AbiFunctionDecoder.kt) providers.

The FVM implementation is *under development*, so cannot be used for now.

## Building and testing

To build and run the tests use the command:

```bash
./gradlew build
```

## Running the integration tests

Before running the [integration tests](src/integration-test/kotlin/org/web3j/aion/protocol/FvmIntegrationTest.kt), 
check that your Docker version is at least 1.6.0 and you have more than 2GB of free disk space. 

To run the integration tests use the command:

```bash
./gradlew integrationTest
```

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
