package hw3;


import java.util.*;

import hw2.BST.Node;


/******************************************************************************
 * 
 * Your homework assignment is to design the below data structure in the form
 * of a BST to maintain a symbol table of words and their lists of definitions.
 * An entry in this dictionary is composed of a word and a list of definitions.
 * 
 * Hint: you may implement and use a LinkedList data structure to store
 * the list of definitions for each entry (i.e., word).
 *
 */

public class BSTDict implements Dictionary {
	// TODO : design the data structure so that the below methods function as intended
	public Node root; 
	
	public class Node {
		public String word;
		public LinkedList<String> defs;
		public Node left, right;

		public Node(String word, LinkedList<String> defs) {
			this.word = word;
			this.defs = defs;			
		}
	}


	@Override
	public void addDefinition(String word, String defn) {
		if (word == null)
			throw new NullPointerException("first argument to addDefinition() is nullnull");
		root = addDefinition(root, word, defn);
	}

	private Node addDefinition(Node x, String word, String defn) {
		if (x == null) {
			LinkedList<String> ll = new LinkedList<String>();
			ll.add(defn);
			return new Node(word, ll);	
		}
		
		int cmp = word.compareTo(x.word);
		if (cmp < 0)
			x.left = addDefinition(x.left, word, defn);
		else if (cmp > 0)
			x.right = addDefinition(x.right, word, defn);
		else if (cmp==0) {
			x.defs.add(defn);
		}
		return x;

	}
	@Override
	public Iterable<String> getDefinitions(String word) {
		// TODO Auto-generated method stub
		return getDefinitionsHelper(root, word);
		
	}
	// return should be type of LinkedList<String> to be used in getDefinitions, remDefinitionsHelper, and contains
	private LinkedList<String> getDefinitionsHelper(Node x, String word) {
		if (x == null) return null;
		int cmp = word.compareTo(x.word);
		if      (cmp < 0) return getDefinitionsHelper(x.left, word);
		else if (cmp > 0) return getDefinitionsHelper(x.right, word);
		else              return x.defs;
	}

	@Override
	public void remDefinition(String word, String defn) {
		if (word == null) throw new IllegalArgumentException("argument to remDefinition() is null");
        root = remDefinition(root, word, defn);
//		LinkedList<String> defs = getDefinitionsHelper(root, word);
//		defs.remove(defn);
//		if (defs.size() == 0) {
//			Node x = root;
//			while (x != null) {
//				int cmp = word.compareTo(x.word);
//				if (cmp < 0) {x = x.left;}
//				else if (cmp > 0) {x = x.right;}
//				else {
//					x.defs = null;
//					return;
//				}
//
//			}
//			
//		}
	}
	private Node remDefinition(Node x, String word, String defn) {
        if (x == null) return null;

        int cmp = word.compareTo(x.word);
        if      (cmp < 0) x.left  = remDefinition(x.left,  word, defn);
        else if (cmp > 0) x.right = remDefinition(x.right, word, defn);
        else { 
        	x.defs.remove(defn);
        	if(x.defs.isEmpty()){
            if (x.right == null) return x.left;
            if (x.left  == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;}
        } 
    
        return x;
    } 
	public String min() {
        return min(root).word;
    } 

    private Node min(Node x) { 
        if (x.left == null) return x; 
        else                return min(x.left); 
    } 
    public void deleteMin() {
        //if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        root = deleteMin(root);
        //assert check();
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        //x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

	@Override
	public boolean contains(String word) {
		// TODO Auto-generated method stub
		return getDefinitionsHelper(root, word) != null;
	}

	@Override
	public int numEntries() {
		// TODO Auto-generated method stub
		return numEntries(root);
		
	}
	private int numEntries(Node x){
		if(x==null) return 0;
		return numEntries(x.left)+numEntries(x.right)+ 1;
	}

}
