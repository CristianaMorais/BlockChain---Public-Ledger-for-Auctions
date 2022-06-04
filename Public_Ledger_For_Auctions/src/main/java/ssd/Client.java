package ssd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

import static ssd.BlockChain.addBlock;
import static ssd.Menus.callInitialMenu;
import static ssd.Menus.userMenu;

public class Client {

    //private static final Logger logger = Logger.getLogger(Client.class.getName());
    static int chIM, chUM;
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<>();
    public static Transaction genesisTransaction;

    BlockChain bc = new BlockChain();


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

        // Calling the menu for the created user
        chUM = userMenu();

        System.out.println("************************************");
        System.out.println("************ Your Menu! ************");
        System.out.println("************************************");

        while(chUM != 0) {
            switch (chUM) {
                case 1:
                    System.out.println("test2.1");
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

    private static Wallet initUser() {
        Wallet user = new Wallet();
        Wallet coinbase = new Wallet();
        //create genesis transaction, which sends 100 NoobCoin to walletA:
        genesisTransaction = new Transaction(coinbase.publicKey, user.publicKey, 100f, null);
        System.out.println("Wallet balance is: " + user.getBalance());
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
}