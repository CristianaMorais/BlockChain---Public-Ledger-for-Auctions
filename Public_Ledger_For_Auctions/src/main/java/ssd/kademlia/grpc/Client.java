package ssd.kademlia.grpc;

import io.grpc.Channel;
import ssd.PingServiceGrpc;
import java.util.logging.Logger;

public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    private final PingServiceGrpc.PingServiceBlockingStub blockingStub;

    public Client(Channel channel) {
        blockingStub = PingServiceGrpc.newBlockingStub(channel);
    }

    public static void main(String[] args) throws Exception {

    }
}