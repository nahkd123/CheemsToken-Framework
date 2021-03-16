package me.nahkd.cheemstoken.framework.chain;

import me.nahkd.cheemstoken.framework.block.Block;
import me.nahkd.cheemstoken.framework.utils.DataComparator;

/**
 * The heart of CheemsToken framework. This interface allow you to implement the way CheemsToken store blocks.
 * For quick start, use {@link MemoryBlockchain}
 * @author nahkd123
 *
 */
public interface Blockchain {
	
	/**
	 * Get the block at index
	 * @param index
	 * @return
	 */
	public Block getBlock(int index);
	
	/**
	 * Get the last/latest block in the chain
	 * @return
	 */
	default Block getLastBlock() {
		return getBlock(getBlocksCount() - 1);
	}
	
	/**
	 * Get the total blocks count inside the chain
	 * @return
	 */
	public int getBlocksCount();
	
	/**
	 * Verify the chain. If this return false, you'll have to sync the chain again
	 * @return The chain status
	 */
	public boolean verifyChain();
	
	/**
	 * Submit the block to the chain. This will perform block check, and you can only use this method to submit the block
	 * to the network
	 * @param block
	 * @return
	 */
	default boolean submit(Block block) {
		int diff = block.diff;
		byte[] hash = block.getBlockHash();
		if (DataComparator.padsAtLeastBits(hash, diff)) {
			realSubmit(block);
			return true;
		}
		return false;
	}
	
	/**
	 * Submit the block to the chain. This should not perform any block check, and you must use {@link Blockchain#submit()}
	 * to submit the block over the network  
	 * @param block The block to submit
	 */
	public void realSubmit(Block block);
	
}
