package com.eclipse.gameDetailsDecrypt;

import com.eclipse.sniffer.Sniffer;
import com.eclipse.sniffer.enums.PacketList;
import com.eclipse.sniffer.network.ROPacketDetail;
import com.google.gson.JsonObject;
import junit.framework.TestCase;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GuildDetailDecryptTest extends TestCase {

    @Test
    public void testWoeBreakerMSG() throws InterruptedException {
        JsonObject breakInfo = new JsonObject();
        System.out.println(breakInfo);
        Sniffer.apiKey = "GriPd5rXzRTjS3ZO4xw8ekVTQMgipSUX123";
        Calendar tim = Calendar.getInstance();

        // WOE PACKET
        ROPacketDetail pd;
        pd = new ROPacketDetail(PacketList.SYSTEM_CHAT.getValue(), "The [Valfreyja 1] stronghold of Mardol is occupied by the [Full NPC] Guild.".getBytes(), 111);
        tim.setTimeInMillis(1599426003880L);
        pd.setTimestamp(tim.getTime());
        GuildDetailDecrypt.process(pd);

        Thread.sleep(100000);
    }

    @Test
    public void randomTest() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println(sdf.format(new Date()));
    }
}