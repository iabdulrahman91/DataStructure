package hw2;

public class Testing {
	private static class Entry {
		String key;
		Integer val;
		
		public Entry(String key, Integer val) {
			this.key = key;
			this.val = val;
		}
	}
	
	private final static Entry[] entries = new Entry[20];
	private final static int[][] cases = new int[10][];
	private static boolean passed;
	
	private static void wrongResult(Object case_id, Object result) {
		System.out.println("Returned incorrect value " + result + " for case " + case_id);
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
	
	private static BST<String, Integer> newBST(int caseId) {
		BST<String, Integer> list = new BST<String, Integer>();
		for (int i = 0; i < cases[caseId].length; i++) {
			list.put(entries[cases[caseId][i]].key, entries[cases[caseId][i]].val);
		}
		return list;
	}

	public static void testIntNodes() {
		int result;
		int answers[] = { 0, 0, 0, 1, 1, 2, 1, 3, 2, 4 };  
		
		for (int i = 0; i < cases.length; i++) {
			result = newBST(i).intermediateNodes();
			if (result != answers[i]) wrongResult(i, result);
		}
	}
	
	private static String[] getKeys(BST<String, Integer> bst, int d) {
		String keys[] = new String[1 << d];
		assignKeys(keys, bst.root, 1);
		return keys;
	}
	
	private static void assignKeys(String keys[], BST<String, Integer>.Node n, int i) {
		if (n == null || i >= keys.length) return;
		keys[i] = n.key;
		assignKeys(keys, n.left, 2 * i);
		assignKeys(keys, n.right, 2 * i + 1);
	}

	public static void testClipDepth() {
		BST<String, Integer> bst, bstres;
		String keys[], keysres[];
		
		for (int i = 0; i < cases.length; i++) {
			bst = newBST(i);
			keys = getKeys(bst, 4);
			for (int d = 0; d < 4; d++) {
				bstres = newBST(i);
				bstres.clipDepth(d);
				keysres = getKeys(bstres, d + 1);
				int j;
				for (j = 1; j < (1 << d); j++) {
					if (keysres[j] == null ? keys[j] != null : !keysres[j].equals(keys[j])) {
						wrongResult("" + i + " depth = " + d + " index = " + j + " should be "+ keys[j], keysres[j]);
					}
				}
				for (; j < (1 << (d + 1)); j++) {
					if (keysres[j] != null) {
						wrongResult("" + i + " depth = " + d + " index = " + j + " should be null", keysres[j]);
					}
				}
			}
		}
	}
	
	public static void testSecondMax() {
		String result;
		String answers[] = { null, null, "E", "K", "Y", "U", "Y", "X", "X", "Y" };  
		
		for (int i = 0; i < cases.length; i++) {
			result = newBST(i).secondMax();
			if (result == null ? answers[i] != null : !result.equals(answers[i])) {
				wrongResult(i, result);
			}
		}
	}

	public static void testClosest() {
		String keys[] = { "B", "D", "F", "K", "Y"};
		String result;
		String answers[][] = { { null, null, null, null, null }, // case 0
	                           { "A", "A", "A", "A", "A" }, // case 1
	                           { "E", "E", "E", "K", "K" }, // case 2
	                           { "A", "C", "C", "K", "P" }, // case 3
	                           { "A", "A", "A", "H", "Y" }, // case 4
	                           { "A", "A", "F", "K", "X" }, // case 5
	                           { "H", "H", "H", "K", "Y" }, // case 6
	                           { "B", "D", "E", "H", "X" }, // case 7
	                           { "A", "D", "D", "K", "X" }, // case 8
	                           { "B", "D", "F", "K", "Y" }, // case 9
	                         };  
		String altanswers[][] = { { null, null, null, null, null }, // case 0
                                  { null, null, null, null, null }, // case 1
                                  { "E", "E", "K", "K", "K" }, // case 2
                                  { "C", "H", "H", "K", "P" }, // case 3
                                  { "H", "H", "H", "S", "Y" }, // case 4
                                  { "F", "F", "F", "K", "X" }, // case 5
                                  { "H", "H", "H", "K", "Y" }, // case 6
                                  { "B", "D", "H", "P", "Z" }, // case 7
                                  { "D", "D", "H", "K", "Z" }, // case 8
                                  { "B", "D", "F", "K", "Y" }, // case 9
                                };
		
		for (int i = 0; i < cases.length; i++) {
			for (int j = 0; j < keys.length; j++) {
				result = newBST(i).closest(keys[j]);
				if (result == null ? answers[i][j] != null : 
					(!result.equals(answers[i][j]) && (altanswers[i][j] == null || !result.equals(altanswers[i][j])))) {
					wrongResult("" + i + " with elem = " + keys[j], result);
					break;
				}
			}
		}
	}

	public static void testMerge() {
		BST<String, Integer> list, arg, orig;
		String errorStr;
		
		for (int i = 0; i < cases.length; i++) {
			for (int j = 0; j < cases.length; j++) {
				orig = newBST(i);
				list = newBST(i);
				arg = newBST(j);
				list.merge(newBST(j));
				errorStr = "";
				String key = "";
				Integer val, vmrg = null;
				for (int k = 0; k < cases[i].length; k++) {
					key = entries[cases[i][k]].key;
					val = orig.get(key);
					vmrg = list.get(key);
					Integer varg = arg.get(key);
					if (varg != null) val = varg;
					if (vmrg == null) {
						errorStr = key + " should not be deleted in the merge";
						break;
					}
					if (!vmrg.equals(val)) {
						errorStr = "correct value should be " + val;
						break;
					}
				}
				if (errorStr != "") {
					wrongResult(i + " (calling merge(case " + j + ")) error for key = " + key + ": " + errorStr, vmrg);
					return;
				}
				for (int k = 0; k < cases[j].length; k++) {
					key = entries[cases[j][k]].key;
					val = arg.get(key);
					vmrg = list.get(key);
					if (vmrg == null) {
						errorStr = key + " should be in the merge";
						break;
					}
					if (!vmrg.equals(val)) {
						errorStr = "correct value should be updated to " + val;
						break;
					}
				}
				if (errorStr != "") {
					wrongResult(i + " (calling merge(case " + j + ")) error for key = " + key + ": " + errorStr, vmrg);
					return;
				}
			}
		}
	}
	
	private static void testMethod(int methodId) {
		String methodStr[] = { "intermediateNodes", "clipDepth", "secondMax", "closest", "merge" };
		
		printIntroMessage(methodStr[methodId]);
		try {
			switch(methodId) {
			case 0: testIntNodes(); break;
			case 1: testClipDepth(); break;
			case 2: testSecondMax(); break;
			case 3: testClosest(); break;
			case 4: testMerge(); break;
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
		initializeEntries();
		
		cases[0] = new int[]{ };
		cases[1] = new int[]{ 0 };
		cases[2] = new int[]{ 2, 4 };
		cases[3] = new int[]{ 1, 0, 3, 14, 15 };
		cases[4] = new int[]{ 9, 0, 6, 16, 13, 17, 18 };
		cases[5] = new int[]{ 0, 3, 7, 8, 12, 14 };
		cases[6] = new int[]{ 4, 3, 18, 19 };
		cases[7] = new int[]{ 6, 8, 2, 0, 3, 9, 13, 15, 17, 11, 10, 16 };
		cases[8] = new int[]{ 8, 0, 3, 4, 5, 11, 17, 19, 14, 13 };
		cases[9] = new int[]{ 5, 2, 4, 0, 3, 7, 1, 6, 8, 9, 10, 12, 11, 15, 14, 13, 18, 19 };
		
		for (int i = 0; i < 5; i++) {
			testMethod(i);
		}
	}
	
	private static void initializeEntries() {
		entries[0] = new Entry("A", 5);
		entries[1] = new Entry("C", 4);
		entries[2] = new Entry("E", 2);
		entries[3] = new Entry("H", 3);
		entries[4] = new Entry("K", -5);
		entries[5] = new Entry("P", -1);
		entries[6] = new Entry("S", 0);
		entries[7] = new Entry("U", 5);
		entries[8] = new Entry("X", 3);
		entries[9] = new Entry("Z", 9);
		entries[10] = new Entry("B", -5);
		entries[11] = new Entry("D", 3);
		entries[12] = new Entry("F", 10);
		entries[13] = new Entry("H", 5);
		entries[14] = new Entry("K", 3);
		entries[15] = new Entry("P", 1);
		entries[16] = new Entry("S", 10);
		entries[17] = new Entry("W", 5);
		entries[18] = new Entry("Y", 3);
		entries[19] = new Entry("Z", 2);
	}
}