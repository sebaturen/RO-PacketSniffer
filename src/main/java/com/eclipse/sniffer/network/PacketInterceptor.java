package com.eclipse.sniffer.network;

import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;

public class PacketInterceptor {

    public List<NetPacket> packetList = new ArrayList<>();
    public static final int SNAP_LEN = 65536;
    public static final int TIMEOUT = 10;
    public static final int BUFFER_SIZE = 1024 * 1024;// * 1024;

    public PacketInterceptor() {
        prepareHandle();
    }

    /**
     * Packet listener
     */
    private PacketListener listener = packet -> {
        if (packet.contains(IpV4Packet.class)) {

            IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
            Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
            if (srcAddr.getHostAddress().matches("128.241.[0-9]*.[0-9]*")) {

                TcpPacket.TcpHeader tcpPacket = packet.get(TcpPacket.class).getHeader();
                byte[] evPacket = packet.get(TcpPacket.class).getPayload().getRawData();

                NetPacket netPacket = new NetPacket(evPacket, tcpPacket.getDstPort().valueAsInt());
                packetList.add(netPacket);
            }
        }
    };

    /**
     * Prepare NIC handle
     * @return
     */
    private void prepareHandle() {

        try {

            InetAddress addr = InetAddress.getByName("192.168.100.119");
            //InetAddress addr = InetAddress.getByName("10.0.0.128");
            PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);

            PcapHandle.Builder phb = new PcapHandle.Builder(nif.getName())
                    .snaplen(SNAP_LEN)
                    .promiscuousMode(PcapNetworkInterface.PromiscuousMode.PROMISCUOUS)
                    .timeoutMillis(TIMEOUT)
                    .bufferSize(BUFFER_SIZE);
                    //.timestampPrecision(PcapHandle.TimestampPrecision.NANO);

            // Loop sniffer
            new Thread(() -> {
                try (PcapHandle handle = phb.build()) {
                    handle.loop(-1, listener, Runnable::run);
                } catch (InterruptedException | NotOpenException | PcapNativeException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (PcapNativeException | UnknownHostException e) {
            e.printStackTrace();
        }

    }


}
