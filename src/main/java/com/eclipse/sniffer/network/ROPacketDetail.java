package com.eclipse.sniffer.network;

import com.eclipse.sniffer.enums.PacketList;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ROPacketDetail {

    private PacketList name;
    private String packetHeader;
    private Date timestamp;
    private byte[] content;
    private int port;

    public ROPacketDetail(PacketList name, String packetHeader, byte[] content, int port) {
        this.name = name;
        this.packetHeader = packetHeader;
        this.content = content;
        this.port = port;
        this.timestamp = new Date();
    }

    public int getPort() {
        return port;
    }

    public PacketList getName() {
        return name;
    }

    public byte[] getContent() {
        return content;
    }

    public String getPacketHeader() {
        return packetHeader;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"ROPacketDetail\", " +
                "\"name\":" + (name == null ? "null" : name) + ", " +
                "\"packetHeader\":" + (packetHeader == null ? "null" : "\"" + packetHeader + "\"") + ", " +
                "\"timestamp\":" + (timestamp == null ? "null" : timestamp) + ", " +
                "\"content\":" + Arrays.toString(content) + ", " +
                "\"port\":\"" + port + "\"" +
                "}";
    }
}
