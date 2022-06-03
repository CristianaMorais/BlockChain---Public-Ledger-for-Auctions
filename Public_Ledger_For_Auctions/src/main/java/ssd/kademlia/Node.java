package ssd.kademlia;

import io.grpc.ServerBuilder;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.util.encoders.Hex;
import ssd.BlockChain;
import ssd.kademlia.grpc.PingServiceImpl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Node {
    private static final Logger logger = Logger.getLogger(Node.class.getName());


    /*
    In kad algorithm, the concept of k-bucket is used to store the state information of
    other neighboring nodes, which is composed of (IP address, UDP port, node ID)
    data list (kad network exchanges information by UDP protocol).
    */
    private final String ipaddr;
    private final int udp_port;
    private final String nodeID;
    final int K_NODES = 20; //Number of adjacent nodes to store on the bucket


    LinkedList<Node> bucket = new LinkedList<Node>(); //List which represents the bucket
    Hashtable<String, BigInteger> ht_distance = new Hashtable<String, BigInteger>();


    private final BlockChain bc;

    public Node(String ipaddr, int udp_port, String nodeID){
        this.ipaddr = ipaddr;
        this.udp_port = udp_port;
        this.nodeID = this.generateNodeID() ;
        this.bc = new BlockChain();
    }

    private String generateNodeID() {
        byte[] b_arr = new byte[20]; // 160 bits = 20 bytes
        new SecureRandom().nextBytes(b_arr);
        return hash_functionSHA1(b_arr);
    }

    private String hash_functionSHA1(byte[] s) {
        SHA1Digest sha1 = new SHA1Digest();

        byte[] hash = new byte[sha1.getDigestSize()];

        sha1.update(s, 0, s.length);
        sha1.doFinal(hash,0);

        return Hex.toHexString(hash);
    }
    public String getNodeID() {
        return this.nodeID;
    }

    public String getIpaddr() {
        return this.ipaddr;
    }

    public Node getNodeFromBucketByNodeID( String nodeId )
    {
        Node node = null;

        for(int i = 0; i < bucket.size(); i++ )
        {
            node = bucket.get( i );

            if( node.getNodeID().equals( nodeId ) )
                return  node;
        }

        return null;
    }

 /*   public BlockChain getBlockChain() {
        return this.bc;
    }
*/
    public BigInteger getDistance(Node n1, Node n2) {
        BigInteger node1 = new BigInteger(n1.getNodeID(), 16);
        BigInteger node2 = new BigInteger(n2.getNodeID(), 16);
        BigInteger distance = node1.xor(node2);

        return distance;
    }

    public boolean isBiggerThan( BigInteger distance1, BigInteger distance2) {
        return distance1.compareTo( distance2 ) == 1;
    }

    public void addAdjNode( Node node)
    {
        if ( bucket.size() == this.K_NODES )
        {
            String maxNodeId = maxDistanceNode();
            logger.info("Nodes with large distance: " + maxNodeId );
            logger.info("New nodeID: " +node.getNodeID() );
            BigInteger distance_node = getDistance(node, this);

            if (isBiggerThan(ht_distance.get( maxNodeId ), distance_node )) {
                logger.info("Removed node more far away. Node removed: " + maxNodeId );
                logger.info("Node add. Node: " + node.getNodeID() );
                ht_distance.remove( maxNodeId );
                Node node_Max_distance = getNodeFromBucketByNodeID( maxNodeId );
                bucket.remove( node_Max_distance );
                ht_distance.put( node.getNodeID() , distance_node );
                bucket.add( node );
            }
        }
        else {
            logger.info("Node add. Node: " + node.getNodeID() );
            ht_distance.put(node.getNodeID(), getDistance(node, this));
            bucket.add( node );
        }
    }

    public String maxDistanceNode()
    {
        Set<String> keys = ht_distance.keySet();
        Iterator<String> itr = keys.iterator();
        BigInteger maxV = new BigInteger("0");
        BigInteger temp = new BigInteger("0");
        String nodeMaxID = "";
        String key;

        while(itr.hasNext())
        {
            key = itr.next();
            temp = ht_distance.get( key );

            if( temp.compareTo(maxV) == 1 )
            {
                maxV = new BigInteger(temp.toString());
                nodeMaxID = key;
            }
        }

        return nodeMaxID;
    }

    public static class Server {
        private final Logger logger = Logger.getLogger(Server.class.getName());

        private io.grpc.Server server;

        private void start() throws IOException {
            /* The port on which the server should run */
            int port = 50051;
            server = ServerBuilder.forPort(port)
                    .addService(new PingServiceImpl())
                    .build()
                    .start();
            logger.info("Server started, listening on " + port);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    ssd.kademlia.Node.Server.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }));
        }

        private void stop() throws InterruptedException {
            if (server != null) {
                server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            }
        }

        private void blockUntilShutdown() throws InterruptedException {
            if (server != null) {
                server.awaitTermination();
            }
        }

        public static void main(String[] args) throws IOException, InterruptedException {
            final Server server = new Server();
            server.start();
            server.blockUntilShutdown();
        }
    }

}
