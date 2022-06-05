package ssd;

import java.security.*;
import java.util.ArrayList;

public class Transaction {

    public String transactionId; // transaction hash
    public PublicKey sender; // sender public key.
    public PublicKey recipient; // address public key.
    public float value; //Value to send (Coins)
    public byte[] signature; // comproves owner of adress is sending it
    // and that data has not changed.

    public ArrayList<TransactionInput> inputs; //Is the reference to previous transactions
    // that prove the sender has funds to send.
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0; //We will use this to avoid 2 transactions having same hash

    // Constructor:
    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs; //references to previous transactions that prove the sender has funds to send
    }

    // This Calculates the transaction hash (which will be used as its Id)
    private String calulateHash() {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recipient) +
                        value + sequence
        );
    }

    //Signs with private key
    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
        signature = StringUtil.applyECDSASig(privateKey,data);
    }
    //Verifies signature to ensure that data wasn't violated
    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction() {

        if(!verifySignature()) {
            System.out.println(" Transaction Signature not verified!");
            return false;
        }

        //Make sure transaction inputs are unspent
        for(TransactionInput i : inputs) {
            i.unspent = Client.unspentTrans.get(i.transactionOutputId);
        }

        //Define the minimum value that can be transferred -- Set to 0.1 coins
        //alternative was using a random number, this option seems cleaner
        //ver melhor este valor , estava a dar problemas :REVER ISTO
        float minimumTransaction = 0.1f;
        if(getInputsValue() < minimumTransaction) {
            System.out.println(" Transaction inputs too small: " + getInputsValue() + ". Use a bigger value.");
            return false;
        }


        float leftOver = getInputsValue() - value;
        transactionId = calulateHash();
        outputs.add(new TransactionOutput( this.recipient, value,transactionId));
        outputs.add(new TransactionOutput( this.sender, leftOver,transactionId));

        //add outputs to Unspent list
        for(TransactionOutput o : outputs) {
            Client.unspentTrans.put(o.id , o);
        }

        //remove transaction inputs from unspentTrans  as spent
        for(TransactionInput i : inputs) {
            if(i.unspent == null) continue;
            //ignore transaction not found
            Client.unspentTrans.remove(i.unspent.id);
        }

        return true;
    }

    //returns sum of inputs(unspentTrans) values
    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.unspent == null) continue; //if Transaction can't be found skip it
            total += i.unspent.value;
        }
        return total;
    }


}