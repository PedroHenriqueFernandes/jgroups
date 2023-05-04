package pt.ipb.dsys.peer.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DnsHelper {

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}
