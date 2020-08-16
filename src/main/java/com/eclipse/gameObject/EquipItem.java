package com.eclipse.gameObject;

import com.eclipse.sniffer.network.PacketDecryption;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class EquipItem {

    private int itemId;
    private int refine;
    private String position;
    private JsonArray cards;
    private JsonArray enchants;

    public EquipItem(int itemId, int refine, String position, JsonArray cards, JsonArray enchants) {
        this.itemId = itemId;
        this.refine = refine;
        this.position = position;
        this.cards = cards;
        this.enchants = enchants;
    }

    public int getItemId() {
        return itemId;
    }

    public int getRefine() {
        return refine;
    }

    public String getPosition() {
        return position;
    }

    public JsonArray getCards() {
        return cards;
    }

    public JsonArray getEnchants() {
        return enchants;
    }
}
