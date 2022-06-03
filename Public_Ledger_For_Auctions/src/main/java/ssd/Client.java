package ssd;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Client {

    public static void main(String[] args) throws UnknownHostException {
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
}
