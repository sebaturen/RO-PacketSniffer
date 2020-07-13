package com.eclipse.sniffer.network;

import com.eclipse.sniffer.enums.PacketList;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PacketDetail {

    private PacketList name;
    private String packetHeader;
    private List<String> content;
    private Date timestamp;
    private int port;

    public PacketDetail(PacketList name, String packetHeader, List<String> content, int port) {
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

    public List<String> getContent() {
        return content;
    }

    public String getPacketHeader() {
        return packetHeader;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"PacketDetail\", " +
                "\"name\":" + (name == null ? "null" : name) + ", " +
                "\"packetHeader\":" + (packetHeader == null ? "null" : "\"" + packetHeader + "\"") + ", " +
                "\"content\":" + (content == null ? "null" : Arrays.toString(content.toArray())) + ", " +
                "\"timestamp\":" + (timestamp == null ? "null" : timestamp) + ", " +
                "\"port\":\"" + port + "\"" +
                "}";
    }
}
