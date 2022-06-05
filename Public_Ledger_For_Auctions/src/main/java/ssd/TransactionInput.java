package ssd;

public class TransactionInput {

    //This class will be used to reference TransactionOutputs that have not yet been spent

    public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
    public TransactionOutput unspent; //Contains the Unspent transaction output

    public TransactionInput(String transactionOutputId) {

        this.transactionOutputId = transactionOutputId;
    }
}
