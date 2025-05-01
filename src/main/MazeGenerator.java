package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
    private final int WALL = 2;
    private final int BATTERY = 1;
    private final int EMPTY = 0;

    private int[][] maze;
    private int size;
    private Random rand;

    public MazeGenerator(int size) {
        // make sure size is odd
        if (size % 2 == 0) size++;
        this.size = size;
        this.maze = new int[size][size];
        this.rand = new Random();

        // initalize with all walls, carve out paths later
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.maze[i][j] = WALL;
            }
        }

        generateMaze();
    }

    private void generateMaze() {
        int startX = 1;
        int startY = 1;

        // starting cell empty
        maze[startY][startX] = EMPTY;

        Stack<int[]> stack = new Stack<>();
        stack.push(new int[] {startX, startY});

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int x = current[0];
            int y = current[1];

            ArrayList<int[]> neighbors = getUnvisitedNeighbors(x,y);

            if (neighbors.isEmpty()) {
                // no unvisited neighbors, backtrack
                stack.pop();
            }
            else {
                // choose unvisited neighbor
                int[] next = neighbors.get(rand.nextInt(neighbors.size()));
                int nextX = next[0];
                int nextY = next[1];

                // remove wall between current cell and chosen cell
                maze[(y + nextY) / 2][(x + nextX) / 2] = EMPTY;

                // mark chosen cell as empty
                maze[nextY][nextX] = EMPTY;

                // push chosen cell to stack
                stack.push(new int[] {nextX, nextY});
            }
        }

        maze[size-2][size-2] = BATTERY;

    }

    private ArrayList<int[]> getUnvisitedNeighbors(int x, int y) {
        ArrayList<int[]> neighbors = new ArrayList<>();

        int[][] directions = {{0,-2}, {2,0}, {0,2}, {-2,0}};
        
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (newX > 0 && newX < size - 1 && newY > 0 && newY < size - 1 && maze[newY][newX] == WALL) {
                neighbors.add(new int[] {newX, newY});
            }
        }

        return neighbors;
    }

    public int[][] getMaze() {
        return maze;
    }

    public void displayMaze() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (maze[i][j] == 0) {
                    System.out.print(". ");
                }
                else {
                    System.out.print(maze[i][j] + " ");
                }
            }
            System.out.print("\n");
        }
    }






}
