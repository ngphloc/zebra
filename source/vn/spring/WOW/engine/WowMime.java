/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * WowMime.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.engine;

import java.util.Hashtable;


public class WowMime {
    public static Hashtable mimehash;

    public WowMime() {
        mimehash = new Hashtable();

	// most of this is taken from the Tomcat web.xml file

        mimehash.put("txt", "text/plain");
        mimehash.put("html", "text/html");
        mimehash.put("htm", "text/html");

        mimehash.put("xml", "text/xml");
        mimehash.put("xhtml", "text/xhtml");
        mimehash.put("xhtm", "text/xhtml");
      	mimehash.put("frm","text/xhtml");

        //added by @Barend, @Loc Nguyen on 14 feb 2008
        mimehash.put("smil", "application/smil");
        mimehash.put("smi", "application/smil");
        //end added by @Barend, @Loc Nguyen on 14 feb 2008

        mimehash.put("css", "text/css");
        mimehash.put("sgml", "text/sgml");
        mimehash.put("sgm", "text/sgml");

        mimehash.put("wml", "text/vnd.wap.wml");
        mimehash.put("wmls", "text/vnd.wap.wmlscript");

        // tex
        mimehash.put("dvi", "application/x-dvi");
        mimehash.put("tex", "application/x-tex");

        // MS-office
        mimehash.put("doc", "application/msword");
        mimehash.put("xls", "application/ms-excel");
        mimehash.put("ppt", "application/ms-powerpoint");

        // pdf etc.
        mimehash.put("pdf", "application/pdf");
        mimehash.put("ps", "application/postscript");
        mimehash.put("eps", "application/postscript");
        mimehash.put("ai", "application/postscript");

        // binary files
        mimehash.put("bin", "octet-stream");
        mimehash.put("dms", "octet-stream");
        mimehash.put("lha", "octet-stream");
        mimehash.put("lzh", "octet-stream");
        mimehash.put("exe", "octet-stream");

        // class files
        mimehash.put("class", "octet-stream");

        // zips
        mimehash.put("gz", "application/x-gzip");
        mimehash.put("zip", "application/zip");

        // special stuff
        mimehash.put("swf", "application/x-shockware-flash");

        // images
        mimehash.put("gif", "image/gif");
        mimehash.put("bmp", "image/bmp");
        mimehash.put("jpeg", "image/jpeg");
        mimehash.put("jpg", "image/jpeg");
        mimehash.put("jpe", "image/jpeg");
        mimehash.put("tif", "image/tif");
        mimehash.put("tiff", "image/tif");
        mimehash.put("xbm", "image/x-xbitmap");
        mimehash.put("xpm", "image/x-xpixmap");

	// windows media
	mimehash.put("wmv", "video/x-ms-wmv");

	// others from tomcat
	mimehash.put("abs", "audio/x-mpeg");
	mimehash.put("aif", "audio/x-aiff");
	mimehash.put("aifc", "audio/x-aiff");
	mimehash.put("aiff", "audio/x-aiff");
	mimehash.put("aim", "application/x-aim");
	mimehash.put("art", "image/x-jg");
	mimehash.put("asf", "video/x-ms-asf");
	mimehash.put("asx", "video/x-ms-asf");
	mimehash.put("au", "audio/basic");
	mimehash.put("avi", "video/x-msvideo");
	mimehash.put("avx", "video/x-rad-screenplay");
	mimehash.put("bcpio", "application/x-bcpio");
	mimehash.put("bin", "application/octet-stream");
	mimehash.put("body", "text/html");
	mimehash.put("cdf", "application/x-cdf");
	mimehash.put("cer", "application/x-x509-ca-cert");
	mimehash.put("class", "application/java");
	mimehash.put("cpio", "application/x-cpio");
	mimehash.put("csh", "application/x-csh");
	mimehash.put("dib", "image/bmp");
	mimehash.put("dtd", "text/plain");
	mimehash.put("dv", "video/x-dv");
	mimehash.put("etx", "text/x-setext");
	mimehash.put("exe", "application/octet-stream");
	mimehash.put("gtar", "application/x-gtar");
	mimehash.put("gz", "application/x-gzip");
	mimehash.put("hdf", "application/x-hdf");
	mimehash.put("hqx", "application/mac-binhex40");
	mimehash.put("ief", "image/ief");
	mimehash.put("jad", "text/vnd.sun.j2me.app-descriptor");
	mimehash.put("jar", "application/java-archive");
	mimehash.put("java", "text/plain");
	mimehash.put("jnlp", "application/x-java-jnlp-file");
	mimehash.put("js", "text/javascript");
	mimehash.put("kar", "audio/x-midi");
	mimehash.put("m3u", "audio/x-mpegurl");
	mimehash.put("mac", "image/x-macpaint");
	mimehash.put("man", "application/x-troff-man");
	mimehash.put("me", "application/x-troff-me");
	mimehash.put("mid", "audio/x-midi");
	mimehash.put("midi", "audio/x-midi");
	mimehash.put("mif", "application/x-mif");
	mimehash.put("mov", "video/quicktime");
	mimehash.put("movie", "video/x-sgi-movie");
	mimehash.put("mp1", "audio/x-mpeg");
	mimehash.put("mp2", "audio/x-mpeg");
	mimehash.put("mp3", "audio/x-mpeg");
	mimehash.put("mpa", "audio/x-mpeg");
	mimehash.put("mpe", "video/mpeg");
	mimehash.put("mpeg", "video/mpeg");
	mimehash.put("mpega", "audio/x-mpeg");
	mimehash.put("mpg", "video/mpeg");
	mimehash.put("mpv2", "video/mpeg2");
	mimehash.put("ms", "application/x-wais-source");
	mimehash.put("nc", "application/x-netcdf");
	mimehash.put("oda", "application/oda");
	mimehash.put("pbm", "image/x-portable-bitmap");
	mimehash.put("pct", "image/pict");
	mimehash.put("pgm", "image/x-portable-graymap");
	mimehash.put("pic", "image/pict");
	mimehash.put("pict", "image/pict");
	mimehash.put("pls", "audio/x-scpls");
	mimehash.put("png", "image/png");
	mimehash.put("pnm", "image/x-portable-anymap");
	mimehash.put("pnt", "image/x-macpaint");
	mimehash.put("ppm", "image/x-portable-pixmap");
	mimehash.put("psd", "image/x-photoshop");
	mimehash.put("qt", "video/quicktime");
	mimehash.put("qti", "image/x-quicktime");
	mimehash.put("qtif", "image/x-quicktime");
	mimehash.put("ras", "image/x-cmu-raster");
	mimehash.put("rgb", "image/x-rgb");
	mimehash.put("rm", "application/vnd.rn-realmedia");
	mimehash.put("roff", "application/x-troff");
	mimehash.put("rtf", "application/rtf");
	mimehash.put("rtx", "text/richtext");
	mimehash.put("sh", "application/x-sh");
	mimehash.put("shar", "application/x-shar");
	mimehash.put("smf", "audio/x-midi");
	mimehash.put("snd", "audio/basic");
	mimehash.put("src", "application/x-wais-source");
	mimehash.put("sv4cpio", "application/x-sv4cpio");
	mimehash.put("sv4crc", "application/x-sv4crc");
	mimehash.put("swf", "application/x-shockwave-flash");
	mimehash.put("t", "application/x-troff");
	mimehash.put("tar", "application/x-tar");
	mimehash.put("tcl", "application/x-tcl");
	mimehash.put("texi", "application/x-texinfo");
	mimehash.put("texinfo", "application/x-texinfo");
	mimehash.put("tr", "application/x-troff");
	mimehash.put("tsv", "text/tab-separated-values");
	mimehash.put("ulw", "audio/basic");
	mimehash.put("ustar", "application/x-ustar");
	mimehash.put("xwd", "image/x-xwindowdump");
	mimehash.put("wav", "audio/x-wav");
	mimehash.put("wbmp", "image/vnd.wap.wbmp");
	mimehash.put("wml", "text/vnd.wap.wml");
	mimehash.put("wmlc", "text/vnd.wap.wmlc");
	mimehash.put("wmls", "text/vnd.wap.wmls");
	mimehash.put("wmlscript", "application/vnd.wap.wmlscript");
	mimehash.put("wrl", "x-world/x-vrml");
	mimehash.put("Z", "application/x-compress");
	mimehash.put("z", "application/x-compress");
	mimehash.put("zip", "application/zip");

    }


    public String getMimeByExt(String ext) {
        return (String) mimehash.get(ext);
    }

    public String getMime(String name) {
        if (name.endsWith("/")) {
            return "text/html";
        } else {
            return getMimeByExt(name.substring(name.lastIndexOf('.') + 1));
        }
    }
}
