package com.eclipse.gameDetailsDecrypt;

import com.eclipse.sniffer.enums.PacketList;
import com.eclipse.sniffer.network.ROPacketDetail;
import com.google.gson.JsonObject;
import junit.framework.TestCase;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GuildDetailDecryptTest extends TestCase {

    @Test
    public void testWoeBreakerMSG() {
        JsonObject breakInfo = new JsonObject();
        System.out.println("br"+ breakInfo);
        ROPacketDetail pd = new ROPacketDetail(
                PacketList.SYSTEM_CHAT.getValue(),
                "The [Power Rangers] guild conquered the [Nithafjoll 4] stronghold of Hljod.".getBytes(),
                111
        );
        GuildDetailDecrypt.process(pd);
    }

    @Test
    public void randomTest() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println(sdf.format(new Date()));
    }
}