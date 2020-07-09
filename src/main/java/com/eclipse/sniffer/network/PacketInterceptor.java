package com.eclipse.sniffer.network;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class PacketInterceptor {

    private PcapHandle handle;

    public PacketInterceptor() {
        handle = getHandle();
    }

    public String[] getNextPacket() throws Exception {

        Packet packet = handle.getNextPacketEx();
        if (packet.contains(IpV4Packet.class)) {

            IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
            Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
            if (srcAddr.getHostAddress().matches("128.241.[0-9]*.[0-9]*")) {

                TcpPacket.TcpHeader tcpPacket = packet.get(TcpPacket.class).getHeader();
                byte[] evPacket = packet.get(TcpPacket.class).getPayload().getRawData();

                return new String[]{ convertBytesToHex(evPacket), ""+tcpPacket.getDstPort().valueAsInt() };

            }

        }

        throw new Exception("not correct type");

    }

    /**
     * Prepare NIC handle
     * @return
     */
    private PcapHandle getHandle() {

        try {

            InetAddress addr = InetAddress.getByName("192.168.100.119");
            //InetAddress addr = InetAddress.getByName("10.0.0.128");
            PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);

            int snapLen = 65536;
            PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
            int timeout = 10;
            handle = nif.openLive(snapLen, mode, timeout);
            return handle;

        } catch (UnknownHostException | PcapNativeException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Convert byte[] -> String [AA BB CC ...]
     * @param bytes
     * @return
     */
    private String convertBytesToHex(byte[] bytes) {

        StringBuilder result = new StringBuilder();

        for (byte temp : bytes) {

            int decimal = (int) temp & 0xff;  // bytes widen to int, need mask, prevent sign extension


            String hex = Integer.toHexString(decimal).toUpperCase();

            if (hex.length() == 1) {
                hex = "0"+ hex;
            }

            result.append(hex+" ");

        }
        return result.toString();


    }

}
