# Robot Evolution Simulation

## Overview
This project implements a genetic algorithm to evolve virtual robots that navigate through a 2D grid environment. The robots must learn to efficiently collect batteries while avoiding walls, with their behavior determined by a genetic encoding of movement patterns.

## Features
- 🤖 Evolutionary algorithm with configurable parameters
- 🗺️ Randomized map generation with customizable dimensions
- 🔋 Battery collection and energy management system
- 🧬 Genetic crossover and mutation operations
- 📊 Generation-by-generation fitness tracking
- 🏆 Elite preservation between generations

## Technical Details

### Configuration Parameters
```java
ROBOTS_PER_GENERATION = 500    // Number of robots in each generation
TOTAL_GENERATIONS = 500        // Number of generations to run
ELITE_PERCENTAGE = 0.5         // Percentage of top performers to preserve
TOURNAMENT_SIZE = 5            // Number of robots in each tournament selection
MUTATION_RATE = 0.03          // Probability of gene mutation
```

### Map Environment
- 22x22 grid environment
- Walls around the perimeter
- 40% of inner cells contain batteries
- Three types of cells:
  - Empty (0)
  - Battery (1)
  - Wall (2)

### Robot Capabilities
- Energy-based lifespan
- Four-directional movement (N, E, S, W)
- Environmental sensing
- Genetic encoding of behavior patterns
- Fitness based on battery collection and survival time

## Class Structure

### Main
Controls the simulation flow and evolutionary process:
- Generation management
- Population evolution
- Fitness evaluation
- Statistics tracking

### Robot
Represents individual robots with:
- Movement logic
- Gene structure
- Fitness tracking
- Energy management

### Map
Handles the environment:
- Grid generation
- Battery placement
- Wall management
- Environment display

### Vector2D
Utility class for 2D coordinates:
- Position management
- Movement calculations
- Coordinate adjustments

## Algorithm Details

### Evolution Process
1. Initialize random population
2. For each generation:
   - Run fitness evaluation
   - Sort by performance
   - Preserve elite performers
   - Select parents via tournament
   - Create offspring through crossover
   - Apply random mutations
   - Replace population

### Fitness Calculation
Robots gain fitness points by:
- Collecting standard batteries (+5 points)
- Surviving longer periods

## Usage

### Running the Simulation
```bash
javac main/*.java
java main.Main
```

### Output
The simulation provides:
- Generation-by-generation fitness averages
- Best performer statistics
- Final generation analysis
- Best robot's path visualization

## Future Improvements
- [ ] Implement parallelization for fitness evaluation
- [ ] Add visualization of evolution progress
- [ ] Introduce more complex environments
