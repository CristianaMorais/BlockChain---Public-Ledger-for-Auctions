package ssd.kademlia;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import ssd.NodeInfo;
import ssd.PingRequest;
import ssd.PingServiceGrpc;
import ssd.kademlia.grpc.PingServiceImpl;

import java.io.IOException;
import java.math.BigInteger;

import static java.lang.Integer.parseInt;

public class Node {

    public String nodeId;
    public String ipAddr;
    public String udp_port;




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

    public void server() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(parseInt(this.udp_port))
                .addService(new PingServiceImpl()).build();

        server.start();
    }

    public void ping(String ip, String port){ // kademlia ping function. The only one implemented
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

    public BigInteger getDistance(Node n1, Node n2) {
        return getDistanceById(n1.getNodeid(), n2.getNodeid());
    }

    private String getNodeid() {
        return this.nodeId;
    }


    public BigInteger getDistanceById(String n1, String n2) { //Distance of nodes measured using XOR
        BigInteger vn1 = new BigInteger(n1, 16);
        BigInteger vn2 = new BigInteger(n2, 16);

        return vn1.xor(vn2);
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
    /*
    static private String hexEncode(byte[] input){
        StringBuilder result = new StringBuilder();
        char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};

        for (byte b : input) {
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }

        return result.toString();
    }
    */
}
