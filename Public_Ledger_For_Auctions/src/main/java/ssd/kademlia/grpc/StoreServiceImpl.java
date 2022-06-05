package ssd.kademlia.grpc;


import io.grpc.stub.StreamObserver;
import ssd.StoreHash;
import ssd.StoreReply;
import ssd.StoreServiceGrpc;
import ssd.kademlia.Node;

public class StoreServiceImpl extends StoreServiceGrpc.StoreServiceImplBase {

    @Override
    public void store(StoreHash request, StreamObserver<StoreReply> responseObserver){
        System.out.println("We are in Store.");
        String nodeid = request.getHashNode();
        String ip_addr = request.getIp();
        int port = request.getPort();
        Node n = new Node(ip_addr, port, nodeid);
        Node.getNode().addAdjNode(n);


        StoreReply response = StoreReply.newBuilder().setReply("true").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}