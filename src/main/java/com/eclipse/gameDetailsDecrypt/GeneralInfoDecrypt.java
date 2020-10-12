package com.eclipse.gameDetailsDecrypt;

import com.eclipse.apiRequest.APIRequest;
import com.eclipse.apiRequest.APIRequestQueue;
import com.eclipse.sniffer.network.NetPacket;
import com.eclipse.sniffer.network.ROPacketDetail;
import com.google.gson.JsonObject;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Pattern;

public class GeneralInfoDecrypt {

    private static final Map<Integer, String> currentMap = new HashMap<>();

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

        currentMap.put(pd.getPort(), mapName);
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

            System.out.println("Account ["+ accountId +"]\nid ["+ id +"]\nlimit ["+ limit +"]\nnumberUsers ["+ numberUsers +"]\nisPublic ["+ isPublic +"]\nChatName: ["+ chatName +"]");
        }

    }

    public static String getMapName(int port) {
        return currentMap.get(port);
    }

}
