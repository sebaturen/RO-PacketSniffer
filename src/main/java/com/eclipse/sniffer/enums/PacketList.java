package com.eclipse.sniffer.enums;

import java.util.Arrays;
import java.util.Optional;

public enum PacketList {
    UNKNOWN("-1"),
    // ACTOR
    ACTOR_EXISTS("09FF"),
    ACTOR_LOOK_AT("009C"),
    ACTOR_INFO_NAME_PREFIX_NAME("0ADF"),
    ACTOR_INFO_NAME_PARTY_GUILD_TITLE("0A30"),
    ACTOR_STATUS_ACTIVE_FLAG("0196"),
    ACTOR_STATUS_ACTIVE_FLAG_TICK_OTHER("043F"),
    ACTOR_STATUS_ACTIVE_FLAG_TOTAL_TICK_OTHER("0983"),
    ACTOR_STATUS_ACTIVE_TICK_OTHER("08FF"),
    ACTOR_MOVE("09FD"),
    ACTOR_DIED_OR_DISAPPEARED("0080"),
    ACTOR_CONNECTED("09FE"),
    ACTOR_ACTION("08C8"),
    // CHARACTER
    CHARACTER_STATUS("0229"),
    CHARACTER_MOVES("0087"),
    CHARACTER_NAME("0AF7"),
    CHARACTER_VENDOR_FOUND("0131"),
    CHARACTER_VENDOR_LOST("0132"),
    CHARACTER_OFFLINE_CLONE_FOUND_1("0A89"),
    CHARACTER_OFFLINE_CLONE_FOUND_2("0B05"),
    CHARACTER_OFFLINE_CLONE_LOST("0A8A"),
    // GUILD
    GUILD_MEMBER_ONLINE_STATUS("01F2"),
    // VENDOR
    VENDOR_ITEMS_LIST("0A8D"),
    // PLAYER
    PLAYER_EQUIPMENT("01D7"),
    // SKILL
    SKILL_USED_NO_DAMAGE("011A"),
    // PET
    PET_EMOTION("01AA"),
    // ITEM
    ITEM_USED("01C8"),
    // ??
    STAT_INFO_1("00B0"),
    STAT_INFO_2("00B1"),
    STAT_INFO_3("0141"),
    MISC_CONFIG_REPLY("02D9"),
    SYNC_REQUEST("0187"),
    RECEIVED_SYNC("007F"),
    EAC_KEY("0A7B"),
    SKILL_CAST("07FB"),
    NO_TELEPORT("0189");

    private String ident;

    PacketList(String ident) {
        this.ident = ident;
    }

    public static PacketList getValue(String ident) {
        Optional<PacketList> opt =  Arrays.stream(values())
                .filter(PacketList -> PacketList.ident.equals(ident))
                .findFirst();

        return opt.orElse(PacketList.UNKNOWN);
    }
}