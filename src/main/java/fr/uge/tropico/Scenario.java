package fr.uge.tropico;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Scenario {

    @SerializedName("scenarioTitle")
    private final String scenarioTitle;

    @SerializedName("factions")
    private List<Faction> factions = new ArrayList<>();

    @SerializedName("Events")
    private List<Event> Events = new ArrayList<>();

    public Scenario(String scenarioTitle, List<Faction> factions, List<Event> qs){
        this.scenarioTitle = scenarioTitle;
        this.factions.addAll(factions);
        Events.addAll(qs);
    }

    /**
     * Return the title of the scenario
     * @return title
     */
    public String getScenarioTitle() {
        return scenarioTitle;
    }

    /**
     * Return the list of factions available in this scenario
     * @return list of Faction objects
     */
    public List<Faction> getFactions() {
        return factions;
    }

    
    public int EventNo(){
        return Events.size();
    }

    public void add(Event q){
        Events.add(q);
    }

    /**
     * Pick a random event from the list for a given season
     * @param seasonName - the season needed for the event
     * @return an Event object
     */
    public Event pickRandomEvent(String seasonName){
        Random r = new Random();
        Event result = Events.get(r.nextInt(Events.size()));
        while (!result.hasSeason(seasonName)){
            result = Events.get(r.nextInt(Events.size()));
        }
        return result;
    }

    @Override
    public String toString() {
        return "Scenario{" +
                "scenarioTitle='" + scenarioTitle + '\'' +
                ", Events=" + Events +
                '}';
    }

    /**
     * Loads the scenario parameters from a json file
     * @param name - name of the json file
     * @return a Scenario object
     * @throws IOException - the file serached doesn't exist
     */
    public static Scenario loadFromResource(String name) throws IOException {
        try (var r = new InputStreamReader(new FileInputStream(name), "UTF-8")) {
            return new Gson().fromJson(r, Scenario.class);
        }
    }

    public static void main(String[] args) throws IOException {
        Scenario scenar = Scenario.loadFromResource("scenario/Guerre Froide.json");
        System.out.println(scenar.toString());
        System.out.println(scenar.pickRandomEvent("SUMMER"));
    }
    
}
