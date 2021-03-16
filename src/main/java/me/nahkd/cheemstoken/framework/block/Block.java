package me.nahkd.cheemstoken.framework.block;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import me.nahkd.cheemstoken.framework.bytestream.ByteWritableStream;
import me.nahkd.cheemstoken.framework.bytestream.WritableStream;
import me.nahkd.cheemstoken.framework.utils.DataComparator;
import me.nahkd.cheemstoken.framework.wallets.WalletAddress;

public abstract class Block {
	
	public final byte[] prevHash;
	public final long timestamp;
	public final int blockId;
	public final int diff;
	public final long reward;
	
	public Block(byte[] prevHash, long timestamp, int blockId, int diff, long reward) {
		this.prevHash = prevHash;
		this.timestamp = timestamp;
		this.blockId = blockId;
		this.diff = diff;
		this.reward = reward;
	}
	
	public void writeBlockHeader(WritableStream stream) {
		stream.writeLengthPrefixedByteArray(prevHash);
		stream.writeULEB128(timestamp);
		stream.writeULEB128(blockId);
	}
	
	public byte[] getBlockHeader() {
		ByteWritableStream stream = new ByteWritableStream();
		writeBlockHeader(stream);
		return stream.getBytes();
	}
	
	public abstract void writeBlockBody(WritableStream stream);
	
	public byte[] getBlockBody() {
		ByteWritableStream stream = new ByteWritableStream();
		writeBlockBody(stream);
		return stream.getBytes();
	}
	
	/**
	 * Get the given block data. This data does not contains mined data like magic number or miner wallet address
	 * @return
	 */
	public byte[] getGivenBlockData() {
		ByteWritableStream stream = new ByteWritableStream();
		writeBlockHeader(stream);
		writeBlockBody(stream);
		return stream.getBytes();
	}
	
	public byte[] getBlockHash() {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(getFinalBlockData());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// Mining
	private WalletAddress minerAddress;
	private long magic;
	private MessageDigest digest;
	
	private ByteWritableStream minerStream;
	private byte[] givenData;
	private byte[] minerWalletData;
	
	public void initMiner(WalletAddress address) {
		this.minerAddress = address;
		this.magic = 0;
		try {
			this.digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		this.minerStream = new ByteWritableStream();
		this.givenData = getGivenBlockData();
		this.minerWalletData = minerAddress.getBytes();
	}
	
	public boolean mineBlock() {
		if (diff == 0) return true;
		long givenMagic = magic++;
		minerStream.reset();
		minerStream.write(givenData);
		minerStream.write(minerWalletData);
		minerStream.writeULEB128(givenMagic);
		
		digest.reset();
		byte[] res = digest.digest(minerStream.getBytes());
		if (DataComparator.padsAtLeastBits(res, diff)) {
			magic = givenMagic;
			return true;
		}
		return false;
	}
	
	public void writeMinerData(ByteWritableStream stream) {
		minerAddress.writeAddress(stream);
		stream.writeULEB128(magic);
	}
	
	// Finalize
	public byte[] getFinalBlockData() {
		ByteWritableStream stream = new ByteWritableStream();
		writeBlockHeader(stream);
		writeBlockBody(stream);
		writeMinerData(stream);
		return stream.getBytes();
	}
	
}
