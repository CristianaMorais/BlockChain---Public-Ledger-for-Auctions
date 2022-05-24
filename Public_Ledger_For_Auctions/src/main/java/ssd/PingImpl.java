package ssd;
import io.grpc.stub.StreamObserver;

public class PingImpl extends PingServiceGrpc.PingServiceImplBase {

    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver){

        PingResponse response = PingResponse.newBuilder().setResponse("true").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}