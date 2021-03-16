package me.nahkd.cheemstoken.framework.wallets;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import me.nahkd.cheemstoken.framework.nft.Token;

/**
 * The wallet object. This object is owned by the client, and if you want to send token to address, you only need
 * {@link WalletAddress}
 * @author nahkd123
 *
 */
public class Wallet {
	
	private WalletAddress address;
	private RSAPrivateKey privateKey;
	
	/**
	 * Create new client wallet
	 * @param keyPair The RSA key pair
	 */
	public Wallet(KeyPair keyPair) {
		this.address = new WalletAddress((RSAPublicKey) keyPair.getPublic());
		this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
	}
	
	/**
	 * Randomly generate new wallet
	 */
	public Wallet() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(617, new SecureRandom());
			
			KeyPair pair = generator.generateKeyPair();
			this.address = new WalletAddress((RSAPublicKey) pair.getPublic());
			this.privateKey = (RSAPrivateKey) pair.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			this.address = null;
			this.privateKey = null;
		}
	}
	
	public WalletAddress getAddress() {
		return this.address;
	}
	
	public byte[] sign(byte[] message) {
		try {
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(privateKey);
			signature.update(message);
			return signature.sign();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Token createToken(long serial) {
		Token token = new Token(getAddress(), serial);
		token.sign(this);
		return token;
	}
	
}
