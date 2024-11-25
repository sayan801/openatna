package org.openhealthtools.openatna.syslog.mina;

import org.apache.mina.common.IoSession;

import java.net.*;
import java.util.Enumeration;

public class SyslogUtil {

    private SyslogUtil() {
    }

    public static String getHostname(IoSession session) {

        InetAddress inetAddress = ((InetSocketAddress) session.getRemoteAddress()).getAddress();
        if (!inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress()
                && (inetAddress instanceof Inet4Address)) {
            return inetAddress.getHostAddress();

        } else {
            Enumeration<NetworkInterface> networkInterfaces;
            try {
                networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {

                    NetworkInterface networkInterface = networkInterfaces.nextElement();
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress localInetAddress = inetAddresses.nextElement();
                        if (!localInetAddress.isLinkLocalAddress() && !localInetAddress.isLoopbackAddress()
                                && (localInetAddress instanceof Inet4Address)) {
                            return localInetAddress.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                // Nothing to do
            }
        }
        return inetAddress.getHostName();
    }
}
