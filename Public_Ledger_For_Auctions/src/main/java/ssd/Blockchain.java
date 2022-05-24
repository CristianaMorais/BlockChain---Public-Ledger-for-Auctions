package ssd;

import ssd.Block;

import java.util.LinkedList;

public class Blockchain {

    LinkedList<Block> chain;

    LinkedList<Transaction> transactions_queue; //transactions waiting to join a block when block becomes available


    public Blockchain() {
        this.chain = new LinkedList<>();
        this.transactions_queue = new LinkedList<>();
    }

    public BlockChain(LinkedList<Block> chain, LinkedList<Transaction> transactions) {
        this.chain = new LinkedList<>();
        this.transactions_queue = new LinkedList<>();
        this.chain.addAll(chain);
        this.transactions_queue.addAll(transactions);
    }



    private Block generateGenesis(){
        Block genesis = new Block("0x200", new java.util.Date(),"<transatcions>");
        genesis.setPreviousHash(null);
        genesis.computeHash();

        return genesis;
    }

    public Block lastBlock() {
        return chain.getLast();
    }

    public boolean add_block(Block block) {
        if ( block_can_be_added(block) || ( block.get_previous_hash().equals("00000000000000000000000000000000") && chain.isEmpty() ) ) {
            chain.add(block);
            return true;
        }
        System.out.println( block.get_previous_hash() );
        return false;
    }

    public void add_transaction_to_queue(Transaction transaction) {

        transactions_queue.add(transaction);
    }

    public boolean block_can_be_added(Block block) {
        //Check if the new block previous hash matches the hash of last block on the chain
        //Check if the nonce matches the hashes
        ProofOfWork pw = new ProofOfWork(block.get_previous_hash(), block.get_root_transaction_hash(), block.getNonce());
        return (this.lastBlock().get_root_transaction_hash().equals(block.get_previous_hash()) && pw.correct_hash_own());
    }
    public void generate_genesis()
    {
        Block new_block = new Block("00000000000000000000000000000000");
        Transaction nTransation = null;

        try {
            nTransation = new Transaction("A","B", (float) 0.0,Transaction.readPublicKey("src/main/certs/public_client_key.der"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        new_block.add_transaction(nTransation);
        chain.add(new_block);
    }

}