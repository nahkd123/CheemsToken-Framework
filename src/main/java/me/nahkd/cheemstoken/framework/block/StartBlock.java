package me.nahkd.cheemstoken.framework.block;

import me.nahkd.cheemstoken.framework.bytestream.WritableStream;

/**
 * The beginning of the chain...
 * @author nahkd123
 *
 */
public class StartBlock extends Block {
	
	public StartBlock() {
		super(new byte[0], System.currentTimeMillis(), 0x00, 0, 0);
	}
	
	@Override
	public void writeBlockBody(WritableStream stream) {}

}
