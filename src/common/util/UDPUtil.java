package common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPUtil {
    public static String getLocalIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

}
