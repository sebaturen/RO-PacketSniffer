package com.eclipse.sniffer;

import com.eclipse.characters.CharacterDetail;
import com.eclipse.guildWoeBreaker.WoEBreaker;
import com.eclipse.sniffer.network.NetPacket;
import com.eclipse.sniffer.network.PacketDecryption;
import com.eclipse.sniffer.network.ROPacketDetail;
import com.eclipse.sniffer.network.PacketInterceptor;

import java.util.List;

public class Sniffer {

    public static void main(String... args) {

        PacketInterceptor pkInter = new PacketInterceptor();
        PacketDecryption pDecrypt = new PacketDecryption();

        // Packet capture thread
        new Thread(() -> {
            while(true) {
                try {
                    NetPacket netPacket = pkInter.getNextPacket();
                    if (netPacket != null) {
                        pDecrypt.decryption(netPacket);
                    }
                } catch (Exception e) {
                    // Exception
                }

            }
        }).start();

    }

    public static void packetAddNotification() {

        // Sniffer packets:
        new Thread(() -> {

            do {
                ROPacketDetail pd = PacketDecryption.getPacket();

                if (pd != null) {
                    switch (pd.getName()) {
                        case LOCAL_BROADCAST:
                            WoEBreaker.process(pd);
                            break;
                        case ACTOR_EXISTS: case ACTOR_CONNECTED: case ACTOR_MOVE:
                            CharacterDetail.process(pd);
                            break;
                        case UNKNOWN:
                            System.out.println(pd);
                    }
                }

            } while (PacketDecryption.sizePackets() > 0);
        }).start();

    }

}
