package ssd;

import java.security.PublicKey;

public class TransactionOutput {
    public String id;
    public PublicKey recipient; //who receives coins
    public float value; //number of coinds
    public String parentTransactionId; //the id of the transaction this output was created in


    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(recipient) + Float.toString(value) + parentTransactionId);
    }

    //Check if coin belongs to user
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == recipient);
    }
}
