/**
 * Your homework is to complete the methods marked TODO.
 * You must not change the declaration of any method.
 */

package hw1;

/**
 *  The LinkedListST class represents an (unordered) symbol table of
 *  generic key-value pairs.  It supports put, get, and delete methods.
 */
public class LinkedListST<Key extends Comparable<Key>, Value> {
    private Node first;      // the linked list of key-value pairs

    // a helper linked list data type
    private class Node {
        private Key key;
        private Value val;
        private Node next;

        public Node(Key key, Value val, Node next)  {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    public LinkedListST() {
    }

    /**
     * Returns the value associated with the given key in this symbol table.
     */
    public Value get(Key key) {
        if (key == null) throw new NullPointerException("argument to get() is null"); 
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key))
                return x.val;
        }
        return null;
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old 
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is null.
     */
    public void put(Key key, Value val) {
        if (key == null) throw new NullPointerException("first argument to put() is null"); 
        if (val == null) {
            delete(key);
            return;
        }

        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        }
        first = new Node(key, val, first);
    }

    /**
     * Removes the specified key and its associated value from this symbol table     
     * (if the key is in this symbol table).    
     */
    public void delete(Key key) {
        if (key == null) throw new NullPointerException("argument to delete() is null"); 
        first = delete(first, key);
    }

    // delete key in linked list beginning at Node x
    // warning: function call stack too large if table is large
    private Node delete(Node x, Key key) {
        if (x == null) return null;
        if (key.equals(x.key)) {
        
            return x.next;
        }
        x.next = delete(x.next, key);
        return x;
    }

    /**
     * size returns the number of key-value pairs in the symbol table.
     * it returns 0 if the symbol table is empty.
     */
    public int size () {
    	int s = 0;
    	for (Node n = first; n != null; n = n.next) s++;
    	return s;
    }

    /**
     * secondMaxKey returns the second maximum key in the symbol table.
     * it returns null if the symbol table is empty or if it has only one key.
     */
    public Key secondMaxKey () {
    	if (size() < 2)
    		return null;

    	Node max = first;
    	Node sec_max = first.next;
    	if (first.key.compareTo(first.next.key) < 0) {
    		max = first.next;
    		sec_max = first;
    	}

    	for (Node x = first.next.next; x != null; x = x.next) {

    		if (x.key.compareTo(sec_max.key) > 0) {
    			if (x.key.compareTo(max.key) > 0) {
    				sec_max = max;
    				max = x;
    			} else {
    				sec_max = x;
    			}
    		}
    	}
    	
    	return sec_max.key;
		// less conditions by keeping the compared node in a variable
		// if (x.key.compareTo(sec_max.key) > 0) {
		// Node s = x;
		// if (x.key.compareTo(max.key) > 0) {
		// sec_max = max;
		// max = x;
		// s = sec_max;
		// }
		//
		// sec_max = s;
		//
		// }
    }

    /**
     * rank returns the number of keys in this symbol table that is less than the given key.
     * your implementation should be recursive. 
     */
    public int rank (Key key) {
        //return rankHelper(first, key, 0);
        Node x = first;
        int count = 0;
        while(x!=null){
        	if(key.compareTo(x.key)>0){
        		count++;
        	}
        	x=x.next;
        }
        return count;
        
    }
    private int rankHelper (Node x, Key key, int res) {
    	if(x==null)return res;
    	if(x.key.compareTo(key)<0){
    		return rankHelper(x.next,key,res+1);
    	}
    	else{
    		return rankHelper(x.next,key,res);
    	}
    }
}