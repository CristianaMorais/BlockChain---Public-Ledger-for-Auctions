package ssd.kademlia.grpc;

import io.grpc.stub.StreamObserver;
import ssd.NodeInfo;
import ssd.PingRequest;
import ssd.PingServiceGrpc;

public class PingServiceImpl extends PingServiceGrpc.PingServiceImplBase {

    @Override
    public void ping(PingRequest request, StreamObserver<NodeInfo> responseObserver){

        //System.out.println(request);

        //System.out.println("Ping " + request.getSender().getNodeid());

        NodeInfo response = NodeInfo.newBuilder()
                .setNodeid(request.getSender().getNodeid())
                .setIp(request.getSender().getIp())
                .setPort(request.getSender().getPort())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}