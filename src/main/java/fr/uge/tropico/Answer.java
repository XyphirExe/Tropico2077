package fr.uge.tropico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Answer {
    private final String name;
    private final HashMap<String,HashMap<String,Integer>> effects;
    private HashMap<String,HashMap<String,Integer>> vseffects = new HashMap<>();
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Event> vsevents = new ArrayList<>();

    /**
     * Initialize answer parameters with multiplayer effects.
     * Parameters :
     * @param name - title of the answer
     * @param effects - list of effects triggered by the answer
     * @param events - list of events triggered by the answer
     * @param vsevents - list of multiplayer events triggered by the answer
     * @param vseffects - list of multiplayer effects triggered by the answer
     */
    public Answer(String name,
                  HashMap<String,HashMap<String,Integer>> effects,
                  ArrayList<Event> events,
                  ArrayList<Event> vsevents,
                  Map<String,HashMap<String,Integer>> vseffects){
        this.name = name;
        this.effects = effects;
        this.vseffects = (HashMap<String, HashMap<String, Integer>>) vseffects;
        this.events = events;
        this.vsevents = vsevents;
    }

    /**
     * Initialize answer parameters without multiplayer effects.
     * Parameters :
     * @param name - title of the answer
     * @param effects - list of effects triggered by the answer
     * @param events - list of events triggered by the answer
     */
    public Answer(String name, HashMap<String,HashMap<String,Integer>> effects, ArrayList<Event> events){
        this.name = name;
        this.effects = effects;
        this.events = events;
    }

    /**
     * Returns the title of the answer.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the answer's effects.
     * @return HashMap with the category of effect as keys and as values a Hashmap with the variable to change and the value to add as values
     */
    public HashMap<String, HashMap<String, Integer>> getEffects() {
        return effects;
    }

    /**
     * Return a list of events triggered by the answer.
     * @return List of events
     */
    public List<Event> getEvents() {
        List<Event> result = new ArrayList<Event>();
        events.forEach(result::add);
        return result;
    }

    /**
     * Return a list of multiplayer events triggered by the answer.
     * @return List of multiplayer events
     */
    public List<Event> getVSevents() {
        List<Event> result = new ArrayList<Event>();
        vsevents.forEach(result::add);
        return result;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "name='" + name + '\'' +
                ", effects=" + effects +
                ", events=" + events +
                ", vsevents=" + vsevents +
                '}';
    }

    public HashMap<String, HashMap<String, Integer>> getVSEffects() {
        return vseffects;
    }
}
