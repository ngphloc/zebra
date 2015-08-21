package es.uco.WOW.Utils;

import java.util.Vector;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * @version 1.0
 */

/**
 * NAME: Adaptive.
 * FUNCTION: This class contains all the neccesary functions to make the 
 *  adaptive calculations.
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class Adaptive {

	/**
	 * IRT model used to make the calculations.
	 * (Remain that this is used to choose the next 
	 * question of an adaptive test)
	 */
	private int irtModel = 3;

	/**
	 * Private constructor.
	 * To create a instance of this class we need to 
	 * specify the IRT Model that we will use.
	 */
	//private Adaptive()
	//{}

	/**
	 * Creates an Adaptive object setting the IRT
	 * model that it will use to make the calculations.
	 * @param aIrtModel Possible values are 1, 2 or 3.
	 */
	public Adaptive(final int aIrtModel) {
		this.irtModel = aIrtModel;
	}
	
	/**
	 * Calculates the value of the information function for a question.
	 * @param discrimination Indicates the discrimination of an item.
	 * @param difficulty Indicates the difficulty of an item.
	 * @param guess Indicates the guess of an item.
	 * @param estimatedTheta Indicates the estimated knowledge
	 * 		 of the student that has done the item
	 * @return double
	 */
	public double information(
			final double discrimination,
			final double difficulty,
			final double guess,
			final double estimatedTheta)
	{
		double info;
		double a = discrimination;
		double b = difficulty;
		double c = guess;
		double prob = probability(a, b, c, estimatedTheta);
		double deri = derived(a, b, c, estimatedTheta);

		if (prob != 99999 && deri != 99999) {
			double x = Math.pow(deri, 2);
			double y = prob * (1 - prob);

			if (y != 0) {
				info = (x / y);
			} else {
				info = 99999;
			}
		} else {
			info = 99999;
		}

		return info;
	}

	/**
	 * Calculates the probability function of theta (the knowledge
	 * of the student).
	 * @param discrimination Indicates the discrimination of an item.
	 * @param difficulty Indicates the difficulty of an item.
	 * @param guess Indicates the guess of an item.
	 * @param theta Indicates the knowledge of the student.
	 * @return double
	 */
	public double probability(
			final double discrimination,
			final double difficulty,
			final double guess,
			final double theta) {
	//{
		double a = discrimination;
		double b = difficulty;
		double c = guess;
		double prob;

		if (irtModel == 2) {
			c = 0;
		} else if (irtModel == 1) {
			c = 0;
			a = 1;
		}

		double x = 1 - c;
		double y = 1 + Math.exp((-1.7 * a * (theta - b)));

		if (y != 0) {
			if ((c + (x / y)) == 1) {
				prob = 0.9999;
			} else {
				prob = (c + (x / y));
			}
		} else {
			prob = 99999;
		}

		return prob;
	}

	
	/**
	 * Calculates the derivative function.
	 * @param discrimination Indicates the discrimination of an item.
	 * @param difficulty Indicates the difficulty of an item.
	 * @param guess Indicates the guess of an item.
	 * @param theta Indicates the knowledge of the student.
	 * @return double
	 */
	public double derived(
			final double discrimination,
			final double difficulty,
			final double guess,
			final double theta) {
	//{
		double deri;
		double a = discrimination;
		double b = difficulty;
		double c = guess;

		if (irtModel == 2) {
			c = 0;
		} else if (irtModel == 1) {
			c = 0;
			a = 1;
		}

		double x = 1.7 * a * (1 - c) * Math.exp(-1.7 * a * (theta - b));
		double y = Math.pow((1 + Math.exp(-1.7 * a * (theta - b))), 2);

		if (y != 0) {
			deri = (x / y);
		} else {
			deri = 99999;
		}

		return deri;
	}

	
	/**
	 * Calculates the sum of the information functions of the items
	 * that are in the Vector passed as argument.
	 * @param informationVector Contains in each position the value of 
	 *   the information funcion for an item.
	 * @return double
	 */
	public double Irr(final Vector informationVector) {
		double adds = 0;
		for (int i = 0; i < informationVector.size(); i++) {
			double aux = Double.valueOf(informationVector.get(i).toString()).doubleValue();
			if (aux != 99999) {
				adds += aux;
			}
		}
		return adds;
	}

	
	/**
	 * Calculates the standar error.
	 * @param irr Sum of the values of the information functions.
	 * @return double
	 */
	public double standardError(final double irr) {
		double standardError;

		if (irr >= 0) {
			if (Math.sqrt(irr) != 0) {
				standardError = (1 / Math.sqrt(irr));
			} else {
				standardError = 99999;
			}
		} else {
			standardError = 99999;
		}

		return standardError;
	}


	/**
	 * Estatistical auxiliar function used for calculate the estimated knowledge of the student.
	 * @param theta Indicates the knowledge of the student.
	 * @param discrimination Indicates the discrimination of an item.
	 * @param difficulty Indicates the difficulty of an item.
	 * @param guess Indicates the guess of an item.
	 * @param u Indicates if the item has been correctly answered. Take the 1 value if has been
	 *           correctly answered, 0 value else.
	 * @return double
	 */
	public double si(
			final double theta,
			final double discrimination,
			final double difficulty,
			final double guess,
			final double u) {
	//{
		double si;

		double prob = probability(discrimination, difficulty, guess, theta);
		double deri = derived(discrimination, difficulty, guess, theta);

		if (prob != 99999 && deri != 99999) {
			if (prob != 0 && prob != 1) {
				si = (u - prob) * (deri / (prob * (1 - prob)));
			} else {
				si = 99999;
			}
		} else {
			si = 99999;
		}

		return si;
	}

	
	/**
	 * Calculates the estimated theta value for the student.
	 * @param previousTheta Previous value of the knowledge of the student.
	 * @param siVector This is a vector where each position contains the value of the
	 *   'si()' function for a question of the test already showed to the student. 
	 * @param irr Contains the sum of the values of the information functions 
	 *   for all the questions already showed to the student. 
	 * @return double
	 */
	public double estimatedTheta(
			final double previousTheta,
			final Vector siVector, double irr)
	{
		double adds = 0;
		double aux;
		double theEstimatedTheta = previousTheta;

		for (int i = 0; i < siVector.size(); i++) {
			adds += Double.valueOf(siVector.get(i).toString()).doubleValue();
		}

		if (irr != 0) {
			aux = theEstimatedTheta + (adds / irr);
			if (aux > 4)
				aux = 4;

			if (aux < -4)
				aux = -4;

			theEstimatedTheta = aux;
		}

		return theEstimatedTheta;
	}

} // End of Adaptive class