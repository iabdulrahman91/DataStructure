/*********************************************
 *  CSC 300 Sections 201, 210 Summer I 2017  *
 *  Homework assignment 1:  the Polynomial   *
 *  class.                                   *
 *                                           *
 *  A polynomial is a function which         *
 *  consists of a summation of terms.        *
 *  Each term consists of a coefficient c    *
 *  and a non-negative power of n.           *
 *  For example, f(n) = 2n^4 - 3n^3 + 4n - 1 *
 *  is a polynomial.                         *
 *                                           *
 *  Your assignment is to complete the code  *
 *  below as follows:                        *
 *                                           *
 *  1.  Complete the constructor             *
 *  2.  Write a method called "simplify"     *
 *  3.  Write a method called "toString"     *
 *  4.  Write a method called "degree"       *
 *  5.  Write a method called "evaluate"     *
 *                                           *
 *  Please refer to the homework write-up    *
 *  for a more detailed description of the   *
 *  assignment.                              *
 *                                           *
 *  Please DO NOT put a main method into the *
 *  Polynomial class.  Notice that I have    *
 *  created a second class, called TestHW1.  *
 *  It has test cases for each of the        *
 *  methods of the Polynomial class          *
 *********************************************
 */ 

package hw1;

public class Polynomial {
	private int[] coefficients;

	// the constructor creates a Polynomial with the specified
	// coefficients (passed as the parameter c).  It should
	// set the "coefficients" instance variable to be an array
	// which is a copy of c.  At the end, the constructor calls
	// the "simplify" method, which makes sure that the first
	// coefficient(s) of the polymonial are non-zero.
	public Polynomial(int[] c) {
		coefficients = new int[c.length];
		// complete this
		for(int i =0; i<c.length; i++){
			coefficients[i]=c[i];	
		}
		// make sure that the first coefficient of the polynomial is not 0
		simplify();
	}

	/* 
	   simplify should (if necessary) create a new array to
	   be stored in the "coefficients" instance variable.
	   In the new array, the first coefficient will be nonzero.
	   For example, 
	  
	   int coeffs[] = {0, 0, 2, 1, 0};
	   Polynomial p = new Polynomial(coeffs);
	   p represents the function f(n) = 0n^4 + 0n^3 + 2n^2 + n.
	   We would like to get ride of the highest order term(s)
	   with coefficient of 0.  In this example after calling
	   
	   p.simplify();
	 
	   p's coefficients should be {2, 1, 0}, which
	   represents f(n) = 2n^2 + n
	*/
	public void simplify() {
		// fill in the code for this method
		int i=0;
		while(i<this.coefficients.length && this.coefficients[i]==0){
			i++;
		}
		int[] n = new int[(this.coefficients.length)-i];
		int j = 0;
		while(i<this.coefficients.length){
			n[j]=this.coefficients[i];
			i++;
			j++;
		}
		this.coefficients=n;
	}
	
	/*
	   return a String that represents the Polynomial.  The
	   toString method should be written as specified in
	   the homework write-up
	*/
	public String toString() {
		int p = degree();
		StringBuilder b = new StringBuilder("");
		int i = 0;
		if (p < 0)
			return "0";
		else if(p==0){
			b.append(coefficients[0]);
			return b.toString();
		}
		// fill in the rest
		
		while (i < p - 1) {
			int k = p - i;
			if (coefficients[i] == 0) {
				i++;
			} else if (coefficients[i] == 1) {
				b.append(" + n^");
				b.append(k);
				i++;
			} else if (coefficients[i] == -1) {
				b.append(" - n^");
				b.append(k);
				i++;
			} else if (coefficients[i] > 1) {
				b.append(" + ");
				b.append(coefficients[i]);
				b.append("n^");
				b.append(k);
				i++;
			} else if (coefficients[i] < -1) {
				b.append("  - ");
				b.append(coefficients[i] * -1);
				b.append("n^");
				b.append(k);
				i++;
			}

		}

		// now the degree is 1
		if (coefficients[i] == 0) {
			i++;
		}

		else if (coefficients[i] == 1) {
			b.append(" + n");
			i++;
		} else if (coefficients[i] == -1) {
			b.append(" - n");
			i++;
		} else if (coefficients[i] > 1) {
			b.append(" + ");
			b.append(coefficients[i]);
			b.append("n");
			i++;
		} else if (coefficients[i] < -1) {
			b.append(" - ");
			b.append(coefficients[i] * -1);
			b.append("n");
			i++;
		}

		// last coefficient
		if (coefficients[i] == 0) {
			i++;
		} else if (coefficients[i] > 0) {
			b.append(" + ");
			b.append(coefficients[i]);
		} else if (coefficients[i] < 0) {
			b.append(" - ");
			b.append(coefficients[i] * -1);
		}

		
		// fixing the the beginning of the formal representation
		if (coefficients[0] < 0) {
			StringBuilder nStr = new StringBuilder("-");
			nStr.append(b.toString().substring(3));
			b = nStr;

		} else if (coefficients[0] > 0) {
			StringBuilder nStr = new StringBuilder("");
			nStr.append(b.toString().substring(3));
			b = nStr;

		}
		return b.toString();
	}

	// return the degree of the polynomial
	public int degree() {
		return this.coefficients.length-1;    // replace this
	}

	// return the value of the Polymonial f(n) when n is equal to x
	public int evaluate(int x) {
		// fill this in
		int result = 0;
		int i = 0;
		int deg = degree();
		if (deg < 0)
			return 0;
		while (i < deg) { // deg = coe.length-1
			int k = deg - i;
			if (coefficients[i] == 0) {
				i++;
			} else {
				int res = x;
				for (; k > 1; k--) {
					res = res * x;
				}
				res = res * coefficients[i];
				result = result + res;
				i++;
			}
		}
		result += coefficients[i];
		return result;

	}
}
