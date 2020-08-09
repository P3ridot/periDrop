package me.peridot.peridrop.drop.fortune;

import api.peridot.periapi.utils.Pair;

public class FortuneDrop {

    private final int fortuneLevel;
    private final float chance;
    private final Pair<Integer, Integer> amount;

    public FortuneDrop(int fortuneLevel, float chance, Pair<Integer, Integer> amount) {
        this.fortuneLevel = fortuneLevel;
        this.chance = chance;
        this.amount = amount;
    }

    public int getFortuneLevel() {
        return fortuneLevel;
    }

    public float getChance() {
        return chance;
    }

    public Pair<Integer, Integer> getAmount() {
        return amount;
    }

    public int getMinAmount() {
        return amount.getKey();
    }

    public int getMaxAmount() {
        return amount.getValue();
    }

}
