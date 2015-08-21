package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.spring.WOW.WOWStatic;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: GetIconAndItemsPath 
 * FUNCTION: Servlet que recibe el nombre de usuario
 * del autor y su contrase�a y analiza el fichero wowconfig.xml para buscar el
 * path del direcctorio que contiene los iconos usados por la herramienta
 * TestEditor. 
 * LAST MODIFICATION: 06-02-2008
 */
public class GetIconAndItemsPath extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Path of the items directory
	 */
	private static String itemsPath = null;
	
	/**
	 * Path of the TestEditor icons directory
	 */
	private static String iconTestEditorPath = null; 
	
	
	public void init(ServletConfig config) throws ServletException 
	{
		if(itemsPath == null)
		{
			itemsPath = WOWStatic.config.Get("XMLROOT");
			
			if (itemsPath.lastIndexOf("/") != -1)
				itemsPath = itemsPath.substring(itemsPath.lastIndexOf("/"));
			else if (itemsPath.lastIndexOf("\\") != -1)
				itemsPath = itemsPath.substring(itemsPath.lastIndexOf("\\"));

			itemsPath = itemsPath.concat(WOWStatic.config.Get("itemspath"));
		}
		
		if(iconTestEditorPath == null)
			iconTestEditorPath = WOWStatic.config.Get("testeditoriconpath");

		super.init(config);
	}

	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		resp.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		out.println(iconTestEditorPath);
		out.println(itemsPath);

		out.flush();
		out.close();
	}
}
