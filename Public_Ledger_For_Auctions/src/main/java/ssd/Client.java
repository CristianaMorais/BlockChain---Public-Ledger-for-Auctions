package ssd;

import com.google.gson.GsonBuilder;
import ssd.kademlia.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static ssd.Menus.*;

public class Client {

    static int chIM, chUM; //choice do Initial Menu and the choice of the User Menu
    static int opF;
    static int opT;
    static List<Wallet> listWallets = new ArrayList<>();
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String,TransactionOutput> unspentTrans = new HashMap<>();
    public static Transaction genesisTransaction;
    public static int difficulty = 3;
    static Block genesis = new Block("0");
    static Block newBlock;

    public static void main(String[] args)  {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
        String ip;

        ip = getAddress();

        Node node = new Node(ip,"8080");
        node.ping("localhost","8080");

        System.out.println("************************************");
        System.out.println("**  Welcome to the Public Ledger  **");
        System.out.println("************************************");

        chIM = callInitialMenu();

        while(chIM != 0) {
            if (chIM == 1) {
                createUser();
            } else {
                System.out.println("Invalid option, please try again.");
                System.out.println();
            }
            chIM = callInitialMenu();
        }

    }

    private static void createUser() {
        System.out.println("************************************");
        System.out.println("************ Your Menu! ************");
        System.out.println("************************************");

        //User initialization
        Wallet user;
        user = initUser();
        listWallets.add(user);

        // Calling the menu for the created user
        chUM = userMenu();


        while(chUM != 0) {
            switch (chUM) {
                case 1:
                    createNewWallet();
                    break;

                case 2:
                    System.out.println();
                    checkBalances();
                    break;

                case 3:
                    doTransaction(listWallets);
                    break;

                case 4:
                    printBlockChain();
                    break;
                case 5:
                    sellItem();
                    break;

                case 6:
                    buyItem();
                    break;

                default:
                    System.out.println("Invalid option, please try again.");
                    System.out.println();
                    break;
            }
            chUM = userMenu();
        }
    }

    private static Wallet initUser() {
        Wallet user = new Wallet();
        Wallet base = new Wallet();
        
        genesisTransaction = new Transaction(base.publicKey, user.publicKey, 100f, null);

        genesisTransaction.generateSignature(base.privateKey); //Sign the genesis transaction
        genesisTransaction.transactionId = "0"; //Set the transaction ID
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
        unspentTrans.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //Store our first transaction
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        return user;
    }

    private static void createNewWallet() {
        Wallet newWallet = new Wallet();
        listWallets.add(newWallet);
        System.out.println("A new Wallet was created.");
        System.out.println();

    }

    private static void checkBalances() {
        if(listWallets.size() > 1)
            System.out.println("Currently you have: " + listWallets.size() + " wallets");
        else
            System.out.println("Currently you have: " + listWallets.size() + " Wallet");
        for(int i=0; i<listWallets.size(); i++) {
            System.out.println("The " +  (i+1) + "ª Wallet has the following balance: " + listWallets.get(i).getBalance());
        }
        System.out.println();
    }

    private static void doTransaction(List<Wallet> list) {
        checkBalances();
        while(true) {
            if(list.size() > 1) {
                System.out.println("What is the Wallet from where you intend to make the transaction?");
                opF = in.nextInt();
                if(opF >= 1 && opF <= list.size()) {
                    System.out.println("Which Wallet do you want to send the transaction to?");
                    opT = in.nextInt();

                    if((opT >= 1 && opT <= list.size()) && opF != opT) {
                        System.out.println("Please insert the value of transaction: ");
                        float val = in.nextFloat();
                        int last = blockchain.size()-1;
                        if(val > 0) {
                            newBlock = new Block(blockchain.get(last).hash);
                            //System.out.println(blockchain.get(last).hash);
                            newBlock.addTransaction(list.get(opF-1).sendFunds(list.get(opT-1).publicKey, val));
                            addBlock(newBlock);
                            System.out.println("The " +  opF + "ª Wallet has now the following balance: " + listWallets.get(opF-1).getBalance());
                            System.out.println("The " +  opT + "ª Wallet has now the following balance: " + listWallets.get(opT-1).getBalance());
                            System.out.println();
                            break;
                        }

                        else {
                            System.out.println("The value of transaction cannot be zero, please try again.");
                            System.out.println();
                        }
                    }

                    else {
                        System.out.println("Invalid option, please try again.");
                        System.out.println();
                    }
                }

                else {
                    System.out.println("Invalid option, please try again.");
                    System.out.println();
                }
            }

            else {
                System.out.println("More than one wallet is needed to make a transaction, please create more.");
                System.out.println();
                break;
            }
        }
    }

    private static void printBlockChain() {

        System.out.println("What format you want to see?");
        System.out.println("1 -> Simple view of the BlockChain");
        System.out.println("2 -> Detailed view of the BlockChain");
        int op = in.nextInt();

        if(op == 1) {
            System.out.println("********************");
            System.out.println("** The BlockChain **");
            System.out.println("********************");
            System.out.println(Arrays.toString(blockchain.toArray()));
            System.out.println();
        }

        else if(op == 2) {
            System.out.println("********************");
            System.out.println("** The BlockChain **");
            System.out.println("********************");
            String blockchainPrint = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
            System.out.println(blockchainPrint);
            System.out.println();
        }

        else {
            System.out.println("Invalid option, please try again");
            printBlockChain();
        }

    }

    private static void sellItem() {
        //auction = new Auction();
        System.out.println();
    }

    private static void buyItem() {

    }

    // Get IP of the machine
    public static String getAddress() {
        try {
            URL url = new URL("https://checkip.amazonaws.com/");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            return br.readLine();

        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // BlockChain

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    @SuppressWarnings("unused")
    public static Boolean isChainValid() {
        Block currentBlock, previousBlock;
        TransactionOutput tempOutput;
        Transaction currentTransaction;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempTrans = new HashMap<>(); 
        
        tempTrans.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));


        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("The current Hashes are not equal.");
                return false;
            }
            
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("The previous Hashes are not equal.");
                return false;
            }
            
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined yet.");
                return false;
            }
            
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                currentTransaction = currentBlock.transactions.get(t);

                if(currentTransaction.verifySignature()) {
                    System.out.println("The Signature on Transaction (" + t + ") is invalid!");
                    return false;
                }
                
                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("The inputs are not equal to outputs on Transaction (" + t + ").");
                    return false;
                }

                for(TransactionInput input: currentTransaction.inputs) {
                    tempOutput = tempTrans.get(input.transOutID);

                    if(tempOutput == null) {
                        System.out.println("The referenced input on Transaction (" + t + ") is missing!");
                        return false;
                    }

                    if(input.unspentTrans.value != tempOutput.value) {
                        System.out.println("The referenced input Transaction (" + t + ") value is invalid!");
                        return false;
                    }

                    tempTrans.remove(input.transOutID);
                }

                for(TransactionOutput output: currentTransaction.outputs) {
                    tempTrans.put(output.id, output);
                }

                if( currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
                    System.out.println("The transaction (" + t + ") output recipient is not who it should be.");
                    return false;
                }
                
                if( currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
                    System.out.println("The transaction (" + t + ") output 'change' is not sender.");
                    return false;
                }
            }
        }
        return true;
    }

}