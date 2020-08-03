package com.eclipse.gameDetails;

import com.eclipse.sniffer.network.ROPacketDetail;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class GeneralInfo {

    private static Map<Integer, String> currentMap = new HashMap<>();

    public static void process(ROPacketDetail pd) {
        GeneralInfo gd = new GeneralInfo();

        switch (pd.getName()) {
            case SYSTEM_CHAT:
                gd.processSystemChat(pd);
                break;
            case MAP_CHANGE:
                gd.processMapChange(pd);
        }

    }

    /**
     * '009A' => ['system_chat', 'v a*', [qw(len message)]],
     * @param pd
     */
    private void processSystemChat(ROPacketDetail pd) {
        new Thread(() -> {
            String msg = new String(pd.getContent());

            Pattern pattern = Pattern.compile(GuildDetail.woeBreakerPattern);
            if ((pattern.matcher(msg)).matches()) {
                GuildDetail.process(pd);
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

    public static String getMapName(int port) {
        return currentMap.get(port);
    }

}
