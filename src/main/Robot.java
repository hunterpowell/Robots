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
		
		// populates genes with random values between 0 and 3: empty space, wall, battery, super battery;
		rand = new Random();
		for (int i = 0; i < totalGenes; i++) {
			for (int j = 0; j < genesPerBot; j++) {
				r = rand.nextInt(4);
				genes[i][j] = r;
			}
		}
		// movement gene gets values between 0 and 4: move n, e, s, w, and random move
		for (int i = 0; i < totalGenes; i++) {
			int r = rand.nextInt(5);
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
            for (int j = 0; j < 4; j++) {
                this.genes[i][j] = other.genes[i][j];
            }
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
		// default value of 9 to use in case of no match
		int dir = 9;
		Random rand = new Random();
		for (int i = 0; i < this.totalGenes; i++) {
			if (this.direction[0] == this.genes[i][0] && 
				this.direction[1] == this.genes[i][1] && 
				this.direction[2] == this.genes[i][2] && 
				this.direction[3] == this.genes[i][3])
					dir = this.movementGene[i];
		}
		
		// if no match, use final movement gene
		if (dir == 9) 
			dir = this.movementGene[this.totalGenes - 1];
		
		// random direction if movement gene is 4
		if (dir == 4)
			dir = rand.nextInt(4);
		
		this.move(m, dir);
	}
	
	private void move(Map m, int dir) {
//		System.out.println("dir = " + dir);
//		System.out.println("this.direction = " + this.direction[dir]);
//		System.out.println("coords = " + this.coords[0] + ", " + this.coords[1]);
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
				case 0:
					// move robot north
					this.coords.adjustY(-1);
					break;
				case 1:
					// move robot east
					this.coords.adjustX(1);
					break;
				case 2:
					// move robot south
					this.coords.adjustY(1);;
					break;
				case 3:
					// move robot west
					this.coords.adjustX(-1);
					break;
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

}
