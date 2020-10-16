package com.eclipse.gameDetailsDecrypt;

import com.eclipse.apiRequest.APIRequest;
import com.eclipse.apiRequest.APIRequestQueue;
import com.eclipse.sniffer.network.NetPacket;
import com.eclipse.sniffer.network.ROPacketDetail;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Pattern;

public class GuildDetailDecrypt {

    private static Logger logger = LoggerFactory.getLogger(GuildDetailDecrypt.class);

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

                APIRequest.shared.PUT(new APIRequestQueue("/guilds/"+ accId, guildInfo, "PUT"));

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

                APIRequest.shared.PUT(new APIRequestQueue("/guilds/"+ guildId +"/emblem/"+ emblemId, guildEmblemInfo, "PUT"));

            }
        }).start();
    }

    public static final String woeBreakerPattern    = "(.*)The \\[.*\\] castle has been conquered by the \\[.*] guild.(.*)";
    public static final String woe2StartPattern     = "(.*)The \\[.*\\] stronghold of (.*) is occupied by the \\[.*] Guild.(.*)";
    public static final String woe2BreakerPattern   = "(.*)The \\[.*\\] guild conquered the \\[.*] (.*).";
    /**
     * Woe Breaker pattern:
     *      The \[.*\] castle has been conquered by the \[.*] guild.
     * @param pd
     */
    private void processGuildBreaker(ROPacketDetail pd) {
        new Thread(() -> {

            String msg = new String(pd.getContent());
            logger.info("Woe BREAK: "+ msg);

            Pattern woe1Pattern = Pattern.compile(GuildDetailDecrypt.woeBreakerPattern);
            if ((woe1Pattern.matcher(msg)).matches()) {
                processWoEBreaker(msg, WOE_1, pd.getTimestamp().getTime());
            }

            Pattern woe2Start = Pattern.compile(GuildDetailDecrypt.woe2StartPattern);
            if ((woe2Start.matcher(msg)).matches()) {
                processWoEBreaker(msg, WOE_2_START, pd.getTimestamp().getTime());
            }

            Pattern woe2Pattern = Pattern.compile(GuildDetailDecrypt.woe2BreakerPattern);
            if ((woe2Pattern.matcher(msg)).matches()) {
                processWoEBreaker(msg, WOE_2_BREAK, pd.getTimestamp().getTime());
            }

        }).start();
    }

    /**
     * Type:
     *      1 => Woe 1
     *      2 => Woe 2
     */
    private static final int WOE_1 = 1;
    private static final int WOE_2_START = 2;
    private static final int WOE_2_BREAK = 3;
    private void processWoEBreaker(String msg, int type, long time) {

        String cast, guild;
        int postCast_start, postCast_end, postGuild_start, postGuild_end;
        switch (type) {
            case WOE_1: case WOE_2_START:
                // CAST
                postCast_start  = msg.indexOf('[');
                postCast_end    = msg.indexOf(']');
                cast = msg.substring(postCast_start+1, postCast_end);
                msg = msg.substring(postCast_end+1);

                // GUILD
                postGuild_start = msg.indexOf('[');
                int counterBlock = 0;
                int i = 0;
                for (char n : msg.substring(postGuild_start).toCharArray()) {
                    if (n == '[') counterBlock++;
                    if (n == ']') counterBlock--;
                    if (counterBlock == 0) break;
                    i++;
                }
                postGuild_end = postGuild_start+i;
                guild = msg.substring(postGuild_start+1, postGuild_end);
                break;
            case WOE_2_BREAK:
                // GUILD
                postGuild_start = msg.indexOf('[');
                postGuild_end   = msg.indexOf(']');
                guild = msg.substring(postGuild_start+1, postGuild_end);
                msg = msg.substring(postGuild_end+1);

                // CAST
                postCast_start  = msg.indexOf('[');
                postCast_end    = msg.indexOf(']');
                cast = msg.substring(postCast_start+1, postCast_end);
                break;
            default:
                return;
        }

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
            case "Valfreyja 1": castId = 21; break;
            case "Valfreyja 2": castId = 22; break;
            case "Valfreyja 3": castId = 23; break;
            case "Valfreyja 4": castId = 24; break;
            case "Valfreyja 5": castId = 25; break;
            case "Nithafjoll 1": castId = 26; break;
            case "Nithafjoll 2": castId = 27; break;
            case "Nithafjoll 3": castId = 28; break;
            case "Nithafjoll 4": castId = 29; break;
            case "Nithafjoll 5": castId = 30; break;
        }

        if (castId > 0) {

            JsonObject breakInfo = new JsonObject();
            breakInfo.addProperty("cast_id", castId);
            breakInfo.addProperty("guild_name", guild);
            breakInfo.addProperty("timestamp", time);
            logger.info("Break info: "+ breakInfo);

            APIRequest.shared.POST(new APIRequestQueue("/woe/break/cast/"+ castId, breakInfo, "POST"));

        }

    }

}
