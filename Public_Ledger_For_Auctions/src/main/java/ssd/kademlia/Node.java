package ssd.kademlia;

import ssd.BlockChain;

import java.math.BigInteger;

public class Node {

    /*
    In kad algorithm, the concept of k-bucket is used to store the state information of
    other neighboring nodes, which is composed of (IP address, UDP port, node ID)
    data list (kad network exchanges information by UDP protocol).
    */
    private String ipaddr;
    private int udp_port;
    private String nodeID;
    final int k_nodes = 20;

    private BlockChain bc;

    public Node(String ipaddr, int udp_port, String nodeID){
        this.ipaddr = ipaddr;
        this.udp_port = udp_port;
        this.nodeID = nodeID;
        this.bc = new BlockChain();
    }

    public String getNodeID() {
        return this.nodeID;
    }

    public BigInteger getDistance(Node n1, Node n2) {
        BigInteger node1 = new BigInteger(n1.getNodeID(), 16);
        BigInteger node2 = new BigInteger(n2.getNodeID(), 16);
        BigInteger distance = node1.xor(node2);

        return distance;
    }
}
