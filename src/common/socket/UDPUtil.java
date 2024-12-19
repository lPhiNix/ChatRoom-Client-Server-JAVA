package common.socket;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPUtil {
    public static String getLocalIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static String getLocalHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    public static DatagramSocket createSocket() throws SocketException {
        return new DatagramSocket();
    }

    public static DatagramSocket createSocket(int port) throws SocketException {
        return new DatagramSocket(port);
    }

    public static InetAddress getInetAddressByName(String host) throws UnknownHostException {
        return InetAddress.getByName(host);
    }
}
