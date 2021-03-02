package fr.uge.tropico;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.*;

public class Difficulty {

    @SerializedName("name")
    private final String name;

    @SerializedName("multiplicator")
    private final float multiplicator;

    @SerializedName("endAt")
    private final int endAt;

    @SerializedName("food")
    private final int food;

    @SerializedName("foodNeeded")
    private final int foodNeeded;

    @SerializedName("treasury")
    private final int treasury;

    @SerializedName("agriculture")
    private final int agriculture;

    @SerializedName("industry")
    private final int industry;

    /**
     * Initialize difficulty parameters.
     * Parameters :
     * @param name - name of the difficulty
     * @param multiplicator - multiplies the effects of every event
     * @param endAt - losing conditions
     * @param food - quantity of food available at the beginning of the game
     * @param foodNeeded - quantity of food that every citizen will eat every year
     * @param treasury - treasury of the player at the beginning of the game
     * @param agriculture - level of agriculture at the beginning of the game
     * @param industry - level of industry at the beginning of the game
     */
    public Difficulty(String name, float multiplicator, int endAt, int food, int foodNeeded, int treasury, int agriculture, int industry) {
        this.name = name;
        this.multiplicator = multiplicator;
        this.endAt = endAt > 100 ? 100 : endAt < 0 ? 0 : endAt;
        this.food = food < 0 ? 0 : food;
        this.foodNeeded = foodNeeded < 0 ? 1 : foodNeeded;
        this.treasury = treasury < 0 ? 0 : treasury;
        this.agriculture = agriculture < 0 ? 0 : agriculture;
        this.industry = industry < 0 ? 0 : industry;
    }

    /**
     * Returns the difficulty's name
     * @return name of the difficulty
     */
    public String getName() {
        return name;
    }

    /**
     * Return the difficulty's effect multiplier.
     * @return effect multiplier of the difficulty
     */
    public float getMultiplicator() {
        return multiplicator;
    }

    /**
     * Returns the ending condition.
     * @return smallest global satisfaction accepted
     */
    public int getEndAt() {
        return endAt;
    }

    /**
     * Returns the quantity of food available at the beginning.
     * @return quantity of food available at the beginning
     */
    public int getFood() {
        return food;
    }

    /**
     * Returns the quantity of food that each citizen will eat every year.
     * @return quantity of food consumed every year by a citizen
     */
    public int getFoodNeeded() {
        return foodNeeded;
    }

    /**
     * Returns the player's treasury at the beginning.
     * @return player's treasury at the beginning
     */
    public int getTreasury() {
        return treasury;
    }

    /**
     * Returns the agriculture level at the beginning.
     * @return agriculture level
     */
    public int getAgriculture() {
        return agriculture;
    }

    /**
     * Returns the industry level at the beginning.
     * @return industry level
     */
    public int getIndustry() {
        return industry;
    }

    public static Difficulty loadFromResource(String name) throws IOException {
        try (var r = new InputStreamReader(new FileInputStream(name), "UTF-8")) {
            return new Gson().fromJson(r, Difficulty.class);
        }
    }
}
