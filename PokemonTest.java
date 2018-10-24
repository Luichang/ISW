public class PokemonTest{
	public static void main(String[] args) {
		Pokemon charmander;
		charmander = new Pokemon("charmander", Type.Fire); 		
		Pokemon squirtle = new Pokemon("Squirtle", Type.Poison);
		
		// Decided against code comments for explanation as this seemed more interactive and fun and still offers the same result
		System.out.println("To ensure everything is working Properly we must compare two different Pokemon from two different Types");
		System.out.println("The testing will be done step by step to use every operation");
		System.out.println("First off let us have a look at the names");
		System.out.println("Pokemon 1 name is: " + charmander.getName());
		System.out.println("The name is not capital, let's change that");
		charmander.setName("Charmander");
		System.out.println("Let's jump to the type");
		System.out.println("Pokemon 2 type is: " + squirtle.getType());
		System.out.println("Oh no! The type is wrong! Can't let that stand");
		squirtle.setType(Type.Water);
		System.out.println("Since they are different Pokemon they must have different numbers, let's see if that is the case");
		System.out.println("Charmander Number: " + charmander.getNumber() + " and Squirtle Number: " + squirtle.getNumber());
		System.out.println("Very good, now how do the two look as a whole?");
		System.out.println(charmander.toString());
		System.out.println(squirtle.toString());
		
		System.out.println("What an exciting day, today my Grandson and another kid from this town get to pick up their first Pokemon");
		System.out.println("I shall prepare their Pokedexes so they can collect the crittical data I need");
		Trainer red = new Trainer("You", "Trainer");
		Trainer blue;
		System.out.println("Oh thank you for comming! I don't seem to have caught your name before could you change it here?");
		red.setFirstname("Red");
		red.setLastname("ofPallet");
		System.out.println("Now if only I could remember my grandsons name.");
		blue = new Trainer("Blue", "Oak");
		System.out.println("Ah yes thank you " + red.toString() + " it was " + blue.getFirstname() + " " + blue.getLastname());
		System.out.println("Now both of you get to choose a Pokemon");
		System.out.println("Red, you chose Charmander and Blue, you chose Squirtle. Here are your Pokedex. You should register them");
		red.addPokemon(charmander);
		blue.addPokemon(squirtle);
		System.out.println("Red, can you please check for the first Pokemon?");
		System.out.println(red.getPokemons(1));
		// Added normal type because Rattata was the first Pokemon Trainer Ash catches in the Origins series 
		Pokemon rattata = new Pokemon("Rattata", Type.Normal);
		// Had to go off script just to add another Pokemon for the other tests
		Pokemon meowth = new Pokemon("Meowth", Type.Normal);
		System.out.println("Oh look a Rattata and a Meowth. Red you should catch them");
		red.addPokemon(rattata);
		red.addPokemon(meowth);
		System.out.println("Let's have a look at all your Pokemon");
		System.out.println(red.getPokemons());
		System.out.println("Now what are your Normal Type Pokemon?");
		System.out.println(red.getPokemons(Type.Normal));
	}
}