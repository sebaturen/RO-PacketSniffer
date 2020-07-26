package com.eclipse.sniffer.enums;

import java.util.Arrays;
import java.util.Optional;

public enum PacketList {
    UNKNOWN("-1"),
    UNKNOWN_2("0A96"),
    UNKNOWN_3("0064"),
    UNKNOWN_4("0A41"),
    UNKNOWN_5("00B9"),
    // ACTOR
    ACTOR_EXISTS("09FF"),
    ACTOR_LOOK_AT("009C"),
    ACTOR_INFO_NAME_PREFIX_NAME("0ADF"),
    ACTOR_INFO_NAME_PARTY_GUILD_TITLE("0A30"),
    ACTOR_STATUS_ACTIVE_FLAG("0196"),
    ACTOR_STATUS_ACTIVE_FLAG_TICK_OTHER("043F"),
    ACTOR_STATUS_ACTIVE_FLAG_TOTAL_TICK_OTHER("0983"),
    ACTOR_STATUS_ACTIVE_TOTAL_TICK_OTHER("0984"),
    ACTOR_STATUS_ACTIVE_TICK_OTHER("08FF"),
    ACTOR_MOVE("09FD"),
    ACTOR_MOVE_INTERRUPTED("0088"),
    ACTOR_DIED_OR_DISAPPEARED("0080"),
    ACTOR_CONNECTED("09FE"),
    ACTOR_ACTION("08C8"),
    ACTOR_ACTION_2("008A"),
    ACTOR_ACTION_3("02E1"),
    // CHARACTER
    CHARACTER_STATUS("0229"),
    CHARACTER_MOVES("0087"),
    CHARACTER_NAME("0AF7"),
    CHARACTER_VENDOR_FOUND("0131"),
    CHARACTER_VENDOR_LOST("0132"),
    CHARACTER_OFFLINE_CLONE_FOUND_1("0A89"),
    CHARACTER_OFFLINE_CLONE_FOUND_2("0B05"),
    CHARACTER_OFFLINE_CLONE_LOST("0A8A"),
    CHARACTER_RECEIVED("099D"),
    CHARACTER_RECEIVED_ID_AND_MAP("0071"),
    CHARACTER_INFO_RECEIVED("082D"),
    CHARACTER_SYNC_RECEIVED("09A0"),
    CHARACTER_OVERWEIGHT_PERCENT("0ADE"),
    CHARACTER_SKILL_LIST("010F"),
    CHARACTER_CREATION_SUCCESSFUL("006D"),
    CHARACTER_DELETE2_RESULT("0828"),
    CHARACTER_DELETE2_CANCEL_RESULT("082C"),
    CHARACTER_DELETE2_ACCEPT_RESULT("082A"),
    CHARACTER_CREATION_FAILED("006E"),
    // GUILD
    GUILD_MEMBER_MAP_CHANGE("01EC"),
    GUILD_MASTER_MEMBER("014E"),
    GUILD_MEMBERS_TITLE_LIST("0166"),
    GUILD_MEMBERS_LIST("0154"),
    GUILD_NOTICE("016F"),
    GUILD_INFO("01B6"),
    GUILD_CHAT("017F"),
    GUILD_MEMBER_ONLINE_STATUS("01F2"),
    GUILD_LOCATION("01EB"),
    GUILD_ALLIES_ENEMY_LIST("014C"),
    GUILD_EMBLEM("0152"),
    GUILD_EMBLEM_UPDATE("01B4"),
    GUILD_NAME("016C"),
    GUILD_MEMBER_ADD("0182"),
    GUILD_LEAVE("015A"),
    GUILD_REQUEST("016A"),
    GUILD_INVITE_RESULT("0169"),
    GUILD_SKILLS_LIST("0162"),
    GUILD_EXPULSION("015C"),
    GUILD_UNALLY("0184"),
    // VENDOR
    VENDOR_ITEMS_LIST("0A8D"),
    // BUYING
    BUYING_STORE_LOST("0816"),
    BUYING_STORE_FOUND("0814"),
    BUYING_STORE_ITEMS_LIST("0A91"),
    // PLAYER
    PLAYER_EQUIPMENT("01D7"),
    // SKILL
    SKILL_CAST("07FB"),
    SKILL_CAST_2("013E"),
    SKILL_USE("01DE"),
    SKILL_USED_LOCATION("0117"),
    SKILL_USED_NO_DAMAGE("011A"),
    SKILL_ADD("0111"),
    SKILL_USE_FAILED("0110"),
    SKILL_POST_DELAY("043D"),
    SKILL_UPDATE("010E"),
    SKILL_POST_DELAYLIST("0985"),
    SKILL_DELETE("0441"),
    // PET
    PET_EMOTION("01AA"),
    PET_INFO2("01A4"),
    // ITEM
    ITEM_APPEARED("009E"),
    ITEM_USED("01C8"),
    ITEM_EXISTS("009D"),
    ITEM_DISAPPEARED("00A1"),
    ITEM_LIST_START("0B08"),
    ITEM_LIST_STACKABLE("0B09"),
    ITEM_LIST_NON_STACKABLE("0B0A"),
    ITEM_LIST_END("0B0B"),
    ITEM_SKILL("0147"),
    // CHAT
    CHAT_PUBLIC("008D"),
    CHAT_SELF("008E"),
    CHAT_INFO("00D7"),
    CHAT_REMOVED("00D8"),
    CHAT_JOIN_RESULT("00DA"),
    CHAT_MODIFIED("00DF"),
    CHAT_USER_LEAVE("00DD"),
    CHAT_CREATED("00D6"),
    CHAT_USER_JOIN("00DC"),
    CHAT_USERS("00DB"),
    // SPELL
    SPELL_AREA("011F"),
    SPELL_AREA_DISAPPEARS("0120"),
    SPELL_AREA_MULTIPLE2("099F"),
    SPELL_AREA_MULTIPLE3("09CA"),
    // PARTY
    PARTY_EXP_1("07D8"),
    PARTY_EXP_2("0101"),
    PARTY_LOCATION("0107"),
    PARTY_JOIN("0AE4"),
    PARTY_HP_INFO("080E"),
    PARTY_CHAT("0109"),
    PARTY_LV_INFO("0ABD"),
    PARTY_DEAD("0AB2"),
    PARTY_ALLOW_INVITE("02C9"),
    PARTY_USERS_INFO("0AE5"),
    PARTY_LEAVE("0105"),
    PARTY_INVITE_RESULT("02C5"),
    PARTY_SHOW_PICKER("02B8"),
    PARTY_LEADER("07FC"),
    PARTY_INVITE("02C6"),
    // MONSTER
    MONSTER_HP_INFO_TINY("0A36"),
    MONSTER_RANGED_ATTACK("0139"),
    // RODEX
    RODEX_MAIL_LIST("0AC2"),
    RODEX_READ_MAIL("09EB"),
    RODEX_GET_ITEM("09F4"),
    RODEX_UNREAD("09E7"),
    RODEX_GET_ZENY("09F2"),
    RODEX_DELETE("09F6"),
    RODEX_OPEN_WRITE("0A12"),
    RODEX_CHECK_PLAYER("0A51"),
    RODEX_ADD_ITEM("0A05"),
    RODEX_REMOVE_ITEM("0A07"),
    RODEX_WRITE_RESULT("09ED"),
    // DEAL
    DEAL_ADD_YOU("00EA"),
    DEAL_BEGIN("01F5"),
    DEAL_BEGIN_2("00E7"),
    DEAL_FINISH("00EC"),
    DEAL_COMPLETE("00F0"),
    DEAL_REQUEST("01F4"),
    // NPC
    NPC_TALK("00B4"),
    NPC_TALK_NUMBER("0142"),
    NPC_TALK_TEXT("01D4"),
    NPC_TALK_CONTINUE("00B5"),
    NPC_TALK_RESPONSE("00B7"),
    NPC_IMAGE("01B3"),
    NPC_TALK_CLOSE("00B6"),
    NPC_STORE_BEGIN("00C4"),
    NPC_STORE_INFO("00C6"),
    NPC_SELL_LIST("00C7"),
    NPC_MARKET_INFO("09D5"),
    // ACCOUNTS
    ACCOUNT_SERVER_INFO("0069"),
    // QUEST
    QUEST_ALL_LIST("09F8"),
    QUEST_ADD("09F9"),
    QUEST_DELETE("02B4"),
    // MAP
    MAP_PROPERTY("01D6"),
    MAP_PROPERTY_3("099B"),
    MAP_LOADED("02EB"),
    MAP_LOADED_2("0073"),
    MAP_MINI_INDICATOR("0446"),
    MAP_CHANGE("0091"),
    MAP_CHANGED("0092"),
    // MISC
    MISC_CONFIG_REPLY("02D9"),
    MISC_EFFECT("01F3"),
    MISC_CONFIG("0ADC"),
    // STORAGE
    STORAGE_OPENED("00F2"),
    STORAGE_ITEM_REMOVED("00F6"),
    STORAGE_ITEM_ADDED("0A0A"),
    STORAGE_CLOSED("00F8"),
    // INVENTORY
    INVENTORY_ITEM_REMOVED("07FA"),
    INVENTORY_ITEM_REMOVED_2("00AF"),
    INVENTORY_ITEMS_NO_STACKABLE("0A0D"),
    INVENTORY_ITEMS_NO_STACKABLE_2("00A4"),
    INVENTORY_ITEM_FAVORITE("0908"),
    INVENTORY_ITEM_ADDED("0A37"),
    // MVP
    MVP_ITEM("010A"),
    MVP_YOU("010B"),
    MVP_OTHER("010C"),
    // MESSAGE
    MESSAGE_STRING("0291"),
    MESSAGE_STRING2("07E2"),
    PRIVATE_MESSAGE("09DE"),
    PRIVATE_MESSAGE_SENT("09DF"),
    // CARD
    CART_INFO("0121"),
    CART_ITEM_REMOVED("0125"),
    CART_ITEM_ADDED("0A0B"),
    // BOARD
    SEARCH_STORE_OPEN("083A"),
    SEARCH_STORE_RESULT("0836"),
    SEARCH_STORE_FAIL("0837"),
    // ??
    HOTKEYS("0A00"),
    HOTKEYS_2("07D9"),
    TOP_10("097D"),
    OPEN_UI("0AE2"),
    BUY_RESULT("00CA"),
    PVP_RANK("019A"),
    COMBO_DELAY("01D2"),
    UNEQUIP_ITEM("099A"),
    EQUIP_ITEM("0999"),
    RESURRECTION("0148"),
    MILLENIUM_SHIELD("0440"),
    DEVOTION("01CF"),
    VENDER_ITEMS_LIST("0800"),
    MEMO_SUCCESS("011E"),
    WARP_PORTAL_LIST("011C"),
    HIGH_JUMP("08D2"),
    HIGH_JUMP_2("01FF"),
    REVOLVING_ENTITY("01D0"),
    LOCAL_BROADCAST("01C3"),
    RATES_INFO_2("097B"),
    ACHIEVEMENT_LIST("0A23"),
    FRIEND_LIST("0201"),
    SWITCH_CHARACTER("00B3"),
    CASH_SHOP_LIST("08CA"),
    LOGIN_ERROR("006A"),
    ACHIEVEMENT_UPDATE("0A24"),
    ATTACK_RANGE("013A"),
    ACCOUNT_ID("0283"),
    UNIT_LEVELUP("019B"),
    HET_EFFECT("0A3B"),
    EMOTICON("00C0"),
    HP_SP_CHANGED("0A27"),
    SHOW_EQUIP("0B03"),
    EXP("0ACC"),
    FRIEND_LOGON("0206"),
    ERRORS("0081"),
    STATS_INFO("00BD"),
    STAT_INFO("00BE"),
    STAT_INFO_1("00B0"),
    STAT_INFO_2("00B1"),
    STAT_INFO_3("0141"),
    STAT_INFO_4("0ACB"),
    STAT_INFO_5("01AB"),
    STAT_ADDED("00BC"),
    SYNC_REQUEST("0187"),
    RECEIVED_SYNC("007F"),
    EAC_KEY("0A7B"),
    NO_TELEPORT("0189"),
    QUIT_RESPONSE("018B"),
    CAST_CANCELLED("01B9"),
    ACTION_UI("0AF0"),
    SYSTEM_CHAT("009A");

    private final String ident;

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