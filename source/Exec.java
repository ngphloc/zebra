/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Exec.java 1.0, August 16, 2005
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import vn.spring.WOW.WOWStatic;

public class Exec extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Hashtable handlers = null;

    public void init() {
        handlers = new Hashtable();
        handlers.put("authordir", new AuthorDirHandler());
    }

    public void service(HttpServletRequest req, HttpServletResponse res)
           throws ServletException, IOException {
        Hashtable resinfo = new Hashtable();
        try {
            ObjectInputStream ois = new ObjectInputStream(req.getInputStream());
            Hashtable reqinfo = (Hashtable) ois.readObject();
            ois.close();
            if (!reqinfo.containsKey("name")) {
                System.out.println("Invalid request table in Exec call");
                return;
            }
            ExecHandler handler = (ExecHandler)handlers.get((String)reqinfo.get("name"));
            if (handler == null) return;
            resinfo = handler.handle(reqinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectOutputStream oos = new ObjectOutputStream(res.getOutputStream());
        oos.writeObject(resinfo);
    }

    private static interface ExecHandler {
        public Hashtable handle(Hashtable reqinfo);
    }

    private static class AuthorDirHandler implements ExecHandler {
        public AuthorDirHandler() {}
        public Hashtable handle(Hashtable reqinfo) {
            Vector files = new Vector();
            Hashtable resinfo = new Hashtable();
            resinfo.put("files", files);
            File root = new File(WOWStatic.WOWroot.toString()+WOWStatic.AUTHORFILESPATH);
            File dir = new File(root, (String)reqinfo.get("dir"));
            File[] filearray = dir.listFiles();
            for (int i=0;i<filearray.length;i++) {
                if (!filearray[i].isDirectory()) files.add(filearray[i].getName());
            }
            return resinfo;
        }
    }

}