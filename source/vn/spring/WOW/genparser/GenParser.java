/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * GenParser.java 1.0, November 23, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

import java.util.*;

/**
 * General parser that uses seperate modules allowing reuse of code
 * in parsing different languages.
 */
public class GenParser {
    private ParseInfo info = new ParseInfo();

    public GenParser() {
    }

    public void registerParseComponent(ParseComponent pc) {
        info.registerParseComponent(pc);
    }

    public ParseNode parse(String method, String program) throws ParserException {
        GenScanner scanner = new GenScanner(program, info);
        // build ParseList
        LinkedList tokens = new LinkedList();
        GenToken token;
        do {
            token = scanner.nextToken();
            tokens.add(token);
        } while (!token.getType().equals("endofprogram"));
        ParseList pl = new ParseList(tokens);
        // parse the specified method
        ParseNode result = null;
        try {
            result = info.parse(method, pl);
        } catch (ParserException e) {
            throw new ParserException(addTokenMessage(e.getMessage(), (GenToken)e.getInfo()), e.getInfo());
        }
        if (pl.valid() && !pl.current().getType().equals("endofprogram")) {
            if (program.indexOf("\n")>0) throw new ParserException(addTokenMessage("end of "+method+" expected", pl.current())); else
                                        throw new ParserException("end of "+method+" expected in '"+program+"'");
        }
        return result;
    }

    private String addTokenMessage(String s, GenToken token) {
        if (token == null) return s;
        if ((token.lcount == 0) && (token.ccount == 0)) return s;
        return s+", line "+token.lcount+" char "+token.ccount;
    }

/*
    public static void main(String[] args) throws ParserException, IOException {
        //prepare parser
        GenParser parser = new GenParser();
        parser.registerParseComponent(new PCComment());
        parser.registerParseComponent(new PCLAGIdentifier());
        parser.registerParseComponent(new PCCommon());
        parser.registerParseComponent(new PCLAG());

        //read input
        BufferedReader bin = new BufferedReader(new FileReader(args[0]));
        StringBuffer r = new StringBuffer("");
        String s;
        do {
            s = bin.readLine();
            if (s != null) r.append(s+"\n");
        } while (s != null);

        //parse program
        ParseNode node = parser.parse("PROGRAM", r.toString());
        System.out.println(node);
    }
*/
}