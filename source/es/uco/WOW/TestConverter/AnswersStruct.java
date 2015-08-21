package es.uco.WOW.TestConverter;

public class AnswersStruct {
	//
	// Attibute
	//
	protected int codeAnswer;

	//
	// Element of answer
	//
	protected String textAnswer;

	protected String correct;

	protected String explanation;

	//
	// Constructor of class
	//
	public AnswersStruct(int id) {

		setCodeAnswer(id);

		textAnswer = null;
		correct = null;
		explanation = null;
	}

	//
	// Visor of codeAnswer
	//
	public int getCodeAnswer() {
		return codeAnswer;
	}

	protected void setCodeAnswer(int CodeAnswer) {
		codeAnswer = CodeAnswer;
	}

	//
	// Visor of textAnswer
	//
	public String getTextAnswer() {
		return textAnswer;
	}

	public void setTextAnswer(String TextAnswer) {
		textAnswer = TextAnswer;
	}

	//
	// Visor of correct
	//
	public String getCorrect() {
		return correct;
	}

	public void setCorrect(String Correct) {
		this.correct = Correct;
	}

	//
	// Visor of explanation
	//
	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String Explanation) {
		explanation = Explanation;
	}

}
