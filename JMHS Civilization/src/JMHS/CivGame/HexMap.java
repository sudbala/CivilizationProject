package JMHS.CivGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

public class HexMap {

	public static HexTile[][] gameHexs;
	public HexTile[][] gameBoard2;
	double x = Main.WIDTH / 2;
	double y = Main.HEIGHT / 2;
	int MapSize = 0;
	public static double ZOOM = 2;
	double startx = HexTile.RADIUS;
	double starty = (HexTile.RADIUS * Main.SCALE);
	int movingSpeed = 10;
	float[][] color;
	float[][] moisture;

	public HexMap(int numx, int numy) {
		int octaveCount = 5;
		int octaveCount2 = 2;
		gameHexs = new HexTile[numx][numy];
		gameBoard2 = new HexTile[numx][numy];
		GenerateMap hexColors = new GenerateMap(gameHexs.length, gameHexs[0].length);
		GenerateMap hexMoisture = new GenerateMap(gameHexs.length, gameHexs[0].length);
		float[][] color = hexColors.GeneratePerlinNoise(gameHexs.length, gameHexs[0].length, octaveCount);
		float[][] moisture = hexMoisture.GeneratePerlinNoise(gameHexs.length, gameHexs[0].length, octaveCount2);

		for (int i = 0; i < gameHexs.length; i++) {
			for (int j = 0; j < gameHexs[0].length; j++) {
				gameHexs[i][j] = new HexTile(j * HexTile.RADIUS * Main.SCALE + (2 * starty * i) + starty,
						startx + j * (HexTile.RADIUS + HexTile.RADIUS * Math.sin(Math.PI / 6)), color[i][j],
						moisture[i][j], i, j);
			}
		}
	}

	public void draw(Graphics g) {
		HexTile.RADIUS = (int) Math.round(HexTile.r * ZOOM);
		for (int i = 0; i < gameHexs.length; i++) {
			for (int j = 0; j < gameHexs[0].length; j++) {
				double boardx = ZOOM * (gameHexs[i][j].getX() - (x - (Main.WIDTH / (ZOOM * 2))));
				double boardy = ZOOM * (gameHexs[i][j].getY() - (y - (Main.HEIGHT / (ZOOM * 2))));
				if (boardx < -(Main.SCALE * HexTile.RADIUS))
					boardx += 2 * gameHexs.length * HexTile.RADIUS * Main.SCALE;
				if (boardx > Main.WIDTH + (Main.SCALE * HexTile.RADIUS))
					boardx -= 2 * gameHexs.length * HexTile.RADIUS * Main.SCALE;
				gameHexs[i][j].setCoords(boardx, boardy);
//				if(i % gameHexs.length == 0) gameHexs[i][j].draw(g, 1);
//				if(gameHexs[i][j].type.equals("land")){}
//					gameHexs[i][j].draw(g, 2);
				 gameHexs[i][j].draw(g);
			}
		}
	}

	public static void getAdjacient(int x, int y, ArrayList<HexTile> tiles) {
		int xleft = x - 1, xright = x + 1, ytop = y - 1, ybottom = y + 1;
		if (x - 1 < 0)
			xleft = gameHexs.length - 1;
		if (x + 1 >= gameHexs.length)
			xright = 0;
		if (y - 1 < 0)
			ytop = 0;
		if (y + 1 >= gameHexs.length)
			ybottom = gameHexs[0].length - 1;

		/*
		 * if(!tiles.contains(gameHexs[x][y])) tiles.add(gameHexs[x][y]);
		 * if(!tiles.contains(gameHexs[x][y - 1])) tiles.add(gameHexs[x][y -
		 * 1]); if(!tiles.contains(gameHexs[x + 1][y - 1])) tiles.add(gameHexs[x
		 * + 1][y - 1]); if(!tiles.contains(gameHexs[x + 1][y]))
		 * tiles.add(gameHexs[x + 1][y]); if(!tiles.contains(gameHexs[x -
		 * 1][y])) tiles.add(gameHexs[x - 1][y]); if(!tiles.contains(gameHexs[x
		 * - 1][y + 1])) tiles.add(gameHexs[x - 1][y + 1]);
		 * if(!tiles.contains(gameHexs[x][y + 1])) tiles.add(gameHexs[x][y +
		 * 1]);
		 */

		if (!tiles.contains(gameHexs[x][y]))
			tiles.add(gameHexs[x][y]);
		if (!tiles.contains(gameHexs[x][ytop]))
			tiles.add(gameHexs[x][ytop]);
		if (!tiles.contains(gameHexs[xright][ytop]))
			tiles.add(gameHexs[xright][ytop]);
		if (!tiles.contains(gameHexs[xright][y]))
			tiles.add(gameHexs[xright][y]);
		if (!tiles.contains(gameHexs[xleft][y]))
			tiles.add(gameHexs[xleft][y]);
		if (!tiles.contains(gameHexs[xleft][ybottom]))
			tiles.add(gameHexs[xleft][ybottom]);
		if (!tiles.contains(gameHexs[x][ybottom]))
			tiles.add(gameHexs[x][ybottom]);
	}

	public static ArrayList<HexTile> getSurroundingTiles(int x, int y, int radius) {
		ArrayList<HexTile> tiles = new ArrayList<HexTile>();
		tiles.add(gameHexs[x][y]);
		getAdjacient(tiles.get(0).i, tiles.get(0).j, tiles);
		for (int i = 1; i < radius; i++) {
			int size = tiles.size();
			for (int j = 1; j < size; j++) {
				getAdjacient(tiles.get(j).i, tiles.get(j).j, tiles);
			}
		}
		return tiles;
	}
}
