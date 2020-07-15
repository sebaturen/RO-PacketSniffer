package com.eclipse.characters;

import com.eclipse.sniffer.Sniffer;
import com.eclipse.sniffer.enums.PacketList;
import com.eclipse.sniffer.network.NetPacket;
import com.eclipse.sniffer.network.PacketDecryption;
import com.eclipse.sniffer.network.ROPacketDetail;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.google.gson.JsonObject;

public class CharacterDetail {

    public static final int OBJECT_TYPE = 0;
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

    public static void process(ROPacketDetail pd) {
        CharacterDetail cd = new CharacterDetail();

        if (pd.getName() == PacketList.SHOW_EQUIP) {
            cd.processEquip(pd);
        } else {
            cd.processActor(pd);
        }

    }

    /**
     * PACKET DOCUMENTATION:
     * '09FF' => ['actor_exists', 'v C a4 a4 v3 V v2 V2 v7 a4 a2 v V C2 a3 C3 v2 V2 C v Z*',    [qw(len object_type ID charID walk_speed opt1 opt2 option type hair_style weapon shield lowhead         tophead midhead hair_color clothes_color head_dir costume guildID emblemID manner opt3 stance sex coords xSize ySize state lv font maxHP HP isBoss opt4 name)]],
     * '09FE' => ['actor_connected', 'v C a4 a4 v3 V v2 V2 v7 a4 a2 v V C2 a3 C2 v2 V2 C v Z*', [qw(len object_type ID charID walk_speed opt1 opt2 option type hair_style weapon shield lowhead         tophead midhead hair_color clothes_color head_dir costume guildID emblemID manner opt3 stance sex coords xSize ySize       lv font maxHP HP isBoss opt4 name)]],
     * '09FD' => ['actor_moved', 'v C a4 a4 v3 V v2 V2 v V v6 a4 a2 v V C2 a6 C2 v2 V2 C v Z*', [qw(len object_type ID charID walk_speed opt1 opt2 option type hair_style weapon shield lowhead tick(4) tophead midhead hair_color clothes_color head_dir costume guildID emblemID manner opt3 stance sex coords xSize ySize       lv font maxHP HP isBoss opt4 name)]],
     * @param pd
     */
    private void processActor(ROPacketDetail pd) {

        new Thread(() -> {
            byte[] inf = pd.getContent();
            if (inf[0] == 0) {
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
                if (pd.getName() == PacketList.ACTOR_MOVE) {
                    topPost += 4;
                    midPost += 4;
                    hairColor += 4;
                    clothesColor += 4;
                    sexPos += 4;
                    guildPost += 4;
                    emblemPost += 4;
                    lvPost += 6;
                    namePost += 6;
                } else if (pd.getName() == PacketList.ACTOR_CONNECTED) {
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
                byte[] bName = Arrays.copyOfRange(inf, namePost, inf.length);
                // Parse data
                int accId       = (ByteBuffer.wrap(bAccId)).getInt();
                int charId      = (ByteBuffer.wrap(bCharId)).getInt();
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

                JsonObject pjInfo = new JsonObject();
                pjInfo.addProperty("account_id", accId);
                pjInfo.addProperty("character_id", charId);
                pjInfo.addProperty("name", new String(bName));
                pjInfo.addProperty("job_id", jobId);
                pjInfo.addProperty("guild_id", guildId);
                pjInfo.addProperty("emblem_id", emblemId);
                pjInfo.addProperty("lvl", lvl);
                pjInfo.addProperty("sex", sex);
                pjInfo.addProperty("hair_style_id", hairStyleId);
                pjInfo.addProperty("weapon_id", weaponId);
                pjInfo.addProperty("shield_id", shieldId);
                pjInfo.addProperty("low_head_view_id", lowHeadId);
                pjInfo.addProperty("top_head_view_id", topHeadId);
                pjInfo.addProperty("mid_head_view_id", midHeadId);
                pjInfo.addProperty("hair_color_id", hairColorId);
                pjInfo.addProperty("clothes_color_id", clothesColorId);

                System.out.println(pjInfo);
            }
        }).start();

    }

    private void processEquip(ROPacketDetail pd) {
        System.out.println(pd);
        System.out.println(PacketDecryption.convertBytesToHex(pd.getContent()));
    }
}
