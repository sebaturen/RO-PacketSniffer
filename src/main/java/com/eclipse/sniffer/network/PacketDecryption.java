package com.eclipse.sniffer.network;

import com.eclipse.sniffer.Sniffer;
import com.eclipse.sniffer.enums.PacketList;
import com.eclipse.sniffer.tables.RecvPackets;

import java.math.BigInteger;
import java.util.*;

public class PacketDecryption {

    private static final List<PacketDetail> packList = new ArrayList<>();
    private Map<Integer, String[]> delayPacket = new HashMap<>();
    private List<String> lastPackets = new ArrayList<>();

    public PacketDecryption() {

    }

    /**
     * All packet is in reverse direction [mirror]
     */
    public void decryption(String packets, int port) {

        packList.addAll(packetSplitter(packets, port));
        Sniffer.packetAddNotification();

    }

    private List<PacketDetail> packetSplitter(String packet, int port) {

        List<PacketDetail> packetDetails = new ArrayList<>();
        //System.out.println(packet);
        String[] sepContent = packet.split(" ");
        lastPackets.add(packet);
        if (lastPackets.size() > 5) {
            lastPackets.remove(0);
        }

        try {
            do {
                // Append new packet to delay packet if exist.
                if (delayPacket.containsKey(port)) {
                    String[] dPacket = delayPacket.remove(port);
                    List<String> appendPacket = new ArrayList<>();
                    appendPacket.addAll(Arrays.asList(dPacket));
                    appendPacket.addAll(Arrays.asList(sepContent));
                    sepContent = appendPacket.toArray(sepContent);
                }
                String pList = sepContent[1]+sepContent[0];
                int pSize = RecvPackets.getPacketSize(pList);
                if (pSize != 0) {
                    int from = 2;
                    int to = pSize;
                    List<String> packetInfo;
                    // For packet unknown length
                    if (pSize == -1) {
                        pSize = new BigInteger(sepContent[3]+sepContent[2], 16).intValue();
                        to = pSize;
                        from += 2;
                    }
                    // If the packet info continue in next packet
                    if (to > sepContent.length) {
                        delayPacket.put(port, sepContent);
                        sepContent = null;
                    } else {
                        // Save a correct info
                        packetInfo = Arrays.asList(Arrays.copyOfRange(sepContent, from, to));
                        packetDetails.add(new PacketDetail(PacketList.getValue(pList), pList, packetInfo, port));
                        sepContent = Arrays.copyOfRange(sepContent, pSize, sepContent.length);
                    }
                } else {
                    System.out.println("UNKNOWN PACKET "+ Arrays.toString(sepContent) +" FULL PACKET -> "+ packet);
                    System.out.println("LAST 5 PACKETS! "+ lastPackets);
                    sepContent = null;
                }
            } while (sepContent != null && sepContent.length > 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Error "+ e);
            System.out.println("Packet: "+ packet);
            System.exit(-1);
        }

        return packetDetails;
    }

    public static PacketDetail getPacket() {
        //System.out.println(packList);
        if (packList.size() > 0) {
            return packList.remove(0);
        }
        return null;
    }

    public static int sizePackets() {
        return packList.size();
    }
}
