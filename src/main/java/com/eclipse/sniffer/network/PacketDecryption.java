package com.eclipse.sniffer.network;

import com.eclipse.sniffer.Sniffer;
import com.eclipse.sniffer.tables.RecvPackets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.*;

public class PacketDecryption {

    // Network packet, forum, web or update
    private static final List<String> packetSkip = Arrays.asList("5448", "3234", "7A67");
    private static final List<ROPacketDetail> packList = new ArrayList<>();
    private final Map<Integer, byte[]> delayPacket = new HashMap<>();
    private final List<byte[]> lastPackets = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(PacketDecryption.class);

    public PacketDecryption() {
    }

    /**
     * All packet is in reverse direction [mirror]
     */
    public void decryption(NetPacket netPacket) {

        try {
            packList.addAll(packetSplitter(netPacket));
        } catch (Exception e) {
            logger.info("Failed to decrypt packet "+ e +" PACKET: "+ netPacket +" PORT "+ netPacket.getPort());
            logger.info("Last 5 Packets: ");
            for(byte[] p : lastPackets) {
                logger.info(Arrays.toString(p)+", ");
            }
        }

    }

    private List<ROPacketDetail> packetSplitter(NetPacket netPacket) {

        List<ROPacketDetail> packetDetails = new ArrayList<>();
        //System.out.println(packet);
        byte[] sepContent = netPacket.getContent();
        lastPackets.add(netPacket.getContent());
        if (lastPackets.size() > 5) {
            lastPackets.remove(0);
        }

        try {
            do {
                // Append new packet to delay packet if exist.
                if (delayPacket.containsKey(netPacket.getPort())) {
                    byte[] dPacket = delayPacket.remove(netPacket.getPort());
                    byte[] c = new byte[dPacket.length + sepContent.length];
                    System.arraycopy(dPacket, 0, c, 0, dPacket.length);
                    System.arraycopy(sepContent, 0, c, dPacket.length, sepContent.length);

                    sepContent = c;
                }
                // Check if have a min packet size
                if (sepContent.length < 2) {
                    delayPacket.put(netPacket.getPort(), sepContent);
                    break;
                }
                String pList = convertBytesToHex(new byte[] {sepContent[1], sepContent[0]});
                if (packetSkip.contains(pList)) {
                    continue;
                }
                int pSize = RecvPackets.getPacketSize(pList);
                if (pSize != 0) {
                    int from = 2;
                    int to = pSize;
                    byte[] packetInfo;
                    // For packet unknown length
                    if (pSize == -1) {
                        // Check if this packet have an all information
                        if (sepContent.length < 4) {
                            delayPacket.put(netPacket.getPort(), sepContent);
                            break;
                        }
                        byte[] packetByteSize = {sepContent[3], sepContent[2]};
                        ByteBuffer byteSizeBuffer = ByteBuffer.wrap(packetByteSize);
                        pSize = byteSizeBuffer.getShort();
                        to = pSize;
                        from += 2;
                    }
                    // If the packet info continue in next packet
                    if ((to+from) > sepContent.length) {
                        delayPacket.put(netPacket.getPort(), sepContent);
                        break;
                    }
                    // Save a correct info
                    packetInfo = Arrays.copyOfRange(sepContent, from, to);
                    if (packetInfo.length > 0) {
                        packetDetails.add(new ROPacketDetail(pList, packetInfo, netPacket.getPort()));
                    }
                    sepContent = Arrays.copyOfRange(sepContent, pSize, sepContent.length);
                } else {
                    //System.out.println("UNKNOWN PACKET ["+ pList +"] "+ Arrays.toString(sepContent)); // FULL PACKET -> "+ Arrays.toString(netPacket.getContent()));
                    sepContent = null;
                }
            } while (sepContent != null && sepContent.length > 0);
        } catch (IllegalArgumentException e) {
            logger.error("Error "+ e +" PACKET: "+ Arrays.toString(netPacket.getContent()));
            e.printStackTrace();
            //System.exit(-1);
        }

        return packetDetails;
    }


    /**
     * Convert byte[] -> String [AA BB CC ...]
     * @param bytes
     * @return
     */
    public static String convertBytesToHex(byte[] bytes) {

        StringBuilder result = new StringBuilder();

        for (byte temp : bytes) {
            int decimal = (int) temp & 0xff;  // bytes widen to int, need mask, prevent sign extension
            String hex = Integer.toHexString(decimal).toUpperCase();
            if (hex.length() == 1) {
                hex = "0"+ hex;
            }
            result.append(hex);
        }
        return result.toString();

    }

    public static ROPacketDetail getPacket() {
        //System.out.println(packList);
        if (packList.size() > 0) {
            try {
                return packList.remove(0);
            } catch (UnsupportedOperationException | IndexOutOfBoundsException e) {
                logger.info("Failed to remove RO Packet from list "+ e);
            }
        }
        return null;
    }

    public static int sizePackets() {
        return packList.size();
    }
}
