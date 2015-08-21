/**
 * 
 */
package vn.spring.zebra.util;

import java.math.BigInteger;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class MathUtil {
	
	public final static BigInteger getFactorial (int n) {
		BigInteger fact = BigInteger.ONE;
		for (int i = n; i > 1; i--) {
			fact = fact.multiply (new BigInteger (Integer.toString (i)));
		}
		return fact;
	}
	public final static BigInteger numberOfCombination(int n, int r) {
		if (r > n || n < 1) throw new IllegalArgumentException ();
		if(n == 1) return BigInteger.ONE;
		BigInteger nFact = getFactorial (n);
		BigInteger rFact = getFactorial (r);
		BigInteger nminusrFact = getFactorial (n - r);
		return nFact.divide (rFact.multiply (nminusrFact));
	}
	public static final double round(double a, int decimal) {
		try {
			String str = String.valueOf(a);
			int idx = str.indexOf(".");
			if(idx == -1 || idx == str.length() - 1) return Double.parseDouble(str);
			return Double.parseDouble(
					str.substring(0, Math.min(str.length(), idx + decimal + 1))
				);
		}
		catch(Exception e) {
			System.out.println("There is some error MathUtil.round: " + e.getMessage());
			return 0;
		}
	}
}
