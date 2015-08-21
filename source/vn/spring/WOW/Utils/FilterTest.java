/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * FilterTest.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Utils;

import vn.spring.WOW.engine.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Random;
import org.w3c.dom.*;

public class FilterTest {

    private String login, FullDocName;
    private Profile profile;
    private StringBuffer sb;


    private String Opening="{{";
    private String Closing="}}";

    private int TotalQuestions; // Total number of questions in file
    private int QuestionsToAsk; // Number of questions to ask
    private String Title=null;  // Title of the page
    private String Knowledge=null; // Knowledge gained
    private Vector Questions=new Vector(5,5); // vector in which questions + extra data is saved
    private boolean Verbose;
    private boolean Rand;
    private Random R;
    private Vector alist; /* vector of integer arrays with indices of answers */
    public String testfile = null;
    public String concname = null;
    private long id = 0;

    public FilterTest(String login, Profile profile, String smallDocName, StringBuffer sb) {

    this.login = login;
    this.profile = profile;
    ConceptDB cdb = WOWStatic.DB().getConceptDB();
    try {
        concname = cdb.getLinkedConcept(smallDocName.substring(1));
    } catch(Exception e) {System.out.println(e);}
    //?if starts with http or ftp
    if (smallDocName.startsWith("/file:")) smallDocName=smallDocName.substring(6);
    this.FullDocName = WOWStatic.config.Get("WEBROOT")+smallDocName;
    this.sb = sb;
    alist=new Vector(5,5);
    int i,j;
    ProfileDB pdb = WOWStatic.DB().getProfileDB();

    try {
        id = pdb.findProfile(login);
    } catch(Exception e) {System.out.println(e);}
    XmlReader reader = null;
    try {
        reader = new XmlReader(FullDocName, true);
    } catch (Exception e) {
        System.out.println("Cannot read the file "+FullDocName+". Please notify the author.");
    }
    testfile = WOWStatic.config.Get("XMLROOT")+"/profile/test_"+id;
    Element el = reader.getRoot();
    fillArrays(el);
    Question q;
    // make sure we have the correct text for the embedded links.
    for (i = 0; i < Questions.size(); ++i) {

        // filter possible links, to ensure the correct link colors.
        q = (Question)Questions.elementAt(i);
        q.setQuestion(FilterLinks(q.getQuestion()));
        Vector V = q.getBadAnswers();
        for (j=0; j < V.size(); ++j)
        V.setElementAt(FilterLinks((String)V.elementAt(j)), j);
        V = q.getGoodAnswers();
        for (j=0; j < V.size(); ++j)
        V.setElementAt(FilterLinks((String)V.elementAt(j)), j);
        V = q.getBadFeedback();
        for (j=0; j < V.size(); ++j)
        V.setElementAt(FilterLinks((String)V.elementAt(j)), j);
        V = q.getGoodFeedback();
        for (j=0; j < V.size(); ++j)
        V.setElementAt(FilterLinks((String)V.elementAt(j)), j);
    }
    }

    public String HtmlConvert(String tempstr) {
    /* pre:  there is a string which might contain {{ }}
       post: the string no longer contains any of the Opening or Closing markers.
       They are converted to < and > */

    //$ the positions of the opening and closing markers
    int openpos;

    //$ convert String to StringBuffer so we can use replace
    StringBuffer tempbuf=new StringBuffer(tempstr);
    while (((openpos=tempstr.indexOf(Opening)) != -1) && ((tempstr.indexOf(Closing)) != -1)) {
        // another approach has to be taken
        // jdk1.1 does not permit StringBuffer.replace(int,int,String);

        String begin = tempstr.substring(0, openpos);
        String end = tempstr.substring(openpos+1);

        int endopenpos = end.indexOf(Closing);
        String closebegin = end.substring(0, endopenpos);
        String closeend = end .substring(endopenpos +1);

        tempstr = begin + "<"+closebegin+">"+closeend;
    }
    //$ return the converted string
    return tempbuf.toString();
    }


    public void getReason(NodeList nodelist, int question,boolean correct) {
    /* pre:  nodelist contains the answer of the questions, question the number of the
       active question, and correct is true if the answer is a correct one.
       post: the reasons are stored in the Questions vector
    */

    //$ we assume that nodelist.getLength()=1, since there can be
    //$ only one "explain" block
    if (nodelist.getLength() == 1) {
        Node current=nodelist.item(0);
        String nodename=current.getNodeName();
        String nodevalue=current.getNodeValue();
        //$ the #text part is the explanation
        if (nodename=="#text") {
            //$ if the nodevalue exists then trim it
            if (nodevalue != null)
            nodevalue=nodevalue.trim();
            //$ if the string contains data and it is text, it must be a answer
            if (nodevalue.length() > 0) {
            //nodevalue=HtmlConvert(nodevalue);
            ((Question) Questions.elementAt(question)).addFeedback(nodevalue);
            } else ((Question) Questions.elementAt(question)).addFeedback("");
        }
    } else ((Question) Questions.elementAt(question)).addFeedback("");
    }

    public void getAnswer(NodeList nodelist, int question,boolean correct) {
    /* post:  the answers are stored in the Questions vector
     */

    // used to determine the current answer

    boolean explain = false;

    for (int i=0;i<nodelist.getLength();i++) {

        //$ request one child and its value
        Node current=nodelist.item(i);

        String nodename=current.getNodeName();
        String nodevalue=current.getNodeValue();
        //$ first check if nodevalue really points to text
        if (nodename =="#text") {

        //$ if the nodevalue exists then trim it
        if (nodevalue != null)
            nodevalue=nodevalue.trim();

        //$ if the string contains data and it is text, it must be a answer
        if (nodevalue.length() > 0) {

            //$ if the answer isn't empty, it can be added to the list
            nodevalue=HtmlConvert(nodevalue);
            if (correct)
            ((Question) Questions.elementAt(question)).addGoodAnswer(nodevalue);
            else
            ((Question) Questions.elementAt(question)).addBadAnswer(nodevalue);
        }
        } else
        //$ if the nodename==answer then the child is the answer text
        if (nodename.equalsIgnoreCase("explain")) {
            explain = true;
            NodeList reason=current.getChildNodes();
            getReason(reason,question,correct);
        }
    }//for
    if (!explain) ((Question) Questions.elementAt(question)).addFeedback("");
    }

    public void getQuestion(NodeList nodelist,int question) {
    /* post: the questions are stored in one vector
     */

    int NbAnswers=0;
    int NbCorrect=0;

    //$ Analyse each of the nodes to see if they contain any
    //$ any questions or answers
    for (int i=0;i<nodelist.getLength();i++) {
        //$ request one child and its value
        Node current=nodelist.item(i);
        String nodename=current.getNodeName();
        String nodevalue=current.getNodeValue();
        //$ first check if nodevalue really points to text
        if (nodename =="#text") {

        //$ if the nodevalue exists then trim it
        if (nodevalue != null)
            nodevalue=nodevalue.trim();

        //$ if the string contains data and it is text, it must be a question
        if (nodevalue.length() > 0) {

            nodevalue=HtmlConvert(nodevalue);
            //$ if the question isn't empty, it can be added to the list
            ((Question) Questions.elementAt(question)).setQuestion(nodevalue);
        }
        } else
        //$ if the nodename==answer then the child is the answer text
        if (nodename.equalsIgnoreCase("answer")) {
            NodeList answer=current.getChildNodes();
            getAnswer(answer,question,((Element)current).getAttribute("correct").equalsIgnoreCase("true"));
            NbAnswers++;

            if (((Element)current).getAttribute("correct").equalsIgnoreCase("true"))
            NbCorrect++;
        } else {
            System.out.println("Not recognised: "+nodename);
            System.out.println(("Value="+nodevalue).trim());
        }
    }
    //$ save the number of answers and the number of correct ones
    ((Question)Questions.elementAt(question)).setSize(NbAnswers, NbCorrect);
    }

    public void fillArrays(Element root) {//throws WowException {
    /* pre:root is the root element of the XML document
       post: The XML document has been parsed and the Question vector is
       filled
    */

    int question=0;
    //$ Save title,knowledge, TotalQuestions and Verbose in global variables

    try {
        Title=((Element) root).getAttribute("title").trim();

        // NB: this value of Knowledge will NOT be used.
        // The dtd of the tests must be changed.
        Knowledge=((Element) root).getAttribute("concept").trim();
        TotalQuestions=Integer.valueOf(((Element) root).getAttribute("total").trim()).intValue();
        Verbose=((Element) root).getAttribute("verbose").trim().equalsIgnoreCase("true");
        QuestionsToAsk=Integer.valueOf(((Element) root).getAttribute("ask").trim()).intValue();
        Rand=((Element) root).getAttribute("random").trim().equalsIgnoreCase("true");

        if (TotalQuestions < 1)
        System.out.println("Invalid quiz file: number of questions (" + TotalQuestions + ") should be at least 1.");
        if (QuestionsToAsk < 1)
        System.out.println("Invalid quiz file: number of questions to ask (" + QuestionsToAsk + ") should be at least 1.");
        if (TotalQuestions < QuestionsToAsk)
        System.out.println("Invalid quiz file: number of questions to ask (" + QuestionsToAsk + ") is higher than total number of questions (" + TotalQuestions + ").");
    } catch (NumberFormatException e) {
        System.out.println("Invalid quiz file: non-number on line with number of answers to question.");
    }


    //$ Add a new question to the vector
    Questions.addElement(new Question());

    for (Node nod = root.getFirstChild();
         nod != null;
         nod = nod.getNextSibling()) {

        //$ Analyse each of the Child nodes

        //$ select nodename
        String nodename=nod.getNodeName();

        //$ Check if a question block is started
        if (nodename.equalsIgnoreCase("question")) {

        //$ Get all the child nodes (the question itself and answers)
        NodeList nodelist=nod.getChildNodes();

        //$ and add the questions to the vector
        getQuestion(nodelist,question);
        Questions.addElement(new Question());

        ((Question)Questions.elementAt(question)).verify();

        ((Question)Questions.elementAt(question)).setAnswersToShow(Integer.valueOf(((Element)nod).getAttribute("answers").trim()).intValue());

        question++;

        }
    }
        if (question != TotalQuestions) System.out.println("Invalid quiz file: number of questions in file (" + question + ") does not match the expected number given at the top of the file (" + TotalQuestions + ").");
        TotalQuestions = question;
    }


    /**
     * This method creates a random list of question numbers.
     */
    public int[] GenerateQuestions() {
        int i;
        int [] qlist = new int[QuestionsToAsk];
        // we encode the answer indices into alist:
        // positive for good answers, negative (-1 -index) for bad answers
        boolean [] chosen = new boolean[TotalQuestions];
        try {
            for (i=0;i<TotalQuestions;i++) chosen[i]=false;
            resetRAND();
            for (i=0;i<QuestionsToAsk;i++) {
                int j, y=0;
                do {
                    if (Rand) j = RAND(TotalQuestions); else j = i;
                } while (chosen[j] && y++<100);
                chosen[j] = true;
                qlist[i] = j;
                alist.addElement(BuildAnswers((Question)Questions.elementAt(j)));
            }
        } catch(Exception e) {e.printStackTrace();}
        return qlist;
    }

    /**
     * This method creates a random list of answers to a question.
     */
    public int[] BuildAnswers(Question question) {
    // position of good answers in output
    int [] goods = new int[question.getGoodAnswers().size()];
    // position of bad answers in output
    int [] bads = new int[question.getBadAnswers().size()];
    // slots to fill with answers
    int [] Answers = new int[question.getAnswersToShow()];
    int i;
    // make sure there are enough slots.
    i = question.getAnswersToShow();
    if (i < question.getGoodAnswers().size())
        i = question.getGoodAnswers().size();
    if (i < question.getBadAnswers().size())
        i = question.getBadAnswers().size();
    boolean [] slots = new boolean[i];

    for (i = 0; i < goods.length; ++i)
        goods[i] = -1;
    for (i = 0; i < bads.length; ++i)
        bads[i] = -1;
    for (i = 0; i < slots.length; ++i)
        slots[i] = false;
    // pick the desired number of good answers and place them randomly.
    for (i = 0; i < question.getRightToShow(); ++i) {
        int x, y=0;
        do
        x = RAND(question.getAnswersToShow());
        while (slots[x] && ++y<10000);
        slots[x] = true;
        goods[i] = x;
    }
    for (i = 0; i < question.getAnswersToShow()-question.getRightToShow(); ++i) {
        // pick the desired number of bad answers and place them randomly
        // in the remaining slots.
        int x, y=0;
        do
        x = RAND(question.getAnswersToShow());
        while (slots[x] && ++y<10000);
        slots[x] = true;
        bads[i] = x;
    }
    // now that we know where to place good and bad answers
    // we pick the answers from the available ones.
    for (i = 0; i < question.getGoodAnswers().size(); ++i)
        slots[i] = false;
    for (i = 0; i < question.getRightToShow(); ++i) {
        int x;
        do
        x = RAND(question.getGoodAnswers().size());
        while (slots[x]);
        slots[x] = true;
        Answers[goods[i]] = x;
    }
    for (i = 0; i < slots.length; ++i)
        slots[i] = false;
    for (i = 0; i < question.getAnswersToShow()-question.getRightToShow(); ++i)
        slots[i] = false;
    for (i = 0; i < question.getAnswersToShow()-question.getRightToShow(); ++i) {
        int x;
        do
        x = RAND(question.getBadAnswers().size());
        while (slots[x]);
        slots[x] = true;
        Answers[bads[i]] = -1 - x;
    }
    return Answers;
    }

    /**
     * This method generates a multiple-choice quiz with randomly selected
     * questions and answers.
     */
    public void PrintQuestions(String QuizCgi) throws InvalidAttributeException {
    int[] questions = GenerateQuestions();
    sb.append("<html>\n<body>\n");
    sb.append("<h1>" + Title + "</h1>\n");
    sb.append("<form method=\"POST\" action=\"" + QuizCgi + "|EvalTest\">\n");
    sb.append("<ol>\n");
    for (int i = 0; i < QuestionsToAsk; ++i) {
        sb.append("<li>\n");
        sb.append(((Question)Questions.elementAt(questions[i])).getQuestion() + "<br />");
        for (int j = 0; j < ((Question)Questions.elementAt(questions[i])).getAnswersToShow(); ++j) {
        int ai = ((int[])(alist.elementAt(i)))[j];
        if (((Question)Questions.elementAt(questions[i])).getRightToShow() > 1)
            sb.append("<input type=\"CHECKBOX\" name=\"check_" + i + "_" + j + "\" value=\"TRUE\" />");
        else
            sb.append("<input type=\"RADIO\" name=\"radio_" + i + "\" value=\"radio_" + j + "\" />");
        if (ai >= 0)
            sb.append((String)(((Vector)(((Question)Questions.elementAt(questions[i])).getGoodAnswers())).elementAt(ai)));
        else
            sb.append((String)(((Vector)(((Question)Questions.elementAt(questions[i])).getBadAnswers())).elementAt(-1 -ai)));
        sb.append("<br />\n");
        }
        sb.append("</li>\n");
    }
    sb.append("</ol>\n");
    sb.append("<hr />\n");
    sb.append("<input type=\"SUBMIT\" value=\"Evaluate this Test\" />\n");
    sb.append("<input type=\"RESET\" value=\"Wipe Answers to Try Again\" />\n");
    sb.append("</form>\n</body>\n</html>\n");
    }

    /**
     * This method reads a user's answers to a multiple-choice quiz
     * and evaluates the answers. The given user model is updated.
     *
     * @exception   WowException    If an error occurs in decoding the user's
     * answers to the quiz.
     */
    public String EvalQuestions(CgiUtil Util) throws InvalidAttributeException {
    int[] thequestions = GenerateQuestions();
    Vector [] questions = new Vector[QuestionsToAsk];
    int CorrectAnswersThisQuestion = 0;
    int ErrorsThisQuestion = 0;
    int CorrectAnswers = 0;
    int QuestionsAnswered;
    int Score=0;
    int i;
    String attribute;
    String value;
    for (i = 0; i < QuestionsToAsk; ++i) questions[i] = null;
    for (Enumeration e = Util.keys(); e.hasMoreElements(); ) {
        attribute = (String)e.nextElement();
        value = (String)Util.get(attribute);
        if (attribute.startsWith("check_")) {
            // checkbox, thus multiple answers possible
            int qnr = Integer.decode(attribute.substring(6, attribute.indexOf('_', 6))).intValue();
            int anr = Integer.decode(attribute.substring(attribute.lastIndexOf('_')+1)).intValue();
            if (questions[qnr] == null) {
                questions[qnr] = new Vector(((Question)Questions.elementAt(thequestions[qnr])).getAnswersToShow(), 1);
                for (i = 0; i < ((Question)Questions.elementAt(thequestions[qnr])).getAnswersToShow(); ++i)
                questions[qnr].addElement("");
            }
            questions[qnr].setElementAt("TRUE", anr);
        } else if (attribute.startsWith("radio_")) {
            // radio button, thus only one answer possible
            int qnr = Integer.decode(attribute.substring(6)).intValue();
            int anr = Integer.decode(value.substring(6)).intValue();
            if (questions[qnr] == null) { // for a radio button this should always be the case
                questions[qnr] = new Vector(((Question)Questions.elementAt(thequestions[qnr])).getAnswersToShow(), 1);
                for (i = 0; i < ((Question)Questions.elementAt(thequestions[qnr])).getAnswersToShow(); ++i)
                questions[qnr].addElement("");
            }
            questions[qnr].setElementAt("TRUE", anr);
        } else {
        // unrecognized form input; ignore (may be submit button or something)
        ;
        }
    }

    //log the results of the test
    Vector results = new Vector();
    for (i=0;i<QuestionsToAsk;i++) {
        Hashtable h = new Hashtable();
        results.add(h);
        h.put("question", String.valueOf(thequestions[i]));
        Vector answers = new Vector();
        h.put("answers", answers);
        for (int j=0;j<questions[i].size();j++) {
            if ( ((String)questions[i].get(j)).equals("TRUE") ) {
                int ai = ((int[])(alist.elementAt(i)))[j];
                answers.add(String.valueOf( (ai>=0?ai:-ai) ));
            }
        }
    }
    try {WOWStatic.DB().getLogDB().addTestLog(login, FullDocName, results);}
        catch (DatabaseException e) {e.printStackTrace();}

    //write output
    sb.append("<html>\n<body>\n");
    sb.append("<h1>" + Title + "</h1>\n");
    for (i = 0, QuestionsAnswered = 0; i < questions.length; ++i)
        if (questions[i] != null)
        ++QuestionsAnswered;
    if (QuestionsAnswered < QuestionsToAsk) {
        sb.append("In order to evaluate this test, you must answer all the questions.");
        sb.append("You appear to have answered only " + QuestionsAnswered + " out of " + QuestionsToAsk + " questions.");
        sb.append("<br />\nPlease go back and try again.");
        return "0/" + QuestionsToAsk;
    } else {
        if (Verbose)
        sb.append("<ol>");
        for (i = 0; i < QuestionsToAsk; ++i) {
        CorrectAnswersThisQuestion = 0;
        ErrorsThisQuestion = 0;
        if (Verbose) {
            sb.append("<li>\n");
            sb.append(((Question)Questions.elementAt(thequestions[i])).getQuestion());
        }
        for (int j = 0; j < ((Question)Questions.elementAt(thequestions[i])).getAnswersToShow(); ++j) {
            if (((String)questions[i].elementAt(j)).equals("TRUE")) {
            if (Verbose) {
                sb.append("<br />Your answer:");
            }
            int ai = ((int[])(alist.elementAt(i)))[j];
            if (ai >= 0) {
                ++CorrectAnswersThisQuestion;
                if (Verbose) {
                sb.append((String)(((Vector)(((Question)Questions.elementAt(thequestions[i])).getGoodAnswers())).elementAt(ai)));
                sb.append("<br />\nThis answer is <b>correct</b>.<br />");
                }
                if (!((String)(((Vector)(((Question)Questions.elementAt(thequestions[i])).getGoodFeedback())).elementAt(ai))).equals("")) {
                if (Verbose) {
                    sb.append("Additional comment:<br />");
                    sb.append((String)(((Vector)(((Question)Questions.elementAt(thequestions[i])).getGoodFeedback())).elementAt(ai)));
                }
                }
            } else {
                ++ErrorsThisQuestion;
                if (Verbose) {
                sb.append((String)(((Vector)(((Question)Questions.elementAt(thequestions[i])).getBadAnswers())).elementAt(-1 -ai)));
                sb.append("<br />\nThis answer is <b>incorrect</b>.<br />");
                }
                if (!((String)(((Vector)(((Question)Questions.elementAt(thequestions[i])).getBadFeedback())).elementAt(-1 -ai))).equals("")) {
                if (Verbose) {
                    sb.append("Additional comment:<br />");
                    sb.append((String)(((Vector)(((Question)Questions.elementAt(thequestions[i])).getBadFeedback())).elementAt(-1 -ai)));
                }
                }
            }
            }
        }
        if (ErrorsThisQuestion==0 && CorrectAnswersThisQuestion == ((Question)Questions.elementAt(thequestions[i])).getRightToShow())
            ++CorrectAnswers;
        else
            if (ErrorsThisQuestion==0 && ((Question)Questions.elementAt(thequestions[i])).getRightToShow() > 1)
            if (Verbose)
                sb.append("<br />There are more correct answers. Therefore your answer to this whole question is considered to be <B>incorrect</B>.");
        if (Verbose) {
            sb.append("</li>\n");
        }
        }
        Score = (100 * CorrectAnswers) / QuestionsToAsk;
        if (Verbose) {
        sb.append("</ol>");
        sb.append("<hr />");
        }


        sb.append("Your score: " + Score + "%.");

        // Score is a percentage (0<=Score<=100), so it fits exactly in the
        // current Knowledge model.


        // now use the name of the testfile as the name of the concept of
        // which the knowledge will be updated.
        String testfilename = FullDocName.substring(FullDocName.lastIndexOf('/')+1);
        if (testfilename.endsWith(".xml")) {
        testfilename = testfilename.substring(0, testfilename.length()-4);
        } else {
        System.out.println("The testfile should have .xml as extension!");
        }
        if (!Verbose && (Score<100)) {
        sb.append("<hr />\nYour score was not high enough. Please go back and try again.");
        }
        try {
            AttributeValue avalue = new AttributeValue(true);
            avalue.setValue(String.valueOf(Score));
            ProfileUpdate update = new ProfileUpdate(profile, WOWStatic.PM());
            update.updateAttribute(Knowledge, "knowledge", avalue);
            WOWStatic.PM().purgeProfile(profile);
            WOWStatic.DB().getProfileDB().setProfile(profile.id, profile);
        } catch (Exception e) { System.out.println(e);
        }
        sb.append("\n</body>\n</html>\n");
    }
    return CorrectAnswers + "/" + QuestionsToAsk;
    }

    private int RAND(int limit) {return (int)(R.nextFloat()*limit)%limit;}
    private void resetRAND() {R = new Random((long)login.hashCode());}

    public String FilterLinks(String Link) {return Link;}

}