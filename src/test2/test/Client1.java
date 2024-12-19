package test2.test;

import test2.client.AbstractUDPClient;
import test2.client.UDPClient;
import test2.common.model.User;

import java.net.InetSocketAddress;

public class Client1 {
    public static void main(String[] args) {
        User user = UDPClient.promptUserDetails();
        UDPClient client = new UDPClient(new InetSocketAddress("localhost", 12345), user);
        client.connect();
        client.start();
    }
}
