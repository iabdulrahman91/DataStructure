package hw6;

import hw5.Queue;

public class TST<Value> implements StringTries<Value> {
    private int N;              // size
    private Node<Value> root;   // root of TST

    private static class Node<Value> {
        private char c;                        // character
        private Node<Value> left, mid, right;  // left, middle, and right subtries
        private Value val;                     // value associated with string
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TST() {
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     */
    public int size() {
        return N;
    }

    /**
     * Does this symbol table contain the given key?
     */
    public boolean contains(String key) {
        return get(key) != null;
    }
    

    /**
     * Returns the value associated with the given key.
     */
    public Value get(String key) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node<Value> x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    // return subtrie corresponding to given key
    private Node<Value> get(Node<Value> x, String key, int d) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        if (x == null) return null;
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     */
    public void put(String key, Value val) {
        if (!contains(key)) N++;
        root = put(root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node<Value>();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
        else                            x.val   = val;
        return x;
    }
    
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), queue);
        return queue;
        
    }
    
    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new Queue<String>();
        Node<Value> x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != null) queue.enqueue(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }

    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.val != null) queue.enqueue(prefix.toString() + x.c);
        collect(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }
    
    // return the number of keys less than the given key (using compareTo < 0)
    public int rank(String key) {
    	// TODO
    	int count=0;
    	for(String k :keys()){
    		if(k.compareTo(key)<0)count++;
    			
    	}
	    return count;
    }
    
    // return the number of keys beginning with the given prefix
    public int size(String prefix) {
    	// TODO
    	if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new Queue<String>();
        Node<Value> x = get(root, prefix, 0);
        if (x == null) return queue.size();
        if (x.val != null) queue.enqueue(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue.size();
    }
    
    // return true iff trie contains a key ending in given character "end"
    public boolean containsKeyEndingIn(char end) {
    	// TODO
    	for(String k :keys()){
    		char c =k.charAt(k.length()-1);
    		if(c == end) return true;		
    	}
	    

    	return false;
    }
}