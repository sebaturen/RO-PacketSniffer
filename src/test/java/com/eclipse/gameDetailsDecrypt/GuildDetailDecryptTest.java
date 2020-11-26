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
        Sniffer.apiKey = "GriPd5rXzRTjS3ZO4xw8ekVTQMgipSUX---";
        Calendar tim = Calendar.getInstance();

        // WOE PACKET
        ROPacketDetail pd;
        pd = new ROPacketDetail(PacketList.SYSTEM_CHAT.getValue(), "The [Valfreyja 1] stronghold of Mardol is occupied by the [o pai ta on ~] Guild.".getBytes(), 111); tim.setTimeInMillis(1603062002604L); pd.setTimestamp(tim.getTime()); GuildDetailDecrypt.process(pd);
        pd = new ROPacketDetail(PacketList.SYSTEM_CHAT.getValue(), "The [Valfreyja 2] stronghold of Cyr is occupied by the [Igreja de Ubatuba] Guild.".getBytes(), 111); tim.setTimeInMillis(1603062002605L); pd.setTimestamp(tim.getTime()); GuildDetailDecrypt.process(pd);
        pd = new ROPacketDetail(PacketList.SYSTEM_CHAT.getValue(), "The [Valfreyja 4] stronghold of Gefn is occupied by the [p2w] Guild.".getBytes(), 111); tim.setTimeInMillis(1603062002605L); pd.setTimestamp(tim.getTime()); GuildDetailDecrypt.process(pd);
        pd = new ROPacketDetail(PacketList.SYSTEM_CHAT.getValue(), "The [Valfreyja 3] stronghold of Horn is occupied by the [AlphaOmega] Guild.".getBytes(), 111); tim.setTimeInMillis(1603062002605L); pd.setTimestamp(tim.getTime()); GuildDetailDecrypt.process(pd);
        pd = new ROPacketDetail(PacketList.SYSTEM_CHAT.getValue(), "The [Valfreyja 5] stronghold of Badanis is occupied by the [Freshmen] Guild.".getBytes(), 111); tim.setTimeInMillis(1603062002605L); pd.setTimestamp(tim.getTime()); GuildDetailDecrypt.process(pd);
        pd = new ROPacketDetail(PacketList.SYSTEM_CHAT.getValue(), "The [Nithafjoll 1] stronghold of Himinn is occupied by the [o pai ta on ~] Guild.".getBytes(), 111); tim.setTimeInMillis(1603062003704L); pd.setTimestamp(tim.getTime()); GuildDetailDecrypt.process(pd);
        pd = new ROPacketDetail(PacketList.SYSTEM_CHAT.getValue(), "The [Nithafjoll 4] stronghold of Hljod is occupied by the [AlphaOmega] Guild.".getBytes(), 111); tim.setTimeInMillis(1603062004003L); pd.setTimestamp(tim.getTime()); GuildDetailDecrypt.process(pd);
        pd = new ROPacketDetail(PacketList.SYSTEM_CHAT.getValue(), "The [Nithafjoll 3] stronghold of Vidblainn is occupied by the [Se Deixar Eu Boto] Guild.".getBytes(), 111); tim.setTimeInMillis(1603062004003L); pd.setTimestamp(tim.getTime()); GuildDetailDecrypt.process(pd);
        pd = new ROPacketDetail(PacketList.SYSTEM_CHAT.getValue(), "The [Nithafjoll 5] stronghold of Skidbladnir is occupied by the [Double Barrel] Guild.".getBytes(), 111); tim.setTimeInMillis(1603062004004L); pd.setTimestamp(tim.getTime()); GuildDetailDecrypt.process(pd);
        pd = new ROPacketDetail(PacketList.SYSTEM_CHAT.getValue(), "The [Nithafjoll 2] stronghold of Andlangr is occupied by the [p2w] Guild.".getBytes(), 111); tim.setTimeInMillis(1603062004003L); pd.setTimestamp(tim.getTime()); GuildDetailDecrypt.process(pd);

        Thread.sleep(100000);
    }

    @Test
    public void randomTest() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println(sdf.format(new Date()));
    }
}