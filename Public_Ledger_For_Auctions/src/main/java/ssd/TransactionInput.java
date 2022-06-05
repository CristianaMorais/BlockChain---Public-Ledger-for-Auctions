package ssd;

public class TransactionInput {

    public String transOutID;
    public TransactionOutput unspentTrans; //Contains the Unspent transaction output

    public TransactionInput(String transOutID) {
        this.transOutID = transOutID;
    }
}
