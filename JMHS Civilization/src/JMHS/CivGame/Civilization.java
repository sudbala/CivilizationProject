package JMHS.CivGame;

import java.awt.Graphics;
import java.util.ArrayList;

public class Civilization {
	ArrayList<City> cities = new ArrayList<City>();
	ArrayList<Unit> units = new ArrayList<Unit>();
	static int myGold, myCulture, myScience;
	static int GPT, CPT, SPT;
	static String myName;
	public Civilization(String name) {
		int locx;
		int locy;
		
		myName = name;
		myGold = myCulture = myScience = GPT = CPT = SPT = 0;
		
		do {
			locx = (int) (Math.random() * HexMap.gameHexs.length);
			locy = (int) (Math.random() * HexMap.gameHexs[0].length);
		} while (!HexMap.gameHexs[locx][locy].getType().equals("land"));
		newUnit(locx, locy, "Settler");
		//newUnit(locx, locy, "Warrior");
	}

	public void newUnit(int locx, int locy, String type) {

		Unit spawnedSettler = new Settler(locx, locy, this);
		units.add(spawnedSettler);
	}

	public void addCity(City city) {
		cities.add(city);
	}
	
	public void draw(Graphics g) {
		for (City city : cities)
			city.draw(g);
		for (Unit unit : units)
			unit.draw(g);
	}
	
	public void setGold(int x){
		myGold = x;
	}
	
	public void setCulture(int x){
		myCulture = x;
	}
	
	public void setScience(int x){
		myScience = x;
	}
	
	public void setGPT(int x) {
		GPT = x;
	}
	
	public void setCPT(int x) {
		CPT = x;
	}
	
	public void setSPT(int x) {
		SPT = x;
	}
	
	public String getName(){
		return myName;
	}
	
	public int getGold(){
		return myGold;
	}
	
	public int getCulture(){
		return myCulture;
	}
	
	public int getScience(){
		return myScience;
	}
	
	public int getGPT() {
		return GPT;
	}
	
	public int getCPT() {
		return CPT;
	}
	
	public int getSPT() {
		return SPT;
	}
	public void update(){
		int foodPT = 0, culturePT = 0, sciencePT = 0, goldPT = 0;
		for(City city: cities){
			for(HexTile tile: city.getTiles()){
				culturePT += tile.getCulture();
				sciencePT += tile.getScience();
				goldPT += tile.getGold();
				foodPT += tile.getFood();
			}
		}
		setGPT(goldPT);
		setCPT(culturePT);
		setSPT(sciencePT);
	}
	public City getCity(int i) {
		return cities.get(i);
	}

	public boolean hasCity() {
		if (cities.isEmpty())
			return false;
		else
			return true;
	}
	public ArrayList<Unit> getUnits(){
		return units;
	}
	public ArrayList<City> getCities(){
		return cities;
	}
	public String toString()
	{
		return myName;
	}
}
