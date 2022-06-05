package ssd;

import java.util.ArrayList;
import java.util.Date;

public class Block {

    public String hash;
    public String previousHash;
    public String merkleRoot;

    public ArrayList<Transaction> transactions = new ArrayList<>(); //The data will be a message
    public long timeSTamp;
    public int nonce;

    public Block(String previousHash ) {
        this.previousHash = previousHash;
        this.timeSTamp = new Date().getTime();

        this.hash = calculateHash();
    }

    // To calculate a new hash based on the blocks contents
    public String calculateHash() {
        return StringUtil.applySha256( previousHash + timeSTamp + nonce + merkleRoot);
    }

    //Increases nonce value until hash target is reached.
    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDifficultyString(difficulty); //String with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block mined with hash: " + hash);
    }

    //Add transactions to the block
    public void addTransaction(Transaction transaction) {

        if(transaction == null)
            return;

        if((!previousHash.equals("0"))) {
            if((!transaction.processTransaction())) {
                System.out.println("Transaction failed to process. It was discarded.");
                return;
            }
        }
        
        transactions.add(transaction);
        System.out.println("Transaction successfully added to Block.");
    }

}