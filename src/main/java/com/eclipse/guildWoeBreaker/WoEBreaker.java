package com.eclipse.guildWoeBreaker;

import com.eclipse.sniffer.Sniffer;
import com.eclipse.sniffer.enums.PacketList;
import com.eclipse.sniffer.network.PacketDecryption;
import com.eclipse.sniffer.network.PacketDetail;

import java.util.Arrays;
import java.util.List;

public class WoEBreaker {

    public static final int START_MSG = 11;

    public static void process (PacketDetail pd) {

        List<String> contentBroadcast = pd.getContent();
        System.out.println(hexToAscii(contentBroadcast.subList(START_MSG, contentBroadcast.size())));

    }

    private static String hexToAscii(List<String> hexStringList) {
        String hexStr = String.join("", hexStringList).toLowerCase();

        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }
}
