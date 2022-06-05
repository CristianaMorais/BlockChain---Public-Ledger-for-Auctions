package ssd;

import com.google.gson.GsonBuilder;
import ssd.kademlia.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.google.common.net.HttpHeaders.USER_AGENT;
import static ssd.Menus.*;

public class Client {

    //private static final Logger logger = Logger.getLogger(Client.class.getName());
    static int chIM, chUM; //choice do Initial Menu and the choice of the User Menu
    static int opF;
    static int opT;
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<>();
    public static Transaction genesisTransaction;
    public static int difficulty = 3;

    static List<Wallet> listWallets = new ArrayList<>();
    static Block genesis = new Block("0");
    static Block newBlock;

    Transaction transaction;

    public static void main(String[] args)  {
        //add our blocks to the blockchain ArrayList:
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
        String machineIP = "localhost";

        try {
            machineIP = getMachineIP();
        }catch (IOException ex){
            System.out.println("GET request didn't work.\n");
            ex.printStackTrace();
        }

        //verificar se o próprio servidor está ativo
        Node thisNode = new Node(machineIP,"8080");
        thisNode.ping("localhost","8080");

        System.out.println("************************************");
        System.out.println("**  Welcome to the Public Ledger  **");
        System.out.println("************************************");

        chIM = callInitialMenu();

        while(chIM != 0) {
            switch (chIM) {
                case 1:
                    createUser();
                    break;

                default:
                    System.out.println("Invalid option, please try again.");
                    System.out.println();
                    break;
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
        Wallet coinbase = new Wallet();
        //create genesis transaction, which sends 100 NoobCoin to walletA:
        genesisTransaction = new Transaction(coinbase.publicKey, user.publicKey, 100f, null);

        genesisTransaction.generateSignature(coinbase.privateKey); //manually sign the genesis transaction
        genesisTransaction.transactionId = "0"; //manually set the transaction id
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //it's important to store our first trans
        //System.out.println("Creating and Mining Genesis block... ");
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
    /*
    private static void checkTransactions() {
        System.out.println("You have the following transactions:");
        System.out.println("Transactions you made: ");
        //
        System.out.println("Transactions you received: ");
        //
    }
    */

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

    // BlockChain

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

    public static String getMachineIP() throws IOException {
        URL ipGetter =new URL("https://myexternalip.com/raw");
        HttpURLConnection con = (HttpURLConnection) ipGetter.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            return response.toString();
        } else {
            return null;
        }
    }
}