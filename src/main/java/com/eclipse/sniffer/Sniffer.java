package com.eclipse.sniffer;

import com.eclipse.gameDetailsDecrypt.ActorDecrypt;
import com.eclipse.gameDetailsDecrypt.GeneralInfoDecrypt;
import com.eclipse.gameDetailsDecrypt.GuildDetailDecrypt;
import com.eclipse.guildWoeBreaker.WoEBreaker;
import com.eclipse.sniffer.network.PacketDecryption;
import com.eclipse.sniffer.network.ROPacketDetail;
import com.eclipse.sniffer.network.PacketInterceptor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Sniffer {

    private static String[] runArgs;
    public static String apiKey;
    private static PacketInterceptor pInter;
    private static final PacketDecryption pDecrypt = new PacketDecryption();
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static boolean verbose = false;

    public static void main(String... args) {
        runArgs = args;
        pInter = new PacketInterceptor(args[0]);
        apiKey = args[1];
        netPacketAddNotification();
        packetAddNotification();

        if (args.length == 3) {
            verbose = Boolean.parseBoolean(args[2]);
        }
    }

    /**
     * List a new packet exist
     */
    private static void netPacketAddNotification() {
        executor.scheduleAtFixedRate(() -> {
            while (pInter.packetList.size() != 0) {
                pDecrypt.decryption(pInter.packetList.remove(0));
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * Start when a new packet is added.
     */
    private static void packetAddNotification() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
                ROPacketDetail pd;
                while( (pd = PacketDecryption.getPacket()) != null) {
                    if (verbose) {
                        System.out.println(pd +" == "+ PacketDecryption.convertBytesToHex(pd.getContent()));
                    }
                    switch (pd.getName()) {
                        case LOCAL_BROADCAST:
                            WoEBreaker.process(pd);
                            break;
                        case ACTOR_DIED_OR_DISAPPEARED:
                        case ACTOR_EXISTS:
                        case ACTOR_CONNECTED:
                        case ACTOR_MOVE:
                        case SHOW_EQUIP:
                        case ACTOR_INFO_NAME_PARTY_GUILD_TITLE:
                        case ITEM_LIST_START:
                        case ITEM_LIST_STACKABLE:
                        case ITEM_LIST_NON_STACKABLE:
                        case ITEM_LIST_END:
                            ActorDecrypt.process(pd);
                            break;
                        case GUILD_EMBLEM:
                            GuildDetailDecrypt.process(pd);
                            break;
                        case SYSTEM_CHAT: case MAP_CHANGE: case MAP_LOADED: case MAP_LOADED_2: case MAP_CHANGED:
                            GeneralInfoDecrypt.process(pd);
                            break;
                        case UNKNOWN:
                            System.out.print("UNKNOWN! -- "+ pd);
                            break;
                        case NPC_TALK:
                            System.out.println(pd);
                            System.out.println(new String(pd.getContent()));
                            break;
                        case MONSTER_HP_INFO_TINY:
                            //System.out.print(pd +" == "+ Arrays.toString(pd.getContent()) +" // ");
                            //System.out.println(PacketDecryption.convertBytesToHex(pd.getContent()));
                            break;
                    }
                }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public static String getApiKey() {
        return apiKey;
    }
}