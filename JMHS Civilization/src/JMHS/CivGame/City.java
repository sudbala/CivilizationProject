package JMHS.CivGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class City {
	
	int locx, locy;
	ArrayList<HexTile> area;
	int PPT, production;
	Buildings building;
	Civilization myCiv;
	Image img2 = new ImageIcon("land1.jpg").getImage();
	Image img1 = new ImageIcon("sea1.jpg").getImage();
	Image desert = new ImageIcon("desert1.jpg").getImage();
	Image hills = new ImageIcon("hills1.jpg").getImage();
	Image jungle = new ImageIcon("jungle1.jpg").getImage();
	Image center = new ImageIcon("city.jpg").getImage();
	public City(int locx, int locy, Civilization c){
		this.locx = locx;
		this.locy = locy;
		this.myCiv = c;
		area = HexMap.getSurroundingTiles(this.locx, this.locy, 2);
		building = new Buildings();
	}
	public void draw(Graphics g){
		HexTile cityCenter = Main.map.gameHexs[locx][locy];
		double sin30 = cityCenter.RADIUS * Math.sin(Math.PI / 6);
		double sin60 = cityCenter.RADIUS * Math.sin(Math.PI / 3);
		
		for(HexTile t: area)
		{	
			if(t.equals(cityCenter))
			{
				g.setClip(t.getShape());
				g.drawImage(center, (int)Math.round(cityCenter.x - sin60), (int)Math.round(cityCenter.y - cityCenter.RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * cityCenter.RADIUS), null);
				Unit.draw(g);
			}
			else if(t.getType().equals("land")){
				if(t.getLandType().equals("desert")){
					
					g.setClip(t.getShape());
					g.drawImage(desert, (int)Math.round(t.x - sin60), (int)Math.round(t.y - t.RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * t.RADIUS), null);
				}
				else if(t.getLandType().equals("hill")){
					g.setClip(t.getShape());
					g.drawImage(hills, (int)Math.round(t.x - sin60), (int)Math.round(t.y - t.RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * t.RADIUS), null);
				}
				else if(t.getLandType().equals("jungle")){
					g.setClip(t.getShape());
					g.drawImage(jungle, (int)Math.round(t.x - sin60), (int)Math.round(t.y - t.RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * t.RADIUS), null);
				}
				else{
				g.setClip(t.getShape());
				g.drawImage(img2, (int)Math.round(t.x - sin60), (int)Math.round(t.y - t.RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * t.RADIUS), null);
				}
				Unit.draw(g);
			}
			else{
				g.setClip(t.getShape());
				g.drawImage(img1, (int)Math.round(t.x - sin60), (int)Math.round(t.y - t.RADIUS), (int)Math.round(sin60 * 2), (int)Math.round(2 * t.RADIUS), null);
				Unit.draw(g);
			}
			if(Main.grid){
				int[] ycoords = { (int) Math.round(t.y + t.RADIUS), (int) Math.round(sin30 + t.y), (int) Math.round(t.y - sin30),
						(int) Math.round(t.y - t.RADIUS), (int) Math.round(t.y - sin30), (int) Math.round(sin30 + t.y) };
				int[] xcoords = { (int) Math.round(t.x), (int) Math.round(sin60 + t.x), (int) Math.round(sin60 + t.x),
						(int) Math.round(t.x), (int) Math.round(t.x - sin60), (int) Math.round(t.x - sin60) };

				Polygon p = new Polygon(xcoords, ycoords, 6);
				
				g.setColor(Color.BLACK);
				g.drawPolygon(p);
			}
		}
	}
	public ArrayList<HexTile> getTiles(){
		return area;
	}
	public Civilization getCiv()
	{
		return myCiv;
	}
	public void setPPT(int PPT){
		this.PPT = PPT;
	}
	public void setProduction(int production){
		this.production = production;
	}
	public int getProduction(){
		return this.production;
	}
	public int getPPT(){
		int temp = 0;
		for(HexTile t: area)
		{
			temp += t.production;
		}
		return temp;
	}
	public String toString(){
		return getCiv().toString() + "'s City";
	}
}
