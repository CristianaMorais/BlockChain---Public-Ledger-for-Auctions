package ssd.kademlia.grpc;
import io.grpc.stub.StreamObserver;
import ssd.PingRequest;
import ssd.PingResponse;
import ssd.PingServiceGrpc;

public class PingServiceImpl extends PingServiceGrpc.PingServiceImplBase {

    public static void pingService(PingRequest request, StreamObserver<PingResponse> responseObserver){

        PingResponse response = PingResponse.newBuilder().setResponse("true").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}