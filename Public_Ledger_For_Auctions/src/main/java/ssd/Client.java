package ssd;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Client {


    public static void main(String[] args)  {
        try {
            InetAddress a = InetAddress.getLocalHost();
            System.out.println("IP address: " + a.getHostAddress());

        }

        catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }



    // Parte do Kademlia
}
