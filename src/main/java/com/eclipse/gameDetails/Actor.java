package com.eclipse.gameDetails;

import com.eclipse.apiRequest.APIRequest;
import com.eclipse.apiRequest.APIRequestQueue;
import com.eclipse.sniffer.enums.EnchantList;
import com.eclipse.sniffer.enums.PacketList;
import com.eclipse.sniffer.network.NetPacket;
import com.eclipse.sniffer.network.PacketDecryption;
import com.eclipse.sniffer.network.ROPacketDetail;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Actor {

    public static void process(ROPacketDetail pd) {
        Actor cd = new Actor();

        if (pd.getName() == PacketList.SHOW_EQUIP) {
            cd.processEquip(pd);
        } else if (pd.getName() == PacketList.ACTOR_INFO_NAME_PARTY_GUILD_TITLE) {
            GuildDetail.process(pd);
        } else {
            cd.processActor(pd);
        }

    }

    public static final int OBJECT_TYPE = 0;
    public static final int ACTOR_TYPE_CHARACTER = 0;
    public static final int ACTOR_TYPE_MONSTER = 5;
    public static final int ACTOR_TYPE_NPC = 6;
    private void processActor(ROPacketDetail pd) {

        new Thread(() -> {
            byte[] inf = pd.getContent();
            switch (inf[OBJECT_TYPE]) {
                case ACTOR_TYPE_CHARACTER:
                    processPlayerShowInfo(inf, pd.getName());
                    break;
                case ACTOR_TYPE_MONSTER:
                    //processMonsterShowInfo(inf, pd.getName());
                    break;
            }
        }).start();

    }

    public static final int ID_START = 1;
    public static final int CHAR_ID_START = 5;
    public static final int JOB_ID_START = 19;
    public static final int HAIR_STYLE_START = 21;
    public static final int WEAPON_START = 23;
    public static final int SHIELD_START = 27;
    public static final int LOW_HEAD_START = 31;
    public static final int TOP_HEAD_START = 33;
    public static final int MID_HEAD_START = 35;
    public static final int HAIR_COLOR_START = 37;
    public static final int CLOTHES_COLOR_START = 39;
    public static final int GUILD_ID_START = 45;
    public static final int EMBLEM_ID_START = 49;
    public static final int SEX_START = 58;
    public static final int LV_START = 65;
    public static final int NAME_START = 80;

    /**
     * PACKET DOCUMENTATION:
     * '09FF' => ['actor_exists',    'v C a4 a4 v3 V v2 V2 v7 a4 a2 v V C2 a3 C3 v2 V2 C v Z*',     [qw(len object_type ID charID walk_speed opt1 opt2 option type hair_style weapon shield lowhead         tophead midhead hair_color clothes_color head_dir costume guildID emblemID manner opt3 stance sex coords xSize ySize state lv font maxHP HP isBoss opt4 name)]],
     * '09FE' => ['actor_connected', 'v C a4 a4 v3 V v2 V2 v7 a4 a2 v V C2 a3 C2 v2 V2 C v Z*',     [qw(len object_type ID charID walk_speed opt1 opt2 option type hair_style weapon shield lowhead         tophead midhead hair_color clothes_color head_dir costume guildID emblemID manner opt3 stance sex coords xSize ySize       lv font maxHP HP isBoss opt4 name)]],
     * '09FD' => ['actor_moved',     'v C a4 a4 v3 V v2 V2 v V v6 a4 a2 v V C2 a6 C2 v2 V2 C v Z*', [qw(len object_type ID charID walk_speed opt1 opt2 option type hair_style weapon shield lowhead tick(4) tophead midhead hair_color clothes_color head_dir costume guildID emblemID manner opt3 stance sex coords xSize ySize       lv font maxHP HP isBoss opt4 name)]],
     * @param pd
     */
    private void processPlayerShowInfo(byte[] inf, PacketList packetType) {
        // Struct info
        byte[] bAccId        = NetPacket.reverseContent(Arrays.copyOfRange(inf, ID_START, ID_START+4));
        byte[] bCharId       = NetPacket.reverseContent(Arrays.copyOfRange(inf, CHAR_ID_START, CHAR_ID_START+4));
        byte[] bJobId        = NetPacket.reverseContent(Arrays.copyOfRange(inf, JOB_ID_START, JOB_ID_START+2));
        byte[] bHairStyleId  = NetPacket.reverseContent(Arrays.copyOfRange(inf, HAIR_STYLE_START, HAIR_STYLE_START+2));
        byte[] bWeaponId     = NetPacket.reverseContent(Arrays.copyOfRange(inf, WEAPON_START, WEAPON_START+4));
        byte[] bShieldId     = NetPacket.reverseContent(Arrays.copyOfRange(inf, SHIELD_START, SHIELD_START+4));
        byte[] bLowHeadId      = NetPacket.reverseContent(Arrays.copyOfRange(inf, LOW_HEAD_START, LOW_HEAD_START+2));
        int topPost = TOP_HEAD_START;
        int midPost = MID_HEAD_START;
        int hairColor = HAIR_COLOR_START;
        int clothesColor = CLOTHES_COLOR_START;
        int namePost = NAME_START;
        int sexPos = SEX_START;
        int lvPost = LV_START;
        int guildPost = GUILD_ID_START;
        int emblemPost = EMBLEM_ID_START;
        if (packetType == PacketList.ACTOR_MOVE) {
            topPost += 4;
            midPost += 4;
            hairColor += 4;
            clothesColor += 4;
            sexPos += 4;
            guildPost += 4;
            emblemPost += 4;
            lvPost += 6;
            namePost += 6;
        } else if (packetType == PacketList.ACTOR_CONNECTED) {
            sexPos -= 1;
            lvPost -= 1;
            namePost -= 1;
        }
        byte[] bTopHeadId      = NetPacket.reverseContent(Arrays.copyOfRange(inf, topPost, topPost+2));
        byte[] bMidHeadId      = NetPacket.reverseContent(Arrays.copyOfRange(inf, midPost, midPost+2));
        byte[] bHairColorId    = NetPacket.reverseContent(Arrays.copyOfRange(inf, hairColor, hairColor+2));
        byte[] bClothesColorId = NetPacket.reverseContent(Arrays.copyOfRange(inf, clothesColor, clothesColor+2));
        byte[] bGuildId        = NetPacket.reverseContent(Arrays.copyOfRange(inf, guildPost, guildPost+4));
        byte[] bEmblemId       = NetPacket.reverseContent(Arrays.copyOfRange(inf, emblemPost, emblemPost+4));
        byte bSex              = inf[sexPos];
        byte[] bLv             = NetPacket.reverseContent(Arrays.copyOfRange(inf, lvPost, lvPost+2));
        byte[] bName           = Arrays.copyOfRange(inf, namePost, inf.length);
        // Parse data
        int accId       = (ByteBuffer.wrap(bAccId)).getInt();
        int charId      = (ByteBuffer.wrap(bCharId)).getInt();
        String name     = new String(bName);
        short jobId     = (ByteBuffer.wrap(bJobId)).getShort();
        short hairStyleId = (ByteBuffer.wrap(bHairStyleId)).getShort();
        int weaponId    = (ByteBuffer.wrap(bWeaponId)).getInt();
        int shieldId    = (ByteBuffer.wrap(bShieldId)).getInt();
        short lowHeadId   = (ByteBuffer.wrap(bLowHeadId)).getShort();
        short topHeadId   = (ByteBuffer.wrap(bTopHeadId)).getShort();
        short midHeadId   = (ByteBuffer.wrap(bMidHeadId)).getShort();
        short hairColorId = (ByteBuffer.wrap(bHairColorId)).getShort();
        short clothesColorId = (ByteBuffer.wrap(bClothesColorId)).getShort();
        int guildId       = (ByteBuffer.wrap(bGuildId)).getInt();
        int emblemId      = (ByteBuffer.wrap(bEmblemId)).getInt();
        short lvl         = (ByteBuffer.wrap(bLv)).getShort();
        short sex         = bSex;

        // Check posible sniffer packet error
        if (name.length() > 0 && emblemId >= 0 && guildId >= 0 && lvl >= 1 && lvl <= 99) {

            JsonObject pjInfo = new JsonObject();
            pjInfo.addProperty("account_id", accId);
            pjInfo.addProperty("character_id", charId);
            pjInfo.addProperty("name", name);
            pjInfo.addProperty("job_id", jobId);
            if (guildId > 0) {
                pjInfo.addProperty("guild_id", guildId);
            }
            pjInfo.addProperty("emblem_id", emblemId);
            pjInfo.addProperty("lvl", lvl);
            pjInfo.addProperty("sex", sex);
            pjInfo.addProperty("weapon_id", weaponId);
            pjInfo.addProperty("shield_id", shieldId);
            pjInfo.addProperty("low_head_view_id", lowHeadId);
            pjInfo.addProperty("top_head_view_id", topHeadId);
            pjInfo.addProperty("mid_head_view_id", midHeadId);
            pjInfo.addProperty("hair_style_id", hairStyleId);
            pjInfo.addProperty("hair_color_id", hairColorId);
            pjInfo.addProperty("clothes_color_id", clothesColorId);

            APIRequest.shared.PUT(new APIRequestQueue("/characters/"+ accId +"/"+ charId, pjInfo));

        }
    }

    private static final int START_EQUIP = 43;
    private static final int EQUIP_INFO_SIZE = 67;
    // Internal equip info position
    private static final int ITEM_ID_START = 2;
    private static final int POSITION_START = 6;
    private static final int REFINE_START = 15;
    private static final int CARD_START = 16;
    private static final int ENCHANT_START = 41;

    /**
     * EXAMPLES
     * Inadecuada - ALL
     *                                     ?       ID     {PP      P                } R    C1      C2       C3        C4                        E1    E1V   E2   E2V    E3    E3v   E4   E4v   E5    E5v
     * FRICA CIRCLE [X]        [5124]  | 2600 | 04140000 |04.00.01.00.00.00.01.00.00|00|00000000|00000000|00000000|00000000|000000000000FB0000|0000|000000|0000|000000|0000|000000|0000|000000|0000|000000|01 HAT
     * ANGRY SNARL [X]         [5113]  | 2700 | F9130000 |04.01.00.00.00.01.00.00.00|00|00000000|00000000|00000000|00000000|000000000000C20000|0000|000000|0000|000000|0000|000000|0000|000000|0000|000000|01 BOCA
     * COAT [0]                [2310]  | 2500 | 06090000 |04.10.00.00.00.10.00.00.00|00|00000000|00000000|00000000|00000000|000000000000000000|0A00|030000|0200|2B0000|0000|000000|0000|000000|0000|000000|01 ARMADURA
     * +6 CHAIN [4126x2][4281] [1520]  | 2300 | F0050000 |05.02.00.00.00.02.00.00.00|06|1E100000|1E100000|B9100000|00000000|000000000000080000|0800|040000|1100|380000|3500|080000|0000|000000|0000|000000|01 ARMA
     * STONE BUCKEL [0]        [2114]  | 2400 | 42080000 |04.20.00.00.00.20.00.00.00|00|00000000|00000000|00000000|00000000|000000000000020000|0000|000000|0000|000000|0000|000000|0000|000000|0000|000000|01 ESCUDO
     * MORPHEUS SHA [X]        [2518]  | 2000 | D6090000 |04.04.00.00.00.04.00.00.00|00|00000000|00000000|00000000|00000000|000000000000000000|0000|000000|0000|000000|0000|000000|0000|000000|0000|000000|01 GARMENT
     * SHOES [4100]            [2404]  | 1F00 | 64090000 |04.40.00.00.00.40.00.00.00|00|04100000|00000000|00000000|00000000|000000000000000000|0700|040000|0200|5E0000|0000|000000|0000|000000|0000|000000|01 TILLAS
     * CLIPE [4084]            [2607]  | 2100 | 2F0A0000 |04.88.00.00.00.08.00.00.00|00|F40F0000|00000000|00000000|00000000|000000000000000000|0000|000000|0000|000000|0000|000000|0000|000000|0000|000000|01 ACC IZQ
     * CLIPE [4077]            [2607]  | 2200 | 2F0A0000 |04.88.00.00.00.80.00.00.00|00|ED0F0000|00000000|00000000|00000000|000000000000000000|0000|000000|0000|000000|0000|000000|0000|000000|0000|000000|01 ACC DR
     *
     * Clein - ALL
     * PANDA HAT               [5030]  | 3000 | A6130000 |04.00.01.00.00.00.01.00.00|00|00000000|00000000|00000000000000000000000000007300000000000000000000000000000000000000000000000000000001  HAT
     * SUNGLASS [X]            [2201]  | 2D00 | 99080000 |04.00.02.00.00.00.02.00.00|00|00000000|00000000|00000000000000000000000000000C00000000000000000000000000000000000000000000000000000001  OJOS
     * ANGRY SNARL [X]         [5113]  | 2700 | F9130000 |04.01.00.00.00.01.00.00.00|00|00000000|00000000|0000000000000000000000000000C200000000000000000000000000000000000000000000000000000001  BOCA
     * +4 COAT [4105]          [2310]  | 2F00 | 06090000 |04.10.00.00.00.10.00.00.00|04|09100000|00000000|00000000000000000000000000000000000A0004000019000C000000000000000000000000000000000001  ARMADURA
     * ARC WAND [0][0]         [1611]  | 2800 | 4B060000 |05.02.00.00.00.02.00.00.00|00|00000000|00000000|00000000000000000000000000000A0000A800280000730016000000000000000000000000000000000001  ARMA
     * +4BUCKLER [2104]        [2104]  | 2C00 | 38080000 |04.20.00.00.00.20.00.00.00|04|DA0F0000|00000000|00000000000000000000000000000200000000000000000000000000000000000000000000000000000001  ESCUDO
     * +4 MUFFLER [4325]       [2504]  | 2900 | C8090000 |04.04.00.00.00.04.00.00.00|04|E5100000|00000000|00000000000000000000000000000000000000000000000000000000000000000000000000000000000001  GARMENT
     * +4 SHOES [4107]         [2404]  | 2E00 | 64090000 |04.40.00.00.00.40.00.00.00|04|0B100000|00000000|00000000000000000000000000000000000800030000140022000000000000000000000000000000000001  TILLAS
     * ROSARY [4252]           [2626]  | 2A00 | 420A0000 |04.88.00.00.00.08.00.00.00|00|9C100000|00000000|00000000000000000000000000000000000000000000000000000000000000000000000000000000000001  ACC IZQ
     * CLIPE [4077]            [4044]  | 2B00 | 2F0A0000 |04.88.00.00.00.80.00.00.00|00|CC0F0000|00000000|00000000000000000000000000000000000000000000000000000000000000000000000000000000000001  ACC DR
     *
     * Ana - Superior mask                                0D.01.02.00.00.01.02.00.00
     * Clein - Custome Fox                                0D.00.18.00.00.00.18.00.00
     * Clein - Rana                                       04.00.04.00.00.00.04.00.00
     * @param pd
     */
    private void processEquip(ROPacketDetail pd) {

        new Thread(() -> {
            if (pd.getContent().length <= START_EQUIP) {
                System.out.println("Not equip");
            } else {
                byte[] inf = pd.getContent();
                byte[] equipInf = Arrays.copyOfRange(inf,START_EQUIP,inf.length);
                // Get character name
                int namePosEnd = 0;
                for(byte pjName : Arrays.copyOfRange(inf, 0, inf.length)) {
                    if (pjName == 0) {
                        break;
                    }
                    namePosEnd++;
                }
                byte[] bName = Arrays.copyOfRange(inf,0,namePosEnd);
                JsonObject characterInfo = new JsonObject();
                characterInfo.addProperty("name", new String(bName));
                // Divide equip information
                JsonArray equips = new JsonArray();
                for(int i = 0; i < equipInf.length; i += 67) {
                    byte[] bEquip   = Arrays.copyOfRange(equipInf,i,i+EQUIP_INFO_SIZE);
                    byte[] bItemId  = NetPacket.reverseContent(Arrays.copyOfRange(bEquip, ITEM_ID_START, ITEM_ID_START+4));
                    byte[] bPos     = Arrays.copyOfRange(bEquip, POSITION_START, POSITION_START+9);
                    byte bRefine    = bEquip[REFINE_START];
                    // CARDS
                    JsonArray cards = new JsonArray();
                    for (int j = CARD_START; j < (CARD_START+16); j+=4) {
                        byte[] bCard   = NetPacket.reverseContent(Arrays.copyOfRange(bEquip, j, j+4));
                        int card   = (ByteBuffer.wrap(bCard)).getInt();
                        if (card != 0) cards.add(card);
                    }
                    JsonArray enchants = new JsonArray();
                    for (int j = ENCHANT_START; j < bEquip.length-1; j+=3) {
                        byte[] bEnch   = NetPacket.reverseContent(Arrays.copyOfRange(bEquip, j, j+2));
                        j +=2 ;
                        byte[] bEnchv  = NetPacket.reverseContent(Arrays.copyOfRange(bEquip, j, j+2));
                        short ench   = (ByteBuffer.wrap(bEnch)).getShort();
                        short enchv  = (ByteBuffer.wrap(bEnchv)).getShort();
                        if (ench != 0) {
                            JsonObject enchInf = new JsonObject();
                            enchInf.addProperty("type", EnchantList.valueOf(ench).toString());
                            enchInf.addProperty("val", enchv);
                            enchants.add(enchInf);
                        }
                    }

                    //short pos   = (ByteBuffer.wrap(bPos)).getShort();
                    int itemId  = (ByteBuffer.wrap(bItemId)).getInt();

                    JsonObject equip = new JsonObject();
                    //equip.addProperty("position", pos);
                    equip.addProperty("item_id", itemId);
                    equip.addProperty("refine", bRefine);
                    equip.addProperty("pos", PacketDecryption.convertBytesToHex(bPos));
                    if (cards.size() > 0) equip.add("cards", cards);
                    if (enchants.size() > 0) equip.add("enchants", enchants);


                    equips.add(equip);
                }
                characterInfo.add("equip", equips);

                System.out.println(characterInfo);
            }

        }).start();
    }
}
