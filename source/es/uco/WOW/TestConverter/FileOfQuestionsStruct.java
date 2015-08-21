package es.uco.WOW.TestConverter;

import java.util.Vector;

public class FileOfQuestionsStruct {
	protected String fileName;

	protected String name;

	protected String course;

	protected String path;

	protected int numberOfAnswersForFile;

	protected String concept;

	protected Vector WOWquestions;

	protected int lastQuestion;

	public FileOfQuestionsStruct() {
		fileName = null;
		name = null;
		course = null;
		path = null;
		numberOfAnswersForFile = 0;
		concept = null;
		WOWquestions = new Vector();
		lastQuestion = -1;
	}

	public String getFileName() {
		return fileName;
	}

	public String getName() {
		return name;
	}

	public String getCourse() {
		return course;
	}

	public String getPath() {
		return path;
	}

	public int getNumberOfAnswersForFile() {
		return numberOfAnswersForFile;
	}

	public String getConcept() {
		return concept;
	}

	public void setFileName(String aFilename) {
		fileName = aFilename;
	}

	public void setName(String Name) {
		name = Name;
	}

	public void setCourse(String Course) {
		course = Course;
	}

	public void setPath(String Path) {
		path = Path;
	}

	public void setNumberOfAnswersForFile(int NumberOfAnswersForFile) {
		numberOfAnswersForFile = NumberOfAnswersForFile;
	}

	public void setConcept(String Concept) {
		concept = Concept;
	}

	//
	// falta por crear todos los visores y extractores
	// de questionsStruct WOWquestions

	public String getImage() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getImage());
	}

	public String getImage(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getImage());
		else
			return null;
	}

	public String getEnunciate() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getEnunciate());
	}

	public String getEnunciate(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getEnunciate());
		else
			return null;
	}

	public int getCodeQuestion() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getCodeQuestion());
	}

	public int getCodeQuestion(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getCodeQuestion());
		else
			return -1;
	}

	public void setEnunciate(String Enunciate) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setEnunciate(Enunciate);
	}

	public void setEnunciate(int positionQuestion, String Enunciate) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setEnunciate(Enunciate);
	}

	public void setImage(String Image) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setImage(Image);
	}

	public void setImage(int positionQuestion, String Image) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setImage(Image);
	}

	public int getNumberOfCorrect() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getNumberOfCorrect());
	}

	public int getNumberOfCorrect(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getNumberOfCorrect());
		else
			return -1;
	}

	public int getNumberOfAnswers() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getNumberOfAnswers());
	}

	public int getNumberOfAnswers(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getNumberOfAnswers());
		else
			return -1;
	}

	public void setNumberOfCorrect(int NumberOfCorrect) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setNumberOfCorrect(NumberOfCorrect);
	}

	public void setNumberOfCorrect(int positionQuestion, int NumberOfCorrect) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion))
					.setNumberOfCorrect(NumberOfCorrect);
	}

	public void setNumberOfAnswers(int NumberOfanswers) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setNumberOfanswers(NumberOfanswers);
	}

	public void setNumberOfAnswers(int positionQuestion, int NumberOfanswers) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion))
					.setNumberOfanswers(NumberOfanswers);
	}

	public float getDifficulty() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getDifficulty());
	}

	public float getDifficulty(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getDifficulty());
		else
			return -1;
	}

	public float getDiscrimination() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getDiscrimination());
	}

	public float getDiscrimination(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getDiscrimination());
		else
			return -1;
	}

	public float getGuessing() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getGuessing());
	}

	public float getGuessing(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getGuessing());
		else
			return -1;
	}

	public void setDifficulty(float Difficulty) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setDifficulty(Difficulty);
	}

	public void setDifficulty(int positionQuestion, float Difficulty) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setDifficulty(Difficulty);
	}

	public void setDiscrimination(float Discrimination) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setDiscrimination(Discrimination);
	}

	public void setDiscrimination(int positionQuestion, float Discrimination) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setDiscrimination(Discrimination);
	}

	public void setGuessing(float Guessing) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setGuessing(Guessing);
	}

	public void setGuessing(int positionQuestion, float Guessing) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setGuessing(Guessing);
	}

	public float getExhibitionRate() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getExhibitionRate());
	}

	public float getExhibitionRate(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getExhibitionRate());
		else
			return -1;
	}

	public float getAnswerTime() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getAnswerTime());
	}

	public float getAnswerTime(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getAnswerTime());
		else
			return -1;
	}

	public float getSuccessRate() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getSuccessRate());
	}

	public float getSuccessRate(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getSuccessRate());
		else
			return -1;
	}

	public int getNumberOfUses() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getNumberOfUses());
	}

	public int getNumberOfUses(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getNumberOfUses());
		else
			return -1;
	}

	public int getNumberOfSuccesses() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getNumberOfSuccesses());
	}

	public int getNumberOfSuccesses(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getNumberOfSuccesses());
		else
			return -1;
	}

	public void setExhibitionRate(float ExhibitionRate) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setExhibitionRate(ExhibitionRate);
	}

	public void setExhibitionRate(int positionQuestion, float ExhibitionRate) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setExhibitionRate(ExhibitionRate);
	}

	public void setAnswerTime(float AnswerTime) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setAnswerTime(AnswerTime);
	}

	public void setAnswerTime(int positionQuestion, float AnswerTime) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setAnswerTime(AnswerTime);
	}

	public void setSuccessRate(float SuccessRate) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setSuccessRate(SuccessRate);
	}

	public void setSuccessRate(int positionQuestion, float SuccessRate) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setSuccessRate(SuccessRate);
	}

	public void setNumberOfUses(int NumberOfUses) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setNumberOfUses(NumberOfUses);
	}

	public void setNumberOfUses(int positionQuestion, int NumberOfUses) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setNumberOfUses(NumberOfUses);
	}

	public void setNumberOfSuccesses(int NumberOfSuccesses) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setNumberOfSuccesses(NumberOfSuccesses);
	}

	public void setNumberOfSuccesses(int positionQuestion, int NumberOfSuccesses) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion))
					.setNumberOfSuccesses(NumberOfSuccesses);
	}

	public int getCodeAnswer() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getCodeAnswer());
	}

	public int getCodeAnswer(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getCodeAnswer());
		else
			return -1;
	}

	public int getCodeAnswer(int nul, int positionAnswer) {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getCodeAnswer(positionAnswer));
	}

	public int getCodeAnswer(int nul, int positionQuestion, int positionAnswer) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion))
					.getCodeAnswer(positionAnswer));
		else
			return -1;
	}

	public void setCodeAnswer(int id) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setCodeAnswer(id);
	}

	public void setCodeAnswer(int positionQuestion, int id) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setCodeAnswer(id);
	}

	//
	// Visor of textAnswer
	//
	public String getTextAnswer() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getTextAnswer());
	}

	public String getTextAnswer(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getTextAnswer());
		else
			return null;
	}

	public String getTextAnswer(int nul, int positionAnswer) {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getTextAnswer(positionAnswer));
	}

	public String getTextAnswer(int nul, int positionQuestion, int positionAnswer) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion))
					.getTextAnswer(positionAnswer));
		else
			return null;
	}

	public void setTextAnswer(String TextAnswer) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setTextAnswer(TextAnswer);
	}

	public void setTextAnswer(int positionQuestion, String TextAnswer) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setTextAnswer(TextAnswer);
	}

	public void setTextAnswer(int nul, int positionAnswer, String TextAnswer) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setTextAnswer(positionAnswer, TextAnswer);
	}

	public void setTextAnswer(int nul, int positionQuestion, int positionAnswer, String TextAnswer) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setTextAnswer(positionAnswer,
					TextAnswer);
	}

	//
	// Visor of correct
	//
	public String getCorrect(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getCorrect());
		else
			return null;
	}

	public String getCorrect() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getCorrect());
	}

	public String getCorrect(int nul, int positionQuestion, int positionAnswer) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getCorrect(positionAnswer));
		else
			return null;
	}

	public String getCorrect(int nul, int positionAnswer) {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getCorrect(positionAnswer));
	}

	public void setCorrect(String Correct) {

		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setCorrect(Correct);
	}

	public void setCorrect(int positionQuestion, String Correct) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setCorrect(Correct);
	}

	public void setCorrect(int nul, int positionAnswer, String Correct) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setCorrect(Correct);
	}

	public void setCorrect(int nul, int positionAnswer, int positionQuestion, String Correct) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion))
					.setCorrect(positionAnswer, Correct);
	}

	//
	// Visor of explanation
	//

	public String getExplanation() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getExplanation());
	}

	public String getExplanation(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getExplanation());
		else
			return null;
	}

	public String getExplanation(int nul, int positionAnswer) {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getExplanation(positionAnswer));
	}

	public String getExplanation(int nul, int positionAnswer, int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return (((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion))
					.getExplanation(positionAnswer));
		else
			return null;
	}

	public void setExplanation(String Explanation) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setExplanation(Explanation);
	}

	public void setExplanation(int positionQuestion, String Explanation) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setExplanation(Explanation);
	}

	public void setExplanation(int nul, int positionAnswer, String Explanation) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setExplanation(positionAnswer,
				Explanation);
	}

	public void setExplanation(int nul, int positionAnswer, int positionQuestion, String Explanation) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setExplanation(positionAnswer,
					Explanation);
	}

	public int getLastAnswer() {
		return ((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getLastAnswer();
	}

	public int getLastQuestion() {
		return lastQuestion;
	}

	public void setCodeQuestion(int id) {
		int i = 0;
		int sw = 0;

		if (!this.WOWquestions.isEmpty())
			for (i = 0; i < lastQuestion; i++)
				if (((QuestionsStruct) this.WOWquestions.elementAt(i)).getCodeQuestion() == id)
					sw = 1;
		if (sw == 0) {
			this.WOWquestions.addElement(new QuestionsStruct(id));
			lastQuestion++;
		}
	}

	public void setCodeQuestion(int nul, int id) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setCodeQuestion(id);
	}

	public int getLastClassic() {
		return (((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getLastClassic());
	}

	public int getLastClassic(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return ((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getLastClassic();
		else
			return -1;
	}

	public void setClassic(int positionQuestion, String Text) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setClassic(Text);
	}

	public void setClassic(String Text) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setClassic(Text);
	}

	public String getClassic() {
		return ((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getClassic();
	}

	public String getClassic(int positionClassic) {
		return ((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getClassic(positionClassic);
	}

	public String getClassic(int nul, int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return ((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getClassic();
		else
			return null;
	}

	public String getClassic(int nul, int positionQuestion, int positionClassic) {
		if (positionQuestion <= lastQuestion)
			return ((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getClassic(positionClassic);
		else
			return null;
	}

	//
	// Adaptative
	//
	public int getLastAdaptative() {
		return ((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getLastAdaptative();
	}

	public int getLastAdaptative(int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return ((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getLastAdaptative();
		else
			return -1;
	}

	public void setAdaptative(int positionQuestion, String Text) {
		if (positionQuestion <= lastQuestion)
			((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).setAdaptative(Text);
	}

	public void setAdaptative(String Text) {
		((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).setAdaptative(Text);
	}

	public String getAdaptative() {
		if (lastQuestion >= 0)
			return ((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion)).getAdaptative();
		else
			return null;
	}

	public String getAdaptative(int positionAdaptative) {
		if (lastQuestion > 0)
			return ((QuestionsStruct) this.WOWquestions.elementAt(lastQuestion))
					.getAdaptative(positionAdaptative);
		else
			return null;
	}

	public String getAdaptative(int nul, int positionQuestion) {
		if (positionQuestion <= lastQuestion)
			return ((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion)).getAdaptative();
		else
			return null;
	}

	public String getAdaptative(int nul, int positionQuestion, int positionAdaptative) {
		if (positionQuestion <= lastQuestion)
			return ((QuestionsStruct) this.WOWquestions.elementAt(positionQuestion))
					.getAdaptative(positionAdaptative);
		else
			return null;
	}

} // Class FileOfQuestionsStruct
