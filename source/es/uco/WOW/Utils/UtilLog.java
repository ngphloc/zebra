package es.uco.WOW.Utils;

import vn.spring.WOW.WOWStatic;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * @version 1.0
 */

/**
 * NAME: UtilLog
 * FUNCTION: This class manages all the options about the log
 * TO Use the log, you must define the LOG_LEVEL variable
 * in the wowconfig.xml configuration file.
 * LAST MODIFICATION: 01-11-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public final class UtilLog {

	/**
	 * Debug level. Prints all
	 * @see vn.spring.WOW.config.WowConfig wowconfig.xml, property LOG_LEVEL
	 */
	public static final short DEBUG_LEVEL = 2;
	/**
	 * Normal level. No prints debug messages
	 * @see vn.spring.WOW.config.WowConfig wowconfig.xml, property LOG_LEVEL
	 */
	public static final short NORMAL_LEVEL = 1;
	/**
	 * Normal level. No prints any message
	 * @see vn.spring.WOW.config.WowConfig wowconfig.xml, property LOG_LEVEL
	 */
	public static final short NOTHING_LEVEL = 0;
	
	/**
	 * Log level in the configuration file
	 * @see wowconfig.xml, property LOG_LEVEL
	 */
	private static final String value = WOWStatic.config.Get("LOG_LEVEL");
	/**
	 * Log level to apply
	 */
	private static short LOG_LEVEL = -1;
	
	/**
	 * Writes a message in the log with DEBUG_LEVEL 
	 * @param message Message to write in the log
	 */
	public static void toLog(final String message){
		toLog(message, DEBUG_LEVEL);
	}
	/**
	 * Writes a message in the log with a certain level  
	 * @param message Message to write in the log
	 * @param logLevel Level of log
	 */
	public static void toLog(final String message, final short logLevel){
		if (getLogLevel() >= logLevel)  {
			System.out.println(message);
		}
	}
	/**
	 * Writes a exception in the log with DEBUG_LEVEL 
	 * @param e Exception to write in the log
	 */
	public static void writeException(final Exception e){
		writeException(e, DEBUG_LEVEL);
	}

	/**
	 * Writes a exception in the log with a certain level  
	 * @param e exception to write in the log
	 * @param logLevel Level of log
	 */
	public static void writeException(final Exception e, final short logLevel){
		if (getLogLevel() >= logLevel)  {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Returns the current log level
	 * @return Current log level
	 */
	private static short getLogLevel() {
		if (LOG_LEVEL < 0 && value != null) {
			try {
				LOG_LEVEL = Short.parseShort(value);
			} catch (Exception e) {
				LOG_LEVEL = NORMAL_LEVEL;
			}
		}
		return LOG_LEVEL;
	}

} // Ends of UtilLog class