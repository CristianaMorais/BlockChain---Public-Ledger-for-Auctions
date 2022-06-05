package ssd;

import java.security.*;
import java.util.ArrayList;

public class Transaction {

    public String transactionId; // hash of the transaction.
    public PublicKey sender; // senders address/public key.
    public PublicKey recipient; // address/public key.
    public float value;
    public byte[] signature; //prevent anybody else from spending funds in our wallet
    public static float minimumTransaction = 0.1f;
    public ArrayList<TransactionInput> inputs;
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0; //count of how many transactions have been generated

    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    // This calculates the transaction hash, which will be used as its ID
    private String calculateHash() {
        sequence++;
        return StringUtil.applySha256(StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value + sequence);
    }

    //Signs all the data we don't wish to be tampered with.
    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
        signature = StringUtil.applyECDSASig(privateKey,data);
    }
    //Verifies the data we signed hasn't been tampered with
    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
        return !StringUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction() {

        if(verifySignature()) {
            System.out.println("The transaction signature failed to verify!");
            return false;
        }

        //gather transaction inputs (Make sure they are unspent):
        for(TransactionInput i : inputs) {
            i.unspentTrans = Client.unspentTrans.get(i.transOutID);
        }

        //check if transaction is valid
        if(getInputsValue() < minimumTransaction) {
            System.out.println("The transaction inputs are to small: " + getInputsValue());
            return false;
        }

        //generate transaction outputs
        float leftOver = getInputsValue() - value;
        transactionId = calculateHash();
        outputs.add(new TransactionOutput( this.recipient, value,transactionId));
        outputs.add(new TransactionOutput( this.sender, leftOver,transactionId));

        //add outputs to Unspent list
        for(TransactionOutput o : outputs) {
            Client.unspentTrans.put(o.id , o);
        }

        //remove transaction inputs from unspentTrans lists as spent
        for(TransactionInput i : inputs) {
            if(i.unspentTrans == null) continue;
            Client.unspentTrans.remove(i.unspentTrans.id);
        }

        return true;
    }

    //returns sum of inputs
    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.unspentTrans == null) continue;
            total += i.unspentTrans.value;
        }
        return total;
    }

    //returns sum of outputs
    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }
}