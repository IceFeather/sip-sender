package fr.icefeather.sip.sender.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSender {

    public static void send(String host, int port, byte[] data) throws IOException {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(host);
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, port);
        clientSocket.send(sendPacket);
        clientSocket.close();
    }

}
