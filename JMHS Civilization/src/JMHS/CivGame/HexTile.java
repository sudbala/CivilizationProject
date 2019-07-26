package JMHS.CivGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;

import javax.swing.ImageIcon;

public class HexTile {

	public static final int r = 64;
	public static double RADIUS = r;
	public double x;
	public double y;
	int i;
	int j;
	private double mapx;
	private double mapy;
	public String type;
	public String landType;
	private float color;
	private float moisture;
	Color c;
	Graphics g;

	int gold, culture, science, food, production;
	
	public HexTile(double mapx, double mapy, float color, float moisture, int i, int j) {
		this.i = i;
		this.j = j;
		this.mapx = mapx;
		this.mapy = mapy;
		this.color = color;
		this.moisture = moisture;
		type = getType();
		if(type.equals("land"))
			landType = getLandType();
	}
	public void setColor(Color c){
		this.c = c;
	}
	public double getX() {
		return this.mapx;
	}

	public double getY() {
		return this.mapy;
	}

	public void setCoords(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getBoardX() {
		return x;
	}

	public double getBoardY() {
		return y;
	}

	public String getType() {
		if (color <= .55)
		{
			production = 0;
			gold = 1;
			culture = 1;
			science = 0;
			return "sea";
		}
		else
			return "land";
	}

	public String getLandType() {
		if (moisture < .25){
			production = 0;
			gold = 0;
			culture = 0;
			science = 0;
			return "desert";
		}
		else if (moisture > .72){
			production = 2;
			gold = 0;
			culture = 0;
			science = 0;
			return "hill";
		}
		else if(moisture > .6){
			production = 0;
			gold = 0;
			culture = 0;
			science = 2;
			return "jungle";
		}
		production = 1;
		gold = 1;
		culture = 0;
		science = 0;
		return "";
	}
	public Color getColor(){
		return c;
	}
	public void draw(Graphics g) {
		Image img2 = new ImageIcon("land1.jpg").getImage();
		Image img1 = new ImageIcon("sea1.jpg").getImage();
		double sin30 = RADIUS * Math.sin(Math.PI / 6);
		double sin60 = RADIUS * Math.sin(Math.PI / 3);
		g.setColor(c);
		double tint_factor = .5;
//		if (Main.playerCiv.hasCity()) {
//			if (Main.playerCiv.getCity(0).area.contains(this)) {
//				if(getType().equals("land"))
//					g.drawImage(img2, (int)Math.round(x - sin60), (int)Math.round(y - RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * RADIUS), null);
//				else
//					g.drawImage(img1, (int)Math.round(x - sin60), (int)Math.round(y - RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * RADIUS), null);
//			}
//		}
		int[] ycoords = { (int) Math.round(y + RADIUS), (int) Math.round(sin30 + y), (int) Math.round(y - sin30),
				(int) Math.round(y - RADIUS), (int) Math.round(y - sin30), (int) Math.round(sin30 + y) };
		int[] xcoords = { (int) Math.round(x), (int) Math.round(sin60 + x), (int) Math.round(sin60 + x),
				(int) Math.round(x), (int) Math.round(x - sin60), (int) Math.round(x - sin60) };
		
		boolean isPartOfCity = false;
		for(Civilization civ: Main.civs){
			for(City city: civ.cities)
			{
				if (city.area.contains(this)) {
					isPartOfCity = true;
				}
			}
		}
		Polygon p = new Polygon(xcoords, ycoords, 6);
		// g.setColor(new Color(color, color, color));
		Image land = new ImageIcon("land.jpg").getImage();
		Image sea = new ImageIcon("sea.jpg").getImage();
		Image desert = new ImageIcon("desert.jpg").getImage();
		Image hills = new ImageIcon("hills.jpg").getImage();
		Image jungle  = new ImageIcon("jungle.jpg").getImage();
		if(isPartOfCity)
		{
			
		}
		else if(getType().equals("land")){
			if(landType.equals("desert")){
				
				g.setClip(getShape());
				g.drawImage(desert, (int)Math.round(x - sin60), (int)Math.round(y - RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * RADIUS), null);
			}
			else if(landType.equals("hill")){
				g.setClip(getShape());
				g.drawImage(hills, (int)Math.round(x - sin60), (int)Math.round(y - RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * RADIUS), null);
			}
			else if(landType.equals("jungle")){
				g.setClip(getShape());
				g.drawImage(jungle, (int)Math.round(x - sin60), (int)Math.round(y - RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * RADIUS), null);
			}
			else{
				g.setClip(getShape());
				g.drawImage(land, (int)Math.round(x - sin60), (int)Math.round(y - RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * RADIUS), null);
			}
			Unit.draw(g);
		}
		else{
			g.setClip(getShape());
			g.drawImage(sea, (int)Math.round(x - sin60), (int)Math.round(y - RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * RADIUS), null);
			Unit.draw(g);
		}
			
		//g.fillPolygon(p);
		

		if(Main.grid){
			g.setColor(Color.BLACK);
			g.drawPolygon(p);
		}
		
		
	}

	public void draw(Graphics g, int num) {
		double sin60 = RADIUS * Math.sin(Math.PI / 3);
		/*g.setColor(Color.BLACK);
		double sin30 = RADIUS * Math.sin(Math.PI / 6);
		double sin60 = RADIUS * Math.sin(Math.PI / 3);
		int[] ycoords = { (int) Math.round(y + RADIUS), (int) Math.round(sin30 + y), (int) Math.round(y - sin30),
				(int) Math.round(y - RADIUS), (int) Math.round(y - sin30), (int) Math.round(sin30 + y) };
		int[] xcoords = { (int) Math.round(x), (int) Math.round(sin60 + x), (int) Math.round(sin60 + x),
				(int) Math.round(x), (int) Math.round(x - sin60), (int) Math.round(x - sin60) };
		Polygon p = new Polygon(xcoords, ycoords, 6);

		g.fillPolygon(p);*/
		
		
//		if(num==1){
//		g.setClip(getShape());
//		g.drawImage(img, (int)Math.round(x - sin60), (int)Math.round(y - RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * RADIUS), null);
//		}
//		else{
//			g.setClip(getShape());
//			g.drawImage(img2, (int)Math.round(x - sin60), (int)Math.round(y - RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * RADIUS), null);
//			
//		}
	}

	public Polygon getShape() {
		double sin30 = RADIUS * Math.sin(Math.PI / 6);
		double sin60 = RADIUS * Math.sin(Math.PI / 3);
		int[] ycoords = { (int) Math.round(y + RADIUS), (int) Math.round(sin30 + y), (int) Math.round(y - sin30),
				(int) Math.round(y - RADIUS), (int) Math.round(y - sin30), (int) Math.round(sin30 + y) };
		int[] xcoords = { (int) Math.round(x), (int) Math.round(sin60 + x), (int) Math.round(sin60 + x),
				(int) Math.round(x), (int) Math.round(x - sin60), (int) Math.round(x - sin60) };
		Polygon p = new Polygon(xcoords, ycoords, 6);
		return p;
	}

	public boolean hasUnit() {
		for (Civilization civ : Main.civs) {
			for (Unit unit : civ.units) {
				if (unit.locx == this.i && unit.locy == this.j)
					return true;
			}
		}
		return false;
	}

	public boolean hasCity() {
		for (Civilization civ : Main.civs) {
			for (City city : civ.cities) {
				if (city.locx == this.i && city.locy == this.j)
					return true;
			}
		}
		return false;
	}
	public Unit getUnit(){
		for (Civilization civ : Main.civs) {
			for (Unit unit : civ.units) {
				if (unit.locx == this.i && unit.locy == this.j)
					return unit;
			}
		}
		return null;
	}
	public City getCity(){
		for (Civilization civ : Main.civs) {
			for (City city : civ.cities) {
				if (city.locx == this.i && city.locy == this.j)
					return city;
			}
		}
		return null;
	}
	public int getGold() {
		// TODO Auto-generated method stub
		return this.gold;
	}
	public int getFood() {
		// TODO Auto-generated method stub
		return this.food;
	}
	public int getCulture() {
		// TODO Auto-generated method stub
		return this.culture;
	}
	public int getScience() {
		// TODO Auto-generated method stub
		return this.science;
	}
}
