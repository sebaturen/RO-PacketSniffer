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

    private static String apiKey;
    private static PacketInterceptor pInter;
    private static final PacketDecryption pDecrypt = new PacketDecryption();
    private static Thread executorNet;
    private static Thread executorRO;
    private static boolean verbose = false;
    private static boolean threadStatus = true;

    public static void main(String... args) {
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
    public static void netPacketAddNotification() {
        if (executorNet != null && executorNet.isAlive()) {
            executorNet.interrupt();
            threadStatus = false;
        }
        executorNet = null;
        executorNet = new Thread(() -> {
            while (threadStatus) {
                //System.out.print("- "+ pInter.packetList.size() +" ");
                while (pInter.packetList.size() != 0) {
                    pDecrypt.decryption(pInter.packetList.remove(0));
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorNet.start();
    }

    /**
     * Start when a new packet is added.
     */
    public static void packetAddNotification() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
                //System.out.print("PNot --");
                ROPacketDetail pd;
                while( (pd = PacketDecryption.getPacket()) != null) {
                    if (verbose) {
                        System.out.println(pd);
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
                            System.out.println("UNKNOWN!");
                            System.out.println(pd);
                            break;
                        case NPC_TALK:
                            //System.out.println(pd);
                            //System.out.println(new String(pd.getContent()));
                            break;
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }


    public static String getApiKey() {
        return apiKey;
    }
}
