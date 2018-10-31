package pokemon.data;

import java.util.ArrayList;
import java.util.List;

import pokemon.Competition;
import pokemon.Swap;

public class Pokemon {

    private String name;
    private Type type;
    private Trainer trainer;
    private int number;
    private static int nextNumber;

    private List<Swap> swaps = new ArrayList<Swap>();
    private boolean swapAllow = true;

    private List<Competition> competitions = new ArrayList<Competition>();

    public Pokemon(String name, Type type) {
        this.name = name;
        this.type = type;
        this.number = nextNumber;
        nextNumber++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // this references the actual object instance
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public int getNumber() {
        return this.number;
    }

    public List<Swap> getSwaps() {
        return swaps;
    }

    public void setSwaps(List<Swap> swaps) {
        this.swaps = swaps;
    }

    public void addSwap(Swap swap) {
        getSwaps().add(swap);
    }

    public List<Competition> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(List<Competition> competitions) {
        this.competitions = competitions;
    }

    public void addCompetition(Competition competition) {
        getCompetitions().add(competition);
    }

    public boolean isSwapAllow() {
        return swapAllow;
    }

    public void setSwapAllow(boolean swapAllow) {
        this.swapAllow = swapAllow;
    }

    public String toString() {
        return "Pokemon(" + getNumber() + ") '" + getName() + "' of type '" + getType() + "' has trainer '"
                + getTrainer() + "'";
    }

}