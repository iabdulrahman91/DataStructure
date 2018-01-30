package hw5;

import java.util.*;

// Rectangular maze without any walls (except for the boundary).

public class RandomMaze extends GenericMaze {
	private LinkedList<IntPair> exits;
	private LinkedList<IntPair> weapons;
	private LinkedList<IntPair> monsters;
	private LinkedList<IntPair> walls;
	
	public RandomMaze(IntPair size, Random rand, int numExits, int numMonsters, int numWeapons, int numWalls) {
		super(size);
		if (numExits + numMonsters + numWeapons + numWalls + 1 > size.x * size.y) {
			throw new IllegalArgumentException("Too many features to place!");
		}

		exits = randomLocations(rand, numExits, Maze.EXIT);
		weapons = randomLocations(rand, numWeapons, Maze.WEAPON);
		monsters = randomLocations(rand, numMonsters, Maze.MONSTER);
		walls = randomLocations(rand, numWalls, Maze.WALL);
	}
	
	private LinkedList<IntPair> randomLocations(Random rand, int num, int feature) {
		LinkedList<IntPair> pairs = new LinkedList<IntPair>();
		while (num-- > 0) {
			IntPair pair = randomEmptyLocation(rand);
			maze[pair.x][pair.y] = feature;
			pairs.add(pair);
		}
		return pairs;
	}
	
	private IntPair randomEmptyLocation(Random rand) {
		int lx, ly;
		do {
			lx = rand.nextInt(size.x);
			ly = rand.nextInt(size.y);
		} while ((lx == 0 && ly == 0) || maze[lx][ly] != Maze.EMPTY);
		return new IntPair(lx, ly);
	}
	
	public Iterable<IntPair> getExits() {
		return exits;
	}
	
	public Iterable<IntPair> getWeapons() {
		return weapons;
	}
	
	public Iterable<IntPair> getMonsters() {
		return monsters;
	}
	
	public Iterable<IntPair> getWalls() {
		return walls;
	}
	
	public Iterable<IntPair> aliveMonsters() {
		LinkedList<IntPair> alive = new LinkedList<IntPair>();
		for (IntPair mon : monsters) {
			if (maze[mon.x][mon.y] == Maze.MONSTER) alive.add(mon);
		}
		return alive;
	}
	
	public boolean atExit() {
		return maze[x][y] == (Maze.EXIT | Maze.VISITED);
	}
}