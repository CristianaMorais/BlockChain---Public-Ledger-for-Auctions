package ssd;

import java.util.*;


public class Block {

    ArrayList<String> transactions_hashes = new ArrayList<String>();
    LinkedList<Transaction> transactions = new LinkedList<Transaction>();

    String root_transaction_hash;

    int nonce; //The int number which concatenated to the end of transactions root hash gives a hash with 4 zeros at left

    String previous_hash;
    final int max_transactions = 2; // TODO: para alterar para 10



    Block(String previous_hash) {
        root_transaction_hash = null;
        nonce=0;
        this.previous_hash = previous_hash;

    }

    public String computeHash() {

        String dataToHash = "" + this.version + this.Timestamp + this.previousHash + this.data;

        MessageDigest digest;
        String encoded = null;

        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
            encoded = Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        this.hash = encoded;
        return encoded;

    }

    public boolean is_block_full() {
        return (transactions_hashes.size() >= max_transactions);
    }

    public String get_root_transaction_hash() {
        return root_transaction_hash;
    }

    public String get_previous_hash()
    {

        return this.previous_hash;
    }

    public int getNonce() {
        return this.nonce;
    }

   public void setNonce(int nonce) {
        this.nonce = nonce;
   }

    public void add_transaction(Transaction t) {
        if (!this.is_full_block() && t.verify_signature(t.getPublicKey(), t.getSig(), t.getMessage())) {
            this.transactions.add(t);
            this.transactions_hashes.add(t.transaction_hash());
        }

        MerkleTree merkle_tree = new MerkleTree();
        root_transaction_hash = merkle_tree.createMerkleTree(transactions_hashes);
    }


}
