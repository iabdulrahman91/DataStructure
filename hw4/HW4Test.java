package hw4;

public class HW4Test {

	public static void main(String[] args) {
		try {
			(new HW4TestJUnit()).testAddSepChain();
		} catch(AssertionError ae) {
			ae.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			(new HW4TestJUnit()).testContainsSepChain();
		} catch(AssertionError ae) {
			ae.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			(new HW4TestJUnit()).testDelSepChain();
		} catch(AssertionError ae) {
			ae.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			(new HW4TestJUnit()).testResizeSepChain();
		} catch(AssertionError ae) {
			ae.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			(new HW4TestJUnit()).testAddLinProbe();
		} catch(AssertionError ae) {
			ae.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			(new HW4TestJUnit()).testContainsLinProbe();
		} catch(AssertionError ae) {
			ae.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			(new HW4TestJUnit()).testDeleteLinProbe();
		} catch(AssertionError ae) {
			ae.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			(new HW4TestJUnit()).testResizeLinProbe();
		} catch(AssertionError ae) {
			ae.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
