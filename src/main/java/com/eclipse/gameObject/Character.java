package com.eclipse.gameObject;

public class Character {

    private int accountId;
    private int characterId;
    private String mapName = "";

    public Character() {

    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"Character\", " +
                "\"accountId\":\"" + accountId + "\"" + ", " +
                "\"characterId\":\"" + characterId + "\"" + ", " +
                "\"mapName\":" + (mapName == null ? "null" : "\"" + mapName + "\"") +
                "}";
    }
}
