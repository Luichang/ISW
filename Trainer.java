public class Trainer {
	private String firstname;
	private String lastname;
	private java.util.ArrayList<Pokemon> pokemons;
	
	//Konstruktor
	
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
	
	public java.util.ArrayList<String> getPokemons() {
	java.util.ArrayList<String> out = new java.util.ArrayList<String>();
	for (int i = 0; i < this.pokemons.size(); i++) {
            out.add(pokemons.get(i).toString());
		}
	return out;
	}
	
	public java.util.ArrayList<String> getPokemons(Type type) {
	java.util.ArrayList<String> out = new java.util.ArrayList<String>();
		for (int i = 0; i < this.pokemons.size(); i++) {
			if (pokemons.get(i).getType() == type) {
		out.add(pokemons.get(i).toString());
			}
		}
	return out;
	}

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
