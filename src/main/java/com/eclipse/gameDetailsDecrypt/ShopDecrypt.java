package com.eclipse.gameDetailsDecrypt;

import com.eclipse.apiRequest.APIRequest;
import com.eclipse.apiRequest.APIRequestQueue;
import com.eclipse.sniffer.network.NetPacket;
import com.eclipse.sniffer.network.PacketDecryption;
import com.eclipse.sniffer.network.ROPacketDetail;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ShopDecrypt {

    private static Logger logger = LoggerFactory.getLogger(ShopDecrypt.class);

    public static void process(ROPacketDetail pd) {
        ShopDecrypt sd = new ShopDecrypt();

        switch (pd.getName()) {
            case SHOP_SOLD_LONG:
                sd.processShopSold(pd);
                break;
        }
    }

    /**
     * 		'09E5' => ['shop_sold_long', 'v2 a4 V2', [qw(number amount charID time zeny)]],
     */
    private void processShopSold(ROPacketDetail pd) {
        System.out.println("vending inf ["+ pd.getName() +"] =>  "+ PacketDecryption.convertBytesToHex(pd.getContent()));
        byte[] inf = pd.getContent();

        int i = 0;
        byte[] bNumber = NetPacket.reverseContent(Arrays.copyOfRange(inf, i,i+=2));
        byte[] bAmount = NetPacket.reverseContent(Arrays.copyOfRange(inf, i, i+=2));
        byte[] bCharId = NetPacket.reverseContent(Arrays.copyOfRange(inf, i, i+=4));
        byte[] bTime   = NetPacket.reverseContent(Arrays.copyOfRange(inf, i, i+=4));
        byte[] bZeny   = NetPacket.reverseContent(Arrays.copyOfRange(inf, i, i+=4));

        short number = (ByteBuffer.wrap(bNumber)).getShort();
        short amount = (ByteBuffer.wrap(bAmount)).getShort();
        int charId   = (ByteBuffer.wrap(bCharId)).getInt();
        int time     = (ByteBuffer.wrap(bTime)).getInt();
        int zeny     = (ByteBuffer.wrap(bZeny)).getInt();

        JsonObject vendingInfo = new JsonObject();
        vendingInfo.addProperty("number", number);
        vendingInfo.addProperty("amount", amount);
        vendingInfo.addProperty("char_id", charId);
        vendingInfo.addProperty("time", time);
        vendingInfo.addProperty("zeny", zeny);

        System.out.println(vendingInfo);
        APIRequest.shared.POST(new APIRequestQueue("/shop/sold", vendingInfo, "POST"));
    }
}
