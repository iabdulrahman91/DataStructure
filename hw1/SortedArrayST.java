/**
 * Your homework is to complete the methods marked TODO.
 * You must not change the declaration of any method.
 */

package hw1;

/**
 *  The SortedArrayST class represents an (ordered) symbol table of
 *  generic key-value pairs.  It supports put, get, and delete methods.
 */
public class SortedArrayST<Key extends Comparable<Key>, Value> {
	private static final int MIN_SIZE = 2;
	private Key[] keys;      // the keys array
	private Value[] vals;    // the values array
	private int N = 0;       // size of the symbol table
	
	/**
	 * Initializes an empty symbol table.
	 */
	public SortedArrayST() {
		this(MIN_SIZE);
	}
	
	/**
	 * Initializes an empty symbol table of given size.
	 */
	@SuppressWarnings("unchecked")
	public SortedArrayST(int size) {
		keys = (Key[])(new Comparable[size]);
		vals = (Value[])(new Object[size]);
	}
	
	/**
	 * Initializes a symbol table with given sorted key-value pairs.
	 * If given keys list is not sorted in (strictly) increasing order,
	 * then the input is discarded and an empty symbol table is initialized.
	 */
	public SortedArrayST(Key[] keys, Value[] vals) {
		this(keys.length < MIN_SIZE ? MIN_SIZE : keys.length);
		N = (keys.length == vals.length ? keys.length : 0);
		int i;
		for (i = 1; i < N && keys[i].compareTo(keys[i - 1]) > 0; i++);
		if (i < N) { // input is not sorted
			System.err.println("SortedArrayST(Key[], Value[]) constructor error:");
			System.err.println("Given keys array of size " + N + " was not sorted!");
			System.err.println("Initializing an empty symbol table!");
			N = 0;
		} else {
			for (i = 0; i < N; i++) {
				this.keys[i] = keys[i];
				this.vals[i] = vals[i];
			}
		}
	}
	
	/**
	 * Returns the keys array of this symbol table.
	 */
	public Comparable<Key>[] keysArray() {
		return keys;
	}
	
	/**
	 * Returns the values array of this symbol table.
	 */
	public Object[] valsArray() {
		return vals;
	}
	
	/**
	 * Returns the number of keys in this symbol table.
	 */
	public int size() {
		return N;
	}
	
	/**
	 * Returns whether the given key is contained in this symbol table at index r.
	 */
	private boolean checkFor(Key key, int r) {
		return (r >= 0 && r < N && key.equals(keys[r]));
	}
	
	/**
	 * Returns the value associated with the given key in this symbol table.
	 */
	public Value get(Key key) {
		int r = rank(key);
		if (checkFor(key, r)) return vals[r];
		else return null;
	}
	
	/**
	 * Inserts the specified key-value pair into the symbol table, overwriting the old 
	 * value with the new value if the symbol table already contains the specified key.
	 * Deletes the specified key (and its associated value) from this symbol table
	 * if the specified value is null.
	 */
	public void put(Key key, Value val) {
		int r = rank(key);
		if (!checkFor(key, r)) {
			shiftRight(r);
			keys[r] = key;
		}
		vals[r] = val;
	}
	
	/**
	 * Removes the specified key and its associated value from this symbol table     
	 * (if the key is in this symbol table).    
	 */
	public void delete(Key key) {
		int r = rank(key);
		if(r>N) return;

		for(int i = 0; i < N-1; i++){
			if(i>=r){
				keys[i] = keys[i+1];
				vals[i] = vals[i+1];
			}
			
		}
		N--;
//		if (checkFor(key, r)) {
//			shiftLeft(r);
//		}
	}
	
	/**
	 * Shifts the keys (and values) at indices r and above to the right by one
	 */
	private void shiftRight(int r) {

		Key[] Ktemp = (Key[]) (new Comparable[N + 1]);
		Value[] Vtemp = (Value[]) (new Comparable[N + 1]);
		for (int i = 0; i < N; i++) {
			if (i < r) {
				Ktemp[i] = keys[i];
				Vtemp[i] = vals[i];
			} else  {
				Ktemp[i + 1] = keys[i];
				Vtemp[i + 1] = vals[i];

			}
		}
		N++;
		keys = (Key[]) (new Comparable[N]);
		vals = (Value[]) (new Object[N]);
		for (int i = 0; i < N; i++) {
			keys[i] = Ktemp[i];
			vals[i] = Vtemp[i];
		}
//		if(N<1){
//		N++;
//		keys[0]=keys[r];
//		vals[0]=vals[r];
//	}
//	else{
//		N++;
//		Key tempKup=keys[r];
//		Value tempVup=vals[r];
//		
//		Key tempKdown=null;
//		Value tempVdown=null;
//		
//		for(int i = 0; i >= N; i++){
//			if(i>=r){
//				tempKdown=keys[i];
//				tempVdown=vals[i];
//				keys[i]=tempKup;
//				vals[i]=tempVup;
//				tempKup=tempKdown;
//				tempVup=tempVdown;
//			}
//		}
//	}
//
//	if(r>=size()){
//		N++;
//		keys[0]=keys[r];
//		vals[0]=vals[r];
//	}
//	else{
//		int i = size()-1;
//		N++;
//		
//		while(rank(keys[i])>r){
//			keys[i]=keys[i-1];
//			vals[i]=vals[i-1];
//			i--;
//		}
//		keys[i]=keys[r];
//		vals[i]=vals[r];
//		
//		
//
//
//
//		
//	}
	}
	
	/**
	 * Shifts the keys (and values) at indices x > r to the left by one
	 * in effect removing the key and value at index r 
	 */
	private void shiftLeft(int r) {
		for (int i = 0; i < N - 1; i++) {
			if (i >= r) {
				keys[i] = keys[i + 1];
				vals[i] = vals[i + 1];
			}
		}
		N--;

	}
	
	/**
	 * rank returns the number of keys in this symbol table that is less than the given key. 
	 */
	public int rank(Key key) {
		int r;
		for (r = 0; r < N && key.compareTo(keys[r]) > 0; r++);
		return r;
	}
	
	/**
	 * countRange returns the number of keys within the range (key1, key2) (inclusive)
	 * note that keys may not be in order (key1 may be larger than key2)
	 */
	public int countRange(Key key1, Key key2) {
		return CRH(key1, key2, 0, 0);
	}
	private int CRH(Key key1, Key key2, int i, int res){
		if(i==N) return res;
		if(key1.compareTo(keys[i]) <= 0 && key2.compareTo(keys[i]) >= 0){
    		return CRH(key1, key2, i+1, res+1);
    	}
		else if(key1.compareTo(keys[i]) >= 0 && key2.compareTo(keys[i]) <= 0){
			return CRH(key1, key2, i+1, res+1);
		}
    	else{
    		return CRH(key1, key2, i+1, res);
    	}
	}

//	return count;
		//return CRH(key1, key2, 0, 0); // TODO
		// BONUS : log time implementation
//		int i;
//		int count = 0;
//		for(i=0; i < size(); i++){
//			if((keys[i].compareTo(key1)>0)&&(keys[i].compareTo(key2)<0)){
//				count++;
//			}
//			else if((keys[i].compareTo(key1)<0)&&(keys[i].compareTo(key2)>=0)){
//				count++;
//			}
//		}
//		return count;
//	}
//	int count = 0;
//
//	for (int i = 0; i < N; i++) {
//		if (key1.compareTo(key2) < 0) {
//			if (key1.compareTo(keys[i]) <= 0 && key2.compareTo(keys[i]) >= 0){
//				count++;}
//		} else if (key1.compareTo(key2) > 0) {
//			if (key1.compareTo(keys[i]) >= 0 && key2.compareTo(keys[i]) <= 0){
//				count++;}
//		} else if ((key1.compareTo(key2) == 0)) {
//			if (key1.compareTo(keys[i]) == 0){
//				count++;}
//		}
//	}
//		int count=0;
//		if(key1.compareTo(key2)==0){
//			return 1;
//		}
//		else if(key1.compareTo(key2)<0){
//			// if key1 is before key2
//			for(int i=0; i < size(); i++){
//				if((keys[i].compareTo(key1)>=0)&&(keys[i].compareTo(key2)<0)){
//					count++;
//				}
//			}
//		}
//		else{
//			// if key1 is after key2
//			for(int i=0; i < size(); i++){
//				if((keys[i].compareTo(key2)>=0)&&(keys[i].compareTo(key1)<0)){
//					count++;
//				}
//			}
//			
//		}
//		return count;
	
		
//	private int CRH(Key key1, Key key2, int i, int res){
//		if(i==N) return res;
//		if((key1.compareTo(keys[i])<=0)&&(key2.compareTo(keys[i])>0)){
//    		return CRH(key1, key2, i+1, res+1);
//    	}
//		else if((key2.compareTo(keys[i])<0)&&(key1.compareTo(keys[i])>=0)){
//			return CRH(key1, key2, i+1, res+1);
//		}
//    	else{
//    		return CRH(key1, key2, i+1, res);
//    	}
//	}
}