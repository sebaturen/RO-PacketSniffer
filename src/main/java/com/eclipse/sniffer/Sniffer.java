package com.eclipse.sniffer;

import com.eclipse.sniffer.enums.PacketList;
import com.eclipse.sniffer.network.PacketDecryption;
import com.eclipse.sniffer.network.PacketInterceptor;

public class Sniffer {

    public static void start() {

        PacketInterceptor pkInter = new PacketInterceptor();
        PacketDecryption pDecrypt = new PacketDecryption();

        // Packet capture thread
        new Thread(() -> {
            while(true) {
                try {
                    //"7B 0A 1C 00 02 00 07 00 95 F1 45 F0 00 00 00 00 01 00 00 00 00 00 00 00 BB A7 5B A1 30 0A 03 C7 03 00 42 75 66 66 20 42 75 66 66 20 41 77 61 79 00 00 2E 3A 3A 64 00 4F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 50 65 6E 67 75 69 6E 50 61 72 61 64 69 73 65 00 69 6A 6F 00 00 00 00 00 4C 69 74 74 6C 65 00 74 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
                    //"48 54 54 50 2F 31 2E 31 20 32 30 30 20 4F 4B 0D 0A 43 6F 6E 74 65 6E 74 2D 54 79 70 65 3A 20 74 65 78 74 2F 70 6C 61 69 6E 0D 0A 4C 61 73 74 2D 4D 6F 64 69 66 69 65 64 3A 20 54 68 75 2C 20 30 32 20 41 70 72 20 32 30 32 30 20 30 30 3A 32 34 3A 32 30 20 47 4D 54 0D 0A 41 63 63 65 70 74 2D 52 61 6E 67 65 73 3A 20 62 79 74 65 73 0D 0A 45 54 61 67 3A 20 22 35 34 61 32 61 37 64 38 35 38 64 36 31 3A 30 22 0D 0A 53 65 72 76 65 72 3A 20 4D 69 63 72 6F 73 6F 66 74 2D 49 49 53 2F 31 30 2E 30 0D 0A 44 61 74 65 3A 20 53 75 6E 2C 20 31 32 20 4A 75 6C 20 32 30 32 30 20 30 32 3A 30 36 3A 33 34 20 47 4D 54 0D 0A 43 6F 6E 74 65 6E 74 2D 4C 65 6E 67 74 68 3A 20 35 0D 0A 0D 0A 61 6C 6C 6F 77";
                    String[] sPacket = pkInter.getNextPacket();
                    pDecrypt.decryption(sPacket[0], Integer.parseInt(sPacket[1]));
                } catch (Exception e) {
                    //e.printStackTrace();
                }

            }
        }).start();
    }

}
