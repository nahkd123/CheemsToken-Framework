package me.nahkd.cheemstoken.framework.utils;

public class DataComparator {
	
	public static boolean compareBytes(byte[] b1, byte[] b2) {
		if (b1.length != b2.length) return false;
		for (int i = 0; i < b1.length; i++) if (b1[i] != b2[i]) return false;
		return true;
	}
	
	private static int[] bits = {
			0b1111_1111,
			0b0111_1111,
			0b0011_1111,
			0b0001_1111,
			0b0000_1111,
			0b0000_0111,
			0b0000_0011,
			0b0000_0001,
			0b000000000
	}; 
	public static boolean padsAtLeastBits(byte[] bs, int zeros) {
		int bytesToCheck = zeros / 8;
		int bitsToCheck = zeros % 8;
		
		if (bs.length < bytesToCheck + 1) return false;
		for (int i = 0; i < bytesToCheck; i++) if (bs[i] != 0x00) return false;
		
		int bitsMask = bits[bitsToCheck];
		int flippedMask = bitsMask ^ 0xFF;
		if ((Byte.toUnsignedInt(bs[bytesToCheck]) & flippedMask) == 0) return true;
		return false;
	}
	
}
