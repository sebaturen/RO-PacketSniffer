package com.eclipse.sniffer.network;

import com.eclipse.sniffer.enums.PacketList;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class PacketDecryptionTest extends TestCase {

    @Test
    public void packetSplitterTest() {

        PacketDecryption pD = new PacketDecryption();

        List<PacketDetail> resultTestOne = new ArrayList<>();
        resultTestOne.add(new PacketDetail(PacketList.PARTY_HP_INFO, PacketList.PARTY_HP_INFO.toString(), Arrays.asList("D0", "BD", "04", "00", "57", "12", "00", "00", "57", "12", "00", "00"), 111));
        resultTestOne.add(new PacketDetail(PacketList.PARTY_HP_INFO, PacketList.PARTY_HP_INFO.toString(), Arrays.asList("D6", "BB", "04", "00", "AE", "17", "00", "00", "AE", "17", "00", "00"), 111));

        //Assert.assertArrayEquals(resultTestOne.toArray(), pD.packetSplitter("0E 08 D0 BD 04 00 57 12 00 00 57 12 00 00 0E 08 D6 BB 04 00 AE 17 00 00 AE 17 00 00", 111).toArray());


        List<PacketDetail> resultTestTwo = new ArrayList<>();
        resultTestTwo.add(new PacketDetail(PacketList.ACTOR_MOVE, PacketList.ACTOR_MOVE.toString(), Arrays.asList("05", "F6", "1C", "00", "00", "00", "00",
                "00", "00", "A5", "00", "00", "00", "00", "00", "00", "00", "00", "00", "5C", "05", "00", "00", "00", "00",
                "00", "00", "00", "00", "00", "00", "00", "00", "36", "BD", "01", "01", "00", "00", "00", "00", "00", "00",
                "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                "00", "00", "1F", "CC", "32", "08", "BC", "96", "00", "00", "50", "00", "00", "00", "FF", "FF", "FF", "FF",
                "FF", "FF", "FF", "FF", "00", "00", "00", "47", "6F", "61", "74"), 111));

        /*Assert.assertArrayEquals(resultTestTwo.toArray(), pD.packetSplitter("FD 09 5E 00 05 F6 1C 00 00 00 00 00 00 A5 00 00 00 00 00 00 00 00 00 " +
                "5C 05 00 00 00 00 00 00 00 00 00 00 00 00 36 BD 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 1F CC 32 08 BC 96 00 00 50 00 00 00 FF FF FF FF FF FF FF FF 00 00 00 47 6F 61 74", 111).toArray());

        /*
            pDecrypt.decryption("0E 08 D0 BD 04 00 57 12 00 00 57 12 00 00 0E 08 D6 BB 04 00 AE 17 00 00 AE 17 00 00", 123);
            pDecrypt.decryption("FD 09 5E 00 05 F6 1C 00 00 00 00 00 00 A5 00 00 00 00 00 00 00 00 00 " +
                    "5C 05 00 00 00 00 00 00 00 00 00 00 00 00 36 BD 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                    "00 00 00 00 00 00 00 00 1F CC 32 08 BC 96 00 00 50 00 00 00 FF FF FF FF FF FF FF FF 00 00 00 47 6F 61 74", 123);

            pDecrypt.decryption("69 00 6F 00 6D 6C EF F6 55 A6 04 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                    "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 80 F1 5C C4 94 11 50", 123);
            pDecrypt.decryption("6F 72 69 6E 67 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FB 07 00 00 00 00 80 F1 5C AB 94 " +
                    "11 53 61 6B 72 61 79 2D 54 65 73 74 20 53 65 72 76 65 72 00 00 00 00 00 00 00 00", 123);
         */


    }

}