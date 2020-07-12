package com.eclipse.guildWoeBreaker;

import com.eclipse.sniffer.Sniffer;
import com.eclipse.sniffer.enums.PacketList;
import com.eclipse.sniffer.network.PacketDecryption;
import com.eclipse.sniffer.network.PacketDetail;

public class main {

    public static void main (String... args) {
        Sniffer.start();
    }
}
