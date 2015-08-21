
/*
 * Copyright (c) 1995-1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies. Please refer to the file "copyright.html"
 * for further important copyright and licensing information.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

import java.util.*;
import java.text.*;

public class DecimalFormatDemo {

   static public void customFormat(String pattern, double value ) {
      DecimalFormat myFormatter = new DecimalFormat(pattern);
      String output = myFormatter.format(value);
      System.out.println(value + "  " + pattern + "  " + output);
   }

   static public void localizedFormat(String pattern, double value, 
                                      Locale loc ) {
      NumberFormat nf = NumberFormat.getNumberInstance(loc);
      DecimalFormat df = (DecimalFormat)nf;
      df.applyPattern(pattern);
      String output = df.format(value);
      System.out.println(pattern + "  " + output + "  " + loc.toString());
   }

   static public void main(String[] args) {

      customFormat("###,###.###", 123456.789);
      customFormat("###.##", 123456.789);
      customFormat("000000.000", 123.78);
      customFormat("$###,###.###", 12345.67);
      customFormat("\u00a5###,###.###", 12345.67);

      Locale currentLocale = new Locale("en", "US");

      DecimalFormatSymbols unusualSymbols = 
         new DecimalFormatSymbols(currentLocale);
      unusualSymbols.setDecimalSeparator('|');
      unusualSymbols.setGroupingSeparator('^');
      String strange = "#,##0.###";
      DecimalFormat weirdFormatter = new DecimalFormat(strange, unusualSymbols);
      weirdFormatter.setGroupingSize(4);
      String bizarre = weirdFormatter.format(12345.678);
      System.out.println(bizarre);

      Locale[] locales = {
         new Locale("en", "US"),
         new Locale("de", "DE"),
         new Locale("fr", "FR")
      };

      for (int i = 0; i < locales.length; i++) {
         localizedFormat("###,###.###", 123456.789, locales[i]);
      }

   }
}

