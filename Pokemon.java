public class Pokemon {
        
  private String name;
  private Type type;
  private int number;
  private Trainer trainer;
  private static int nextnumber = 1;
  private java.util.ArrayList<Swap> swaps;
  private java.util.ArrayList<Competition> competitions;
  private boolean swapAllow = true;
  // static elements are to be accessed by class, not this.
  
  /** Creates a new Pokemon with entered name and type. It also assigns a number to it. */
  public Pokemon(String name, Type type) {
    // convention is to start variable names small
    this.name = name; 
    this.type = type;
    this.number = Pokemon.nextnumber;
    Pokemon.nextnumber = Pokemon.nextnumber + 1;
    this.swaps = new java.util.ArrayList<Swap>();
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
  
  public void toggleTrade() {
    this.swapAllow = !this.swapAllow;
    System.out.println("Successfully toggled ability to be traded to " + this.swapAllow);
  }
    
  public void trade(Swap swap, Trainer trainer) {
    this.swaps.add(swap);
    this.trainer = trainer;
  }
  
  public void lose(Competition competition, Trainer trainer) {
    this.competitions.add(competition);
    this.trainer = trainer;
  }  
 
  public boolean won(Pokemon pokemon) {
    if(this.type == pokemon.type) {
      double number1 = java.lang.Math.random(); //Zahl von this
      double number2 = java.lang.Math.random(); //Zahl von pokemon
      if(number1 > number2) {
        return true;
      } else {
        return false;
      }
    } else {
      if (this.type == Type.Fire) {
        return false;
      }else if(this.type == Type.Poison){
        return true;
      } else if(pokemon.type == Type.Poison){
          return false;
      } else {
        return true;
      }
    }
  }
}
