package hw6;

public interface StringTries<Value> {
	
	public boolean contains(String key);
	
	public Value get(String key);
	
	public void put(String key, Value val);
	
	public int size();
	
	public int size(String prefix);
	
	public int rank(String key);
	
	public boolean containsKeyEndingIn(char end);
	
}