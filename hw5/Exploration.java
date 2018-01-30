
package hw5;
import java.util.ArrayList;
import java.util.Random;

public class Exploration {
	private Maze m;
	private IntPair dim;
	public Exploration(Maze m) {
		this.m = m;
		this.dim = m.getDimensions();
	}
	private class init{
		private boolean[][] visited;
		private IntPair loc;
		private Queue<IntPair> q;
		public init(){
			this.visited = new boolean[dim.x][dim.y];
			this.loc = m.getLocation();
			this.q = new Queue<IntPair>();
		}
	}
	
	public int getDirection(IntPair lookup) {
		IntPair currentLocation = m.getLocation();
		if(lookup.x < currentLocation.x) return Maze.WEST;
		else if (lookup.x > currentLocation.x) return Maze.EAST;
		else if(lookup.y < currentLocation.y) return Maze.NORTH;
		else return Maze.SOUTH;
	}
	
	public boolean safePickUpWeapon() {
		try {
			m.pickUpWeapon();
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	public int safeWalk1(int dir) {
		int check = m.peek(dir);
		switch (check){
		case 2:
			return 2;
		case 1:
			return 1;
		default:
			m.walk(dir);
			return 0;
		} 
	}
	
	public boolean marked(IntPair w, boolean[][] visited) {
		return visited[w.x][w.y] == true;
	}
	
	public ArrayList<IntPair> adj(IntPair v) {
		ArrayList<IntPair> ret = new ArrayList<IntPair>();
		if (v.x-1 > -1) ret.add(new IntPair(v.x-1,v.y));
		if (v.x+1 < dim.x) ret.add(new IntPair(v.x+1,v.y));
		if (v.y+1 < dim.y) ret.add(new IntPair(v.x,v.y+1));
		if(v.y-1 > -1) ret.add(new IntPair(v.x,v.y-1));	
		return ret;
	}
	public ArrayList<Integer> getDirectionsWithExits() {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for (int i=0;i<4;i++) if(m.peek(i) == 4) ret.add(i);
		return ret;
		
	}

	// return the number of monsters in maze m
	public int numMonsters() {
		init nMon = new init();
		int count = 0;
		nMon.visited[nMon.loc.x][nMon.loc.y] = true;
		nMon.q.enqueue(nMon.loc);
		while (!nMon.q.isEmpty()){
			IntPair v = nMon.q.dequeue();
			m.jumpTo(v);
			for (IntPair w : adj(v)){
				if (!marked(w,nMon.visited)){
					int direction = getDirection(w);
					nMon.visited[w.x][w.y] = true;
					int monster = safeWalk1(direction);
					if (monster == 2) {
						m.jumpTo(v);
						count++;
						continue;
					}else if (monster == 1){
						m.jumpTo(v);
					} else {
						nMon.q.enqueue(w);
						m.jumpTo(v);
					}
				}
			}
		}
		return count;
	}
		
	// walk to location l in the maze m
	public void walkTo(IntPair l) {
		init W2 = new init();
		W2.visited[W2.loc.x][W2.loc.y] = true;
		W2.q.enqueue(W2.loc);
		if (W2.visited[l.x][l.y] == true) return;
		while (!W2.q.isEmpty()){
			IntPair v = W2.q.dequeue();
			m.jumpTo(v);
			IntPair loc =m.getLocation();
			for (IntPair w : adj(v)){
				if (!marked(w,W2.visited)){
					if (loc.x==l.x&&loc.y==l.y){
						return;
					}
					int direction = getDirection(w);
					if (!(safeWalk1(direction)==0)) {
						m.jumpTo(v);
						continue;
					}
					W2.q.enqueue(w);
					W2.visited[w.x][w.y] = true;
					m.jumpTo(v);
				} else if (marked(w,W2.visited)){
					if (loc.x==l.x&&loc.y==l.y){
						return;
					}
				}
			}
		}
	}
		
	// find the location of a weapon, return null if none exists 
	public IntPair findWeapon() {
		init fW = new init();
		fW.visited[fW.loc.x][fW.loc.y] = true;
		fW.q.enqueue(fW.loc);
		if (safePickUpWeapon()) {
			return fW.loc;
		}
		fW.visited[fW.loc.x][fW.loc.y] = true;
		fW.q.enqueue(fW.loc);
		while (!fW.q.isEmpty()){
			IntPair v = fW.q.dequeue();
			m.jumpTo(v);
			for (IntPair w : adj(v)){
				if (!marked(w,fW.visited)){
					int direction = getDirection(w);
					int worked = safeWalk1(direction);
					
					if (safePickUpWeapon()) {
						m.jumpTo(w);
						return w;
					}
					
					if (!(worked==0)) {
						m.jumpTo(v);
						continue;
					}
					
					
					fW.q.enqueue(w);
					fW.visited[w.x][w.y] = true;
					m.jumpTo(v);
				}
			}
		}
		m.jumpTo(fW.loc);
		return null;
	}
	// find a weapon, pick it up, and kill all of the monsters in maze m 
	public void killMonsters() {
		findWeapon();
		init kMon = new init();
		kMon.visited[kMon.loc.x][kMon.loc.y] = true;
		kMon.q.enqueue(kMon.loc);
		while (!kMon.q.isEmpty()){
			IntPair v = kMon.q.dequeue();
			m.jumpTo(v);
			for (IntPair w : adj(v)){
				if (!marked(w,kMon.visited)){
					kMon.visited[w.x][w.y] = true;
					int direction = getDirection(w);
					int worked = safeWalk1(direction);
					if (worked==2) {
						m.walk(direction);
						m.kill();
						kMon.q.enqueue(w);
						m.jumpTo(w);
					}else if (worked==1){
						m.jumpTo(v);
						continue;
					}
					kMon.q.enqueue(w);
					m.jumpTo(v);
				}
			}
		}
	}
	// walk to the nearest exit and return its location
	// distance of an exit from current location is the fewest number of hops needed to reach that exit
	public IntPair findNearestExit() {
		init fNE = new init();
		fNE.visited[0][0] = true;
		fNE.q.enqueue(new IntPair(0,0));
		int[][] dis2 = new int [dim.x][dim.y];
		IntPair closest = null;
		int closestDist = Integer.MAX_VALUE;
		while (!fNE.q.isEmpty()){
			IntPair v = fNE.q.dequeue();
			int distance = dis2[v.x][v.y];
			m.jumpTo(v);
			ArrayList<Integer> directionsWithExits = getDirectionsWithExits();
			for (IntPair w : adj(v)){
				
				if (!marked(w,fNE.visited) || dis2[w.x][w.y] > distance+1){
					dis2[w.x][w.y] = distance + 1;
					int direction = getDirection(w);
					int worked = safeWalk1(direction);
					if (!(worked==0)) {
						m.jumpTo(v);
						continue;
					}
					if(directionsWithExits.indexOf(direction) > -1) {
						if (distance+1 < closestDist) {
							closestDist = distance+1;
							closest = w;
						}
					}
					fNE.q.enqueue(w);
					fNE.visited[w.x][w.y] = true;
					m.jumpTo(v);	
				}
			}
		}
		m.jumpTo(closest);
		return closest;	
	}
	
}
