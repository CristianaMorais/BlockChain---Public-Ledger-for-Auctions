package ssd.kademlia;

import ssd.BlockChain;

public class Node {

    private String ipaddr;
    private int udp_port;
    private String nodeID;
    final int k_nodes = 20;

    private BlockChain bc;

    public Node(String ip, int udp_port){
        this.ipaddr = ip;
        this.udp_port = udp_port;
        this.bc = new BlockChain();
    }
}
