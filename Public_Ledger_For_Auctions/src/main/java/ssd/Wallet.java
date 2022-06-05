package ssd;


import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;


public class Wallet {

    public String name; // Rever isto
    public PrivateKey privateKey;
    public PublicKey publicKey;
    public HashMap<String,TransactionOutput> unspentTrans = new HashMap<>();


    public Wallet(){
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");



            keyGen.initialize(ecSpec, random);   //256 bytes
            KeyPair keyPair = keyGen.generateKeyPair();


            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


    //returns balance of wallet
    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: Client.unspentTrans.entrySet()){
            TransactionOutput unspent = item.getValue();
            if(unspent.isMine(publicKey)) { //calls isMine to assure that coins are of the user
                unspentTrans.put(unspent.id,unspent);
                total += unspent.value ;
            }
        }
        return total;
    }
    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(PublicKey _recipient,float value ) {
        if(getBalance() < value) { //gather balance and check funds.
            //do not allow to send more than we have
            System.out.println(" Not Enough funds to send transaction! Transaction Discarded.");
            return null;
        }

        ArrayList<TransactionInput> inputs = new ArrayList<>(); //inputs arraylist

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: unspentTrans.entrySet()){
            TransactionOutput unspent = item.getValue();
            total += unspent.value;
            inputs.add(new TransactionInput(unspent.id));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
        newTransaction.generateSignature(privateKey); //signs new transaction with private key.

        for(TransactionInput input: inputs){
            unspentTrans.remove(input.transactionOutputId);
        }
        return newTransaction;
    }

}

