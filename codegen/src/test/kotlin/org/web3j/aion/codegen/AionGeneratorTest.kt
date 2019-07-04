package org.web3j.aion.codegen

import assertk.assertThat
import assertk.assertions.exists
import assertk.assertions.isEqualTo
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.nio.file.Files
import java.nio.file.Paths

class AionGeneratorTest {

    @Rule
    @JvmField
    val tmpFolder = TemporaryFolder()

    @Test
    fun `generate contract wrapper`() {

        val abiFile = javaClass.classLoader.getResource("erc20/ERC20Token.abi")
        val binFile = javaClass.classLoader.getResource("erc20/ERC20Token.jar")

        AionGeneratorMain.main(
            "--outputDir", tmpFolder.root.absolutePath,
            "--abiFile", abiFile?.file ?: "",
            "--binFile", binFile?.file ?: "",
            "--package", "erc20",
            "--targetVm", "AVM"
        )

        val expectedCode = javaClass.classLoader.getResource("erc20/ERC20Token.java")
        val actualCode = Paths.get(tmpFolder.root.absolutePath, "erc20", "ERC20Token.java")

        assertThat(actualCode.toFile()).exists()
        assertThat(Files.readString(actualCode)).isEqualTo(
            Files.readString(Paths.get(expectedCode?.file ?: ""))
        )
    }
}
