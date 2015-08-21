package es.uco.WOW.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * @version 1.0
 */

/**
 * NAME: UtilZip
 * FUNCTION: This class manages all the options about zipping and unzipping
 * files and folders
 * LAST MODIFICATION: 01-11-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class UtilZip {

	/**
	 * Deflates a byte array
	 * @param byteArray Byte array to deflate
	 * @return the result of deflating the byte array
	 * @throws Exception Error deflating byte array
	 */
	public static byte [] deflate(final byte [] byteArray) throws Exception {
		byte [] result = null;
		if (byteArray != null) {
			byte [] theReturn = new byte[64];
			Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION);
			deflater.setInput(byteArray);
			deflater.finish();
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			int readedBytes = 0;
			while ((readedBytes = deflater.deflate(theReturn)) != 0) {
				bOut.write(theReturn, 0, readedBytes);
			}
			bOut.flush();
			result = bOut.toByteArray();
		}
		return result;
	}

	/**
	 * Deflates a String object
	 * @param s String to deflate
	 * @return the result of deflating the String as a byte array
	 * @throws Exception Error deflating string
	 */
	public static byte [] deflate(final String s) throws Exception {
		byte output [] = null;
		if (s != null) {
			output = deflate(s.getBytes());
		}
		return output;
	}

	/**
	 * Inflates a String object
	 * @param s String to inflate
	 * @return the result of inflating the string as a byte array
	 * @throws Exception Error inflating string
	 */
	public static byte [] inflate(final String s) throws Exception {
		byte [] output = null;
		if (s != null) {
			output = inflate(s.getBytes());
		}
		return output;
	}

	/**
	 * Inflates a byte array
	 * @param byteArray byte array to inflate
	 * @return the result of inflating the byte array
	 * @throws Exception Error inflating byte array
	 */
	public static byte [] inflate(final byte [] byteArray) throws Exception {
		byte [] result = null;
		if (byteArray != null) {
			byte [] theReturn = new byte[64];
			Inflater inflater = new Inflater();
			inflater.setInput(byteArray);
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			int readedBytes = 0;
			while ((readedBytes = inflater.inflate(theReturn)) != 0) {
				bOut.write(theReturn, 0, readedBytes);
			}
			bOut.flush();
			result = bOut.toByteArray();
		}
		return result;
	}

	/**
	 * Unzip the file received as parameter
	 * @param fileObj The file to unzip
	 * @throws IOException Input/Ouput error
	 */
	public static void unzipFile(final File fileObj) throws IOException {
		unzipFile(fileObj, fileObj.getParentFile());
	}

	/**
	 * Unzips the file received as parameter in the folder received
	 * as parameter
	 * @param fileObj File to unzip
	 * @param objRootFolder Folder where the zip file will be unzipped
	 * @throws IOException Input/Output errro
	 */
	public static void unzipFile(final File fileObj, final File objRootFolder) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(fileObj));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry e;

		while ((e = zin.getNextEntry()) != null) {
			unzip(zin, objRootFolder.getAbsolutePath() + File.separator + e.getName());
		}
		zin.close();
	}
	
	/**
	 * Unzip a entry of a zip file
	 * @param zin Zip entry from a zip file 
	 * @param s Path to the file or folder where the unzipped data will be located
	 * @throws IOException
	 */
	public static void unzip(final ZipInputStream zin, final String s) throws IOException {
		File objFile = new File(s);
		if (s.endsWith("/") || s.endsWith("\\")) {
			// Is a folder - Creates the folder
			objFile.mkdirs();
		}
		if (objFile.isDirectory()) {
			return;
		}
		if (objFile.exists()) {
			objFile.delete();
		}
		objFile.getParentFile().mkdirs();
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(s));
		byte [] b = new byte[512];
		int len = 0;
		while ((len = zin.read(b)) != -1) {
			out.write(b, 0, len);
		}
		out.close();
	}
	
	
	/**
	 * Unzip a entry from a zip file as a String
	 * @param objZipFile References to zip file
	 * @param strEntryName name of the zip entry
	 * @return String with the result of unzip the entry
	 */
	public static String unzipEntryAsString(final File objZipFile, final String strEntryName) {
		InputStream is = unzipEntry(objZipFile, strEntryName);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			int len = 0;
			while ((len = is.read()) != -1) {
				out.write(len);
			}
			out.close();
		} catch (Exception e) {}
		return new String(out.toByteArray());
	}

	/**
	 * Unzips a entry from a file as a ZipInputStream
	 * @param zipFileObj References to the zip file
	 * @param strEntryName Name of the zip entry
	 * @return ZipInputStream object witht the entry
	 */
	public static ZipInputStream unzipEntry(final File zipFileObj, final String strEntryName) {
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(zipFileObj));
			ZipInputStream zin = new ZipInputStream(in);
			ZipEntry e;
			boolean blnContinue = ((e = zin.getNextEntry()) != null);
			while (blnContinue) {
				if (e.getName().equals(strEntryName)) {
					blnContinue = false;
					return zin;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

} // End of UtilZip class