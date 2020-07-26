package com.eclipse.gameDetails;

import com.eclipse.sniffer.network.ROPacketDetail;

import java.util.regex.Pattern;

public class GeneralInfo {

    public static void process(ROPacketDetail pd) {
        GeneralInfo gd = new GeneralInfo();

        switch (pd.getName()) {
            case SYSTEM_CHAT:
                gd.processSystemChat(pd);
                break;
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

}
