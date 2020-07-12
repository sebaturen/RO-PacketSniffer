package com.eclipse.sniffer.network;

import com.eclipse.sniffer.enums.PacketList;
import com.eclipse.sniffer.tables.RecvPackets;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;

public class PacketDecryption {

    private static final List<PacketDetail> packList = new ArrayList<>();
    private String[] delayPacket;
    private int delayPacketPort;

    public PacketDecryption() {
    }

    /**
     * All packet is in reverse direction [mirror]
     */
    public void decryption(String packets, int port) {

        for (PacketDetail pd : packetSplitter(packets, port)) {
            try {
                //System.out.println(pd);
                if (pd.getName() == PacketList.LOCAL_BROADCAST) {
                    System.out.println((new Date()) +" "+ pd.getContent());
                    String ab = hexToAscii(String.join("", pd.getContent()).toLowerCase());
                    String pattern = ".*The \\[[aA-zZ].*\\] castle has been conquered by the \\[[aA-zZ].*\\] guild.";
                    if (ab.matches(pattern)) {
                        System.out.println((new Date()) +" "+ ab);
                    }
                }
                if (pd.getName() == PacketList.UNKNOWN) {
                    System.out.println(pd);
                }
            } catch (Exception e) {
                System.out.println("ERROR! "+ e);
            }
        }

    }

    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

    private List<PacketDetail> packetSplitter(String packet, int port) {

        List<PacketDetail> packetDetails = new ArrayList<>();
        //System.out.println(packet);
        String[] sepContent = packet.split(" ");

        try {
            do {
                // Append new packet to delay packet if exist.
                if (delayPacket != null) {
                    if (port == delayPacketPort) {
                        List<String> appendPacket = new ArrayList<>();
                        appendPacket.addAll(Arrays.asList(delayPacket));
                        appendPacket.addAll(Arrays.asList(sepContent));
                        sepContent = appendPacket.toArray(sepContent);
                        delayPacket = null;
                    }
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
                        delayPacket = sepContent;
                        delayPacketPort = port;
                        sepContent = null;
                    } else {
                        // Save a correct info
                        packetInfo = Arrays.asList(Arrays.copyOfRange(sepContent, from, to));
                        packetDetails.add(new PacketDetail(PacketList.getValue(pList), pList, packetInfo, port));
                        sepContent = Arrays.copyOfRange(sepContent, pSize, sepContent.length);
                    }
                } else {
                    System.out.println("UNKNOWN PACKET "+ Arrays.toString(sepContent) +" FULL PACKET -> "+ packet);
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
            System.out.println("removed?");
            return packList.remove(0);
        }
        return null;
    }
}
