package me.nahkd.cheemstoken.framework.wallets;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import me.nahkd.cheemstoken.framework.block.Block;
import me.nahkd.cheemstoken.framework.bytestream.ByteWritableStream;
import me.nahkd.cheemstoken.framework.bytestream.WritableStream;
import me.nahkd.cheemstoken.framework.chain.Blockchain;
import me.nahkd.cheemstoken.framework.utils.ByteArraysConverter;

public class WalletAddress {
	
	public final RSAPublicKey key;
	
	public WalletAddress(RSAPublicKey publicKey) {
		this.key = publicKey;
	}
	
	public WalletAddress(String walletString) {
		String[] splits = walletString.split("\\-");
		byte[] exp = Base64.getDecoder().decode(splits[1]);
		byte[] mod = Base64.getDecoder().decode(splits[2]);
		
		RSAPublicKey key = null;
		try {
			KeyFactory keyFac = KeyFactory.getInstance("RSA");

			BigInteger expi = new BigInteger(exp);
			BigInteger modi = new BigInteger(mod);
			
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modi, expi);
			key = (RSAPublicKey) keyFac.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		this.key = key;
	}
	
	public void writeAddress(WritableStream stream) {
		byte[] exp = key.getPublicExponent().toByteArray();
		byte[] mod = key.getModulus().toByteArray();
		stream.writeLengthPrefixedByteArray(exp);
		stream.writeLengthPrefixedByteArray(mod);
	}
	
	public byte[] getBytes() {
		ByteWritableStream stream = new ByteWritableStream();
		writeAddress(stream);
		return stream.getBytes();
	}
	
	public boolean verify(byte[] data, byte[] signature) {
		try {
			Signature sign = Signature.getInstance("SHA256withRSA");
			sign.initVerify(key);
			sign.update(data);
			return sign.verify(signature);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Perform full block mining
	 * @param block
	 */
	public void mineBlock(Block block) {
		block.initMiner(this);
		long hash = 1;
		long timestamp = System.currentTimeMillis();
		
		while (!block.mineBlock()) hash++;
		System.out.println(hash + " hashes in " + (System.currentTimeMillis() - timestamp) + "ms");
	}
	
	/**
	 * Calculate the wallet balance. This includes rewards for mining and balance changes on transactions
	 * @param chain The blocks chain
	 * @return
	 */
	public long calculateBalance(Blockchain chain) {
		long bal = 0;
		for (int i = 0; i < chain.getBlocksCount(); i++) {
			Block block = chain.getBlock(i);
			if (this.equals(block.getMinerAddress())) bal += block.reward;
		}
		return bal;
	}
	
	@Override
	public String toString() {
		return 
				"cheems-"
				+ ByteArraysConverter.toBase64(key.getPublicExponent().toByteArray()) + "-"
				+ ByteArraysConverter.toBase64(key.getModulus().toByteArray());
	}
	
	/**
	 * Check if 2 addresses are equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof WalletAddress)) return false;
		WalletAddress another = (WalletAddress) obj;
		return
			this.key.getPublicExponent().equals(another.key.getPublicExponent()) &&
			this.key.getModulus().equals(another.key.getModulus())
		;
	}
	
}
