package me.nahkd.cheemstoken.framework.chain;

import java.util.ArrayList;

import me.nahkd.cheemstoken.framework.block.Block;
import me.nahkd.cheemstoken.framework.block.StartBlock;
import me.nahkd.cheemstoken.framework.utils.DataComparator;
import me.nahkd.cheemstoken.framework.wallets.Wallet;
import me.nahkd.cheemstoken.framework.wallets.WalletAddress;

public class MemoryBlockchain implements Blockchain {

	private ArrayList<Block> blocks = new ArrayList<>();
	
	public MemoryBlockchain() {
		initStart();
	}
	
	private void initStart() {
		Wallet throwaway = new Wallet();
		WalletAddress miner = throwaway.getAddress();
		
		StartBlock block = new StartBlock();
		block.initMiner(miner);
		block.mineBlock();
		this.blocks.add(block);
	}
	
	@Override
	public Block getBlock(int index) {
		return index < blocks.size() && index >= 0? blocks.get(index) : null;
	}
	
	@Override
	public int getBlocksCount() {
		return blocks.size();
	}

	@Override
	public synchronized boolean verifyChain() {
		byte[] prevHash = new byte[0];
		
		for (int i = 0; i < blocks.size(); i++) {
			Block b = blocks.get(i);
			if (!DataComparator.compareBytes(b.prevHash, prevHash)) return false;
			prevHash = b.getBlockHash();
		}
		return true;
	}
	
	@Override
	public synchronized void realSubmit(Block block) {
		blocks.add(block);
	}

}
