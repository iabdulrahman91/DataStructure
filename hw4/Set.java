package hw4;

public interface Set<Item> extends Iterable<Item> {
	
    /**
     * Returns true iff this set contains the specified item.
     */
    public boolean contains(Item s);
    
    /**
     * Inserts the specified item into this set (no duplicates).
     */
    public void add(Item s);

    /**
     * Removes the specified item from this set (if it is in this set)    
     */
    public void delete(String s);
    
}
