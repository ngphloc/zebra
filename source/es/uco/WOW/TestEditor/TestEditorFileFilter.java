package es.uco.WOW.TestEditor;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;

/**
 * <p>Title: Wow! TestEditor</p> <p>Description: Herramienta para la edicion
 * de preguntas tipo test adaptativas </p> <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * @version 1.0
 */

/**
 * Esta clase se encarga de seleccionar s�lo los ficheros de una extensi�n
 * determinada cuando se muestran las pantallas de selecci�n de archivos.
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 * @version 1.0
 */
public class TestEditorFileFilter extends FileFilter {

	/**
	 * Filtros existentes
	 */
	private Hashtable filters = null;

	/**
	 * Descripci�n de la extensi�n
	 */
	private String description = "";

	/**
	 * Descripci�n completa de la extensi�n
	 */
	private String completeDescription = null;

	/**
	 * Fija si se usan las descripciones en la descripci�n
	 */
	private boolean useExtensions = true;

	/**
	 * Constructor sin par�metros
	 */
	public TestEditorFileFilter() {
		getFiltros(0);
	} // Fin del constructor de FiltroFichero

	/**
	 * Crea un filtro de ficheros para un determinado tipo nada m�s
	 * 
	 * @param extension
	 *           Extensi�n que se desea restringir con este filtro
	 */
	public TestEditorFileFilter(String extension) {
		this(extension, null);
	} // Fin del constructor de FiltroFichero

	/**
	 * Crea un filtro de ficheros para un determinada extensi�n, pas�ndole su
	 * descripci�n. No es necesaria el '.' para la extensi�n. Si se a�ade, se
	 * ignorar�.
	 * 
	 * Ejemplo: new FiltroFichero("jpg", "Im�genes JPEG");
	 * 
	 * @param extension
	 *           Extensi�n que se desea restringir con este filtro
	 * @param aDescription
	 *           Descripci�n de la extensi�n a restringir
	 */
	public TestEditorFileFilter(String extension, String aDescription) {
		getFiltros(0);
		if (extension != null) {
			addExtension(extension);
		}
		if (aDescription != null) {
			setDescription(aDescription);
		}
	} // Fin del constructor de FiltroFichero

	/**
	 * Crea un filtro de ficheros para un array de extensiones No es necesaria el
	 * '.' para la extension. Si se a�ade, se ignorar�.
	 * 
	 * Example: new FiltroFichero(String {"gif", "jpg"});
	 * 
	 * @param theFilters
	 *           Vector de extensiones
	 */
	public TestEditorFileFilter(String [] theFilters) {
		this(theFilters, null);
	} // Fin del constructor de FiltroFichero

	/**
	 * Crea un filtro de ficheros para un array de extensiones, con una
	 * description. No es necesaria el '.' para las extensiones. Si se a�aden, se
	 * ignorar�.
	 * 
	 * Ejemplo: new FiltroFichero(String {"gif", "jpg"}, "Gif and JPG Images");
	 * 
	 * @param theFilters
	 *           Vector de extensiones
	 * @param aDescription
	 *           Descripci�n com�n a todas las extensiones
	 */
	public TestEditorFileFilter(String [] theFilters, String aDescription) {
		getFiltros(0);

		for (int i = 0; i < theFilters.length; i++) {
			addExtension(theFilters[i]);
		}

		if (aDescription != null) {
			setDescription(aDescription);
		}
	} // Fin del constructor de FiltroFichero

	/**
	 * Devuelve los filtros. Si no est�n creados, se crean
	 * 
	 * @param numero
	 *           N�mero de extensiones que contendr� el fitro
	 */
	protected Hashtable getFiltros(int numero) {
		if (this.filters == null) {
			if (numero == 0) {
				this.filters = new Hashtable();
			} else {
				this.filters = new Hashtable(numero);
			}
		}

		return (this.filters);
	} // Fin de FiltroFichero.getFiltros()

	/**
	 * Devuelve verdadero si este fichero debe ser mostrado en el panel de
	 * ficheros. Falso si no. Los ficheros que comienzan por un punto '.' ser�n
	 * ignorados(ficheros ocultos de UNIX).
	 * 
	 * @param f
	 *           Ruta hacia un fichero
	 */
	public boolean accept(File f) {
		boolean retorno = false;

		if (f != null) {
			if (f.isDirectory()) {
				retorno = true;
			}
			String extension = getExtension(f);
			if (extension != null && filters.get(getExtension(f)) != null) {
				retorno = true;
			}
		}
		return retorno;
	} // Fin de FiltroFichero.accept()

	/**
	 * Devuelve la extension de un fichero, dado su nombre completo
	 * 
	 * @param f
	 *           Ruta hacia un fichero
	 */
	public String getExtension(File f) {
		String retorno = null;
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				retorno = filename.substring(i + 1).toLowerCase();
			}
		}
		return retorno;
	} // Fin de FiltroFichero.getExtension()

	/**
	 * A�ade una extensi�n al conjunto de extensiones, dado su nombre completo
	 * 
	 * @param extension
	 *           Extensi�n a a�adir al conjunto de extensiones
	 */
	public void addExtension(String extension) {
		getFiltros(5);
		filters.put(extension.toLowerCase(), this);
		completeDescription = null;
	} // Fin de FiltroFichero.addExtension()

	/**
	 * Devuelve la description de este filtro, para mostrar en el combo del
	 * JFileChooser
	 */
	public String getDescription() {

		if (completeDescription == null) {
			if (description == null || isExtensionListInDescription()) {
				completeDescription = description == null ? "(" : description + " (";
				// Construyo la description desde la lista de extensiones
				Enumeration extensions = filters.keys();
				if (extensions != null) {
					completeDescription += "." + (String) extensions.nextElement();
					while (extensions.hasMoreElements()) {
						completeDescription += ", " + (String) extensions.nextElement();
					}
				}
			}
			completeDescription += ")";
		} else {
			completeDescription = description;
		}
		return completeDescription;
	} // Fin de FiltroFichero.getDesciption()

	/**
	 * Fija la description de este filtro, para mostrar en el combo del
	 * JFileChooser
	 * 
	 * Ejemplo: filtro.setDescription("Gif and JPG Images");
	 * 
	 * @param aDescription
	 *           Descripci�n que tendr�n los ficheros que restrinja el filtro
	 */
	public void setDescription(String aDescription) {
		this.description = aDescription;
		completeDescription = null;
	} // Fin de FiltroFichero.setDescription()

	/**
	 * Fija si la lista de extensiones debe aparecer en la description que se lee
	 * en el combo del JFileChooser.
	 * 
	 * @param b
	 *           Valor que indica si debe aparecer la lista de extensiones en el
	 *           combo
	 */
	public void setExtensionListInDescription(boolean b) {
		useExtensions = b;
		completeDescription = null;
	} // Fin de FiltroFichero.setExtensionListInDescription()

	/**
	 * Devuelve si la lista de extensiones debe aparecer en la description que se
	 * lee en el combo del JFileChooser.
	 */
	public boolean isExtensionListInDescription() {
		return useExtensions;
	} // Fin de FiltroFichero.isExtensionListInDescription()

	/**
	 * Le a�ade la extension al final del fichero, si procede
	 * 
	 * @param nombre
	 *           Nombre de un fichero con o sin extensi�n
	 * @param extension
	 *           extensi�n que debe tener el nombre del fichero
	 */
	public static String ponerExtension(String nombre, String extension) {
		String retorno = "";
		int index = 0;

		// Si ya la tiene al final, no se la pongo
		if (nombre.lastIndexOf(extension) != -1) {
			if (nombre.lastIndexOf(extension) == nombre.length() - extension.length()) {
				retorno = nombre;
			} else {
				// �Tiene el punto la extension?
				index = extension.lastIndexOf(".");
				if ((index != -1) && (index < extension.length() - 1)) {
					extension = extension.substring(index + 1);
				}
				extension = extension.toLowerCase();
				retorno = nombre + "." + extension;
			}
		} else {
			// �Tiene el punto la extension?
			index = extension.lastIndexOf(".");
			if ((index != -1) && (index < extension.length() - 1)) {
				extension = extension.substring(index + 1);
			}
			extension = extension.toLowerCase();
			retorno = nombre + "." + extension;
		}
		return retorno;
	} // Fin de FiltroFichero.ponerExtension()

}
