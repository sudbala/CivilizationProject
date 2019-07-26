package JMHS.CivGame;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Main extends JPanel implements Runnable, KeyListener, MouseWheelListener {

	private static final String title = "End of Year Project";
	public static final double WIDTH = 1280;
	public static final double HEIGHT = 720;
	public static final double SCALE = Math.sqrt(3) / 2;
	double px1;
	double py1;
	boolean dragging = false;
	public static boolean grid = false;
	static JPanel jpanel = new JPanel();
	static JPanel blank = new JPanel();
	static JScrollPane jscroll = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	static boolean framer = true;
	static Container pane = new Container();
	static JFrame frame;

	public static void main(String[] args) {
		frame = new JFrame();
		frame.setSize((int) WIDTH, (int) HEIGHT);
		frame.setResizable(false);
		frame.add(new Main(), BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(title);
		frame.setVisible(false);
	}

	private boolean isRunning = false;
	private Thread thread;

	private boolean movingRight = false;
	private boolean movingLeft = false;
	private boolean movingUp = false;
	private boolean movingDown = false;

	public static HexMap map;
	public static VictoryProgress victoryProgress;
	static boolean displayingMap, displayingTechTree, displayingVictoryProgress;

	public static Civilization playerCiv;
	public static ArrayList<Civilization> civs;
	Object selected;
	boolean isSelected = false;
	ArrayList<HexTile> availableTiles;
	JMenuItem goldDisplay, cultureDisplay, scienceDisplay, turnDisplay;
	JLabel label1,label2;
	static JButton endTurn;
	ArrayList<JComponent> scrollComponents = new ArrayList<JComponent>();
	int turn;

	public Main() {
		setFocusable(true);
		addKeyListener(this);
		addMouseWheelListener(this);
		addMouseMotionListener(mouse);
		addMouseListener(m);
		map = new HexMap(80, 52);
		victoryProgress = new VictoryProgress();
		playerCiv = new Civilization("Player");
		civs = new ArrayList<Civilization>();
		civs.add(playerCiv);
		frame.add(jscroll, BorderLayout.EAST);
		jpanel.setPreferredSize(new Dimension(200, 0)); //changed in updatePanelSize(), referenced line 114
		jpanel.setVisible(false);
		jscroll.setVisible(true);
		turn = 0;
		
	
		jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.PAGE_AXIS));
		
		endTurn = addFittedButton(jpanel, "End Turn");
		scrollComponents.add(endTurn);
		scrollComponents.add(addFittedLabel(jpanel, "--------------------------------------------------"));
		
		label1 = addFittedLabel(jpanel, "Welcome to DoCaBa's Civ!");
		label2 = addFittedLabel(jpanel, "Find your settler to start.");
		
		scrollComponents.add(label1);
		scrollComponents.add(label2);
		
		for(int i = 0; i < 100; i++)
		{
			JButton temp = addFittedButton(jpanel, ""); //Buttons in the scrollComponents start at 4
			scrollComponents.add(temp);
			temp.setVisible(false);
		}

		displayingMap = displayingTechTree = displayingVictoryProgress = true;

		goldDisplay = new JMenuItem("Gold: " + playerCiv.getGold() + " (+" + playerCiv.getGPT() + ")");
		cultureDisplay = new JMenuItem("Culture: " + playerCiv.getCulture() + " (+" + playerCiv.getCPT() + ")");
		scienceDisplay = new JMenuItem("Science: " + playerCiv.getScience() + " (+" + playerCiv.getSPT() + ")");
		turnDisplay = new JMenuItem("Turn: " + turn);

		goldDisplay.setEnabled(false);
		cultureDisplay.setEnabled(false);
		scienceDisplay.setEnabled(false);
		turnDisplay.setEnabled(false);
		
		JMenuItem map = new JMenuItem("World Map");
		JMenuItem techTree = new JMenuItem("Tech Tree");
		JMenuItem victoryProgress = new JMenuItem("Victory Progress");
		JMenuItem toggleActionBar = new JMenuItem("Toggle Action Bar");
		toggleActionBar();

		JMenu displayMenu = new JMenu("Display");
		displayMenu.add(map);
		displayMenu.add(techTree);
		displayMenu.add(victoryProgress);
		displayMenu.add(toggleActionBar);

		JMenuBar bar = new JMenuBar();

		bar.add(goldDisplay);
		bar.add(cultureDisplay);
		bar.add(scienceDisplay);
		bar.add(turnDisplay);
		bar.add(displayMenu);
		frame.add(bar, BorderLayout.NORTH);
		map.addActionListener(new DisplayMapListener());
		techTree.addActionListener(new DisplayTechTreeListener());
		toggleActionBar.addActionListener(new DisplayActionBarListener());
		victoryProgress.addActionListener(new DisplayVictoryProgressListener());
		endTurn.addActionListener(new EndTurnListener());
		start();
	}

	public void start() {

		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	public void run() {

		long start, elapsed, wait;
		while (isRunning) {
			start = System.nanoTime();
			repaint();
			elapsed = System.nanoTime() - start;
			wait = (200 / 6) - elapsed / 100000;
			if (wait <= 0)
				wait = 5;
			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}
			tick();
		}
	}

	private void tick() {
		/*
		 * try { Thread.sleep(100); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		// System.out.println(map.x + "," + map.y + " " + HexTile.RADIUS);
		if (map.x >= 2 * HexTile.r * map.gameHexs.length * SCALE + (map.ZOOM * WIDTH / 2)) {
			map.x = map.ZOOM * WIDTH / 2;
		}

		if (map.x < 0) {
			map.x = 2 * HexTile.r * map.gameHexs.length * SCALE;
		}

		if (movingLeft)
			map.x -= map.movingSpeed / map.ZOOM;
		else if (movingRight)
			map.x += map.movingSpeed / map.ZOOM;
		else if (movingUp && map.y > 160)
			map.y -= map.movingSpeed / map.ZOOM;
		else if (movingDown && map.y < 4865)
			map.y += map.movingSpeed / map.ZOOM;
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, (int) WIDTH, (int) HEIGHT);
		g.setColor(Color.BLACK);
		if (displayingMap) {
			map.draw(g);
			playerCiv.draw(g);
		} else if (displayingTechTree) {

		} else if (displayingVictoryProgress) {
			victoryProgress.draw(g);
		}
		
		frame.repaint();

	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (displayingMap) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				movingRight = true;
			else if (e.getKeyCode() == KeyEvent.VK_LEFT)
				movingLeft = true;
			else if (e.getKeyCode() == KeyEvent.VK_UP)
				movingUp = true;
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				movingDown = true;
			}

			else if (e.getKeyCode() == KeyEvent.VK_G) {
				if (grid)
					grid = false;
				else
					grid = true;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			movingRight = false;
		else if (e.getKeyCode() == KeyEvent.VK_LEFT)
			movingLeft = false;
		else if (e.getKeyCode() == KeyEvent.VK_UP)
			movingUp = false;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			movingDown = false;
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (displayingMap) {
			if (e.getWheelRotation() > 0 && map.ZOOM > .1875)
				map.ZOOM -= .0625;
			if (e.getWheelRotation() < 0 && map.ZOOM < 2)
				map.ZOOM += .0625;
		}
	}

	MouseListener m = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
			dragging = false;

		}

		@Override
		public void mousePressed(MouseEvent e) {
			px1 = e.getPoint().getX();
			py1 = e.getPoint().getY();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (displayingMap) {
				for (int i = 0; i < map.gameHexs.length; i++) {
					for (int j = 0; j < map.gameHexs[0].length; j++) {
						if (map.gameHexs[i][j].getShape().contains((int) e.getPoint().getX(),
								(int) e.getPoint().getY())) {
							if (map.gameHexs[i][j].hasUnit() && map.gameHexs[i][j].getUnit().canMove()) {
								label1.setText(map.gameHexs[i][j].getUnit().getCiv().getName() + "'s " + map.gameHexs[i][j].getUnit().toString());
								label2.setText("Movement: " + "ADD CURRENT MOVES /" + map.gameHexs[i][j].getUnit().movingSpeed());
								isSelected = true;
								selected = map.gameHexs[i][j].getUnit();
								availableTiles = HexMap.getSurroundingTiles(i, j, ((Unit) selected).movingSpeed);
								/*for (HexTile tile : availableTiles) {
									Color c = new Color((int) Math.round((tile.getColor().getRed() + 255) / 2),
											(int) Math.round((tile.getColor().getGreen()) / 2),
											(int) Math.round(tile.getColor().getBlue()));

									tile.setColor(c);
								}*/
								if(selected instanceof Settler)
								{
									resetButtons();
									((JButton)scrollComponents.get(4)).addActionListener(new CreateCityListener((Settler)selected));
									((JButton)scrollComponents.get(4)).setText("Create City!");
									((JButton)scrollComponents.get(4)).setVisible(true);

									for(int n = 5; n < scrollComponents.size(); n++)
									{
										scrollComponents.get(n).setVisible(false);
									}
								}
							}
							else if(map.gameHexs[i][j].hasCity() && map.gameHexs[i][j].getCity().getCiv().equals(playerCiv)){

								selected = map.gameHexs[i][j].getCity();
								int k = 4;
								resetButtons();
								for(String str: ((City)selected).building.buildings.keySet()){
									((JButton)scrollComponents.get(k)).setText(str);
									((JButton)scrollComponents.get(k)).setVisible(true);
									String[] effects = ((City)selected).building.buildings.get(str);
									String effectsStr = "<html>" + "Production Cost: " + effects[0];
									for(int n = 1; n < effects.length; n++){
										effectsStr += "<br>" + Buildings.toString(effects[n]);
									}
									effectsStr += "</html>";
									((JButton)scrollComponents.get(k)).setToolTipText(effectsStr);
									((JButton)scrollComponents.get(k)).addActionListener(new BuyListener(str, map.gameHexs[i][j].getCity().getCiv()));
									k++;
								}
								label1.setText(selected.toString());
								label2.setText("Production: " + ((City)selected).getPPT());
							} else if (e.getButton() == e.BUTTON3 && isSelected
									&& availableTiles.contains(map.gameHexs[i][j]) && selected instanceof Unit) {
								((Unit) selected).move(i, j);
								isSelected = false;
								selected = null;
								for (HexTile tile : availableTiles) {
									Color c;
									if (tile.type.equals("land"))
										c = Color.GREEN;
									else if (tile.type.equals("mountain"))
										c = Color.GRAY;
									else if (tile.type.equals("desert"))
										c = Color.YELLOW;
									else
										c = Color.BLUE;
									tile.setColor(c);
								}
								availableTiles.clear();
							}
						}
					}
				}
			}
		}
	};
	MouseMotionListener mouse = new MouseMotionListener() {

		@Override
		public void mouseMoved(MouseEvent p) {

		}

		@Override
		public void mouseDragged(MouseEvent m) {
			if (displayingMap) {
				Point a = m.getPoint();
				double px2 = a.getX();
				double py2 = a.getY();
				map.x += (px1 - px2) / map.ZOOM;
				if (map.y > 160 && (py1 - py2) < 0) {
					map.y += (py1 - py2) / map.ZOOM;
				} else if (map.y < 4865 && (py1 - py2) > 0) {
					map.y += (py1 - py2) / map.ZOOM;
				}
				px1 = px2;
				py1 = py2;
			}
		}
	};
	public JButton addFittedButton(JPanel p, String t)
	{		
		JPanel temp = new JPanel();
		temp.setLayout(new GridLayout(1, 0));
		JButton button = new JButton(t);
		temp.add(button);
		p.add(temp);
		return button;
	}
	public JLabel addFittedLabel(JPanel p, String t)
	{
		JPanel temp = new JPanel();
		temp.setLayout(new GridLayout(1, 0));
		JLabel label = new JLabel(t);
		temp.add(label);
		p.add(temp);
		return label;
	}
	public void toggleActionBar() {
		if (framer) {
			jpanel.setVisible(false);
			jscroll.setVisible(false);
			jscroll.setViewportView(null);
			frame.revalidate();
			framer = false;
		} else {
			jpanel.setVisible(true);
			jscroll.setVisible(true);
			updatePanelSize();
			jscroll.setViewportView(jpanel);
			frame.revalidate();
			framer = true;
			System.out.println(jpanel.getPreferredSize());
		}
	}
	public void updatePanelSize() {
		int l = 0;
		for(int i = 0; i < scrollComponents.size(); i++)
		{
			if (scrollComponents.get(i).isVisible())
				l += 26;
		}
		jpanel.setPreferredSize(new Dimension(200, l - 30));
	}
	private class BuyListener implements ActionListener{
		public String myX; public Civilization civ;
		public BuyListener(String x, Civilization c)
		{
			myX = x;
			civ = c;
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println(myX);
			requestFocus();
		}
	}
	private class CreateCityListener implements ActionListener{
		public Settler mySettler;
		public CreateCityListener(Settler s)
		{
			mySettler = s;
		}
		public void actionPerformed(ActionEvent e)
		{
			try{
				mySettler.createCity();
				mySettler = null;
				for(int i = 4; i < scrollComponents.size(); i++)
				{
					scrollComponents.get(i).setVisible(false);
				}
				label1.setText("");
				label2.setText("");
				playerCiv.update();
				updateJMenu();
			}
			catch (java.lang.NullPointerException j)
			{
				System.out.println("You've already created a city with the selected Settler!");
			}
			requestFocus();
		}
	}
	private class DisplayTechTreeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			displayingMap = false;
			displayingTechTree = true;
			displayingVictoryProgress = false;
		}
	}

	private class DisplayMapListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			displayingMap = true;
			displayingTechTree = false;
			displayingVictoryProgress = false;
		}
	}

	private class DisplayVictoryProgressListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			displayingMap = false;
			displayingTechTree = false;
			displayingVictoryProgress = true;
		}
	}

	private class DisplayActionBarListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			toggleActionBar();
		}
	}
	private class EndTurnListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			nextTurn();
			requestFocus();
		}
	}
	public void resetButtons()
	{
		for(int i = 4; i < scrollComponents.size(); i++)
		{
			ActionListener[] temp = ((JButton)scrollComponents.get(i)).getActionListeners();
			for(int k = 0; k < ((JButton)scrollComponents.get(i)).getActionListeners().length; k++)
			{
				((JButton)scrollComponents.get(i)).removeActionListener(temp[k]);	
			}
		}
	}
	public void updateJMenu()
	{
		scienceDisplay.setText("Science: " + playerCiv.getScience() + " (+" + playerCiv.getSPT() + ")");
		cultureDisplay.setText("Culture: " + playerCiv.getCulture() + " (+" + playerCiv.getCPT() + ")");
		goldDisplay.setText("Gold: " + playerCiv.getGold() + " (+" + playerCiv.getGPT() + ")");
	}
	public void nextTurn() {
		turn++;
		turnDisplay.setText("Turn: " + turn);
		for (Civilization civ : civs) {
			civ.update();
			civ.setCulture(civ.getCulture() + civ.getCPT());
			civ.setGold(civ.getGold() + civ.getGPT());
			civ.setScience(civ.getScience() + civ.getSPT());
			for (Unit unit : civ.getUnits()) {
				unit.canMove(true);
			}
		}
		updateJMenu();
	}
}
