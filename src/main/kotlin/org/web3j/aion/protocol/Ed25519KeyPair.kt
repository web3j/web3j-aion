package org.web3j.aion.protocol

import org.bouncycastle.asn1.x9.X9ECParameters
import org.bouncycastle.crypto.ec.CustomNamedCurves
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer
import org.web3j.crypto.ECDSASignature
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Sign.publicKeyFromPrivate
import java.math.BigInteger

/**
 * Elliptic Curve ED-25519 generated key pair.
 */
class Ed25519KeyPair(privateKey: BigInteger, publicKey: BigInteger) : ECKeyPair(privateKey, publicKey) {

    private val parameters = Ed25519PrivateKeyParameters(privateKey.toByteArray(), 0)

    /**
     * Sign a hash with the private key of this key pair.
     *
     * @param transactionHash the hash to sign
     * @return An [ECDSASignature] of the hash
     */
    override fun sign(transactionHash: ByteArray): ECDSASignature {
        return with(Ed25519Signer()) {
            init(true, parameters)
            update(transactionHash, 0, transactionHash.size)
            generateSignature()
        }.run {
            ECDSASignature(BigInteger(this), BigInteger(CURVE_PARAMETERS.seed))
        }
    }

    companion object {
        val CURVE_PARAMETERS: X9ECParameters = CustomNamedCurves.getByName("ed25519")

        fun create(privateKey: BigInteger): Ed25519KeyPair {
            return Ed25519KeyPair(privateKey, publicKeyFromPrivate(privateKey))
        }
    }
}
