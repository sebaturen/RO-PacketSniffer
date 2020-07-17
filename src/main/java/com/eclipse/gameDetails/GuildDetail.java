package com.eclipse.gameDetails;

import com.eclipse.apiRequest.APIRequest;
import com.eclipse.sniffer.network.NetPacket;
import com.eclipse.sniffer.network.PacketDecryption;
import com.eclipse.sniffer.network.ROPacketDetail;
import com.google.gson.JsonObject;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class GuildDetail {

    public static final int ACCOUNTS_ID_START = 0;
    public static final int CHAR_NAME_START = 4;
    public static final int GUILD_NAME_START = 52;

    public static void process(ROPacketDetail pd) {
        GuildDetail gd = new GuildDetail();

        gd.processGuildInfo(pd);

    }

    /**
     * '0A30' => ['actor_info', 'a4 Z24 Z24 Z24 Z24 V', [qw(ID name partyName guildName guildTitle titleID)]],
     * @param pd
     */
    private void processGuildInfo(ROPacketDetail pd) {

        new Thread(() -> {
            byte[] inf = pd.getContent();
            byte[] bAccId = NetPacket.reverseContent(Arrays.copyOfRange(inf, ACCOUNTS_ID_START, ACCOUNTS_ID_START+4));
            int charNamePostEnd = CHAR_NAME_START;
            for(byte cName : Arrays.copyOfRange(inf, CHAR_NAME_START, inf.length)) {
                if (cName == 0) {
                    break;
                }
                charNamePostEnd++;
            }
            int guildNamePostEnd = GUILD_NAME_START;
            for(byte gName : Arrays.copyOfRange(inf, GUILD_NAME_START, inf.length)) {
                if (gName == 0) {
                    break;
                }
                guildNamePostEnd++;
            }
            byte[] bGuildName = Arrays.copyOfRange(inf, GUILD_NAME_START, guildNamePostEnd);
            byte[] bName = Arrays.copyOfRange(inf, CHAR_NAME_START, charNamePostEnd);

            int accId = (ByteBuffer.wrap(bAccId)).getInt();
            String charName = new String(bName);
            String guildName = new String(bGuildName);

            if (guildName.length() > 0) {

                JsonObject guildInfo = new JsonObject();
                guildInfo.addProperty("account_id", accId);
                guildInfo.addProperty("character_name", charName);
                guildInfo.addProperty("guild_name", guildName);

                APIRequest.shared.PUT("/guilds/"+ accId, guildInfo);

                System.out.println(guildInfo);

            }

        }).start();

    }

}
