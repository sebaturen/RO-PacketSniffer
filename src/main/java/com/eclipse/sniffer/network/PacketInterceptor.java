package com.eclipse.sniffer.network;

import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class PacketInterceptor {

    private PcapHandle handle;

    public PacketInterceptor() {
        handle = getHandle();
    }

    public NetPacket getNextPacket() throws PcapNativeException, NotOpenException, EOFException, TimeoutException {

        Packet packet = handle.getNextPacketEx();
        if (packet.contains(IpV4Packet.class)) {

            IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
            Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
            if (srcAddr.getHostAddress().matches("128.241.[0-9]*.[0-9]*")) {

                TcpPacket.TcpHeader tcpPacket = packet.get(TcpPacket.class).getHeader();
                byte[] evPacket = packet.get(TcpPacket.class).getPayload().getRawData();

                return new NetPacket(evPacket, tcpPacket.getDstPort().valueAsInt());
            }

        }

        return null;

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


}
