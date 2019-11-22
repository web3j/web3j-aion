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
package org.web3j.aion.abi

import org.web3j.abi.datatypes.Type

sealed class AbiException(message: String, cause: Throwable? = null) : Exception(message, cause)

class UnsupportedTypeException(value: String) : AbiException("Unsupported type $value") {
    constructor(value: Type<*>) : this(value.typeAsString)
    constructor(type: Class<*>) : this(type.canonicalName)
}

class UnknownTypeException(value: String) : AbiException("Unknown type $value") {
    constructor(value: Type<*>) : this(value.typeAsString)
    constructor(type: Class<*>) : this(type.canonicalName)
}
