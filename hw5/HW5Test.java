package hw5;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Random;

public class HW5Test {
	String border = "*******************************************\n";
	String passed = "* Passed!                                 *\n";
	String failed = "* Failed!                                 *\n";
	String test;
	
	AssertionError ae;
	Exception e;
	
	int x, y;
	int ex, mo, wp, wl;
	int r[][];
	IntPair src, dest, min;
	RandomMaze maze;
	Exploration exp;
	
	private void newExp(Random rand) {
		int k = 0;
		while (true) {
			IntPair sz = new IntPair(x, y);
			maze = new RandomMaze(sz, rand, ex, mo, wp, wl);
			exp = new Exploration(maze);
			if (isReachable())
				return;
			if (k++ > 1000) {
				System.out.println("ERROR: Infinite loop in test file!");
				throw new IllegalStateException();
			}
		}
	}
	
	private boolean isReachable() {
		r = new int[x][y];
		for (IntPair mon : maze.getMonsters()) {
			r[mon.x][mon.y] = -1;
		}
		for (IntPair mon : maze.getWalls()) {
			r[mon.x][mon.y] = -2;
		}
		r[0][0] = 1;
		boolean update = true;
		while (update) {
			update = false;
			for (int i = 0; i < x; i++) {
				for (int j = 0; j < y; j++) {
					if (r[i][j] == 1 || r[i][j] < 0) continue;
					if (i > 0 && r[i - 1][j] == 1 || j > 0 && r[i][j - 1] == 1 ||
						i < x - 1 && r[i + 1][j] == 1 || j < y - 1 && r[i][j + 1] == 1) {
						update = true;
						r[i][j] = 1;
					}
				}
			}
		}
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				if (r[i][j] >= 0) continue;
				if (i > 0 && r[i - 1][j] == 1 || j > 0 && r[i][j - 1] == 1 ||
					i < x - 1 && r[i + 1][j] == 1 || j < y - 1 && r[i][j + 1] == 1) {
					r[i][j] = 1 - r[i][j];
				}
			}
		}
		boolean reachable = true;
		for (IntPair loc : maze.getExits()) {
			reachable = r[loc.x][loc.y] == 1;
			if (reachable) break;
		}
		if (!reachable) return false;
		for (IntPair loc : maze.getWeapons()) {
			reachable = r[loc.x][loc.y] == 1;
			if (reachable) break;
		}
		for (IntPair loc : maze.getMonsters()) {
			if (!reachable) break;
			reachable = r[loc.x][loc.y] == 2;
		}
		return reachable;
	}
	
	private void tstNumM() {
		Random rand = new Random(0);
		for (mo = 0; mo < 10; mo++) {
			for (ex = 0; ex < 2; ex++) {
				for (wp = 0; wp < 2; wp++) {
					for (x = 8; x <= 32; x += 8) {
						for (y = 8; y <= 32; y += 8) {
							for (wl = 0; wl <= x * y / 4; wl += 16) {
								newExp(rand);
								int m = exp.numMonsters();
								assertEquals(mo, m);
							}
						}
					}
				}
			}
		}
	}
	
	private void tstWalk() {
		Random rand = new Random(0);
		for (mo = 0; mo < 10; mo++) {
			for (ex = 0; ex < 3; ex++) {
				for (wp = 0; wp < 3; wp++) {
					for (x = 8; x <= 32; x += 8) {
						for (y = 8; y <= 32; y += 8) {
							for (wl = 0; wl <= x * y / 4; wl += 16) {
								newExp(rand);
								src = new IntPair(0, 0);
								for (int i = 0; i < 5; i++) {
									while (true) {
										int dx = rand.nextInt(x);
										int dy = rand.nextInt(y);
										if (r[dx][dy] != 1)
											continue;
										dest = new IntPair(dx, dy);
										break;
									}
									exp.walkTo(dest);
									IntPair loc = maze.getLocation();
									assertEquals(dest.x, loc.x);
									assertEquals(dest.y, loc.y);
									src = dest;
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void tstFindW() {
		Random rand = new Random(0);
		for (mo = 0; mo < 10; mo++) {
			for (ex = 0; ex < 2; ex++) {
				for (wp = 1; wp < 3; wp++) {
					for (x = 8; x <= 32; x += 8) {
						for (y = 8; y <= 32; y += 8) {
							for (wl = 0; wl <= x * y / 4; wl += 16) {
								newExp(rand);
								dest = exp.findWeapon();
								src = maze.getLocation();
								assertEquals(dest.x, src.x);
								assertEquals(dest.y, src.y);
								boolean found = false;
								for (IntPair wep : maze.getWeapons()) {
									found = found || (wep.x == dest.x && wep.y == dest.y);
								}
								assertTrue(found);
							}
						}
					}
				}
			}
		}
	}
	
	private void tstKillM() {
		Random rand = new Random(0);
		for (ex = 0; ex < 2; ex++) {
			for (wp = 1; wp < 3; wp++) {
				for (x = 8; x <= 32; x += 8) {
					for (y = 8; y <= 32; y += 8) {
						for (wl = 0; wl <= x * y / 4; wl += 16) {
							int maxmon = x * y / 2 - wl - 10;
							for (mo = 1; mo <= 16 && mo <= maxmon; mo++) {
								newExp(rand);
								exp.killMonsters();
								assertFalse(maze.aliveMonsters().iterator().hasNext());
								boolean alive = false;
								assertFalse(alive);
							}
						}
					}
				}
			}
		}
	}
	
	private int[][] distances() {
		int d[][] = new int[x][y];
		for (IntPair mon : maze.aliveMonsters()) {
			d[mon.x][mon.y] = -2;
		}
		for (IntPair wal : maze.getWalls()) {
			d[wal.x][wal.y] = -2;
		}
		boolean update = true;
		int k;
		for (k = 0; update; k++) {
			update = false;
			for (int i = 0; i < x; i++) {
				for (int j = 0; j < y; j++) {
					if (i == 0 && j == 0 || d[i][j] != k || r[i][j] <= 0) continue;
					if (i > 0 && d[i - 1][j] == k || j > 0 && d[i][j - 1] == k) continue;
					if (i < x - 1 && d[i + 1][j] == k - 1 || j < y - 1 && d[i][j + 1] == k - 1) continue;
					update = true;
					d[i][j] = k + 1;
				}
			}
			if (k > 1000) {
				System.out.println("ERROR: Infinite loop in test file!");
				throw new IllegalStateException();
			}
		}
		return d;
	}
	
	private void tstExit(boolean nearest) {
		Random rand = new Random(0);
		for (mo = 0; mo <= 10; mo++) {
			for (ex = 1; ex <= 5; ex++) {
				if (nearest == (ex == 1)) continue;
				for (wp = 0; wp < 3; wp++) {
					for (x = 8; x <= 32; x += 8) {
						for (y = 8; y <= 32; y += 8) {
							for (wl = 0; wl <= x * y / 4; wl += 16) {
								newExp(rand);
								src = maze.getLocation();
								dest = exp.findNearestExit();
								IntPair loc = maze.getLocation();
								assertEquals(dest.x, loc.x);
								assertEquals(dest.y, loc.y);
								assertTrue(maze.atExit());

								if (nearest) {
									int d[][] = distances();
									min = dest;
									int mindist = d[dest.x][dest.y] * 2 + dest.x + dest.y;
									for (IntPair ext : maze.getExits()) {
										if (r[ext.x][ext.y] == 0) continue;
										int dist = d[ext.x][ext.y] * 2 + ext.x + ext.y;
										if (dist < mindist)
											min = ext;
									}
									assertEquals(min, dest);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void testMethod(int methodID) throws Exception {
		try {
			System.out.print(border + test + border);
			switch (methodID) {
			case 0: tstNumM(); break;
			case 1: tstWalk(); break;
			case 2: tstFindW(); break;
			case 3: tstKillM(); break;
			case 4: tstExit(false); break;
			case 5: tstExit(true); break;
			}
		} catch(AssertionError aerr) {
			ae = aerr;
		} catch(Exception err) {
			e = err;
		}
		
		if (ae != null || e != null) {

			switch (methodID) {
			case 0: System.out.println("Failure in numMonsters, total number of monsters = " + mo);
					break;
			case 1: System.out.println("Failure in walkTo, source = " + src + ", destination = " + dest);
					break;
			case 2: System.out.println("Failure in findWeapon, current location = " + src + ", returned location = " + dest);
					break;
			case 3: System.out.println("Failure in killMonsters, there are alive monsters at below locations: ");
					for (IntPair mon : maze.aliveMonsters()) {
						System.out.println("Alive monster at " + mon);
					}
					break;
			case 4: System.out.println("Failure in findTheOnlyExit, source = " + src + ", current location = " + maze.getLocation() + ", returned location = " + dest);
					break;
			case 5: System.out.println("Failure in findNearestExit, source = " + src + ", current location = " + maze.getLocation() + ", returned location = " + dest);
					break;
			}
			
			System.out.println("The current state of the maze:");
			System.out.println(maze);
			
			System.out.print("\n" + border + test + failed + border);
			
			if (ae != null) throw ae;
			if (e != null) throw e;
		} else {
			System.out.print(border + test + passed + border);
		}
	}
	
	@Test
	public void testNumMonsters() throws Exception {
		test = "* Testing numMonsters                     *\n";
		testMethod(0);
	}
	
	@Test
	public void testWalkTo() throws Exception {
		test = "* Testing walkTo                          *\n";
		testMethod(1);
	}
	
	@Test
	public void testFindWeapon() throws Exception {
		test = "* Testing findWeapon                      *\n";
		testMethod(2);
	}
			
	@Test
	public void testKillMonsters() throws Exception {
		test = "* Testing killMonsters                    *\n";
		testMethod(3);
	}
	
	@Test
	public void testExit() throws Exception {
		test = "* Testing findTheOnlyExit                 *\n";
		testMethod(4);
	}
	
	@Test
	public void testNearestExit() throws Exception {
		test = "* Testing findNearestExit                 *\n";
		testMethod(5);
	}
}