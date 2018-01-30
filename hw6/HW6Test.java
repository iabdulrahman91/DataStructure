package hw6;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Random;

public class HW6Test {
	String border = "*******************************************\n";
	String passed = "* Passed!                                 *\n";
	String failed = "* Failed!                                 *\n";
	String test;
	
	AssertionError ae;
	Exception e;
	
	int size, presize, twosize;
	String[] keys, prefixes, two;
	Integer[] vals;
	
	String arg;
	
	StringTries<Integer> trie;
	boolean isTrie;
	
	private StringTries<Integer> createST() {
		StringTries<Integer> st;
		if (isTrie) st = new Trie<Integer>();
		else st = new TST<Integer>();
		for (int i = 0; i < size; i++) {
			st.put(keys[i], vals[i]);
		}
		return st;
	}
	
	private String keyvalsToString() {
		StringBuilder s = new StringBuilder();
		s.append("size = " + size + ", key-value pairs = ");
		int i, k;
		for (i = 0, k = 0; i < size - 1; i++, k++) {
			s.append("(" + keys[k] + ", " + vals[k] + "), ");
		}
		if (size > 0) {
			s.append("(" + keys[k] + ", " + vals[k] + ")");
		}
		
		return s.toString();
	}

	public static String twoLetterString(int val) {
		char chars[] = new char[2];
		chars[0] = (char)('A' + ((val / 26) % 26));
		chars[1] = (char)('A' + (val % 26));
		return String.valueOf(chars);
	}
	
	public static String randomTwoLetterAbove(Random rand, int threshold) {
		int val = threshold + 1 + rand.nextInt(26 * 26 - threshold - 1);
		return twoLetterString(val);
	}
	
	public static String randomTwoLetterBelow(Random rand, int threshold) {
		int val = rand.nextInt(threshold);
		return twoLetterString(val);
	}
	
	private void randomTwoLetterStringsAbove(Random rand, int i0, int i1, int threshold) {
		threshold++;
		int space = 26 * 26 - threshold;
		int size = i1 - i0;
		int i, val;
		
		boolean[] taken = new boolean[space];
		for (i = 0; i < size; i++) {
			while(true) {
				val = rand.nextInt(space);
				if (!taken[val]) break;
			}
			taken[val] = true;
		}
		for (i = i0, val = 0; val < space; val++) {
			if (taken[val]) two[i++] = twoLetterString(val + threshold);
		}
	}
	
	private void randomTwoLetterStringsBelow(Random rand, int i0, int i1, int threshold) {
		int space = threshold;
		int size = i1 - i0;
		int i, val;
		
		boolean[] taken = new boolean[space];
		for (i = 0; i < size; i++) {
			while(true) {
				val = rand.nextInt(space);
				if (!taken[val]) break;
			}
			taken[val] = true;
		}
		for (i = i0, val = 0; val < space; val++) {
			if (taken[val]) two[i++] = twoLetterString(val);
		}
	}
	
	private void randomPrefixes(Random rand, int size) {
		int numbersz = size * 100;
		boolean picked[] = new boolean[numbersz];
		prefixes = new String[size];
		for (int i = 0; i < size; i++) {
			int j;
			do {
				j = rand.nextInt(numbersz);
			} while (picked[j]);
			picked[j] = true;
			prefixes[i] = "" + j;
		}
	}
	
	private void randomKeyVals(Random rand) {
		size = presize * (twosize + 1);
		
		keys = new String[size];
		vals = new Integer[size];
		
		int i, j, k;
		for (i = 0; i < presize; i++) {
			for (j = 0; j < twosize; j++) {
				k = i * twosize + i + j;
				keys[k] = prefixes[i] + two[j];
				vals[k] = rand.nextInt(100);
			}
			k = i * twosize + i + j;
			keys[k] = prefixes[i];
			vals[k] = rand.nextInt(100);
		}
	}
	
	private int prefixsize(String s) {
		int cnt = 0;
		for (int i = 0; i < presize; i++) {
			if (prefixes[i].startsWith(s)) cnt++;
		}
		return cnt;
	}
	
	private void tstSize() {
		Random rand = new Random(0);
		for (presize = 1; presize <= 20; presize++) {
			randomPrefixes(rand, presize);
			for (twosize = 1; twosize <= 100; twosize++) {
				two = new String[twosize];
				randomTwoLetterStringsBelow(rand, 0, twosize, 26 * 26);
				randomKeyVals(rand);
				trie = createST();
				// testing size;
				for (int i = 0; i < presize; i++) {
					arg = prefixes[i];
					int j = prefixsize(arg);
					assertEquals(j * (twosize + 1), trie.size(arg));
				}
			}
		}
	}
	
	private int rank(String s) {
		s += 'A';
		int cnt = 0;
		for (int i = 0; i < presize; i++) {
			if (s.compareTo(prefixes[i] + 'A') > 0) cnt += twosize;
			if (s.compareTo(prefixes[i]) > 0) cnt++;
		}
		return cnt;
	}
	
	private void tstRank() {
		Random rand = new Random(0);
		for (presize = 1; presize <= 20; presize++) {
			randomPrefixes(rand, presize);
			for (twosize = 1; twosize <= 50; twosize++) {
				two = new String[twosize];
				int threshold = rand.nextInt(13 * 13 * 2) + 13 * 13; 
				String randStr = twoLetterString(threshold);
				int j = rand.nextInt(twosize);
				{
					randomTwoLetterStringsBelow(rand, 0, j, threshold - 1);
					two[j] = randStr;
					randomTwoLetterStringsAbove(rand, j + 1, twosize, threshold + 1);
					randomKeyVals(rand);
					trie = createST();
					for (int k = 0; k < presize; k++) {
						int rk = rank(prefixes[k]);
						arg = prefixes[k] + two[j];
						assertEquals(rk + j, trie.rank(arg));
						arg = prefixes[k] + twoLetterString(threshold - 1);
						assertEquals(rk + j, trie.rank(arg));
						arg = prefixes[k] + twoLetterString(threshold + 1);
						assertEquals(rk + j + 1, trie.rank(arg));
					}
				}
			}
		}
	}
	
	private boolean endsWith(char end) {
		for (int i = 0; i < twosize; i++) {
			if (two[i].endsWith("" + end)) return true;
		}
		for (int i = 0; i < presize; i++) {
			if (prefixes[i].endsWith("" + end)) return true;
		}
		return false;
	}
	
	private void tstContains() {
		Random rand = new Random(0);
		for (presize = 1; presize <= 20; presize++) {
			randomPrefixes(rand, presize);
			for (twosize = 1; twosize <= 50; twosize++) {
				two = new String[twosize];
				randomTwoLetterStringsBelow(rand, 0, twosize, 26 * 26);
				randomKeyVals(rand);
				trie = createST();
				// testing contains
				for (char end = 'A'; end < 'Z'; end++) {
					assertEquals(endsWith(end), trie.containsKeyEndingIn(end));
				}
				for (char end = '0'; end < '9'; end++) {
					assertEquals(endsWith(end), trie.containsKeyEndingIn(end));
				}
			}
		}
	}
	
	private void testMethod(int methodID) throws Exception {
		try {
			isTrie = (methodID > 3);
			if (isTrie) methodID -= 3;
			System.out.print(border + test + border);
			switch (methodID) {
			case 1: tstSize(); break;
			case 2: tstRank(); break;
			case 3: tstContains(); break;
			}
		} catch(AssertionError aerr) {
			ae = aerr;
		} catch(Exception err) {
			e = err;
		}
		
		if (ae != null || e != null) {

			if (size <= 100) {
				System.out.println("Fails on the below key value pairs:");
				System.out.println(keyvalsToString());
				switch (methodID) {
				case 1: System.out.println("Failure in computing size with prefix " + arg); break;
				case 2: System.out.println("Failure in computing the rank of " + arg); break;
				case 3: System.out.println("Failure in computing containsKeyEndingIn(" + arg + ")"); break;
				}
			}
			
			System.out.print("\n" + border + test + failed + border);
			
			if (ae != null) throw ae;
			if (e != null) throw e;
		} else {
			System.out.print(border + test + passed + border);
		}
	}
	
	@Test
	public void testTSTSize() throws Exception {
		test = "* Testing size (TST)                      *\n";
		testMethod(1);
	}
	
	@Test
	public void testTSTRank() throws Exception {
		test = "* Testing rank (TST)                      *\n";
		testMethod(2);
	}
	
	@Test
	public void testTSTContains() throws Exception {
		test = "* Testing containsKeyEndingIn (TST)       *\n";
		testMethod(3);
	}
	
	@Test
	public void testTrieSize() throws Exception {
		test = "* Testing size (Trie)                     *\n";
		testMethod(4);
	}
	
	@Test
	public void testTrieRank() throws Exception {
		test = "* Testing rank (Trie)                     *\n";
		testMethod(5);
	}
	
	@Test
	public void testTrieContains() throws Exception {
		test = "* Testing containsKeyEndingIn (Trie)      *\n";
		testMethod(6);
	}
}