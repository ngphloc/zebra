package vn.spring.zebra.math;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
//Chinh hop co lap, chap n cua 2, tuc 2 mu n
public class BinaryArrangementWithRepetitionGenerator {
	
	private ArrayList<int[]>  arrangements;
	private int        n;
	private int        r;
	private BigInteger total;

	private BinaryArrangementWithRepetitionGenerator (int n, int r) {
		if (r == 0 || n == 0) throw new IllegalArgumentException ();
		this.n = n;
		this.r = r;
		this.total        = getExponent(n, r);
		this.arrangements = new ArrayList();
	}
	private BigInteger getExponent(int n, int r) {
		BigInteger fact = BigInteger.ONE;
	    for (int i = 0; i < r; i++) {
	      fact = fact.multiply (new BigInteger (Integer.toString (n)));
	    }
	    return fact;
	}
	
	private void get(int[] a, int i) {
		for(int j = 0; j < n; j++) {
			a[i] = j;
			if(i < r - 1) {
				get(a, i + 1);
			}
			else if(i == r -1) {
				int[] b = new int [a.length];
				for(int k = 0; k < a.length;  k++) b[k] = a[k];
				arrangements.add(b);
			}
		}
	}
	
	public int[] getArrangement(int i) {
		return arrangements.get(i);
	}
	public long getNumArrangement() {
		return arrangements.size();
	}
	public static BinaryArrangementWithRepetitionGenerator parse(int n, int r) {
		BinaryArrangementWithRepetitionGenerator arr = new BinaryArrangementWithRepetitionGenerator(n, r);
		int[] a = new int[r]; for(int i=0; i<r; i++) a[i] = -1;
		arr.get(a, 0);
		if(arr.arrangements.size() != arr.total.longValue()) throw new IllegalArgumentException ();
		return arr;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] elements = {"a", "b", "c", "d", "e", "f", "g"};
		BinaryArrangementWithRepetitionGenerator arr = BinaryArrangementWithRepetitionGenerator.parse(elements.length, 3);
		int[] indices = null;
		StringBuffer combination;
		for(int i=0; i<arr.getNumArrangement(); i++) {
			combination = new StringBuffer ();
			indices = arr.getArrangement(i);
			for (int j = 0; j < indices.length; j++) {
				combination.append (elements[indices[j]]);
			}
			System.out.println (combination.toString ());
		}
	}

}
