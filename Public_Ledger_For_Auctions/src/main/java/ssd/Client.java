package ssd;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import ssd.kademlia.Node;

public class Client {

    private static final Logger logger = Logger.getLogger(Client.class.getName());
    public static Node node;

    public static void main(String[] args) throws UnknownHostException, Exception {
        try {
            InetAddress a = InetAddress.getLocalHost();
            //String uniqueID = UUID.randomUUID().toString();
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            String num = Integer.valueOf(random.nextInt()).toString();
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result =  sha.digest(num.getBytes());

            System.out.println("IP address: " + a.getHostAddress());
            System.out.println("UniqueID: " + hexEncode(result));

        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        ManagedChannel channel = ManagedChannelBuilder.forTarget("50051")
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
        try {
            Client client = new Client(channel);
            client.ping();
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    static private String hexEncode(byte[] input){
        StringBuilder result = new StringBuilder();
        char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};

        for (byte b : input) {
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }

        return result.toString();
    }

    // Parte do Kademlia
    private PingServiceGrpc.PingServiceBlockingStub blockingStub;

    public Client(Channel channel) {
        blockingStub = PingServiceGrpc.newBlockingStub(channel);
    }

    public boolean ping() {
        logger.info("Will try ping");
        PingRequest request = PingRequest.newBuilder().setRequest("ping").build();
        PingResponse response;

        try {
            response = blockingStub.ping(request);
            if (response.getResponse().equals("true")) return true;
        }

        catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return false;
        }

        logger.info("Ping: " + response.getResponse());
        return false;
    }
}
