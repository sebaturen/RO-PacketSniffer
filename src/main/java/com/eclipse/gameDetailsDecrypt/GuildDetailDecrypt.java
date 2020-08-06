package com.eclipse.gameDetailsDecrypt;

import com.eclipse.apiRequest.APIRequest;
import com.eclipse.apiRequest.APIRequestQueue;
import com.eclipse.sniffer.network.NetPacket;
import com.eclipse.sniffer.network.ROPacketDetail;
import com.google.gson.JsonObject;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

public class GuildDetailDecrypt {

    public static void process(ROPacketDetail pd) {
        GuildDetailDecrypt gd = new GuildDetailDecrypt();

        switch (pd.getName()) {
            case ACTOR_INFO_NAME_PARTY_GUILD_TITLE:
                gd.processGuildInfo(pd);
                break;
            case GUILD_EMBLEM:
                gd.processGuildEmblem(pd);
                break;
            case SYSTEM_CHAT:
                gd.processGuildBreaker(pd);
                break;
        }

    }

    public static final int ACCOUNTS_ID_START = 0;
    public static final int CHAR_NAME_START = 4;
    public static final int GUILD_NAME_START = 52;

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

            if (guildName.length() > 0
                && GeneralInfoDecrypt.isProbablyArabic(guildName)) {

                JsonObject guildInfo = new JsonObject();
                guildInfo.addProperty("account_id", accId);
                guildInfo.addProperty("character_name", charName);
                guildInfo.addProperty("guild_name", guildName);

                APIRequest.shared.PUT(new APIRequestQueue("/guilds/"+ accId, guildInfo));

            }

        }).start();

    }

    public static final int GUILD_ID_START = 0;
    public static final int EMBLEM_ID_START = 4;
    public static final int EMBLEM_START = 8;

    /**
     * '0152' => ['guild_emblem', 'v a4 a4 a*', [qw(len [guildID emblemID emblem])]],
     * @param pd
     */
    private void processGuildEmblem(ROPacketDetail pd) {
        new Thread(() -> {
            byte[] inf = pd.getContent();
            byte[] bGuildId = NetPacket.reverseContent(Arrays.copyOfRange(inf, GUILD_ID_START, GUILD_ID_START+4));
            byte[] bEmblemId = NetPacket.reverseContent(Arrays.copyOfRange(inf, EMBLEM_ID_START, EMBLEM_ID_START+4));
            byte[] bEmblem = Arrays.copyOfRange(inf, EMBLEM_START, inf.length);

            int guildId = (ByteBuffer.wrap(bGuildId)).getInt();
            int emblemId = (ByteBuffer.wrap(bEmblemId)).getInt();
            String emblemBase64 = Base64.getEncoder().encodeToString(bEmblem);

            if (guildId > 0 && emblemId > 0 && emblemBase64.length() > 0) {

                JsonObject guildEmblemInfo = new JsonObject();
                guildEmblemInfo.addProperty("guild_id", guildId);
                guildEmblemInfo.addProperty("emblem_id", emblemId);
                guildEmblemInfo.addProperty("emblem", emblemBase64);

                APIRequest.shared.PUT(new APIRequestQueue("/guilds/"+ guildId +"/emblem/"+ emblemId, guildEmblemInfo));

            }
        }).start();
    }

    public static final String woeBreakerPattern = "(.*)The \\[.*\\] castle has been conquered by the \\[.*] guild.(.*)";
    /**
     * Woe Breaker pattern:
     *      The \[.*\] castle has been conquered by the \[.*] guild.
     * @param pd
     */
    private void processGuildBreaker(ROPacketDetail pd) {
        new Thread(() -> {
            String msg = new String(pd.getContent());

            // CAST
            int postCast_start  = msg.indexOf('[');
            int postCast_end    = msg.indexOf(']');
            String cast = msg.substring(postCast_start+1, postCast_end);
            msg = msg.substring(postCast_end+1);

            // GUILD
            int postGuild_start = msg.indexOf('[');
            int postGuild_end   = msg.indexOf(']');
            String guild = msg.substring(postGuild_start+1, postGuild_end);

            int castId = 0;
            switch (cast) {
                case "Valkyrie Realms 1": castId = 1; break;
                case "Valkyrie Realms 2": castId = 2; break;
                case "Valkyrie Realms 3": castId = 3; break;
                case "Valkyrie Realms 4": castId = 4; break;
                case "Valkyrie Realms 5": castId = 5; break;
                case "BalderGuild1": castId = 6; break;
                case "BalderGuild2": castId = 7; break;
                case "BalderGuild3": castId = 8; break;
                case "BalderGuild4": castId = 9; break;
                case "BalderGuild5": castId = 10; break;
                case "Britoniah Guild 1": castId = 11; break;
                case "Britoniah Guild 2": castId = 12; break;
                case "Britoniah Guild 3": castId = 13; break;
                case "Britoniah Guild 4": castId = 14; break;
                case "Britoniah Guild 5": castId = 15; break;
                case "Luina Guild 1": castId = 16; break;
                case "Luina Guild 2": castId = 17; break;
                case "Luina Guild 3": castId = 18; break;
                case "Luina Guild 4": castId = 19; break;
                case "Luina Guild 5": castId = 20; break;
            }

            if (castId > 0) {

                JsonObject breakInfo = new JsonObject();
                breakInfo.addProperty("cast_id", castId);
                breakInfo.addProperty("guild_name", guild);
                breakInfo.addProperty("timestamp", (new Date()).getTime());

                APIRequest.shared.PUT(new APIRequestQueue("/woe/break/cast/"+ castId, breakInfo));

            }

        }).start();
    }


}
