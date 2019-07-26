package JMHS.CivGame;
import java.awt.*;

public class VictoryProgress {
	public VictoryProgress()
	{

	}
	public boolean hasWon()
	{
		//will get science, culture, and gold and see if civilization has met win condition
		//if not, will display current progress towards winning each victory
		
		return true;
	}
	public void draw(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawString("Victory Progress should be displayed here.", 10, 15);		
	}
}
