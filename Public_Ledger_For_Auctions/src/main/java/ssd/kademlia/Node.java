package ssd.kademlia;

import io.grpc.*;
import ssd.*;
import ssd.kademlia.grpc.PingServiceImpl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;

public class Node {

    private BlockChain bc;
    public String nodeId;
    public String ipAddr;
    public String udp_port;
    public static Node node;
    LinkedList<Node> bucket = new LinkedList<>();
    Hashtable<String, BigInteger> distanceHT = new Hashtable<>();
    
    final int kNodes = 20;
    private String nodeID;
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    private PingServiceGrpc.PingServiceBlockingStub blockingStubPingService;
    private StoreServiceGrpc.StoreServiceBlockingStub blockingStubStoreService;


    public Node(String ip, String port){
        this.nodeId = String.valueOf(Math.random()).substring(2);
        this.ipAddr = ip;
        this.udp_port = port;

        try{
            server();
        }
        catch (IOException ioEx){
            System.out.println("Server start not possible.\n");
            ioEx.printStackTrace();
        }
        catch (InterruptedException iEx){
            System.out.println("Server process interrupted\n");
            iEx.printStackTrace();
        }
    }

    public Node(String ipaddr, int listen_port, String nodeid) {
        this.nodeId = nodeid;
        this.ipAddr = ipaddr;
        this.udp_port = String.valueOf(listen_port);
        this.bc = new BlockChain();
    }


    public static Node getNode() {
        return node;
    }

    public String getNodeid() {
        return this.nodeID;
    }

    public String getIpaddr() {
        return this.ipAddr;
    }

    public String getPort() {
        return this.udp_port;
    }

    public BigInteger getDistance(Node n1, Node n2) {

        return getDistanceById(n1.getNodeid(), n2.getNodeid());
    }

    public BigInteger getDistanceById(String n1, String n2) {
        BigInteger vn1 = new BigInteger(n1, 16);
        BigInteger vn2 = new BigInteger(n2, 16);
        BigInteger rv = vn1.xor(vn2);

        return rv;
    }

    public void addAdjNode( Node node) {

        if ( bucket.size() == this.kNodes ) {
            String maxNodeId = maxDistanceNode();
            BigInteger distance_node = getDistance(node, this);

            if (isBiggerThan(distanceHT.get( maxNodeId ), distance_node )) {
                distanceHT.remove( maxNodeId );
                Node node_Max_distance = getNodeFromBucketByNodeId( maxNodeId );
                bucket.remove( node_Max_distance );
                distanceHT.put( node.getNodeid() , distance_node );
                bucket.add( node );
            }
        }

        else {
            distanceHT.put(node.getNodeid(), getDistance(node, this));
            bucket.add( node );
        }
    }

    public Node getNodeFromBucketByNodeId( String nodeId ) {
        Node node = null;

        for(int i = 0; i < bucket.size(); i++ ){
            node = bucket.get( i );

            if(node.getNodeid().equals(nodeId ))
               return  node;
        }

        return null;
    }

    public boolean isBiggerThan( BigInteger distance1, BigInteger distance2) {
        return distance1.compareTo( distance2 ) == 1;
    }

    public String maxDistanceNode() {
        Set<String> keys = distanceHT.keySet();
        Iterator<String> itr = keys.iterator();
        BigInteger maxV = new BigInteger("0");
        BigInteger temp = new BigInteger("0");
        String nodeMaxID = "";
        String key;

        while(itr.hasNext()) {
            key = itr.next();
            temp = distanceHT.get( key );

            if( temp.compareTo(maxV) == 1 ) {
                maxV = new BigInteger(temp.toString());
                nodeMaxID = key;
            }
        }

        return nodeMaxID;
    }

    public void server() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(parseInt(this.udp_port))
                .addService(new PingServiceImpl()).build();

        server.start();
    }

    public void ping(String ip, String port){
        int portInt = parseInt(port);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, portInt).usePlaintext().build();
        PingServiceGrpc.PingServiceBlockingStub stub =
                PingServiceGrpc.newBlockingStub(channel);


        NodeInfo node = NodeInfo.newBuilder()
                .setIp(this.ipAddr)
                .setPort(this.udp_port)
                .build();

        NodeInfo nodeInfo = stub.ping(
                PingRequest.newBuilder()
                        .setId(ip)
                        .setSender(node)
                        .build());
        channel.shutdown();
    }

    public void store(String hash_v, String big_int_str) {
        StoreHash request = StoreHash.newBuilder()
                .setHashNode(hash_v)
                .setVal(big_int_str)
                .setIp(this.node.getIpaddr())
                .setPort(Integer.parseInt(this.node.getPort()))
                .build();
        StoreReply response;
        try {
            response = blockingStubStoreService.storeVal(request);
        }
        catch (StatusRuntimeException e) {
            return;
        }
        logger.info("Stored reply: " + response.getReply());
    }

    /*
    final public String getPeerID(String id) {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            String num = Integer.valueOf(random.nextInt()).toString();
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result =  sha.digest(num.getBytes());
            //System.out.println("UniqueID: " + hexEncode(result));
            return hexEncode(result);

        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    */

    static private String hexEncode(byte[] input){
        StringBuilder result = new StringBuilder();
        char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};

        for (byte b : input) {
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }

        return result.toString();
    }
}
