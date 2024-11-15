package main;

import java.util.Random;

public class Main {
	// configurable sim numbers
	private static final class SimulationConfig {
		private static final int robotsPerGen = 500;
		private static final int generations = 500;
		private static final double topPercent = 0.5;
		private static final int tournamentSize = 5;
		private static final double mutationRate = 0.03;
	}

	private static class SimulationState {
		public int bestGen = 0;
		public int bestBot = 0;
		public Map bestMap = new Map();
		public final Robot roboArray[];
		public final int avgFitness[];
		public final Map map1[];

		public SimulationState() {
			this.roboArray = new Robot[SimulationConfig.robotsPerGen];
			this.avgFitness = new int[SimulationConfig.generations];
			this.map1 = new Map[SimulationConfig.robotsPerGen];
		}
	}

	public static void main(String args[]) {
		SimulationState state = new SimulationState();
		initializeSimulation(state);
		runSimulation(state);
	}

	private static void initializeSimulation(SimulationState state) {
		for (int i = 0; i < SimulationConfig.robotsPerGen; i++) {
			state.roboArray[i] = new Robot();
		}
	}

	private static void runSimulation(SimulationState state) {
		
		// main loop
		for (int k = 0; k < SimulationConfig.generations; k++) {
			
			int avg = 0;
			// loops the generation through the maze
			for (int i = 0; i < SimulationConfig.robotsPerGen; i++) {
				state.map1[i] = new Map();
				state.roboArray[i].reset();
				state.roboArray[i].randomStart(state.map1[i]);
				state.roboArray[i].look(state.map1[i]);
				while (state.roboArray[i].energy > 0) {
					state.roboArray[i].movement(state.map1[i]);
				}
				avg += state.roboArray[i].fitness;
				// p(roboArray[i].fitness);
			}
			 
			// displays 69th robot from gen 1 for comparison
			if (k == 0) {
				p("Random selection of gen 1: " + state.roboArray[0].turnsAlive);
				state.map1[69].displayMap();
			}

			state.avgFitness[k] = (avg / SimulationConfig.robotsPerGen);
			// sorts by fitness
			evaluateFitness(state, k);
			avg = 0;
			
			// display stats if final gen
			if (k == SimulationConfig.generations - 1) {
				finalStats(state);
			}

			// next generation
			evolveNextGen(state);
		}
	}

	private static void evolveNextGen(SimulationState state) {
		// next generation
		Robot[] nextGen = new Robot[SimulationConfig.robotsPerGen];

		// preserve the top n% of performers
		for (int i = 0; i < SimulationConfig.robotsPerGen * SimulationConfig.topPercent; i++) 
			nextGen[i] = new Robot(state.roboArray[i]);
		
		// bottom n% get new genes from crossover
		for (int i = (int)(SimulationConfig.robotsPerGen * SimulationConfig.topPercent); i < SimulationConfig.robotsPerGen; i+= 2) {
			// tourney size of 5 promotes decent evolutionary pressure while preserving diversity. higher number is more pressure but lower diversity
			Robot parent1 = tournament(state.roboArray);
			Robot parent2 = tournament(state.roboArray);

			// babies
			Robot[] children = crossover(parent1, parent2);

			// this is why we're stepping by 2
			nextGen[i] = children[0];
			if (i + 1 < SimulationConfig.robotsPerGen) 
				nextGen[i + 1] = children[1];
		}

		// replace old gen with new
		System.arraycopy(nextGen, 0, state.roboArray, 0, SimulationConfig.robotsPerGen);
	}

	private static Robot tournament(Robot[] population) {
		Robot best = null;
		for (int i = 0; i < SimulationConfig.tournamentSize; i++) {
			int x = (int)(Math.random() * population.length);
			if (best == null || population[x].fitness > best.fitness) 
				best = population[x];
		}
		return best;
	}

	private static Robot[] crossover(Robot p1, Robot p2) {
		Robot child1 = new Robot();
		Robot child2 = new Robot();
		Random rand = new Random();

		for (int y = 0; y < 16; y++) {
			if (Math.random() < 0.5) {
				// copy parent 1 to child 1 and vice versa
				for (int z = 0; z < 4; z++) {
					child1.genes[y][z] = p1.genes[y][z];
					child2.genes[y][z] = p2.genes[y][z];
					if (Math.random() < SimulationConfig.mutationRate) { 
						// randomly changes a gene to a new state
						child1.genes[y][z] = rand.nextInt(3);
						child2.genes[y][z] = rand.nextInt(3);
					}
				}
				child1.movementGene[y] = p1.movementGene[y];
				child2.movementGene[y] = p2.movementGene[y];
			}
			else {
				// copy parent 1 to child 2 and vice versa
				for (int z = 0; z < 4; z++){
					child1.genes[y][z] = p2.genes[y][z];
					child2.genes[y][z] = p1.genes[y][z];
					if (Math.random() < SimulationConfig.mutationRate) {
						// randomly changes a gene to a new state
						child1.genes[y][z] = rand.nextInt(3);
						child2.genes[y][z] = rand.nextInt(3);
					}
				}
				child1.movementGene[y] = p2.movementGene[y];
				child2.movementGene[y] = p1.movementGene[y];
			}
		}
		
		return new Robot[]{child1, child2};
	}

	private static void evaluateFitness(SimulationState state, int currentGen) {
		sort(state.roboArray, state.map1);
		if (state.avgFitness[currentGen] > state.avgFitness[state.bestGen]){
			state.bestGen = currentGen;
			state.bestBot = state.roboArray[0].fitness;
			state.bestMap = state.map1[0];
		}

		// prints avg fitness of each gen
		p("Avg fitness of gen " + (currentGen + 1) + ": " + state.avgFitness[currentGen]);
	}

	private static void sort(Robot[] r, Map[] m) {
		// selection sort
		for (int i = 0; i < SimulationConfig.robotsPerGen; i++) {
			for (int j = 0; j < SimulationConfig.robotsPerGen; j++) {
				if (r[i].fitness > r[j].fitness) {
					Robot tmp = r[i];
					r[i] = r[j];
					r[j] = tmp;
					// keeps each map associated with its robot
					Map tmp2 = m[i];
					m[i] = m[j];
					m[j] = tmp2;
				}	
			}
		}
	}

	private static void finalStats(SimulationState state){
		p("\nFinal Results:");
				p("-------------------------------------------");
				p("Best generation: " + (state.bestGen + 1));
				p("Best generation average fitness: " + state.avgFitness[state.bestGen]);
				p("Best bot of best gen fitness: " + state.bestBot);
				p("Mathematical maximum fitness: " + (int)(Math.pow(state.map1[0].size-2, 2))*2);
				p("-------------------------------------------");
				state.bestMap.displayMap();
	}
	
	// handy dandy print method
	public static <E> void p(E item){

		System.out.println(item);

	}
}