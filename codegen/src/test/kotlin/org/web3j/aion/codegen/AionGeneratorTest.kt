/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.aion.codegen

import assertk.assertThat
import assertk.assertions.exists
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class AionGeneratorTest {

    @Test
    fun `generate contract wrapper`(@TempDir tmpFolder: Path) {

        val abiFile = javaClass.classLoader.getResource("erc20/ERC20Token.abi")
        val binFile = javaClass.classLoader.getResource("erc20/ERC20Token.jar")

        AionGeneratorMain.main(
                "--outputDir", tmpFolder.toString(),
                "--abiFile", abiFile?.file ?: "",
                "--binFile", binFile?.file ?: "",
                "--package", "erc20",
                "--targetVm", "AVM"
        )

        val expectedCode = javaClass.classLoader.getResource("erc20/ERC20Token.java")
        val actualCode = Paths.get(tmpFolder.toString(), "erc20", "ERC20Token.java")

        assertThat(actualCode.toFile()).exists()
        assertThat(Files.readString(actualCode)).isEqualTo(
                Files.readString(Paths.get(expectedCode?.file ?: ""))
        )
    }
}
