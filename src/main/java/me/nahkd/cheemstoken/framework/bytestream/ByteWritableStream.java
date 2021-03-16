package me.nahkd.cheemstoken.framework.bytestream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;

public class ByteWritableStream implements WritableStream {

	private ByteArrayOutputStream wrapped = new ByteArrayOutputStream();
	
	@Override
	public void write(byte[] bs) {
		try {
			wrapped.write(bs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeByte(int v) {
		wrapped.write(v);
	}
	
	public byte[] getBytes() {
		return wrapped.toByteArray();
	}
	
	public byte[] signData(RSAPrivateKey key) {
		try {
			byte[] input = wrapped.toByteArray();
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(key);
			signature.update(input);
			return signature.sign();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void reset() {
		wrapped.reset();
	}

}
