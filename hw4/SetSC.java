package hw4;

import java.util.Iterator;

public class SetSC<Item> implements Set<Item> {
    private static final int INIT_CAPACITY = 4;

    private int N;                                // number of items
    private int M;                                // hash table size
    private Node[] st;                            // array of linked-lists

    private static class Node {
    	Object item;
    	Node next;
    	
    	public Node(Object s) {
    		item = s;
    	}
    	
    	public Node(Object s, Node next) {
    		this(s);
    		this.next = next;
    	}
    }

    /**
     * Initializes an empty set.
     */
    public SetSC() {
        this(INIT_CAPACITY);
    } 

    /**
     * Initializes an empty set with M chains.
     */
	public SetSC(int M) {
        this.M = M;
        st = new Node[M];
    } 

    // hash value between 0 and M-1
    private int hash(Item s) {
        return (s.hashCode() & 0x7fffffff) % M;
    } 

    /**
     * Returns the size of this set.
     */
    public int size() {
        return N;
    } 


    /**
     * Retuns true iff this set contains the specified item.
     */
    public boolean contains(Item s) {
    	// TODO
    	int i = hash(s);
    	for(Node x = st[i]; x!=null; x=x.next){
    		if(s.equals(x.item)){
    			return true;
    		}
    	}
        return false;
    }
    private void resize(int capacity) {
    	SetSC temp = new SetSC(capacity);
        for (int i = 0; i < M; i++) {
        	for(Node x = st[i]; x!=null; x=x.next){
        		temp.add(x.item);
        	}
        }
        st = temp.st;
        M = temp.M;
    }
    
    /**
     * Inserts the specified item into this set (no duplicates).
     */
    public void add(Item s) {
    	// TODO
    	if(contains(s)) return;
    	if(N >= 10*M) resize(2*M);
    	int i = hash(s);
    	st[i] = new Node(s,st[i]);
    	N++;
    	
    }

    /**
     * Removes the specified item from this set (if it is in this set)    
     */
    public void delete(String s) {
    	// TODO
    	int h = (s.hashCode() & 0x7fffffff) % M;
    	st[h] = deleteHelper(st[h],s);
    	if (M > INIT_CAPACITY && N <= 2*M) resize(M/2);
    }
    private Node deleteHelper(Node x, String s) {
        if (x == null) return null;
        if (s.equals(x.item)) {
            N--;
            return x.next;
        }
        x.next = deleteHelper(x.next, s);
        return x;
    }

    /**
     * Returns an iterator for this set
     */
    public Iterator<Item> iterator() {
    	return new SetIterator();
    }
    
    /**
     * Iterator class for SetLP    
     */
    private class SetIterator implements Iterator<Item> {
    	private int i = 0;
    	private Node n = st[0];

		public boolean hasNext() {
			while (n == null) {
				i++;
				if (i < M) {
					n = st[i];
				} else {
					return false;
				}
			}
			return true;
		}

		@SuppressWarnings("unchecked")
		public Item next() {
			Item item = (Item) n.item;
			n = n.next;
			return item;
		}
    }
}