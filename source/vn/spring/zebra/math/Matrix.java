package vn.spring.zebra.math;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class Matrix {
	protected double[][]     a;
	protected int            m, n;
	
	public Matrix(int m, int n) throws Exception{
		if(m==0 || n==0) throw new Exception();
		this.m = m;
		this.n = n;
		a = new double[m][n];
	}
	
	public Matrix(Matrix matrix) {
		m = matrix.m;
		n = matrix.n;
		a = new double[m][n];
		for(int i=0; i<m; i++) {
			for(int j=0; j<n; j++) {
				a[i][j] = matrix.a[i][j];
			}
		}
	}
		
	public double value(int i, int j) {
		return a[i][j];
	}
	public boolean isSquare() {
		return (m==n);
	}
	
	public Matrix remove(int i, int j) throws Exception {
		if(n==1 || n==1) throw new Exception();
		Matrix matrix = new Matrix(m-1, n-1);
		int u=0, x=0;
		for(u=0; u<m; u++) {
			if(u==i) continue;
			int v=0, y=0;
			for(v=0; v<n; v++) {
				if(v==j) continue;
				matrix.a[x][y] = a[u][v];
				y++;
			}
			x++;
		}
		return matrix;
	}
	
	public Matrix opposite() throws Exception {
		Matrix result = new Matrix(m,n);
		for(int i=0; i<m; i++) {
			for(int j=0; j<n; j++) {
				result.a[i][j] = -a[i][j];
			}
		}
		return result;
	}
	public Matrix translate() throws Exception {
		Matrix result = new Matrix(n, m);
		for(int i=0; i<n; i++) {
			for(int j=0; j<m; j++) {
				result.a[i][j] = a[j][i];
			}
		}
		return result;
	}
	public Matrix add(Matrix matrix) throws Exception{
		if(m!=matrix.m || n!=matrix.n) throw new Exception();
		Matrix result = new Matrix(m, n);
		
		for(int i=0; i<m; i++) {
			for(int j=0; j<n; j++) {
				result.a[i][j] = a[i][j] + matrix.a[i][j];
			}
		}
		return result;
	}
	public Matrix subtract(Matrix matrix) throws Exception {
		if(m!=matrix.m || n!=matrix.n) throw new Exception();
		Matrix result = new Matrix(m, n);
		
		for(int i=0; i<m; i++) {
			for(int j=0; j<n; j++) {
				result.a[i][j] = a[i][j] - matrix.a[i][j];
			}
		}
		return result;
	}
	
	public Matrix multiply(double c)  throws Exception {
		Matrix result = new Matrix(m, n);
		for(int i=0; i<m; i++) {
			for(int j=0; j<n; j++) {
				result.a[i][j] = c*a[i][j];
			}
		}
		return result;
	}
	
	public Matrix multiply(Matrix matrix) throws Exception{
		if(n!=matrix.m) throw new Exception();
		
		Matrix result = new Matrix(m, matrix.n);
		for(int i=0; i<m; i++) {
			for(int j=0; j<matrix.n; j++) {
				result.a[i][j] = 0;
				for(int k=0; k<n; k++) {
					result.a[i][j] += a[i][k] * matrix.a[k][j];
				}
			}
		}
		return result;
	}
	public Matrix inverse() throws Exception {
		Matrix result = new Matrix(m,n);
		double det = determinant();
		for(int i=0; i<m; i++) {
			for(int j=0; j<n; j++) {
				result.a[i][j] = Aij(i, j) / det;
			}
		}
		return result.translate();
	}
	
	public double determinant() throws Exception{
		if(m<2 || n<2 || !isSquare()) throw new Exception();
		if(m==2) {
			return a[0][0]*a[1][1] - a[0][1]*a[1][0];
		}
				
		double det = 0;
		for(int j=0; j<n; j++) {
			det += a[0][j] * (j%2 == 0 ? 1 : -1) * remove(0,j).determinant();
		}
		return det;
	}
	
	public double Aij(int i, int j) throws Exception {
		return ( (i+j)%2 == 0 ? 1 : -1 ) * remove(i,j).determinant();
	}
	
	public String toString() {
		StringBuffer output = new StringBuffer();
		output.append(m + ", " + n + "\n");
		for(int i=0; i<m; i++) {
			for(int j=0; j<n; j++) {
				output.append(a[i][j]);
				if(j < n - 1) output.append(", ");
			}
			if(i < m - 1) output.append("\n");
		}
		return output.toString();
	}
	
	public static Matrix createFrom(String input) throws Exception {
		String[] list = input.split("\n");
		return createFrom(trim(list), 0);
	}
	public static Matrix createFrom(String[] list, int begin) throws Exception {
		String[] rowcol = list[begin].split(",");
		
		Matrix result = new Matrix(Integer.parseInt(rowcol[0].trim()), Integer.parseInt(rowcol[1].trim()));
		for(int i=0; i < result.m; i++) {
			String[] rows = list[begin + 1 + i].split(",");
			for(int j=0; j < result.n; j++) {
				result.a[i][j] = Double.parseDouble(rows[j].trim());
			}
		}
		return result;
	}
	
	public static Matrix equas(Matrix A, Matrix B) throws Exception {
		if(!A.isSquare() || A.n!=B.m) throw new Exception();
		return A.inverse().multiply(B);
	}
	public static void performe(String[] list) throws Exception {
		Matrix m1 = null, m2 = null;
		
		if(list[0].startsWith("remove")) {
			String[] xy = list[0].split(" ");
			int x = Integer.parseInt(xy[1].trim());
			int y = Integer.parseInt(xy[2].trim());
			
			m1 = createFrom(list, 1);
			m1 = m1.remove(x, y);
			System.out.println(m1);
		}
		else if(list[0].equals("translate") || list[0].equals("opposite") || 
				list[0].equals("determinant") || list[0].equals("multiplyk") || 
				list[0].equals("inverse")) {
			m1 = createFrom(list, 1);

			if(list[0].equals("translate")) {
				System.out.println(m1.translate());
			}
			else if(list[0].equals("opposite")) {
				System.out.println(m1.opposite());
			}
			else if(list[0].equals("determinant")) {
				System.out.println(m1.determinant());
			}
			else  if(list[0].equals("multiplyk")) {
				double k = Double.parseDouble(list[2 + m1.m]);
				System.out.println(m1.multiply(k));
			}
			else if(list[0].equals("inverse")) {
				System.out.println(m1.multiply(m1.inverse()));
			}
		}
		else if(list[0].equals("add") || list[0].equals("subtract") || 
				list[0].equals("multiply") || list[0].equals("equas")) {
			m1 = createFrom(list, 1);
			m2 = createFrom(list, 2 + m1.m);
			
			if(list[0].equals("add")) {
				System.out.println(m1.add(m2));
			}
			else if(list[0].equals("subtract")) {
				System.out.println(m1.subtract(m2));
			}
			else if(list[0].equals("multiply")) {
				System.out.println( m1.multiply(m2) );
			}
			else if(list[0].equals("equas")) {
				System.out.println( equas(m1,m2) );
			}

		}
	}
	
	public static void performe(String input) throws Exception {
		performe(trim(input.split("\n")));
	}
	public static void performe(File f) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String input = "", line = null;
		while( (line = reader.readLine()) != null ) {
			input += line + "\n";
		}
		performe(input);
	}
	
	private static String[] trim(String[] list) {
		ArrayList<String> list2 = new ArrayList<String>();
		for(int i=0; i<list.length; i++) {
			String e = list[i].trim();
			if(e.length() > 0) list2.add(e);
		}
		list = new String[list2.size()];
		return list2.toArray(list);
	}
	
	public static void main(String[] args) throws Exception{
		String filepath = "W:/home/eclipse-j2ee-workspace/linearalgrebra/bin/matrix.txt";
		if(args.length > 1) filepath = args[0];
		
		performe(new File(filepath));
	}
}