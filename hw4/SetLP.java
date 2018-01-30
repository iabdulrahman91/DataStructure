package hw4;

import java.util.Iterator;

public class SetLP<Item> implements Set<Item> {
    private static final int INIT_CAPACITY = 4;

    private int N;           // number of items in the hash table
    private int M;           // size of linear probing table
    private Item[] items;    // the items

    /**
     * Initializes an empty set.
     */
    public SetLP() {
        this(INIT_CAPACITY);
    }

    /**
     * Initializes an empty set with the specified initial capacity.
     */
    @SuppressWarnings("unchecked")
	public SetLP(int capacity) {
        M = capacity;
        items = (Item [])new Object[M];
    }

    /**
     * Returns the size of this set.
     */
    public int size() {
        return N;
    }

    // hash function for items - returns value between 0 and M-1
    private int hash(Item s) {
        return (s.hashCode() & 0x7fffffff) % M;
    }
    
    
    private void resize(int capacity) {
    	SetLP<Item> temp = new SetLP<Item>(capacity);
        for (int i = 0; i < M; i++) {
            if (items[i] != null) {
                temp.add(items[i]);
            }
        }
        this.items = temp.items;
        this.M = temp.M;
    }

    /**
     * Returns true iff this set contains the specified item.
     */
    public boolean contains(Item s) {
    	// TODO
    	for (int i = hash(s); items[i] != null; i = (i + 1) % M) {
    		if (s == items[i]) {
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * Inserts the specified item into this set (no duplicates).
     */
    public void add(Item s) {
    	// TODO
    	if (s == null) throw new IllegalArgumentException("first argument to add() is null");
        if (N >= M/2) resize(2*N);
        if(!contains(s)){
        	int i=hash(s);
            while(items[i] != null) {
            	i = (i + 1) % M;
            }
            items[i]=s;
            N++;	
        }
    }

    /**
     * Removes the specified item from this set (if it is in this set)    
     */
    public void delete(String s) {
    	// TODO
    	if (s == null) throw new IllegalArgumentException("first argument to delete() is null");
    	if(!contains((Item)s)) return;
    	int h = hash((Item)s);
    	while (!s.equals(items[h].toString())) {
            h = (h + 1) % M;
        }
        items[h] = null;
        N--;
        h = (h + 1) % M;
        while (items[h] != null) {
            Item   itemToRehash = items[h];
            items[h] = null;
            N--;
            add(itemToRehash);
            h = (h + 1) % M;
        }
        if (N > 0 && N <= M/8) resize(M/2);

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

		public boolean hasNext() {
			while (i < M) {
				if (items[i] != null) return true;
				i++;
			}
			return false;
		}

		public Item next() {
			return items[i++];
		}
    }
}