package ssd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import static ssd.Menus.callInitialMenu;
import static ssd.Menus.userMenu;

public class Client {

    //private static final Logger logger = Logger.getLogger(Client.class.getName());
    static int chIM, chUM;
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<>();
    public static Transaction genesisTransaction;
    public static int difficulty = 3;

    static List<Wallet> listWallets = new ArrayList<>();


    public static void main(String[] args)  {
        //add our blocks to the blockchain ArrayList:
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        System.out.println("************************************");
        System.out.println("**  Welcome to the Public Ledger  **");
        System.out.println("************************************");

        chIM = callInitialMenu();

        while(chIM != 0) {
            switch (chIM) {
                case 1:
                    createUser();
                    break;

                case 2:
                    System.out.println("test");
                    break;

                default:
                    break;
            }
            chIM = callInitialMenu();
        }

    }

    private static void createUser() {

        //User initialization
        Wallet user;
        user = initUser();
        listWallets.add(user);

        // Calling the menu for the created user
        chUM = userMenu();

        System.out.println("************************************");
        System.out.println("************ Your Menu! ************");
        System.out.println("************************************");

        while(chUM != 0) {
            switch (chUM) {
                case 1:
                    createNewWallet();
                    break;

                case 2:
                    System.out.println("Wallet balance is: " + user.getBalance());
                    break;

                case 3:
                    doMining(user);
                    break;

                default:
                    break;
            }
            chUM = userMenu();
        }
    }

    private static void createNewWallet() {
        Wallet newWallet = new Wallet();
        listWallets.add(newWallet);
        System.out.println(Arrays.toString(listWallets.toArray()));

    }

    private static Wallet initUser() {
        Wallet user = new Wallet();
        Wallet coinbase = new Wallet();
        //create genesis transaction, which sends 100 NoobCoin to walletA:
        genesisTransaction = new Transaction(coinbase.publicKey, user.publicKey, 100f, null);

        genesisTransaction.generateSignature(coinbase.privateKey); //manually sign the genesis transaction
        genesisTransaction.transactionId = "0"; //manually set the transaction id
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //it's important to store our first trans
        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        Block block1 = new Block(genesis.hash);
        addBlock(block1);

        return user;
    }

    private static void doMining(Wallet user) {
        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.hash);
        System.out.println("Wallet balance is: " + user.getBalance());
    }

    // Parte do Kademlia
    public static String getAddress() {
        try {
            URL url = new URL("https://checkip.amazonaws.com/");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            System.out.println(br.readLine());
            return br.readLine();

        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop through blockchains transactions:
            TransactionOutput tempOutput;
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for(TransactionInput input: currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for(TransactionOutput output: currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if( currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
                    System.out.println("#Transaction(" + t + ") output recipient is not who it should be");
                    return false;
                }
                if( currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        return true;
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

}