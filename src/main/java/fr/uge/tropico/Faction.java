package fr.uge.tropico;

import java.io.Serializable;

public class Faction {

    private final String factionName;
    private int satisfaction;
    private int supporter;

    /**
     * Initialize faction parameters
     * @param factionName - name of the faction
     * @param satisfaction - satisfaction of the faction at the beginning
     * @param supporter - number of supporters at the beginning
     */
    public Faction(String factionName, int satisfaction, int supporter) {
        this.factionName = factionName;
        this.satisfaction = satisfaction;
        this.supporter = supporter;
    }

    /**
     * Changes the value of satisfaction of the faction.
     * It must be between 0 and 100 and if a faction has a satisfaction of 0, it can't change until the end.
     * @param x - the value to add
     */
    public void changeSatisfaction (int x) {
        if (satisfaction > 0) {
            if (satisfaction + x > 100) {
                satisfaction = 100;
            }
            else if (satisfaction + x < 0) {
                satisfaction = 0;
            }
            else if (satisfaction + x <= 100) {
                satisfaction += x;
            }
        }
    }

    /**
     * Change the number of supporters of a faction
     * @param x - number of supporters to add
     */
    public void changeSupporter(int x) {
        if (supporter + x < 0) {
            supporter = 0;
        }
        else
            supporter += x;
    }

    /**
     * Returns the name of the faction
     * @return name
     */
    public String getFactionName() {
        return factionName;
    }

    /**
     * Returns the satisfaction level of the faction
     * @return satisfaction
     */
    public int getSatisfaction() {
        return satisfaction;
    }

    /**
     * Returns the numbers of supporters of the faction
     * @return number of supporters
     */
    public int getSupporter() {
        return supporter;
    }
}
