package main;

import client.UDPClient;
import common.model.User;

import java.net.InetSocketAddress;

public class Client1 {
    public static void main(String[] args) {
        User user = UDPClient.promptUserDetails(12345);
        UDPClient client = new UDPClient(new InetSocketAddress("localhost", 12345), user);
        client.connect();
        client.start();
    }
}
