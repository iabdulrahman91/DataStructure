package hw6;

import hw5.Queue;

public class Trie<Value> implements StringTries<Value> {
    private static final int R = 256;        // extended ASCII


    private Node root;      // root of trie
    private int N;          // number of keys in trie

    // R-way trie node
    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

   /**
     * Initializes an empty string symbol table.
     */
    public Trie() {
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
    @SuppressWarnings("unchecked")
	public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.val;
    }

    // return subtrie corresponding to given key
    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        
        
        return get(x.next[c], key, d+1);
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     */
    public void put(String key, Value val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.val == null) N++;
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        
        
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     */
    public int size() {
        return N;
    }

    /**
     * Is this symbol table empty?
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }
    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) return;
        if (x.val != null) results.enqueue(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    
    // return the number of keys less than the given key (using compareTo < 0)
    public int rank(String key) {
    	// TODO
    	int count=0;
    	for(String k : this.keys()){
    		if(k.compareTo(key)<0)count++;
    			
    	}
	    return count;
    }
    
    // return the number of keys beginning with the given prefix
    public int size(String prefix) {
    	// TODO
    	Queue<String> results = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results.size();
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