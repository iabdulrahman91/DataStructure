package hw5;

public interface Maze {
	public static final int EMPTY = 0;
	public static final int WALL = 1;
	public static final int MONSTER = 2;
	public static final int WEAPON = 3;
	public static final int EXIT = 4;
	public static final int VISITED = 8;
	
	public static final int WEST = 0;
	public static final int EAST = 1;
	public static final int NORTH = 2;
	public static final int SOUTH = 3;
	
	// returns the size of the maze
	public IntPair getDimensions();

	// returns the coordinates of the current location
	public IntPair getLocation();
	
	// returns what is in the given direction 
	public int peek(int dir);
	
	// take a step along the given direction, throws an Exception for one of the below circumstances 
	// there is a wall in that direction
	// you do not have a weapon and there is a monster in that direction
	public void walk(int dir);

	// jump to a previously visited location loc
	// throws an Exception if loc is not already visited
	public void jumpTo(IntPair loc);

	// picks up the weapon in the cell
	// throws an Exception if there is no weapon to pick up 
	public void pickUpWeapon();
	
	
	// kills the monster in the cell
	// throws an Exception if there is no monster in the cell
	public void kill();
}