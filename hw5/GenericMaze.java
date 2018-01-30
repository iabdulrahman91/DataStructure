package hw5;

public class GenericMaze implements Maze {
	protected final IntPair size;
	protected int x, y;
	protected boolean hasWeapon;
	protected int maze[][];

	public GenericMaze(IntPair size) {
		this.size = size;
		maze = new int[size.x][size.y];
		visit();
	}
	
	private void visit() {
		maze[x][y] = maze[x][y] | Maze.VISITED;
	}
	
	private int feature(int x, int y) {
		return maze[x][y] & (Maze.VISITED - 1);
	}

	public IntPair getDimensions() {
		return size;
	}

	public IntPair getLocation() {
		return new IntPair(x, y);
	}

	public int peek(int dir) {
		int px = x;
		int py = y;
		
		switch(dir) {
		case WEST: px--; break;
		case EAST: px++; break;
		case NORTH: py--; break;
		case SOUTH: py++; break;
		}
		
		if (px < 0 || px >= size.x || py < 0 || py >= size.y) {
			return Maze.WALL;
		} else {
			return feature(px, py);
		}
	}

	public void walk(int dir) {
		int pk = peek(dir);
		if (pk == Maze.WALL) {
			throw new IllegalStateException("Just walked into a wall!");
		}
		
		switch(dir) {
		case WEST: x--; break;
		case EAST: x++; break;
		case NORTH: y--; break;
		case SOUTH: y++; break;
		}
		
		visit();
		
		if (pk == Maze.MONSTER && !hasWeapon) {
			System.out.println(new String("(:::"));
			System.out.println(this.getLocation());
			throw new IllegalStateException("Just walked into... the monster killed you!");
		}
	}

	public void jumpTo(IntPair loc) {
		if ((maze[loc.x][loc.y] & Maze.VISITED) != Maze.VISITED) {
			throw new IllegalStateException("You can't jump to a location you haven't visited yet!");
		}
		x = loc.x;
		y = loc.y;
	}

	public void pickUpWeapon() {
		if (feature(x, y) != Maze.WEAPON) {
			throw new IllegalStateException("Just tried to pick a weapon where there is none!");
		} else {
			hasWeapon = true;
			maze[x][y] = Maze.VISITED;
		}
	}

	public void kill() {
		if (feature(x, y) != Maze.MONSTER) {
			if (hasWeapon) {
				throw new IllegalStateException("No monsters here... you accidentally killed youself!");
			} else {
				throw new IllegalStateException("There are no monsters here plus you don't have a weapon!");
			}
		} else if (!hasWeapon) {
			throw new IllegalStateException("How were you even able to get here?");
		} else {
			maze[x][y] = Maze.VISITED;
		}
	}
	
	public String toString() {
		String EOL = System.lineSeparator();
		StringBuilder s = new StringBuilder();
		if (size.x > 10) {
			s.append("  ");
			for (int i = 0; i <= (size.x - 1) / 10; i++) {
				s.append(String.format("%-10d", i));
			}
			s.append(EOL);
		}
		s.append("  ");
		for (int i = 0; i < size.x; i++) {
			s.append(i % 10);
		}
		s.append(EOL);
		
		for (int yy = 0; yy < size.y; yy++) {
			String d = "" + (yy / 10);
			int l = yy % 10;
			s.append(l < d.length() ? d.charAt(l) : " ");
			s.append(yy % 10);
			for (int xx = 0; xx < size.x; xx++) {
				switch (maze[xx][yy]) {
				case Maze.EMPTY:
					s.append(" ");
					break;
				case Maze.EXIT:
					s.append("E");
					break;
				case Maze.MONSTER:
					s.append("M");
					break;
				case Maze.WALL:
					s.append("#");
					break;
				case Maze.WEAPON:
					s.append("G");
					break;
				case Maze.VISITED:
					s.append(".");
					break;
				case Maze.VISITED | Maze.EXIT:
					s.append("e");
					break;
				case Maze.VISITED | Maze.MONSTER:
					s.append("m");
					break;
				case Maze.VISITED | Maze.WEAPON:
					s.append("g");
					break;
				}
			}
			s.append(EOL);
		}
		s.append("Currently at location " + getLocation() + EOL);
		return s.toString();
	}
}