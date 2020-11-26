package com.eclipse.gameDetailsDecrypt;

import com.eclipse.apiRequest.APIRequest;
import com.eclipse.apiRequest.APIRequestQueue;
import com.eclipse.gameObject.Character;
import com.eclipse.sniffer.network.NetPacket;
import com.eclipse.sniffer.network.ROPacketDetail;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Pattern;

public class GeneralInfoDecrypt {

    private static Logger logger = LoggerFactory.getLogger(GeneralInfoDecrypt.class);
    private static final Map<Integer, Character> currentMap = new HashMap<>();

    public static void process(ROPacketDetail pd) {
        GeneralInfoDecrypt gd = new GeneralInfoDecrypt();

        switch (pd.getName()) {
            case SYSTEM_CHAT:
                gd.processSystemChat(pd);
                break;
            case MAP_CHANGE:
                gd.processMapChange(pd);
                break;
            case CHAT_INFO:
                gd.processChat(pd);
                break;
            case PARTY_JOIN:
                gd.processPartyJoin(pd);
                break;
            case PLAYER_EQUIPMENT:
                gd.processPlayerEquipment(pd);
                break;
            case CHARACTER_STATUS:
                gd.processCharacterStatus(pd);
                break;
        }

    }

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 32 || c > 126) {
                return false;
            }
        }
        return true;
    }

    /**
     * '009A' => ['system_chat', 'v a*', [qw(len message)]],
     * @param pd
     */
    private void processSystemChat(ROPacketDetail pd) {
        new Thread(() -> {
            String msg = new String(pd.getContent());

            Pattern woe1Pattern = Pattern.compile(GuildDetailDecrypt.woeBreakerPattern);
            if ((woe1Pattern.matcher(msg)).matches()) {
                GuildDetailDecrypt.process(pd);
            }

            Pattern woe2Pattern = Pattern.compile(GuildDetailDecrypt.woe2BreakerPattern);
            if ((woe2Pattern.matcher(msg)).matches()) {
                GuildDetailDecrypt.process(pd);
            }

            Pattern woe2PatternStartEnd = Pattern.compile(GuildDetailDecrypt.woe2StartPattern);
            if ((woe2PatternStartEnd.matcher(msg)).matches()) {
                GuildDetailDecrypt.process(pd);
            }

            JsonObject systemChatInf = new JsonObject();
            systemChatInf.addProperty("chat", msg);
            systemChatInf.addProperty("timestamp", pd.getTimestamp().getTime());
            APIRequest.shared.POST(new APIRequestQueue("/system/chat", systemChatInf, "POST"));

        }).start();
    }

    /**
     * '0091' => ['map_change', 'Z16 v2', [qw(map x y)]],
     * @param pd
     */
    private void processMapChange(ROPacketDetail pd) {
        List<Byte> blMapName = new ArrayList<>();
        for (int i = 0; i < pd.getContent().length; i++) {
            if (pd.getContent()[i] == 0) {
                break;
            }
            blMapName.add(pd.getContent()[i]);
        }
        byte[] bMapName = new byte[blMapName.size()];
        for (int i = 0; i < blMapName.size(); i++) {
            bMapName[i] = blMapName.get(i);
        }
        String mapName = new String(bMapName);
        mapName = mapName.substring(0, mapName.length()-4);

        Character ch = removeCharacterInfo(pd.getPort());
        ch.setMapName(mapName);
        currentMap.put(pd.getPort(), ch);

    }

    /**
     *  Account ID	|  ID | Limit | NumUsers | public/private(1/0) | Chat name
     *  Examples:
     * ACA90400|14000000|1400|0100|01|736164617364
     * ACA90400|18000000|1400|0100|01|736164617364
     * ACA90400|19000000|1400|0100|00|736164617364
     * @param pd
     */
    private void processChat(ROPacketDetail pd) {
        int post = 0;
        byte[] bChatInfo = pd.getContent();
        byte[] bAccountId = NetPacket.reverseContent(Arrays.copyOfRange(bChatInfo, post, post+=4));
        byte[] bId = NetPacket.reverseContent(Arrays.copyOfRange(bChatInfo, post, post+=4));
        byte[] bLimit = NetPacket.reverseContent(Arrays.copyOfRange(bChatInfo, post, post+=2));
        byte[] bNumUsers = NetPacket.reverseContent(Arrays.copyOfRange(bChatInfo, post, post+=2));
        byte[] bPublicPrivate = NetPacket.reverseContent(Arrays.copyOfRange(bChatInfo, post, post+=1));
        byte[] bChatName = Arrays.copyOfRange(bChatInfo, post, bChatInfo.length);

        int accountId = (ByteBuffer.wrap(bAccountId)).getInt();
        int id = (ByteBuffer.wrap(bId)).getInt();
        short limit = (ByteBuffer.wrap(bLimit)).getShort();
        short numberUsers = (ByteBuffer.wrap(bNumUsers)).getShort();
        boolean isPublic = bPublicPrivate[0] == 1;
        String chatName = new String(bChatName);

        // Check possible sniffer packet error
        if (accountId > 0 && !isPublic) {

            JsonObject chatInfo = new JsonObject();
            chatInfo.addProperty("account_id", accountId);
            chatInfo.addProperty("chat_name", chatName);
            APIRequest.shared.PUT(new APIRequestQueue("/link/"+ accountId, chatInfo, "PUT"));

            logger.info("Chat: "+ "Account ["+ accountId +"] id ["+ id +"] limit ["+ limit +"] numberUsers ["+ numberUsers +"] isPublic ["+ isPublic +"] ChatName: ["+ chatName +"]");
        }

    }

    /**
     * CHARACTER_STATUS:
     *      [xx][xx][xx][xx] Account ID
     *
     *      '0229' => ['character_status', 'a4 v2 V C', [qw(ID opt1 opt2 option stance)]],
     * @param pd
     */
    private void processCharacterStatus(ROPacketDetail pd) {
        byte[] bCharacterStatus = pd.getContent();
        byte[] bAccountId = NetPacket.reverseContent(Arrays.copyOfRange(bCharacterStatus, 0, 4));

        int accountId = (ByteBuffer.wrap(bAccountId)).getInt();

        Character ch = removeCharacterInfo(pd.getPort());
        ch.setAccountId(accountId);
        currentMap.put(pd.getPort(), ch);
    }

    /**
     * PLAYER_EQUIPMENT
     *      [xx][xx][xx][xx] Account ID
     *
     *      '01D7' => ['player_equipment', 'a4 C v2', [qw(sourceID type ID1 ID2)]],
     * @param pd
     */
    private void processPlayerEquipment(ROPacketDetail pd) {
        byte[] bCharacterStatus = pd.getContent();
        byte[] bAccountId = NetPacket.reverseContent(Arrays.copyOfRange(bCharacterStatus, 0, 4));

        int accountId = (ByteBuffer.wrap(bAccountId)).getInt();

        Character ch = removeCharacterInfo(pd.getPort());
        ch.setAccountId(accountId);
        currentMap.put(pd.getPort(), ch);
    }

    /**
     * PARTY_JOIN
     *
     *		'0AE4' => ['party_join', 'a4 a4 V v4 C Z24 Z24 Z16 C2', [qw(ID charID role jobID lv x y type name user map item_pickup item_share)]],
     * @param pd
     * @return
     */
    private void processPartyJoin(ROPacketDetail pd) {
        byte[] bCharacterStatus = pd.getContent();
        byte[] bAccountId = NetPacket.reverseContent(Arrays.copyOfRange(bCharacterStatus, 0, 4));
        byte[] bCharacterId = NetPacket.reverseContent(Arrays.copyOfRange(bCharacterStatus, 4, 8));

        int accountId = (ByteBuffer.wrap(bAccountId)).getInt();
        int characterId = (ByteBuffer.wrap(bCharacterId)).getInt();

        Character ch = removeCharacterInfo(pd.getPort());
        if (ch.getAccountId() == accountId) {
            ch.setCharacterId(characterId);
        }
        currentMap.put(pd.getPort(), ch);
    }

    public static Character getCharacterInfo(int port) {
        return currentMap.get(port);
    }
    private Character removeCharacterInfo(int port) {
        if (currentMap.containsKey(port)) {
            return currentMap.remove(port);
        } else {
            return new Character();
        }
    }
}
