package fr.uge.tropico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Event {
    private final List<String> seasons;
    private String title;
    private ArrayList<Answer> answers = new ArrayList<>();
    private boolean father;
    private boolean son;
    private int when;

    /** Initialize event parameters.
     * Parameters :
     * @param title - title of the event
     * @param seasons - list of season where the event can happen
     * @param answers - list of answers possible
     * @param father - if the event can be randomly triggered (useless for now)
     * @param son - if the event is a part of another one
     * @param when - in the case of a son event, in how many turn it will happen
     */
    public Event(String title, List<String> seasons, ArrayList<Answer> answers, boolean father, boolean son, int when){
        this.title = title;
        this.answers = answers;
        this.seasons = seasons;
        this.son = son;
        this.father = father;
        this.when = when;
    }

    /**
     * Returns the title of the event.
     * @return title of the event
     */
    public String getTitle() {
        return title;
    }

    /**
     * Return the list of the possible answers.
     * @return list of answers
     */
    public ArrayList<Answer> getAnswers() { return answers; }

    /**
     * Verifies if the event can happen on a given season.
     * Parameters :
     * @param seasonName - season to verify
     * @return if the event can happen
     */
    public boolean hasSeason(String seasonName) {
        return seasons.contains(seasonName);
    }

    /**
     * Return when the event can happen.
     * @return the number of turns
     */
    public int getWhen() {
        return when;
    }

    @Override
    public String toString() {
        return "Event{" +
                "seasons=" + seasons +
                ", title='" + title + '\'' +
                ", answers=" + answers +
                ", father=" + father +
                ", son=" + son +
                ", when=" + when +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Event)){
            return false;
        }
        Event q = (Event) o;
        return title.equals(q.title) && answers.equals(q.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, answers);
    }

    /**
     * Shuffle the answers of th event
     * @return list of answers with a random order
     */
    public List<Answer> getRandomlyOrderedAnswers(){
        List<Answer> result = new ArrayList<Answer>(answers);
        Collections.shuffle(result);
        return result;
    }

    public static void main(String[] args) {}


}
