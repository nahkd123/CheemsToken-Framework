package me.nahkd.cheemstoken.framework.block;

import me.nahkd.cheemstoken.framework.bytestream.WritableStream;

/**
 * The only block for people with epic computing power. Grant a low reward for each of these block.
 * @author nahkd123
 *
 */
public class WorkerBlock extends Block {
	
	public WorkerBlock(byte[] prevHash, long timestamp) {
		super(prevHash, timestamp, 0x01, 16, 50);
	}
	
	@Override
	public void writeBlockBody(WritableStream stream) {}

}
