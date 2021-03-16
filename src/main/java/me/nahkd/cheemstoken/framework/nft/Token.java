package me.nahkd.cheemstoken.framework.nft;

import me.nahkd.cheemstoken.framework.bytestream.ByteWritableStream;
import me.nahkd.cheemstoken.framework.bytestream.WritableStream;
import me.nahkd.cheemstoken.framework.wallets.Wallet;
import me.nahkd.cheemstoken.framework.wallets.WalletAddress;

/**
 * The NFT (non fungible token) class
 * @author nahkd123
 *
 */
public class Token {
	
	public final WalletAddress author;
	public final long serial;
	public byte[] signature;
	
	public Token(WalletAddress author, long serial, byte[] signature) {
		this.author = author;
		this.serial = serial;
		this.signature = signature;
	}
	
	public Token(WalletAddress author, long serial) {
		this(author, serial, null);
	}
	
	public void writeTokenData(WritableStream stream) {
		author.writeAddress(stream);
		stream.writeULEB128(serial);
	}
	
	public void sign(Wallet wallet) {
		if (!wallet.getAddress().equals(this.author)) throw new IllegalArgumentException("the author public key and given wallet public key does not match");
		byte[] data = toUnsignedByteArray();
		this.signature = wallet.sign(data);
	}
	
	public byte[] toUnsignedByteArray() {
		ByteWritableStream stream = new ByteWritableStream();
		writeTokenData(stream);
		return stream.getBytes();
	}
	
}
