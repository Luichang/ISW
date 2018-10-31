package pokemon.data;

import java.util.ArrayList;
import java.util.List;

public class Trainer {

    private String firstname;
    private String lastname;
    private List<Pokemon> pokemons = new ArrayList<Pokemon>();

    public Trainer(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    // for bidirectional linking it is necessary to set this as trainer
    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
        for (Pokemon p : getPokemons()) {
            p.setTrainer(this); // set this as trainer for all
        }
    }

    public Pokemon getPokemon(int index) {
        return pokemons.get(index);
    }

    public List<Pokemon> getPokemonsOfType(Type type) {
        List<Pokemon> pokemonsOfType = new ArrayList<Pokemon>();
        for (Pokemon p : getPokemons()) {
            if (p.getType() == type) {
                pokemonsOfType.add(p);
            }
        }
        return pokemonsOfType;
    }

    public void addPokemon(Pokemon pokemon) {
        getPokemons().add(pokemon); // add to list
        pokemon.setTrainer(this); // set as trainer
    }

    public String toString() {
        return getFirstname() + " " + getLastname();
    }

}
