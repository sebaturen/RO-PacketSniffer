package com.eclipse.sniffer;

import com.eclipse.apiRequest.APIRequest;
import com.eclipse.gameDetails.Actor;
import com.eclipse.gameDetails.GeneralInfo;
import com.eclipse.gameDetails.GuildDetail;
import com.eclipse.guildWoeBreaker.WoEBreaker;
import com.eclipse.sniffer.enums.PacketList;
import com.eclipse.sniffer.network.NetPacket;
import com.eclipse.sniffer.network.PacketDecryption;
import com.eclipse.sniffer.network.ROPacketDetail;
import com.eclipse.sniffer.network.PacketInterceptor;
import com.google.gson.JsonObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Sniffer {

    private static PacketInterceptor pInter;
    private static PacketDecryption pDecrypt = new PacketDecryption();
    private static boolean verbose = false;

    public static void main(String... args) {

        pInter = new PacketInterceptor(args[0]);
        netPacketAddNotification();
        packetAddNotification();

        if (args.length == 2) {
            verbose = Boolean.parseBoolean(args[1]);
        }

    }

    /**
     * List a new packet exist
     */
    public static void netPacketAddNotification() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            while (pInter.packetList.size() != 0) {
                NetPacket np = pInter.packetList.remove(0);
                pDecrypt.decryption(np);
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * Start when a new packet is added.
     */
    public static void packetAddNotification() {

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {

            ROPacketDetail pd;
            while( (pd = PacketDecryption.getPacket()) != null) {
                if (verbose) {
                    System.out.println(pd);
                }
                switch (pd.getName()) {
                    case LOCAL_BROADCAST:
                        WoEBreaker.process(pd);
                        break;
                    case ACTOR_EXISTS:
                    case ACTOR_CONNECTED:
                    case ACTOR_MOVE:
                    case SHOW_EQUIP:
                    case ACTOR_INFO_NAME_PARTY_GUILD_TITLE:
                        Actor.process(pd);
                        break;
                    case GUILD_EMBLEM:
                        GuildDetail.process(pd);
                        break;
                    case SYSTEM_CHAT:
                        GeneralInfo.process(pd);
                        break;
                    case UNKNOWN:
                        System.out.println("UNKNOWN!");
                        System.out.println(pd);
                        break;
                }
            }

        }, 0, 100, TimeUnit.MILLISECONDS);
    }

}
