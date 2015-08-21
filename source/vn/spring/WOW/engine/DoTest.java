/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * DoTest.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import vn.spring.WOW.Utils.*;
import java.io.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
/**
 * This class activates a filter which generates a HTML-form
 * with a test that a user can fill in.
 * The results of the test will be checked by EvaluateTest.
 */

public class DoTest {

    private StringBuffer sb;

    public Resource getOutput(Profile profile, String login, String pathinfo) {
        try {
            sb = new StringBuffer();
            String test=pathinfo;
            test = test.substring(0, test.indexOf("|"));
                FilterTest filtertest = new FilterTest(login, profile, test, sb);

                // dump the test to out.
                filtertest.PrintQuestions(WOWStatic.config.Get("CONTEXTPATH")+"/ViewGet"+test);
        } catch (Exception e) {
                System.out.println("DoTest: IO error");
        }
        InputStream insb = new ByteArrayInputStream(sb.toString().getBytes());
        return new Resource(insb, new ResourceType("text/html"));
    }

}
