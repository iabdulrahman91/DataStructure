package hw3;

import java.util.*;

public class Testing {
	private static boolean passed;
	private static Random r;
	private static BSTDict test;
	private static LinkedList<Operation> exec;
	private static int max;
	private static int w;
	
	private static class Operation {
		final int op;
		final String word, defn;
		
		public Operation(int op, String word, String defn) {
			this.op = op;
			this.word = word;
			this.defn = defn;
		}
		
		public String toString() {
			switch (op) {
			case 0: return "addDefinition(" + word + ", " + defn + ");";
			case 1: return "getDefinitions(" + word + ");";
			case 2: return "remDefinition(" + word + ", " + defn + ");";
			case 3: return "contains(" + word + ");";
			case 4: return "numEntries();";
			}
			return null;
		}
	}
	
	private static void wrongResult(Object result) {
		System.out.println("Incorrect value " + result + " after executing the following calls");
		for (Operation o : exec) {
			System.out.println(o);
		}
		passed = false;
	}
	
	private static void printIntroMessage(String methodStr) {
		passed = true;
		System.out.println("Testing " + methodStr);
	}
	
	private static void printPassMessage(String methodStr) {
		System.out.print("Done testing " + methodStr + "...");
		if (passed) {
			System.out.println("all tests passed!");
		} else {
			System.out.println("some tests failed!");
		}
		System.out.println();
		System.out.println("**********");
		System.out.println();
	}
	
	private static String word(int w) {
		return "word #" + w;
	}
	
	private static String defn(int w, int d) {
		return "defn #" + d + " of " + word(w);
	}
	
	private static void reset(int m) {
		max = m;
		exec = new LinkedList<Operation>();
		test = new BSTDict();
		r = new Random(0);
	}
	
	private static int addDefinition(int c) {
		// making sure to generate different entries
		w = r.nextInt(max) * max + c;
		int d = 0;
		do {
			d++;
			exec.add(new Operation(0, word(w), defn(w, d)));
			test.addDefinition(word(w), defn(w, d));
		} while (r.nextBoolean());
		
		return d;
	}
	
	private static Iterable<String> getDefinitions(int w) {
		exec.add(new Operation(1, word(w), null));
		return test.getDefinitions(word(w));
	}
	
	private static void remDefinition(int w, int d) {
		exec.add(new Operation(2, word(w), defn(w, d)));
		test.remDefinition(word(w), defn(w, d));
	}
	
	private static boolean contains(String word) {
		exec.add(new Operation(3, word, null));
		return test.contains(word);
	}
	
	private static int numEntries() {
		exec.add(new Operation(4, null, null));
		return test.numEntries();
	}
	
	private static boolean verifyDefinitions(Iterable<String> defns, int w, int dc) {
		if (dc == 0) return (defns == null);
		if (defns == null) return false;
		String strs[] = new String[dc];
		for (int d = 0; d < dc; d++) {
			strs[d] = defn(w, d + 1);
		}
		for (String def : defns) {
			boolean found = false;
			for (int d = 0; d < dc; d++) {
				if (strs[d].equals(def)) {
					found = true;
					strs[d] = "";
					break;
				}
			}
			if (!found) return false;
		}
		for (int d = 0; d < dc; d++) {
			if (!strs[d].equals("")) return false;
		}
		return true;
	}

	private static void testRemDefinition() {
		reset(50);
		int words[] = new int[max];
		int defct[] = new int[max];
		for (int c = 0; c < max; c++) {
			defct[c] = addDefinition(c);
			words[c] = w;
		}
		for (int c = 0; c < max; c++) {
			for (int d = defct[c]; d > 0; d--) {
				Iterable<String> defns = getDefinitions(words[c]);
				if (!verifyDefinitions(defns, words[c], d)) {
					wrongResult(defns);
					return;
				}
				if (d > 1) remDefinition(words[c], d);
			}
		}
		for (int c = 0; c < max; c++) {
			remDefinition(words[c], 1);
			Iterable<String> defns = getDefinitions(words[c]);
			if (!verifyDefinitions(defns, words[c], 0)) {
				wrongResult(defns);
				return;
			}
		}
	}
	
	private static void testGetDefinitions() {
		reset(100);
		int words[] = new int[max];
		int defct[] = new int[max];
		for (int c = 0; c < max; c++) {
			defct[c] = addDefinition(c);
			words[c] = w;
			for (int t = 0; t <= c; t++) {
				Iterable<String> defns = getDefinitions(words[t]);
				if (!verifyDefinitions(defns, words[t], defct[t])) {
					wrongResult(defns);
					return;
				}
			}
		}
	}

	private static void testContains() {
		reset(100);
		int words[] = new int[max];
		for (int c = 0; c < max; c++) {
			addDefinition(c);
			words[c] = w;
		}
		reset(100);
		for (int c = 0; c < max; c++) {
			boolean cont = contains(word(words[c]));
			if (cont) {
				wrongResult(cont);
				return;
			}
			addDefinition(c);
			words[c] = w;
			for (int t = 0; t <= c; t++) {
				cont = contains(word(words[t]));
				if (!cont) {
					wrongResult(cont);
					return;
				}
			}
		}
	}
	
	private static void testNumEntries() {
		reset(100);
		for (int c = 0; c < max; c++) {
			addDefinition(c);
			int n = numEntries();
			if (n != c + 1) {
				wrongResult(n);
				return;
			}
		}
	}
	
	private static void testMethod(int methodId) {
		String methodStr[] = { "numEntries", "contains", "getDefinitions", "remDefinition" };
		
		printIntroMessage(methodStr[methodId]);
		try {
			switch(methodId) {
			case 0: testNumEntries(); break;
			case 1: testContains(); break;
			case 2: testGetDefinitions(); break;
			case 3: testRemDefinition(); break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			System.out.flush();
			passed = false;
		}
		printPassMessage(methodStr[methodId]);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 4; i++) {
			testMethod(i);
		}
	}
}