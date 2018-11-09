package fr.icefeather.sip.sender.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

public class SipSender {

    public static void send(String toHost, int toPort, String toService, String fromUser, int fromPort, String message) throws IOException {

        String fromHost = InetAddress.getLocalHost().getHostAddress();

        StringBuilder trame = new StringBuilder();
        trame.append("MESSAGE sip:").append(toService).append("@").append(toHost).append(":").append(toPort).append(" SIP/2.0").append("\n");
        trame.append("Via: SIP/2.0/UDP ").append(fromHost).append(":").append(fromPort).append(";rport;branch=").append(UUID.randomUUID()).append("\n");
        trame.append("From: \"").append(fromUser).append("\" <sip:").append(toService).append("@").append(fromHost).append(":").append(fromPort).append(">;tag=").append(UUID.randomUUID()).append("\n");
        trame.append("Content-Length: ").append(message.getBytes("UTF-8").length).append("\n");
        trame.append("\n");
        trame.append(message);

        byte[] trameBytes = trame.toString().getBytes();

        UdpSender.send(toHost, toPort, trameBytes);
    }

}
