package ssd;

import java.util.ArrayList;
import java.util.Date;

public class Block {

    public String hash;
    public String previousHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<>(); //save transactions on an ArrayList
    public long timeStamp; //in milliseconds
    public int nonce; // when rehashed, meets the difficulty level restrictions

    //Block Constructor
    public Block(String previousHash ) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash(); //Making sure we do this after we set the other values.
    }

    //Calculate new hash with : hash of previous block + timestamp in millic + nonce + merkleroot with translations
    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
                previousHash +
                        timeStamp +
                        nonce +
                        merkleRoot
        );
        return calculatedhash;
    }


    //MineBlock of blockchain. Nonce increases each time -> target
    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block mined -> hash : " + hash);
    }

    //Boolean function
    //true if is added with sucess
    //false if not.
    public boolean addTransaction(Transaction transaction) {
        //ignores genesis block (first block of blockchain
        if(transaction == null) return false;
        if((previousHash != "0")) { // != hash of genesis
            if((!transaction.processTransaction())) {
                System.out.println("Transaction failed. It was discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction successful.  It was added to the block");
        return true;
    }

}