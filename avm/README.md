Integration with the Aion VM
============================

This module contains the Service Provider Interface (SPI) implementation of the web3j 
[encoder](src/main/kotlin/org/web3j/aion/abi/avm/AbiFunctionEncoder.kt) and 
[decoder](src/main/kotlin/org/web3j/aion/abi/avm/AbiFunctionDecoder.kt) providers.

The library `web3j-aion-avm`, produced by this module, **must** be present in your 
classpath when running an Aion Java contract wrapper. Otherwise, the encoding and decoding 
won't work properly.

## Building and testing

To build and run the tests use the command:

```bash
./gradlew build
```

### Running the integration tests

Before running the [integration tests](src/integration-test/kotlin/org/web3j/aion/protocol/AvmIntegrationTest.kt), 
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
2019-07-02 15:08:05 [Test worker] DEBUG [aionnetwork/aion:latest] - Starting container: aionnetwork/aion:Latest
2019-07-02 15:08:05 [Test worker] DEBUG [aionnetwork/aion:latest] - Trying to start container: aionnetwork/aion:Latest
2019-07-02 15:08:05 [Test worker] DEBUG [aionnetwork/aion:latest] - Trying to start container: aionnetwork/aion:Latest (attempt 1/1)
2019-07-02 15:08:05 [Test worker] DEBUG [aionnetwork/aion:latest] - Starting container: aionnetwork/aion:Latest
2019-07-02 15:08:05 [Test worker] INFO  [aionnetwork/aion:latest] - Creating container for image: aionnetwork/aion:Latest
2019-07-02 15:08:07 [Test worker] INFO  [aionnetwork/aion:latest] - Starting container with ID: 199b643113b66e3f7c7a5c3cad8c486b6e2d8585f61fff844b696652d04ddc01
2019-07-02 15:08:07 [Test worker] INFO  [aionnetwork/aion:latest] - Container aionnetwork/aion:Latest is starting: 199b643113b66e3f7c7a5c3cad8c486b6e2d8585f61fff844b696652d04ddc01
2019-07-02 15:08:14 [Test worker] INFO  [aionnetwork/aion:latest] - Container aionnetwork/aion:Latest started
```

### Building a shaded JAR

The project provides a Gradle task to create a shaded JAR with all the required dependencies to run your 
Aion AVM contract wrappers. Running the command:

```bash
./gradlew shadowJar
```

will produce a ~10MB JAR file named `web3j-aion-avm-<version>-all.jar` located in the `build/libs` 
directory of this module. You can use it by simply adding it to your classpath:

```bash
java -cp .:web3j-aion-avm-<version>-all.jar ... // Add here your Java main class
```

Note that this is not required within a Gradle or Maven project, since the project dependencies will be resolved 
automatically.

For instance, having the shaded JAR in your classpath is equivalent to a Gradle file with this dependency configuration: 

```groovy
dependencies {
    implementation "org.web3j:core:4.5.0"
    implementation "org.web3j:web3j-aion-avm:0.1.0"

    implementation "org.aion:avm-api:1.4"
    implementation "org.aion:avm-tooling:1.4"
    implementation "org.aion:avm-userlib:1.4"
}
```
