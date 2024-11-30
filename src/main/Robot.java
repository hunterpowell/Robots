package main;

import java.util.Random;

public class Robot {
	// fields
	int direction[];
	Vector2D coords;
	int energy;
	int fitness;
	int turnsAlive;
	int genes[][];
	int movementGene[];
	int totalGenes;
	int genesPerBot;
	Random rand;
	int r;
	
	// constructor
	public Robot() {
		direction = new int[4];
		rand = new Random();
		energy = 5;
		fitness = 0;
		turnsAlive = 0;
		totalGenes = 16;
		genesPerBot = 4;
		genes = new int[totalGenes][genesPerBot];
		movementGene = new int[totalGenes];
		
		// populates genes with random values between 0 and 2: empty space, wall, battery;
		rand = new Random();
		for (int i = 0; i < totalGenes; i++) {
			for (int j = 0; j < genesPerBot; j++) {
				r = rand.nextInt(4);
				genes[i][j] = r;
			}
		}
		// movement gene gets values between 0 and 4: move n, e, s, w, and random move
		for (int i = 0; i < totalGenes; i++) {
			r = rand.nextInt(5);
			movementGene[i] = r;
		}
		
	}

    // DEEP COPY CONSTRUCTOR (these r neat)
    public Robot(Robot other) {
        // deep copy fields
		direction = new int[4];
		rand = new Random();
		energy = 5;
		fitness = 0;
		turnsAlive = 0;
		totalGenes = 16;
		genesPerBot = 4;

		// deep copy the genes array
		genes = new int[totalGenes][genesPerBot];
		movementGene = new int[totalGenes];
        for (int i = 0; i < 16; i++) {
			System.arraycopy(other.genes, 0, this.genes, 0, this.genes.length);
            this.movementGene[i] = other.movementGene[i];
        }
    }
	
	// methods
	public void displayGenes() {
		for (int i = 0; i < totalGenes; i++) {
			for (int j = 0; j < genesPerBot; j++) 
				System.out.print(genes[i][j] + " ");
			System.out.print(movementGene[i] + "\n");
		}
	}
	
	public void randomStart(Map m) {
		rand = new Random();
		this.coords = new Vector2D(rand.nextInt(10)+1, rand.nextInt(10)+1);
		m.map[this.coords.getY()][this.coords.getX()] = 9;
	}
	
	public void look(Map m) {
		this.direction[0] = m.map[this.coords.getY()-1][this.coords.getX()];			// north
		this.direction[1] = m.map[this.coords.getY()]  [this.coords.getX()+1];			// east
		this.direction[2] = m.map[this.coords.getY()+1][this.coords.getX()];			// south
		this.direction[3] = m.map[this.coords.getY()]  [this.coords.getX()-1];			// west
	}

	public void movement(Map m) {
		rand = new Random();
		int dir = this.movementGene[checkGenes()];
		
		// random direction if movement gene is 4
		if (dir == 4)
			dir = rand.nextInt(4);
		
		this.move(m, dir);
	}
	
	private void move(Map m, int dir) {
		this.energy -= 1;
		this.turnsAlive += 1;
		if (this.direction[dir] != 2) {
			// if battery, eat it
			if (this.direction[dir] == 1) {
				this.energy += 5;
				this.fitness += 5;
			}
			// else if super battery, eat it
			else if (this.direction[dir] == 3) {
				this.energy += 10;
				this.energy += 10;
			}
			
			// reset current square, see end of switch statement
			m.map[this.coords.getY()][this.coords.getX()] = 0;
			switch (dir) {
				case 0 -> this.coords.adjustY(-1);
				case 1 -> this.coords.adjustX(1);
				case 2 -> this.coords.adjustY(1);
				case 3 -> this.coords.adjustX(-1);
			}
			
			// set new current square depending on direction moved
			m.map[this.coords.getY()][this.coords.getX()] = 9;
			this.look(m);
		}
		// consume energy but can't move :(
		else 
			this.look(m);
	}

    public void reset() {
        this.energy = 5;
        this.fitness = 0;
		this.turnsAlive = 0;
    }

	private int checkGenes() {
		for (int i = 0; i < this.totalGenes; i++) {
			if (this.direction[0] == this.genes[i][0] && 
				this.direction[1] == this.genes[i][1] && 
				this.direction[2] == this.genes[i][2] && 
				this.direction[3] == this.genes[i][3])
				return i;
		}
		// defaults to last gene if no matches
		return this.movementGene[this.totalGenes - 1];
	}
}
