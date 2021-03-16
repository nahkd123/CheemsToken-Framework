package me.nahkd.cheemstoken.framework.test.manually;

import me.nahkd.cheemstoken.framework.block.WorkerBlock;
import me.nahkd.cheemstoken.framework.chain.Blockchain;
import me.nahkd.cheemstoken.framework.chain.MemoryBlockchain;
import me.nahkd.cheemstoken.framework.wallets.Wallet;

public class WalletTest {

	public static void main(String[] args) {
		Wallet wallet = new Wallet();
		System.out.println(wallet.getAddress().toString());
		
		Blockchain chain = new MemoryBlockchain();
		System.out.println(chain.verifyChain());
		
		for (int i = 0; i < 10; i++) {
			WorkerBlock block = new WorkerBlock(chain.getLastBlock().getBlockHash(), System.currentTimeMillis());
			wallet.getAddress().mineBlock(block);
			System.out.println("Block mined");
			System.out.println("Submit = " + chain.submit(block));
			System.out.println(wallet.getAddress().calculateBalance(chain) + " Cheems in balance - " + chain.getBlocksCount() + " blocks in chain");
		}
	}

}
