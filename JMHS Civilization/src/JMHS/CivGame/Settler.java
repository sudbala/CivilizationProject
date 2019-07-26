package JMHS.CivGame;

public class Settler extends Unit {
	
	public Settler(int locx, int locy, Civilization civ) {
		super(locx, locy, civ);
		// TODO Auto-generated constructor stub
	}

	public void createCity() {
		City newCity = new City(locx, locy, getCiv());
		civ.addCity(newCity);
		getCiv().units.remove(this);
		this.img = null;
	}
	
	public String toString(){
		return getCiv().toString() + "'s Settler";
	}
}
