
public class Pokemon {
		
    private String name;
    private Type type;
	private int number;
	private Trainer trainer;
	private static int nextnumber = 1;
	private java.util.ArrayList<Swap> swaps;
	private boolean swapAllow = true;
	// static elements are to be accessed by class, not this.
	
	public Pokemon(String name, Type type) {
		// convention is to start variable names small
	this.name = name; 
	this.type = type;
	this.number = Pokemon.nextnumber;
	Pokemon.nextnumber = Pokemon.nextnumber + 1;
	}
	
	public String getName() {
	return name;
	}
	
	/* 
	 * this. is for the Program to know from where to 
	 * get the value of the following variable name
	 * this is especially useful/needed when the same 
	 * variable name is used in a public area
	 */
	//SETTER METHODEN
	public void setName(String name) {
	this.name = name;
	}

	public void setType(Type type) {
	this.type = type;
	}
	
	//GETTER METHODEN
	public int getNumber() {
	return this.number;
	}
	
	public Type getType() {
	return type;
	}
	
	public boolean getSwapAllow() {
	return this.swapAllow;
	}
	//WEITERE FUNKTIONEN
	
	public String toString() {
		// returning the result of Number then Name then Type
	return Integer.toString(this.number) + " " + this.name + " " + this.type.name();
	}

	public Boolean hasTrainer() {
	return this.trainer != null;
	}
	
	public void addTrainer(Trainer trainer) {
	this.trainer = trainer;
	}
	
	public void trade(Swap swap, Trainer trainer) {
	this.swaps.add(swap);
	this.trainer = trainer;
	}
	
}
