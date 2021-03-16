package me.nahkd.cheemstoken.framework.bytestream;

public interface WritableStream {
	
	void write(byte[] bs);
	void writeByte(int v);
	
	default void writeULEB128(long val) {
		long remaining = val >>> 7;
		
		while (remaining != 0) {
			writeByte((int) (val & 0x7F) | 0x80);
			val = remaining;
			remaining >>>= 7;
		}
		
		writeByte((int) (val & 0x7F));
	}
	
	default void writeLengthPrefixedByteArray(byte[] bs) {
		writeULEB128(bs.length);
		write(bs);
	}
	
}
