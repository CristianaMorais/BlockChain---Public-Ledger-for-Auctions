package ssd;

import java.net.InetAddress;
import java.net.UnknownHostException;


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
