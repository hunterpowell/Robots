package main;

import java.util.Random;

public class Map {
	// fields
	int[][] map;
	Random rand;
	int size;
	private final int EMPTY = 0;
	private final int BATTERY = 1;
	private final int WALL = 2;
	
	// constructor
	public Map() {
		
		size = 22;
		map = new int[size][size];
		rand = new Random();
		
		// makes walls all 2
		for (int i = 0; i < size; i++) {
			map[0][i] = WALL;
			map[size - 1][i] = WALL;
			map[i][0] = WALL;
			map[i][size - 1] = WALL;
		}

		// exactly 40% batteries populated per map
		int innerCells = (int)Math.pow(size-2, 2);
		int number = (int)(innerCells * 0.4);
		for (int i = 1; i < size - 1; i++) {
			for (int j = 1; j < size - 1; j++) {
				map[i][j] = EMPTY;
			}
		}
		
		int batteriesPlaced = 0;
		while (batteriesPlaced < number) {
			int y = 1 + rand.nextInt(size - 2);
			int x = 1 + rand.nextInt(size - 2);
		
			if (map[y][x] == EMPTY){
				map[y][x] = BATTERY;
				batteriesPlaced++;
			}
		}		
	}
	
	public void displayMap() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.print("\n");
		}
	}
}
