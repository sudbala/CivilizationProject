package JMHS.CivGame;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Buildings {
	
	Scanner sc;
	HashMap<String, String[]> buildings = new HashMap<String, String[]>();
	public Buildings(){
		openFile();
	}
	public void openFile(){
		try{
			sc = new Scanner(new File("Buildings.txt"));
			while(sc.hasNextLine()){
				String str = sc.nextLine();
				String key = str.substring(0, str.indexOf('.'));
				String[] value = str.substring(str.indexOf('.') + 1).split("\\s+");
					buildings.put(key, value);
			}
		}
		catch(Exception e){
			System.out.println("Error");
		}
	}
	public void completeBuilding(String building, Civilization civ){
		String[] effects = buildings.get(building);
		for(int i = 1; i < buildings.get(building).length; i++){
			add(effects[i], civ);
		}
		buildings.remove(building);
	}
	public void add(String str, Civilization civ){
		switch(str.substring(1)){
			case "g":	civ.setGPT(civ.getGPT() + Integer.parseInt(str.substring(0, 1)));
					break;
			case "s":	civ.setSPT(civ.getSPT() + Integer.parseInt(str.substring(0, 1)));
					break;
			case "c":	civ.setCPT(civ.getCPT() + Integer.parseInt(str.substring(0, 1)));
					break;
		}
	}
	public static String toString(String str){
		String type = "";
		switch(str.charAt(1)){
			case 'c': type = "Culture";
					break;
			case 's': type = "Science";
					break;
			case 'g': type = "Gold";
					break;
			case 'p': type = "Production";
					break;
		}
		return "+ " + str.substring(0, 1) + " " + type;
	}
}
