/**
 * Your homework is to complete the methods marked TODO.
 * You must not change the declaration of any method.
 */

package hw2;

/**
 *  The B(inary)S(earch)T(ree) class represents a symbol table of
 *  generic key-value pairs.  It supports put, get, and delete methods.
 */
public class BST<Key extends Comparable<Key>, Value> {
	public Node root;             // root of BST

	public class Node {
		public Key key;           // sorted by key
		public Value val;         // associated data
		public Node left, right;  // left and right subtrees
		
		public Node(Key key, Value val) {
			this.key = key;
			this.val = val;
		}
		
		/**
		 * Appends the preorder string representation of the sub-tree to the given StringBuilder.
		 */
		public void buildString(StringBuilder s) {
			s.append(left == null ? '[' : '(');
			s.append(key + "," + val);
			s.append(right == null ? ']' : ')');
			if (left != null) left.buildString(s);
			if (right != null) right.buildString(s);
		}
	}
	
	/**
	 * Initializes an empty symbol table.
	 */
	public BST() {
	}
	
	/**
	 * Returns the value associated with the given key.
	 */
	public Value get(Key key) {
		return get(root, key);
	}
	
	private Value get(Node x, Key key) {
		if (x == null) return null;
		int cmp = key.compareTo(x.key);
		if      (cmp < 0) return get(x.left, key);
		else if (cmp > 0) return get(x.right, key);
		else              return x.val;
	}
	
	/**
	 * Inserts the specified key-value pair into the symbol table, overwriting the old 
	 * value with the new value if the symbol table already contains the specified key.
	 */
	public void put(Key key, Value val) {
		if (key == null) throw new NullPointerException("first argument to put() is null");
		root = put(root, key, val);
	}
	
	private Node put(Node x, Key key, Value val) {
		if (x == null) return new Node(key, val);
		int cmp = key.compareTo(x.key);
		if      (cmp < 0) x.left  = put(x.left,  key, val);
		else if (cmp > 0) x.right = put(x.right, key, val);
		else              x.val   = val;
		return x;
	}
	
	/**
	 * Returns the preorder string representation of the BST.
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		root.buildString(s);
		return s.toString();
	}
	
	/**
	 * Returns the number of those nodes which have non-null left and non-null right children.
	 * (both left and right child should be a valid node to qualify as an intermediate node)
	 */
	public int intermediateNodes() {
		return intermediateNodes(root); // TODO
	}
	
	private int intermediateNodes(Node x) {
		// TODO
		
//		if((x.left!=null)&(x.right!=null)){
//			return (1+intermediateNodesHelper(x.left)+intermediateNodesHelper(x.right));
//		}
//		else if((x.left==null)&(x.right!=null)){
//			return (intermediateNodesHelper(x.right));
//		}
//		else if((x.left!=null)&(x.right==null)){
//			return (intermediateNodesHelper(x.left));
//		}
//		else{
//			return 0;
//		}
		
		if(x==null){
			return 0;
		}
		else if((x.left!=null)&(x.right!=null)){
			return (1+intermediateNodes(x.left)+intermediateNodes(x.right));
		}
		else{
			return (intermediateNodes(x.left)+intermediateNodes(x.right));
		}
		
	}
	
	/**
	 * Modifies the tree by removing all nodes that are at depth >= d
	 * Assume root is at depth 0, i.e., clipDepth(0) should remove every node
	 * Assume children of root is at depth 1, i.e., clipDepth(1) should remove every node except the root node
	 */
	public void clipDepth(int d) {
		clipDepthHelper(root, d, 0);
	}

	private void clipDepthHelper(Node x, int d, int pos) {
		if (x == null) {
			return;
		}
		else if (d == pos) {
			x.key = null;
			x.val = null;
		}
		else {
			clipDepthHelper(x.right, d, pos + 1);
			clipDepthHelper(x.left, d, pos + 1);
		}
	}
	
	
//	public void clipDepth(int d) {
//		if(d==0)root = null;
//		clipDepth(root,d,0);
//	}
//	private void clipDepth(Node x, int d, int pos){
//		if(x==null) {
//			return;
//		}
//		else{
//			if(d==pos+1){
//				x.right=null;
//				x.left=null;}
//			else{
//				clipDepth(x.right,d,pos+1);
//				clipDepth(x.left,d,pos+1);}
//		}
//
//	}
	/**
	 * secondMax returns the second maximum key in the symbol table.
	 */
	public Key secondMax() {
		Node x = root;
		if (x == null || (x.right == null && x.left == null))
			return null;
		Node secMax = null;
		Node Max = root;
		while (x.right != null) {
			secMax = x;
			x = x.right;
			Max = x;
		}
		// to check if the most right node has no left child
		if (x.left != null) {
			x = x.left;
			secMax = x;
		}
		// in case the most right node of Root has left child, return most right
		// node of x.left
		while (x.right != null) {
			x = x.right;
			secMax = x;
		}
		return secMax.key;

	}
//		Node x = root;
//		if(x==null){return null;}
//		if((x.left==null)&&(x.right==null)){return null;}
//		while(x.right.right!=null){
//			x=x.right;
//		}
//		if(x.right.left==null){
//			return x.key;
//		}
//		else{
//			if(x.right.left.right==null){
//				return x.right.left.key;
//			}
//			else{
//				x=x.right.left;
//				while(x.right!=null){
//					x=x.right;
//				}
//				return x.key;
//			}
//		}
	//	private Key secondMax(Node x){
//		if((x.right.right!=null)&&(x.right.left!=null)){
//			return secondMax(x.right);
//		}
//		else if(x.right.right==null){
//			return x.right.left.key;
//		}
//		else{
//			return x.key;
//		}
//	}
	
	/**
	 * closest returns a key in the symbol table that is equal to
	 * or otherwise that is the closest on either side of the given key.
	 * it returns null if there is no such key (empty symbol table).
	 */
	public Key closest(Key key) {
		if (root == null){return null;}
		return closest(key, root); // TODO
	}

	private Key closest(Key key, Node x) {
		int cmp = key.compareTo(x.key);
		if (cmp == 0) {
			return x.key;
		} else if ((cmp < 0) && (x.left != null)) {
			return closest(key, x.left);

		} else if ((cmp > 0) && (x.right != null)) {
			return closest(key, x.right);
		}
		return x.key;
	}

//	private Key closest(Key key, Node x) {
//		int cmp = key.compareTo(x.key);
//		if (cmp == 0) {
//			return x.key;
//		}
//
//		else if (cmp < 0) {
//			if (x.left != null) {
//				return closest(key, x.left);
//
//			} else {
//				return x.key;
//			}
//		} else {
//			if (x.right != null) {
//				return closest(key, x.right);
//
//			} else {
//				return x.key;
//			}
//		}
//	}
//	
	

	
	/**
	 * Merges the given symbol table into this symbol table.
	 * If the given symbol table contains keys that is already in this symbol table
	 * merge overwrites those keys' values with the values in the given symbol table.
	 */
	public void merge(BST<Key, Value> bst) {
		if(root==null){
			root = bst.root;
			return;
		}
		else{
			mergeHelper(bst.root);
		}
	}
	private void mergeHelper(Node x){
		if(x==null){
			return;
		}
		put(x.key,x.val);
		mergeHelper(x.left);
		mergeHelper(x.right);
		
	}
}