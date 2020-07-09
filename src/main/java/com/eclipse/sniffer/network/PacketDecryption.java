package com.eclipse.sniffer.network;

import com.eclipse.sniffer.enums.PacketList;
import com.eclipse.sniffer.tables.RecvPackets;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PacketDecryption {

    public PacketDecryption() {

    }

    /**
     * All packet is in reverse direction [mirror]
     */
    public void decryption(String packet, int port) {

        packetSplitter(packet);

    }

    private Map<String, String[]> packetSplitter(String packet) {

        String[] sepContent = packet.split(" ");

        do {
            String pList = sepContent[1]+sepContent[0];
            int pSize = RecvPackets.getPacketSize(pList);
            if (pSize != 0) {
                int startLoc = 2;
                if (pSize == -1) {
                    pSize = new BigInteger(sepContent[3]+sepContent[2], 16).intValue();
                    startLoc += startLoc;
                }
                List<String> packetInfo = Arrays.asList(Arrays.copyOfRange(sepContent, startLoc, startLoc+pSize-2));
                sepContent = Arrays.copyOfRange(sepContent, pSize, sepContent.length);
                System.out.println(PacketList.getValue(pList) +" ("+ pList +") -> {"+ pSize +"} => "+ packetInfo);
            } else {
                System.out.println("UNKNOWN PACKET "+ Arrays.toString(sepContent));
                sepContent = null;
            }
        } while (sepContent != null && sepContent.length > 0);

        return null;
    }
}
