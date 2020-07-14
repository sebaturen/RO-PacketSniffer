package com.eclipse.sniffer.network;

import java.util.Arrays;

public class NetPacket {

    private byte[] content;
    private int port;

    public NetPacket(byte[] content, int port) {
        this.content = content;
        this.port = port;
    }

    public byte[] getContent() {
        return content;
    }

    public int getPort() {
        return port;
    }

    public static byte[] reverseContent(byte[] content) {
        for(int i = 0; i < content.length / 2; i++) {
            byte t = content[i];
            content[i] = content[content.length -1 -i];
            content[content.length -1 -i] = t;
        }
        return content;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"NetPacket\", " +
                "\"content\":" + Arrays.toString(content) + ", " +
                "\"port\":\"" + port + "\"" +
                "}";
    }
}
