package pokemon.data;

public class Trainer {
  private String firstname;
  private String lastname;
  private java.util.ArrayList<Pokemon> pokemons;
    
  //Konstruktor
  
  /** Creates a new Trainer with entered first and last name. */
  public Trainer(String firstname, String lastname) {
    this.firstname = firstname;
    this.lastname = lastname;
    pokemons = new java.util.ArrayList<Pokemon>();
  }
    
  //Getter-Methoden
  
  public String getFirstname() {
    return this.firstname;
  }
    
  public String getLastname() {
    return this.lastname;
  }
  
  /** Depending on Parameter you get a list of all Pokemon you own, all of a Type you 
   * own or a specific one, based on the index. */
  public java.util.ArrayList<String> getPokemons() {
    java.util.ArrayList<String> out = new java.util.ArrayList<String>();
    for (int i = 0; i < this.pokemons.size(); i++) {
      out.add(pokemons.get(i).toString());
    }
    return out;
  }
  
  /** Depending on Parameter you get a list of all Pokemon you own, all of a Type you 
   * own or a specific one, based on the index. */
  public java.util.ArrayList<String> getPokemons(Type type) {
    java.util.ArrayList<String> out = new java.util.ArrayList<String>();
    for (int i = 0; i < this.pokemons.size(); i++) {
      if (pokemons.get(i).getType() == type) {
        out.add(pokemons.get(i).toString());
      }
    }
    return out;
  }
  
  /** Depending on Parameter you get a list of all Pokemon you own, all of a Type you 
   * own or a specific one, based on the index. */
  public String getPokemons(int index) {
    for (int i = 0; i < this.pokemons.size(); i++) {
      if (index == pokemons.get(i).getNumber()) {
        return pokemons.get(i).toString();
      }
    }
    return "You don't have access to this pokemon";
        
  }
  //Setter-Methoden
    
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }
    
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }
    
  public String toString() {
    return (this.firstname + " " + this.lastname);
  }
    
  //Sonstige Funktionen
  
  /** Catching a Pokemon adds it to your list that you own. */
  public void addPokemon(Pokemon pokemon) {
    if (!pokemon.hasTrainer()) {
      this.pokemons.add(pokemon);
      pokemon.addTrainer(this);
    }
  }
    
  public void trade(Pokemon own, Pokemon other) {
    pokemons.remove(own);
    this.pokemons.add(other);
  }
}
