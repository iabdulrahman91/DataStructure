package hw3;

// this interface defines a set of methods that generalizes symbol tables as follows:
// this Dictionary interface models a dictionary entry with a list of definitions,
// and allows addition and removal of a single definition as opposed to
// addition and removal of an entry (along with a list of definitions).

public interface Dictionary {
	// adds a (word, definition) pair into the dictionary
	// if the word does not exist in the dictionary, in creates a new entry
	// otherwise, it adds "defn" into the list of definitions for "word"
	public void addDefinition(String word, String defn);
	
	// returns the list of definitions associated with the given word
	public Iterable<String> getDefinitions(String word);
	
	// removes the particular definition of the given word
	public void remDefinition(String word, String defn);
	
	// returns true if and only if the dictionary contains an entry for the given word
	// with at least one definition
	public boolean contains(String word);
	
	// returns the number of entries (words) in the dictionary 
	public int numEntries();
}