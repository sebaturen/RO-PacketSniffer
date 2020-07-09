package com.eclipse.sniffer;

import com.eclipse.sniffer.network.PacketDecryption;
import com.eclipse.sniffer.network.PacketInterceptor;

public class main {

    public static void main(String... args) {

        PacketInterceptor pkInter = new PacketInterceptor();
        PacketDecryption pDecrypt = new PacketDecryption();

        // Packet capture thread
        new Thread(() -> {
            while(true) {
                try {
                    String[] sPacket = pkInter.getNextPacket();
                    new Thread(() -> pDecrypt.decryption(sPacket[0], Integer.parseInt(sPacket[1]))).start();
                } catch (Exception e) {
                    //e.printStackTrace();
                }

            }
        }).start();
    }

}
