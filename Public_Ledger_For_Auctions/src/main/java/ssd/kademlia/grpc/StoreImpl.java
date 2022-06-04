package ssd.kademlia.grpc;


import io.grpc.stub.StreamObserver;
import ssd.StoreReply;
import ssd.StoreServiceGrpc;
import ssd.StoreValue;
import ssd.kademlia.Node;

public class StoreImpl extends StoreServiceGrpc.StoreServiceImplBase {
    public void StoreImpl(StoreValue request, StreamObserver<StoreReply> responseObserver) {
        String value = request.getValue();
        responseObserver.onNext(StoreReply.newBuilder().setReply("true").build());
        responseObserver.onCompleted();
    }
}