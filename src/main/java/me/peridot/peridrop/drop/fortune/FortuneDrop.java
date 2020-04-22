package me.peridot.peridrop.drop.fortune;

import api.peridot.periapi.utils.Pair;
import jdk.nashorn.internal.objects.annotations.Getter;

public class FortuneDrop {

    private final int fortuneLevel;
    private final float chance;
    private final Pair<Integer, Integer> amount;

    public FortuneDrop(int fortuneLevel, float chance, Pair<Integer, Integer> amount) {
        this.fortuneLevel = fortuneLevel;
        this.chance = chance;
        this.amount = amount;
    }

    @Getter
    public int getFortuneLevel() {
        return fortuneLevel;
    }

    @Getter
    public float getChance() {
        return chance;
    }

    @Getter
    public Pair<Integer, Integer> getAmount() {
        return amount;
    }

    @Getter
    public int getMinAmount() {
        return amount.getKey();
    }

    @Getter
    public int getMaxAmount() {
        return amount.getValue();
    }
}
