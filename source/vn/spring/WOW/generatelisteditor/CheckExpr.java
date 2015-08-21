/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * CheckExpr.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.generatelisteditor;

import vn.spring.WOW.exceptions.ParserException;
import vn.spring.WOW.parser.Parser;

// event handling
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

//utils
import java.awt.Color;

import javax.swing.text.JTextComponent;

class CheckExpr implements DocumentListener {
    //private boolean reqOk = false;
    private JTextComponent textComponent;
    private Parser parser = null;

    public CheckExpr(JTextComponent tComponent)
    {
        this.textComponent = tComponent;
        GLEVariableLocator glevl = new GLEVariableLocator(GenerateListData.concepts);
        parser = new Parser(glevl);
    }

    // Check the requirement for correctness
    public void changedUpdate(DocumentEvent e) {}

    public void insertUpdate(DocumentEvent e) {
        textValueChanged();
    }

    public void removeUpdate(DocumentEvent e) {
        textValueChanged();
    }

    public void textValueChanged () {
        String expr = textComponent.getText();
        boolean ok = check(expr);
        textComponent.setForeground(ok?Color.black:Color.red);
    }

    public boolean check(String expr) {
        try {
            parser.parse(expr);
        } catch (ParserException e) {
            return false;
        }
        return true;
    }

/*      String token;
        String newString = "";
        String newExpressionString = this.textComponent.getText();
        StringTokenizer boolConverter = new StringTokenizer(newExpressionString, "=()\n ", true);
        while (boolConverter.hasMoreTokens()) {
            token = boolConverter.nextToken();
            if (token.equals("NOT")) {
                newString = newString + "!";
            }
            else if (token.equals("AND")) {
                newString = newString + "&&";
            }
            else if (token.equals("OR")) {
                newString = newString + "||";
            }
            else if (token.equals("TRUE")) {
                newString = newString + "1";
            }
            else if (token.equals("FALSE")) {
                newString = newString + "0";
            }
            else if (token.equals("=")) {
                newString = newString + "==";
            }
            else if (token.equals("\n")) {
                newString = newString + " ";
            }
            else newString = newString + token;
        }
        reqOk = exprParser.CheckReqExpr(newString);
        if (reqOk) {
            this.textComponent.setForeground(Color.black);
        }
        else {
            this.textComponent.setForeground(Color.red);
        }
*/
}
