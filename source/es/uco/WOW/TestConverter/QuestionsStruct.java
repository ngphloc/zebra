package es.uco.WOW.TestConverter;

import java.util.Vector;

public class QuestionsStruct
{
//
//		Attribute
//
	protected int codeQuestion;
//
//	Element of Questions
//
	protected String enunciate;
	protected String image;

	protected Vector answers;
	protected int lastAnswer;
//
//		Attrubutes of answers
//
	protected int numberOfCorrect;
	protected int numberOfanswers;
//
//		Elements of irtParameters
//
	protected float difficulty;
	protected float discrimination;
	protected float guessing;
//
//		Elements of statisticParameters
//
	protected float exhibitionRate;
	protected float answerTime;
	protected float successRate;
	protected int numberOfUses;
	protected int numberOfSuccesses;
//
//		Elements of testFiles
//
	protected Vector classicTest;
	protected int lastClassic;
	protected Vector AdaptativeTest;
	protected int lastAdaptative;


public QuestionsStruct ( int id )
{
//
//		Attribute
//
	codeQuestion = id;
//
//	Element of questions
//
	enunciate = "";
	image = "";

	answers = new Vector ();
	lastAnswer = -1;
//
//		Attrubutes of answers
//
	numberOfCorrect = 0;
	numberOfanswers = 0;
//
//		Elements of irtParameters
//
	difficulty = 0;
	discrimination = 0;
	guessing = 0;
//
//		Elements of statisticParameters
//
	exhibitionRate = 0;
	answerTime = 0;
	successRate = 0;
	numberOfUses = 0;
	numberOfSuccesses = 0;
//
//	Element of testFiles
//
	classicTest = new Vector ();
	lastClassic = -1;
	AdaptativeTest = new Vector ();
	lastAdaptative = -1;
}


public void setCodeQuestion ( int id )
{
	codeQuestion = id;
}
public String getImage ( )
{
	return image;
}

public String getEnunciate ()
{
	return enunciate;
}
public int getCodeQuestion ()
{
	return codeQuestion;
}
public void setEnunciate ( String Enunciate )
{
	enunciate = Enunciate;
}
public void setImage ( String Image )
{
	image = Image;
}

public int getNumberOfCorrect ( )
{
	return numberOfCorrect;
}
public int getNumberOfAnswers ( )
{
	return numberOfanswers;
}

public void setNumberOfCorrect ( int NumberOfCorrect )
{
	numberOfCorrect = NumberOfCorrect;
}
public void setNumberOfanswers ( int NumberOfanswers )
{
	numberOfanswers = NumberOfanswers;
}


public float getDifficulty ()
{
	return difficulty;
}
public float getDiscrimination ()
{
	return discrimination;
}
public float getGuessing ()
{
	return guessing;
}
public void setDifficulty ( float Difficulty )
{
	difficulty = Difficulty;
}
public void setDiscrimination ( float Discrimination )
{
	discrimination = Discrimination;
}
public void setGuessing ( float Guessing )
{
	guessing = Guessing;
}

public float getExhibitionRate ()
{
	return exhibitionRate;
}
public float getAnswerTime ()
{
	return answerTime;
}
public float getSuccessRate ()
{
	return successRate;
}
public int getNumberOfUses ()
{
	return numberOfUses;
}
public int getNumberOfSuccesses ()
{
	return numberOfSuccesses;
}

public void setExhibitionRate ( float anExhibitionRate )
{
	exhibitionRate = anExhibitionRate;
}
public void setAnswerTime ( float AnswerTime )
{
	answerTime = AnswerTime;
}
public void setSuccessRate ( float SuccessRate )
{
	successRate = SuccessRate;
}
public void setNumberOfUses ( int NumberOfUses )
{
	numberOfUses = NumberOfUses;
}
public void setNumberOfSuccesses ( int NumberOfSuccesses )
{
	numberOfSuccesses = NumberOfSuccesses;
}
public int getCodeAnswer ( )
{
	return 	( ( ( AnswersStruct ) this.answers.elementAt ( lastAnswer )).getCodeAnswer ( ) );
}
public int getCodeAnswer ( int pos )
{
	return 	( ( ( AnswersStruct ) this.answers.elementAt ( pos )).getCodeAnswer ( ) );
}
public void setCodeAnswer ( int id )
{
	int i = 0;
	int sw = 0;

	if ( !this.answers.isEmpty ( ) )	
		for ( i = 0; i < lastAnswer ;i++)
			if ( ( ( AnswersStruct ) this.answers.elementAt ( i ) ).getCodeAnswer ( ) == id )
				sw = 1;
	if ( sw == 0 )
	{
		this.answers.addElement ( new AnswersStruct ( id ) );
		lastAnswer++;
	}
}
//
//		Visor of textAnswer
//
public String getTextAnswer ( )
{
	return 	( ( ( AnswersStruct ) this.answers.elementAt ( lastAnswer )).getTextAnswer ( ) );
}
public String getTextAnswer ( int positionAnswer )
{
	if ( positionAnswer <= lastAnswer )
		return 	( ( ( AnswersStruct ) this.answers.elementAt ( positionAnswer )).getTextAnswer ( ) );
	else
		return null;
}
public void setTextAnswer ( String TextAnswer )
{
	( ( AnswersStruct ) this.answers.elementAt ( lastAnswer )).setTextAnswer ( TextAnswer );
}
public void setTextAnswer ( int positionAnswer , String TextAnswer )
{
	if ( positionAnswer <= lastAnswer )
		( ( AnswersStruct ) this.answers.elementAt ( positionAnswer )).setTextAnswer ( TextAnswer );
}
//
//		Visor of correct
//
public String getCorrect ( int positionAnswer )
{
	if ( positionAnswer <= lastAnswer )
		return 	( ( ( AnswersStruct ) this.answers.elementAt ( positionAnswer ) ).getCorrect ( ) );
	else
		return null;
}
public String getCorrect ( )
{
	if ( lastAnswer >= 0 )
		return 	( ( ( AnswersStruct ) this.answers.elementAt ( lastAnswer )).getCorrect ( ) );
	else
		return null;
}

public void setCorrect ( String Correct )
{

	( ( AnswersStruct ) this.answers.lastElement ( ) ).setCorrect ( Correct );
}

public void setCorrect ( int positionAnswer , String Correct )
{
	if ( positionAnswer <= lastAnswer )
		( ( AnswersStruct ) this.answers.elementAt ( positionAnswer )).setCorrect ( Correct );
}

//
//		Visor of explanation
//
public String getExplanation ( )
{
	if ( lastAnswer >= 0 )
		return 	( ( ( AnswersStruct ) this.answers.elementAt ( lastAnswer )).getExplanation ( ) );
	else
		return null;
}
public String getExplanation ( int positionAnswer )
{
	if ( positionAnswer <= lastAnswer )
		return 	( ( ( AnswersStruct ) this.answers.elementAt ( positionAnswer )).getExplanation ( ) );
	else
		return null;
}

public void setExplanation ( String Explanation )
{
	if ( lastAnswer >= 0 )
		( ( AnswersStruct ) this.answers.elementAt ( lastAnswer )).setExplanation ( Explanation );
}

public void setExplanation ( int positionAnswer , String Explanation )
{
	if ( positionAnswer <= lastAnswer )
		( ( AnswersStruct ) this.answers.elementAt ( positionAnswer )).setExplanation ( Explanation );
}
public int getLastAnswer ( ) {
	return lastAnswer;
}
//
//		Elements of testFiles
//
//
//			Classic
//
public int getLastClassic ( )
{
	return this.lastClassic;
}
public void setClassic ( String Text )
{
	this.classicTest.addElement ( new FileName ( Text ) );
	this.lastClassic++;
}

public String getClassic ( )
{
	if ( lastClassic >= 0 )
		return 	( ( ( FileName ) this.classicTest.elementAt ( lastClassic )).getFile ( ) );
	else
		return null;
}
public String getClassic ( int positionClassic )
{
	if ( positionClassic <= lastClassic )
		return 	( ( ( FileName ) this.classicTest.elementAt ( positionClassic )).getFile ( ) );
	else
		return null;
}
//
//			Adaptative
//
public int getLastAdaptative ( )
{
	return this.lastAdaptative;
}
public void setAdaptative ( String Text )
{
	this.AdaptativeTest.addElement ( new FileName ( Text ) );
	this.lastAdaptative++;
}

public String getAdaptative ( )
{
	if ( lastAdaptative >= 0 )
		return 	( ( ( FileName ) this.AdaptativeTest.elementAt ( lastAdaptative ) ).getFile ( ) );
	else
		return null;
}
public String getAdaptative ( int positionAdaptative )
{
	if ( positionAdaptative <= lastAdaptative )
		return 	( ( ( FileName ) this.AdaptativeTest.elementAt ( positionAdaptative )).getFile ( ) );
	else
		return null;
}


}
