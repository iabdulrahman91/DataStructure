package hw6;

import hw5.Queue;


public class HybridT<Value> implements StringTries<Value> {
	private static final int R = 256; 
	private int N;              // size
	private Node<Value> root;   // root of TST

    private static class Node<Value> {
    	private Node<Value>[] next = new Node[R];
        private char c;                        // character
        private Node<Value> left, mid, right;  // left, middle, and right subtries
        private Value val;                     // value associated with string
    }
    
    public HybridT() {
    }
    
	@Override
	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return get(key) != null;
	}

	@Override
	public Value get(String key) {
		// TODO Auto-generated method stub
		if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node<Value> x = get(root, key, 0);
        if (x == null) return null;
        return (Value)x.val;
    }
	private Node<Value> get(Node<Value> x, String key, int d) {
		if(d<3){
			if (x == null) return null;
			if (d == key.length()) return x;
			char c = key.charAt(d);
			return get(x.next[c], key, d+1);
		}
		else{if (key == null) throw new NullPointerException();
		if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
		if (x == null) return null;
		char c = key.charAt(d);
		if      (c < x.c)              return get(x.left,  key, d);
		else if (c > x.c)              return get(x.right, key, d);
		else if (d < key.length() - 1) return get(x.mid,   key, d+1);
		else                           return x;}
	}

	@Override
	public void put(String key, Value val) {
		// TODO Auto-generated method stub
		//if (!contains(key)) N++;
        root = put(root, key, val, 0);
    }
	
	private Node<Value> put(Node<Value> x, String key, Value val, int d){
//		if (x == null) x = new Node<Value>();
//		if (d == key.length()) {
//			if (x.val == null){
//				x.val = val;
//				return x;}
//			x.val = val;
//			return x;
//		}
//		char c = key.charAt(d);
//		if(d>2){
//			x.c = c;
//			if      (c < x.c)               x.left  = put(x.left,  key, val, d);
//			else if (c > x.c)               x.right = put(x.right, key, val, d);
//			else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
//			else                            x.val   = val;
//			return x;
//		}
//		else{
//			x.next[c] = put(x.next[c], key, val, d+1);
//			return x;
//		}
		
		if(d<3){

			if (x == null) x = new Node<Value>();
			if (d == key.length()) {
				if (x.val == null){ N++;}
				x.val = val;
				return x;
			}
			char c = key.charAt(d);
			//d = d+1;
			x.next[c] = put(x.next[c], key, val, d+1);
			return x;

		}
		else{
			char c = key.charAt(d);
			if (x == null) {
				x = new Node<Value>();
				x.c = c;
			}
			if      (c < x.c)               x.left  = put(x.left,  key, val, d);
			else if (c > x.c)               x.right = put(x.right, key, val, d);
			else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
			else                            x.val   = val; N++;
			return x;
		}
	}

	

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return N;
	}

	@Override
	public int size(String prefix) {
		// TODO Auto-generated method stub
		if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
		int l = prefix.length();
        Queue<String> results = new Queue<String>();
        Node<Value> x = get(root, prefix, 0);
        
        if(l<2){ // next node is Trie
        	collect(x, new StringBuilder(prefix), results, l);
            return results.size();
        }
        else{ // next node is TST
        	if (x == null) return results.size();
            if (x.val != null) results.enqueue(prefix);
            collect(x.mid, new StringBuilder(prefix), results, l);
            return results.size();
        }
	}

	private void collect(Node<Value> x, StringBuilder prefix, Queue<String> results, int d) {
		if (x == null) return;
		if(d<2){
			if (x.val != null) results.enqueue(prefix.toString());
			for (char c = 0; c < R; c++) {
				prefix.append(c);
				collect(x.next[c], prefix, results, d+1);
				prefix.deleteCharAt(prefix.length() - 1);
			}

		}


		else{
			collect(x.left,  prefix, results,d);
			if (x.val != null) results.enqueue(prefix.toString() + x.c);
			collect(x.mid,   prefix.append(x.c), results, d+1);
			prefix.deleteCharAt(prefix.length() - 1);
			collect(x.right, prefix, results,d);

		}
	}
	@Override
	public int rank(String key) {
		// TODO Auto-generated method stub
		int count=0;
    	for(String k : this.keys()){
    		if(k.compareTo(key)<0)count++;
    			
    	}
	    return count;
    }
	
	public Iterable<String> keys() {
		Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), queue,0);
        return queue;
    }

	@Override
	public boolean containsKeyEndingIn(char end) {
		// TODO Auto-generated method stub
		for(String k :keys()){
    		char c =k.charAt(k.length()-1);
    		if(c == end) return true;		
    	}
	    
    	return false;
    }


}
