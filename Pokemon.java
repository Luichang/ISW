
public class Pokemon {
		
	private String name;
	private Type type;
	private int number;
	private Trainer trainer;
	private static int nextnumber = 1;
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
	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public String toString() {
		// returning the result of Number then Name then Type
		return Integer.toString(this.number) + " " + this.name + " " + this.type.name();
	}

	public static void main(String[] args) {
		Pokemon charmander;
		charmander = new Pokemon("Charmander", Type.Fire); 
		// To access values from the Type chart the syntax is to use Type.
		
		Pokemon charmeleon = new Pokemon("Charmeleon", Type.Fire);
		
		System.out.println(charmander.toString());
		System.out.println(charmeleon.toString());
	}

	public Boolean hasTrainer() {
		return this.trainer != null;
	}
	
	public void addTrainer(Trainer trainer) {
		this.trainer = trainer;
	}
}
