package ssd;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<Block>();
        chain.add(generateGenesis());
    }

    private Block generateGenesis(){
        Block genesis = new Block("0x200", new java.util.Date(),"<transatcions>");
        genesis.setPreviousHash(null);
        genesis.computeHash();

        return genesis;
    }

    public void addBlock(Block blk){
        Block newBlock= blk;
        blk.setPreviousHash(chain.get(chain.size()-1).getHash());
        newBlock.computeHash();
        this.chain.add(newBlock);

    }

    public void displayChain(){
        for (int i = 0; i < chain.size(); i++) {
            System.out.println("ssd.Block: " + i );
            System.out.println("Version: " + chain.get(i).getVersion());
            System.out.println("TimeStamp: " +  chain.get(i).getTimestamp());
            System.out.println("PreviousHash: " +  chain.get(i).getPreviousHash());
            System.out.println("Hash: " +  chain.get(i).getHash());
            System.out.println();

        }
    }

}