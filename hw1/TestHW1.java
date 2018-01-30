package hw1;

public class TestHW1 {
	public static void main(String[] args) {
		int coeffs1[] = {1, -1, 2};   // represents f(n) = n^2 - n + 2
		int coeffs2[] = {2, 0, 1, 0}; // represents f(n) = 2n^3 + n
		int coeffs3[] = {0, 0, 1, 0}; // represents f(n) = 0
		int coeffs4[] = {};          // represents f(n) = n (after simplication)
		Polynomial p1 = new Polynomial(coeffs1);
		Polynomial p2 = new Polynomial(coeffs2);
		Polynomial p3 = new Polynomial(coeffs3);
		Polynomial p4 = new Polynomial(coeffs4);		
		System.out.println("Polynomial 1: " + p1);
		System.out.println("Polynomial 2: " + p2);
		System.out.println("Polynomial 3: " + p3);
		System.out.println("Polynomial 4: " + p4);
		

		System.out.println("\nValues for f(n) = " + p1);
		for (int i=-6; i<=6; i+=3)
			System.out.println("f(" + i + ") = " + p1.evaluate(i));
		System.out.println("\nValues for f(n) = " + p2);
		for (int i=-6; i<=6; i+=3) 
			System.out.println("f(" + i + ") = " + p2.evaluate(i));
		System.out.println("\nValues for f(n) = " + p3);
		for (int i=-6; i<=6; i+=3)
			System.out.println("f(" + i + ") = " + p3.evaluate(i));
		System.out.println("\nValues for f(n) = " + p4);
		for (int i=1; i<100; i+=50)
			System.out.println("f(" + i + ") = " + p4.evaluate(i));
	} 

}
