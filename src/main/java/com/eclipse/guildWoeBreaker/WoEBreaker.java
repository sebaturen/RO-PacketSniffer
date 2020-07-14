package com.eclipse.guildWoeBreaker;

import com.eclipse.sniffer.Sniffer;
import com.eclipse.sniffer.network.ROPacketDetail;

import java.util.Arrays;
import java.util.List;

/**
 * '01C3' => ['local_broadcast', 'v V v4 Z*', [qw(len color font_type font_size font_align font_y message)]],
 * MESSAGE [11-n]
 */
public class WoEBreaker {

    public static final int MESSAGE_START = 11;

    public static void process (ROPacketDetail pd) {

        new Thread(() -> {
            byte[] msgBroadcast = Arrays.copyOfRange(pd.getContent(), MESSAGE_START, pd.getContent().length);
            System.out.println(new String(msgBroadcast));
        }).start();

    }
}
