package org.web3j.aion.crypto

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer
import org.web3j.aion.AionConstants
import org.web3j.crypto.Hash
import org.web3j.utils.Numeric
import java.math.BigInteger

/**
 * Elliptic Curve ED-25519 generated key pair.
 */
class Ed25519KeyPair private constructor(
    private val publicKeyParameters: Ed25519PublicKeyParameters,
    private val privateKeyParameters: Ed25519PrivateKeyParameters
) {
    constructor(publicKey: ByteArray, privateKey: ByteArray) : this(
        Ed25519PublicKeyParameters(publicKey, 0),
        Ed25519PrivateKeyParameters(privateKey, 0)
    )

    constructor(privateKey: ByteArray) : this(
        Ed25519PrivateKeyParameters(privateKey, 0)
    )

    constructor(publicKey: BigInteger, privateKey: BigInteger) : this(
        publicKey.toByteArray(), privateKey.toByteArray()
    )

    constructor(privateKey: String) : this(
        Numeric.hexStringToByteArray(privateKey)
    )

    constructor(publicKey: String, privateKey: String) : this(
        Numeric.hexStringToByteArray(publicKey),
        Numeric.hexStringToByteArray(privateKey)
    )

    private constructor(privateKeyParameters: Ed25519PrivateKeyParameters) : this(
        privateKeyParameters.generatePublicKey(),
        privateKeyParameters
    )

    val publicKey: ByteArray by lazy { publicKeyParameters.encoded }
    val privateKey: ByteArray by lazy { privateKeyParameters.encoded }

    val address: String by lazy {
        val pkHash = Hash.blake2b256(publicKey).slice(1..31)
        Numeric.toHexString(AionConstants.ADDRESS_ID + pkHash)
    }

    /**
     * Sign a hash with the private key of this key pair.
     *
     * @param transactionHash the hash to sign
     * @return An ED-25519 signature of the hash
     */
    fun sign(transactionHash: ByteArray): ByteArray {
        return with(Ed25519Signer()) {
            init(true, privateKeyParameters)
            update(transactionHash, 0, transactionHash.size)
            generateSignature()
        }
    }

    /**
     * Verify data with the public key of this key pair.
     *
     * @param data the data to verify
     * @param signature the signature to verify with
     * @return An ED-25519 signature of the hash
     */
    fun verify(data: ByteArray, signature: ByteArray): Boolean {
        return with(Ed25519Signer()) {
            init(false, publicKeyParameters)
            update(data, 0, data.size)
            verifySignature(signature)
        }
    }
}
