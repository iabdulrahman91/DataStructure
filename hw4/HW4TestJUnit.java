package hw4;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Random;
import java.lang.management.*;

public class HW4TestJUnit {
	String border = "*******************************************\n";
	String passed = "* Passed!                                 *\n";
	String failed = "* Failed!                                 *\n";
	String test;
	
	AssertionError ae;
	Exception e;
	
	int size, psize;
	int p;
	String[] keys;
	boolean[] picked;
	
	String arg;
	
	Set<String> st;
	boolean sepChain;
	
	private Set<String> createSet(int cap, int n) {
		Set<String> st = sepChain ? new SetSC<String>() : new SetLP<String>();
		if (cap > 0) st = sepChain ? new SetSC<String>(cap) : new SetLP<String>(cap);
		if (keys != null) for (int i = 0; i < keys.length && i < n; i++) st.add(keys[i]);
		return st;
	}
	
	private String keysToString(boolean justPicked) {
		int sz = justPicked ? psize : size;
		StringBuilder s = new StringBuilder();
		s.append("size = " + sz + ", strings = ");
		int i, k;
		for (i = 0, k = 0; i < sz - 1; i++, k++) {
			if (justPicked) for (; p == k || !picked[k]; k++);
			s.append(keys[k] + ", ");
		}
		if (sz > 0) {
			if (justPicked) for (; p == k || !picked[k]; k++);
			s.append(keys[k]);
		}
		
		return s.toString();
	}

	private static String twoLetterString(int val) {
		char chars[] = new char[2];
		chars[0] = (char)('A' + ((val / 26) % 26));
		chars[1] = (char)('A' + (val % 26));
		return String.valueOf(chars);
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
			if (taken[val]) keys[i++] = twoLetterString(val);
		}
	}
	
	private void tstAdd() {
		Set<String> st = createSet(0, 0);
		Random rand = new Random(0);
		int i;
		for (size = 1; size <= 100; size++) {
			st = createSet(size * 4, 0);
			keys = new String[size];
			picked = new boolean[size];
			randomTwoLetterStringsBelow(rand, 0, size, 26 * 26);
			for (psize = 0; psize < size; psize++) {
				while(true) {
					p = rand.nextInt(size);
					if (!picked[p]) break;
				}
				picked[p] = true;
				for (i = 0; i < size; i++) {
					if (picked[i]) st.add(keys[i]);
				}
				// testing size and keys
				if (sepChain) {
					assertEquals(psize + 1, ((SetSC<String>)st).size());
				} else {
					assertEquals(psize + 1, ((SetLP<String>)st).size());
				}
				boolean check[] = new boolean[psize + 1];
				for (String s : st) {
					for (i = 0; i <= psize; i++) {
						if (s.equals(keys[i])) {
							assertTrue("String " + s + " should not be in the set (was not picked)", picked[i]);
							assertFalse("String " + s + " appears multiple times in the set", check[i]);
							check[i] = true;
							break;
						}
					}
					assertTrue("String " + s + " should not be in the set", i < size);
				}
			}
		}
	}
	
	private void tstContains() {
		Set<String> st = createSet(0, 0);
		Random rand = new Random(0);
		int i;
		for (size = 1; size <= 100; size++) {
			st = createSet(size * 4, 0);
			keys = new String[size];
			picked = new boolean[size];
			randomTwoLetterStringsBelow(rand, 0, size, 26 * 26);
			for (psize = 0; psize < size; psize++) {
				while(true) {
					p = rand.nextInt(size);
					if (!picked[p]) break;
				}
				picked[p] = true;
				for (i = 0; i < size; i++) {
					if (picked[i]) st.add(keys[i]);
				}
				for (i = 0; i <= psize; i++) {
					if (picked[i]) {
						assertTrue("should contain " + keys[i], st.contains(keys[i]));
					} else {
						assertFalse("should not contain " + keys[i], st.contains(keys[i]));
					}
				}
			}
		}
	}
	
	private void tstDel() {
		Set<String> st;
		Random rand = new Random(0);
		int i;
		for (size = 1; size <= 100; size++) {
			keys = new String[size];
			picked = new boolean[size];
			randomTwoLetterStringsBelow(rand, 0, size, 26 * 26);
			st = createSet(size * 4, size);
			for (psize = 0; psize < size; psize++) {
				while(true) {
					p = rand.nextInt(size);
					if (!picked[p]) break;
				}
				picked[p] = true;
				st.delete(keys[p]);
				for (i = 0; i < size; i++) {
					if (!picked[i]) st.add(keys[i]);
				}
				// testing size and keys
				if (sepChain) {
					assertEquals(size - psize - 1, ((SetSC<String>)st).size());
				} else {
					assertEquals(size - psize - 1, ((SetLP<String>)st).size());
				}
				boolean check[] = new boolean[size];
				for (String s : st) {
					for (i = 0; i < size; i++) {
						if (s.equals(keys[i])) {
							assertTrue("String " + s + " should not be in the set (was deleted)", !picked[i]);
							assertFalse("String " + s + " appears multiple times in the set", check[i]);
							check[i] = true;
							break;
						}
					}
					assertTrue("String " + s + " should not be in the set", i < size);
				}
			}
		}
	}
	
	private void randomKeys(int size) {
		this.size = size;
		int mask = 16;
		for (mask = 16; mask < size; mask *= 16);
		
		keys = new String[size];
		
		for (int i = 0; i < size; i++) {
			keys[i] = Integer.toHexString(mask | i).toUpperCase().substring(1);
		}
	}
	
	private void printTimes(int l, int[] sizes, long[] times, long[] avg) {
		for (int i = 0; i < l; i++) {
	
			System.out.println("Time elapsed for size " + sizes[i] + ": " + times[i] + " => average (per key) : " + avg[i]);
		}		
	}
	
	private int binarySearch(String s, int sz) {
		int lo = 0;
		int hi = sz - 1;
		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			int cmp = s.compareTo(keys[mid]);
			if (cmp == 0) return mid;
			else if (cmp < 0) hi = mid - 1;
			else lo = mid + 1;
		}
		return sz;
	}
	
	private void tstResize() {
		ThreadMXBean thread = ManagementFactory.getThreadMXBean();
		
		randomKeys(1024 * 1024);
		System.out.println("Testing efficiency for " + size + " keys in total.");
		
		int[] sizes = new int[11];
		long[] times = new long[11];
		long[] avg = new long[11];
		
		long nanoTime = 0;
		boolean usingNanoTime = false;
		int beginInd = 0;
		int failures = 0;
		st = createSet(0, 0);

		int i, l;
		for (l = 0; l < 11; l++) {
			sizes[l] = size >> (10 - l);
			times[l] = thread.getCurrentThreadCpuTime();
			nanoTime = System.nanoTime();
			for (i = 0; i < sizes[l]; i++) {
				st.add(keys[i]);
			}
			times[l] = thread.getCurrentThreadCpuTime() - times[l];
			if (times[l] == 0) usingNanoTime = true;
			if (usingNanoTime) {
				times[l] = System.nanoTime() - nanoTime;
			}
			avg[l] = times[l] / sizes[l];
			if (usingNanoTime && l < 3) {
				if (avg[beginInd] > 2 * avg[l]) beginInd = l;
			} else {
				if (avg[l] > avg[beginInd] * 8) failures++;
			}
			if ((!usingNanoTime && failures > 0) || failures > 2) {
				if (usingNanoTime) System.out.println("USING NANO TIME!!!");
				printTimes(l + 1, sizes, times, avg);
				System.out.println("Add is inefficient without resize!");
				fail("Add is inefficient without resize!");
			}
			// testing size and keys
			if (sepChain) {
				assertEquals(sizes[l], ((SetSC<String>)st).size());
			} else {
				assertEquals(sizes[l], ((SetLP<String>)st).size());
			}
			boolean check[] = new boolean[sizes[l]];
			for (String s : st) {
				i = binarySearch(s, sizes[l]);
				assertTrue("String " + s + " should not be in the set", i < sizes[l]);
				assertFalse("String " + s + " appears multiple times in the set", check[i]);
				check[i] = true;
			}
		}
		if (usingNanoTime) System.out.println("USING NANO TIME!!!");
		printTimes(l, sizes, times, avg);
		System.out.println("Passed add performance test!");
	}
	
	private void testMethod(int methodID, boolean sepChain) throws Exception {
		try {
			this.sepChain = sepChain;
			System.out.print(border + test + border);
			switch (methodID) {
			case 0: tstAdd(); break;
			case 1: tstContains(); break;
			case 2: tstDel(); break;
			case 3: tstResize(); break;
			}
		} catch(AssertionError aerr) {
			ae = aerr;
		} catch(Exception err) {
			e = err;
		}
		
		if (ae != null || e != null) {

			if (size <= 100) {
				System.out.println("Fails on the below strings:");
				System.out.println(keysToString(false));
				switch (methodID) {
				case 0: System.out.println("Failure in add after trying to add the string = " + keys[p] +  " (successfully added the following:)");
						System.out.println(keysToString(true));
						break;
				case 1: System.out.println("Failure in contains after adding string = " + keys[p] +  " (successfully added the following:)");
						System.out.println(keysToString(true));
						break;
				case 2: System.out.println("Failure in deleting string = " + keys[p] +  " (successfully deleted the following:)");
						System.out.println(keysToString(true));
						break;
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
	public void testResizeSepChain() throws Exception {
		test = "* Testing resize (SepChain)               *\n";
		testMethod(3, true);
	}
	
	@Test
	public void testDelSepChain() throws Exception {
		test = "* Testing delete (SepChain)               *\n";
		testMethod(2, true);
	}
	
	@Test
	public void testContainsSepChain() throws Exception {
		test = "* Testing contains (SepChain)             *\n";
		testMethod(1, true);
	}
	
	@Test
	public void testAddSepChain() throws Exception {
		test = "* Testing add (SepChain)                  *\n";
		testMethod(0, true);
	}
	
	@Test
	public void testResizeLinProbe() throws Exception {
		test = "* Testing resize (LinProbe)               *\n";
		System.out.println("Make sure you have implemented resize in SetLP!");
		System.out.println("Once you did, you can comment lines 349-351 in HW4TestJUnit.java to test.");
		fail("Failing by default!");
		testMethod(3, false);
	}
	
	@Test
	public void testDeleteLinProbe() throws Exception {
		test = "* Testing delete (LinProbe)               *\n";
		testMethod(2, false);
	}
	
	@Test
	public void testContainsLinProbe() throws Exception {
		test = "* Testing contains (LinProbe)             *\n";
		testMethod(1, false);
	}
	
	@Test
	public void testAddLinProbe() throws Exception {
		test = "* Testing add (LinProbe)                  *\n";
		testMethod(0, false);
	}
}