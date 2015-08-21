package es.uco.WOW.TestEditor;

import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import es.uco.WOW.Utils.Course;
import es.uco.WOW.Utils.EvalTestLogStudent;
import es.uco.WOW.Utils.Question;
import es.uco.WOW.Utils.QuestionsFileTest;
import es.uco.WOW.Utils.Student;
import es.uco.WOW.Utils.StudentTest;
import es.uco.WOW.Utils.Test;
import es.uco.WOW.Utils.TestLogStudent;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/*
 NOMBRE: DlgUpdateStatisticData.
 FUNCION: JDialog que muestra al usuario la lista de cursos de los que es
          propietario y la lista de nombres de ficheros de preguntas para
          cada uno de los cursos anteriores para que seleccione uno para
          actualizar los datos estad�sticos de las preguntas contenidas en el.
 LAST MODIFICATION: 06-02-2008
*/

public class DlgUpdateStatisticData extends JDialog
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//Variables de usuario.
  private Vector courseVector; //Almacena la lista de nombres de los
                               //ficheros de preguntas para cada curso.
  //private String courseName = ""; //Almacena el curso seleccionado.
  //private String questionsFileName = ""; //Almacena el nombre del fichero de
                                         //preguntas seleccionado.

  //Componentes de la ventana.
  public JFrame parent;
  private JPanel jPanelTitle; //JPanel que muestra el t�tulo en jPanelMain.
  private JPanel jPanelMain; //JPanel principal del JDialog.
  private JScrollPane jScrollPaneTextAreaStatus;
  private JScrollPane jScrollPaneListQuestionsFile;

  private JLabel jLabelTitle;
  private JLabel jLabelCourse;
  private JLabel jLabelQuestionsFile;

  public boolean id = false;
  public boolean changeJComboCourse = true;
  public boolean update = false;
  public String title = "";

  private JButton ContinueButton;
  private JButton OkButton;
  private JButton StopButton;
  private JButton ExitButton;

  public JComboBox jComboCourse;

  public JList jListQuestionsFile;

  public JTextArea jTextAreaStatus;

  private JProgressBar jProgressBarQuestions;
  private JProgressBar jProgressBarQuestionsFile;

  private Question question = null;
  private Vector studentTestVector = null;
  private Vector questionsFileNameVector = null;
  private Vector questionsFileNameVectorUpdate = null;
  private Test test = null; //Almacenar� los datos del test a actualizar.
  public boolean taskDone = false;
  private Update workerUpdate = null;
  
//  private Vector studentVector = null; //Almacenar� los datos de los estudiantes que han realizado el test.
//  private Vector questionsFileTestVector = null;
//  private Vector questionVector = null;
//  private int numberOfQuestionsInFile = 0;
//  
//  private double CPT = 0;
//  private double A = 0;
//  private int MAXIT = 0;
//  private int NXL = 0;
//  private int NIT = 0;
//  private int k = 0;
//  private double sfw = 0, sfwv = 0, sfwv2 = 0, sfwx = 0, sfwxv = 0, sfwx2 = 0;
//  private double PI = 0, DEV = 0, PH = 0, W = 0, V = 0;
//  private double p1 = 0, p2 = 2, p3 = 0, p4 = 0, p5 = 0, p6 = 0;
//  private double DIFF = 0, DM = 0, DCPT = 0, DA = 0;
  
  //Variables para la actualizaci�n de los datos estadisticos.
  private int numberOfUses = 0;
  private int numberOfSuccesses = 0;
  private int timeToAnswer = 0;
  //private double successRate = 0;
  //private double exhibitionRate = 0;

  


  /*
  NOMBRE: DlgUpdateStatisticData (Constructor de esta misma clase).
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: Al invocar la clase.
  LLAMA A:  JPanelInit();
            JLabelInit();
            JButtonInit();
            JTextAreaInit();
            JProgressBarInit();
            JComboInit();
            JListInit();
            JDialogInit();
  RECIBE:
          title: Objeto de tipo String. Indica el t�tulo del JDialog.
          courseVector: Objeto de tipo Vector. Contiene en cada posici�n
                           un objeto de tipo Course con los datos usados
                           del curso.
          parent: Objeto de tipo JFrame. Hace referencia al JFrame que invoc� a
                  este JDialog, osea su padre.

  DEVUELVE: nada.
  FUNCI�N: Constructor de la clase DlgUpdateStatisticData. Se encarga
           de configurar el aspecto del dialogo, su funcionalidad, asi como de
           inicializar los componentes que componen el mismo.
  */

  public DlgUpdateStatisticData(String title, Vector courseVector, JFrame parent)
  {
    //Llamada al constructor de la clase padre.
    super(parent, title, true);

    //Establecimiento del aspecto del JDialog.
    try{
      UIManager.setLookAndFeel(((TestEditor)parent).lookAndFeelClassName);
    }catch(Exception e){
    }


    //Establecimiento de los valores de las variables propias de la clase
    //con los recibidos.
    this.title = title;
    this.courseVector = courseVector;
    this.parent = (TestEditor)parent;


    //Inicializaci�n de los componentes de la clase.
    try{
      //NO CAMBIAR EL ORDEN!!!!!!!!!
      JLabelInit();
      JButtonInit();
      JTextAreaInit();
      JProgressBarInit();
      JComboInit();
      JListInit();
      JPanelInit();
      JDialogInit();
    }catch (Exception e){
    }
  }



////////////////////////////////////////////////////////////////////////////////
////////// INICIALIZACI�N DE LOS COMPONENTES DEL JDIALOG ///////////////////////
////////////////////////////////////////////////////////////////////////////////



  /*
  NOMBRE: JPanelInit
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: El constructor de la clase a la que pertenece.
  LLAMA A: nada.
  RECIBE: void.
  DEVUELVE: void.
  FUNCI�N: Inicializa los JPanel del JDialog, reservando memoria para los mismo
           y estableciendo su tama�o y su tipo de layout.
  */

  private void JPanelInit()
  {
    //Reserva de memoria para los JPanel.
    jPanelTitle = new JPanel();
    jPanelMain = new JPanel();

    jPanelTitle.setDoubleBuffered(true);
    jPanelMain.setDoubleBuffered(true);

    //Establecimiento de sus tama�os, bordes y layout.
    jPanelTitle.setLayout(null);
    jPanelMain.setBorder(BorderFactory.createLoweredBevelBorder());

    if(update == false)
      jPanelMain.setBounds(new Rectangle(10, 50, 407, 160));
    else
      jPanelMain.setBounds(new Rectangle(10, 50, 407, 350));

    jPanelMain.setLayout(null);

    //Reserva de memoria para el jScrollPaneListQuestionsFile.
    jScrollPaneListQuestionsFile = new JScrollPane(jListQuestionsFile,
                                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jScrollPaneListQuestionsFile.setBounds(new Rectangle(166, 45, 215, 75));
    jScrollPaneListQuestionsFile.setDoubleBuffered(true);
    jScrollPaneListQuestionsFile.setAutoscrolls(true);


    //Reserva de memoria para el jScrollPaneTextAreaStatus.
    jScrollPaneTextAreaStatus = new JScrollPane(jTextAreaStatus,
                                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneTextAreaStatus.setVisible(false);
    jScrollPaneTextAreaStatus.setBounds(new Rectangle(20, 155, 361, 125));
    jScrollPaneTextAreaStatus.setDoubleBuffered(true);
    jScrollPaneTextAreaStatus.setAutoscrolls(true);
  }



  /*
  NOMBRE: JLabelInit
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: El constructor de la clase a la que pertenece.
  LLAMA A: nada.
  RECIBE: void.
  DEVUELVE: void.
  FUNCI�N: Inicializa los JLabel del JDialog, reservando memoria para los mismo,
           estableciendo sus tama�osy sus textos.
  */

  private void JLabelInit()
  {
    //Reserva de memoria par los JLabel.
    jLabelTitle = new JLabel();
    jLabelCourse = new JLabel();
    jLabelQuestionsFile = new JLabel();

    //Establecimiento de sus tama�os, textos y  bordes.
    jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 16));
    jLabelTitle.setBorder(BorderFactory.createLoweredBevelBorder());
    jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelTitle.setText(this.getTitle());
    jLabelTitle.setBounds(new Rectangle(10, 5, 407, 44));

    jLabelCourse.setBounds(new Rectangle(20, 17, 50, 17));
    jLabelCourse.setText("Course:");
    jLabelCourse.setFont(new java.awt.Font("Dialog", 1, 12));

    jLabelQuestionsFile.setBounds(new Rectangle(20, 47, 140, 17));
    jLabelQuestionsFile.setText("Question file name:");
    jLabelQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 12));
  }



  /*
  NOMBRE: JButtonInit
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: El constructor de la clase a la que pertenece.
  LLAMA A: nada.
  RECIBE: void.
  DEVUELVE: void.
  FUNCI�N: Inicializa los JButton del JDialog, reservando memoria para los mismo
           y estableciendo sus tama�os y funcionalidades.
  */

  private void JButtonInit()
  {
    //Reserva de memoria para los JButton.
    ContinueButton = new JButton();
    OkButton = new JButton();
    StopButton = new JButton();
    ExitButton = new JButton();

    //Establecimiento del tama�o, textos y tipo de letra para ContinueButton.
    ContinueButton.setVisible(false);
    ContinueButton.setText("Continue");
    ContinueButton.setFont(new java.awt.Font("Button", 1, 12));


    //Establecimiento del tama�o, textos y tipo de letra para OkButton.
    OkButton.setText("Ok");
    OkButton.setFont(new java.awt.Font("Button", 1, 12));
    OkButton.setEnabled(false);


    //Establecimiento del tama�o, texto y tipo de letra para StopButton.
    StopButton.setText("Stop");
    StopButton.setFont(new java.awt.Font("Button", 1, 12));
    StopButton.setVisible(false);

    //Establecimiento del tama�o, texto y tipo de letra para ExitButton.
    ExitButton.setText("Exit");
    ExitButton.setFont(new java.awt.Font("Button", 1, 12));

    if(update == true)
    {
      ContinueButton.setBounds(new Rectangle(20, 405, 100, 30));
      OkButton.setBounds(new Rectangle(195, 405, 100, 30));
      ExitButton.setBounds(new Rectangle(310, 405, 100, 30));
    }
    else
    {
      ContinueButton.setBounds(new Rectangle(20, 215, 100, 30));
      OkButton.setBounds(new Rectangle(195, 215, 100, 30));
      ExitButton.setBounds(new Rectangle(310, 215, 100, 30));
    }

    StopButton.setBounds(new Rectangle(310, 405, 100, 30));

    //Establecimiento de la funcionalidad de ContinueButton.
    ContinueButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ContinueButtonActionPerformed();
      }
    });

    //Establecimiento de la funcionalidad de OkButton.
    OkButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        questionsFileNameVectorUpdate = new Vector();

        if(jListQuestionsFile.getSelectedIndices().length > 0)
        {
          for(int i = 0; i < jListQuestionsFile.getSelectedValues().length; i++)
            questionsFileNameVectorUpdate.add(jListQuestionsFile.
                                               getSelectedValues()[i].
                                               toString().trim());
        }
        OkButtonActionPerformed();
      }
    });

    //Establecimiento de la funcionalidad de StopButton.
    ExitButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ExitButtonActionPerformed();
      }
    });
  }



  /*
  NOMBRE: JTextAreaInit.
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: El constructor de la clase a la que pertenece.
  LLAMA A: nada.
  RECIBE: void.
  DEVUELVE: void.
  FUNCI�N: Inicializa los JTextArea del JDialog.
  */

  private void JTextAreaInit()
  {
    //Reserva de memoria para el jTextAreaEnunciate, que contendr� el
    //enunciado de la pregunta.
    jTextAreaStatus = new JTextArea();
    jTextAreaStatus.setVisible(false);
    jTextAreaStatus.setLineWrap(true);
    jTextAreaStatus.setWrapStyleWord(true);
    jTextAreaStatus.setFont(new java.awt.Font("Dialog", 0, 12));
    jTextAreaStatus.setBackground(SystemColor.white);
    jTextAreaStatus.setBorder(BorderFactory.
                              createLineBorder(SystemColor.black));
    jTextAreaStatus.setEditable(false);
    jTextAreaStatus.setDoubleBuffered(true);
  }



  /*
  NOMBRE: JProgressBarInit.
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: Step0Init.
  LLAMA A: nada.
  RECIBE: void.
  DEVUELVE: void.
  FUNCI�N: Inicializa el JProgressBar del JDialog.
  */

  private void JProgressBarInit()
  {
    //Reserva de memoria y establecimiento de algunas propiedades de los
    //JProgressBar.
    jProgressBarQuestions = new JProgressBar(JProgressBar.HORIZONTAL);
    jProgressBarQuestions.setBorderPainted(true);
    jProgressBarQuestions.setStringPainted(true);
    jProgressBarQuestions.setBounds(new Rectangle(20, 285, 361, 25));
    jProgressBarQuestions.setVisible(false);
    jProgressBarQuestions.setBorder(BorderFactory.createLineBorder(SystemColor.black));
    jProgressBarQuestions.setDoubleBuffered(true);


    jProgressBarQuestionsFile = new JProgressBar(JProgressBar.HORIZONTAL);
    jProgressBarQuestionsFile.setBorderPainted(true);
    jProgressBarQuestionsFile.setStringPainted(true);
    jProgressBarQuestionsFile.setBounds(new Rectangle(20, 315, 361, 25));
    jProgressBarQuestionsFile.setVisible(false);
    jProgressBarQuestionsFile.setBorder(BorderFactory.
                                        createLineBorder(SystemColor.black));
    jProgressBarQuestionsFile.setDoubleBuffered(true);
  }



  /*
  NOMBRE: JComboInit
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: El constructor de la clase a la que pertenece.
  LLAMA A: nada.
  RECIBE: void.
  DEVUELVE: void.
  FUNCI�N: Inicializa los JCombo del JDialog, reservando memoria para los mismo
           y estableciendo su tama�o y su funcionalidad.
  */

  private void JComboInit()
  {
    //Reserva de memoria para los JComboBox.
    jComboCourse = new JComboBox();

    //Borrado de sus contenidos previos.
    jComboCourse.removeAllItems();

    //Establecimiento de sus tama�os.
    jComboCourse.setBounds(new Rectangle(86, 15, 295, 25));

    //Adici�n a jComboCourse de la lista de cursos del usuario.
    for(int i = 0; i < courseVector.size(); i++)
      jComboCourse.addItem(((Course)courseVector.get(i)).getName());

    questionsFileNameVector = ((Course)courseVector.get(0)).
                              getQuestionsFileNames();

    //Establecimiento de la funcionalidad de jComboCourse.
    jComboCourse.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jComboCourseActionPerformed();
      }
    });
  }



  /*
  NOMBRE: JListInit.
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: El constructor de la clase a la que pertenece.
  LLAMA A: nada.
  RECIBE: void.
  DEVUELVE: void.
  FUNCI�N: Inicializa los JList del frame.
  */

  private void JListInit()
  {
    if(questionsFileNameVector == null)
      questionsFileNameVector = new Vector();

    //Reserva de memoria para los JList.
    jListQuestionsFile = new JList(questionsFileNameVector);

    //Establecimiento del scroll de los JList.
    jListQuestionsFile.setAutoscrolls(true);

    //Establecimiento del borde de los JList.
    jListQuestionsFile.setBorder(BorderFactory.
                                 createLineBorder(SystemColor.BLACK));

    //Establecimiento del n�mero de filas visibles de los JList.
    jListQuestionsFile.setVisibleRowCount(5);

    //Establecimiento del tama�o y posici�n
    jListQuestionsFile.setBounds(new Rectangle(166, 45, 195, 75));

    //Establecimiento del modo de selecci�n de los JList.
    jListQuestionsFile.setSelectionMode(ListSelectionModel.
                                        MULTIPLE_INTERVAL_SELECTION);

    //Establecimiento de la funcionalidad de jListQuestionsFile.
    jListQuestionsFile.
        addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      public void valueChanged(javax.swing.event.ListSelectionEvent e) {
        if(jListQuestionsFile.getSelectedIndices().length > 0)
          OkButton.setEnabled(true);
        else
          OkButton.setEnabled(false);
      }
    });
  }



  /*
  NOMBRE: JDialogInit
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: El constructor de la clase a la que pertenece.
  LLAMA A: nada.
  RECIBE: void.
  DEVUELVE: void.
  FUNCI�N: Inicializa el JDialog. Establece el contenido de cada JPanel del
           JDialog.
  */

  private void JDialogInit() throws Exception
  {
    jPanelTitle.add(jLabelTitle, null);
    jPanelTitle.add(ContinueButton, null);
    jPanelTitle.add(OkButton, null);
    jPanelTitle.add(ExitButton, null);
    jPanelTitle.add(StopButton, null);
    jPanelTitle.add(jPanelMain, null);
    jPanelMain.add(jLabelCourse, null);
    jPanelMain.add(jComboCourse, null);
    jPanelMain.add(jLabelQuestionsFile, null);
    jPanelMain.add(jScrollPaneListQuestionsFile, null);

    if(update == true)
    {
      jPanelMain.add(jScrollPaneTextAreaStatus, null);
      jPanelMain.add(jProgressBarQuestions, null);
      jPanelMain.add(jProgressBarQuestionsFile, null);

      this.jPanelTitle.paintAll(this.jPanelTitle.getGraphics());
      this.jPanelMain.paintAll(jPanelMain.getGraphics());
      this.getContentPane().paintAll(this.getContentPane().getGraphics());

      //Establecimiento de las caracter�sticas del JDialog, tama�o...
      this.setSize(433, 490);
    }
    else
    {
      this.setSize(433, 305);
      this.setLocation(200, 150);
    }

    this.setResizable(false);
    this.setLocation(this.getLocation());
    this.setContentPane(jPanelTitle);
    this.setVisible(true);
    this.setModal(true);
    this.setTitle(title);
    this.setBackground(SystemColor.window);
  }



////////////////////////////////////////////////////////////////////////////////
////////// FUNCIONALIDADES DE LOS COMPONETES DEL JDIALOG ///////////////////////
////////////////////////////////////////////////////////////////////////////////




  /*
  NOMBRE: ContinueButtonActionPerformed.
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: Al hacer click sobre ContinueButton y por el m�todo
               OkButtonActionPerfomed.
  LLAMA A: nada.
  RECIBE: nada.
  DEVUELVE: void.
  FUNCI�N: Realiza la calibraci�n.
  */

void ContinueButtonActionPerformed()
  {
    //Deshabilitaci�n del OkButton, ContinueButton, jComboCourse,
    //jListQuestionsFile y jTextFieldIteration.
    OkButton.setEnabled(false);
    ContinueButton.setEnabled(false);
    ExitButton.setVisible(false);
    ExitButton.setEnabled(false);
    StopButton.setVisible(true);
    StopButton.setEnabled(true);
    jComboCourse.setFocusable(false);
    jListQuestionsFile.setFocusable(false);

    this.ContinueButton.paint(this.ContinueButton.getGraphics());
    this.OkButton.paint(this.OkButton.getGraphics());
    this.StopButton.paint(this.StopButton.getGraphics());

    workerUpdate = new Update(jComboCourse.getSelectedItem().
                                     toString().trim(),
                                     questionsFileNameVectorUpdate);
    if(workerUpdate == null || workerUpdate.isAlive() == false)
      workerUpdate.start();
    else
      workerUpdate.resume();


    //Establecimiento de la funcionalidad de StopButton.
    StopButton.removeAll();
    StopButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        workerUpdate.suspend();

        OkButton.setEnabled(true);
        ContinueButton.setEnabled(true);
        StopButton.setVisible(false);
        StopButton.setEnabled(false);
        ExitButton.setVisible(true);
        ExitButton.setEnabled(true);
      }
    });
  }


  /*
  NOMBRE: OkButtonActionPerformed.
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: Al hacer click sobre OkButton.
  LLAMA A: nada.
  RECIBE: nada.
  DEVUELVE: void.
  FUNCI�N: Prepara la informaci�n necesaria para actualizar los datos
           estad�sticos las preguntas del fichero de preguntas y modifica la
           configuraci�n del aspecto del JDialog.
  */

  void OkButtonActionPerformed()
  {
    id = true;
    question = null;

    if(update == false)
    {
      update = true;
      try{
        JButtonInit();
        JPanelInit();
        JDialogInit();
      }catch(Exception e){
      }
    }

    jScrollPaneTextAreaStatus.setVisible(true);
    jTextAreaStatus.setText("");

    jProgressBarQuestions.setValue(0);

    jTextAreaStatus.setVisible(true);
    jProgressBarQuestions.setVisible(true);

    //Llamada el m�todo que comienza o continua con la calibraci�n.
    ContinueButtonActionPerformed();
  }



  /*
  NOMBRE: ExitButtonActionPerformed.
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: Al hacer click sobre ExitButton.
  LLAMA A: nada.
  RECIBE: nada.
  DEVUELVE: void.
  FUNCI�N: Oculta el JDialog, eliminando los valores en el contenido.
  */

  void ExitButtonActionPerformed()
  {
    id = false;
    update = false;
    this.setVisible(false);

    //Indicaci�n de que al disparar el evento producido por el cambio en el
    //jCombocourse y el jListQuestionsFile, estos no realizen ninguna tarea.
    changeJComboCourse = false;

    //Borrado del contenido de los componentes del JDialog.
    jComboCourse.removeAllItems();
    jListQuestionsFile = new JList();

    changeJComboCourse = true;

    this.dispose();
  }



  /*
  NOMBRE: jComboCourseActionPerformed.
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: Al cambiar el valor mostrado por el jComboCourse.
  LLAMA A: Nada.
  RECIBE: Nada.
  DEVUELVE: void.
  FUNCI�N: Cambia el contenido del jListQuestionsFile, estableciendo en el
           mismo la lista de ficheros de preguntas correspondientes al curso
           actualmente mostrado en el jComboCourse.
  */

  void jComboCourseActionPerformed()
  {
    //Comprobaci�n de si es correcto realizar las tareas al ser disparado
    //este evento.
    if(changeJComboCourse == false)
      return;

    //Obtenci�n del nombre del curso actualmente mostrado en jComboCourse.
    String nameCourse = (String)jComboCourse.getSelectedItem();

    //Bucle para calcular la posici�n que ocupa el vector que contiene
    //la lista de ficheros de preguntas del curso mostrado en jComboCourse,
    //en el array de objetos Vector que contiene los nombres de todos los
    //ficheros de preguntas para cada curso propiedad del usuario.
    questionsFileNameVector = new Vector();
    int j = 0;
    for(j = 0; j < courseVector.size(); j++)
    {
      if(nameCourse.equals(((Course)courseVector.get(j)).getName()))
      {
        questionsFileNameVector = ((Course)courseVector.get(j)).
                                  getQuestionsFileNames();
        break;
      }
    }

    JButtonInit();
    JListInit();
    JPanelInit();
    try{
      JDialogInit();
    }catch(Exception e){
    }
  }



  /*
  NOMBRE: update
  PERTENECE A: Clase DlgUpdateStatisticData.
  LLAMADA POR: El Constructor de la clase Update.
  LLAMA A: nada.
  RECIBE:
            courseName: Objeto de tipo String. Contiene el nombre del curso.
            questionsFileName: Objeto de tipo String. Contiene el nombre
                               del fichero de preguntas a actualizar.
            numberOfQuestionsInFile: Objeto de tipo int. Contiene el n�mero
                                     total de preguntas que hay en el fichero.
  DEVUELVE: void.
  FUNCI�N: Realiza la calibraci�n
  */

 private void update(String courseName, String questionsFileName,
                     int numberOfQuestionsInFile)
 {
   //Mensaje para indicar el comienzo de la calibraci�n.
   jTextAreaStatus.append("\n" + "Starting the update...");
   jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());

   jTextAreaStatus.append("\n" + "This operation can take several minutes...");
   jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());

   jProgressBarQuestions.setValue(0);
   jProgressBarQuestions.setMinimum(0);
   jProgressBarQuestions.setMaximum(numberOfQuestionsInFile);

   //Bucle para la obtenci�n de las preguntas del fichero de preguntas.
   for(int qCont = 0; qCont < numberOfQuestionsInFile; qCont++)
   {
     numberOfUses = 0;
     numberOfSuccesses = 0;
     timeToAnswer = 0;
     //successRate = 0;
     //exhibitionRate = 0;

     jProgressBarQuestions.setValue(qCont + 1);

     //Obtenci�n de una pregunta del fichero.
     question = ((TestEditor)parent).getQuestionByOrder(courseName,
                                     questionsFileName,  qCont);
     jTextAreaStatus.append("\n");

     //Mensaje indicando la pregunta que se esta calibrando.
     jTextAreaStatus.append("\n" + "Item file: " +
                            question.getQuestionsFileName() +
                            ". Code question: " +
                            question.getCodeQuestion());
     jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                      getLength());

     //Bucle para la obtenci�n de los nombres de los test de esta pregunta.
     Vector testVector = new Vector();
     if (question.getClassicTestVector() != null &&
         question.getClassicTestVector().isEmpty() == false)
     {
       for (int tCont = 0; tCont < question.getClassicTestVector().size();
            tCont++) {
         testVector.add(question.getClassicTestVector().get(tCont).toString().
                        substring(question.getClassicTestVector().get(tCont).
                        toString().lastIndexOf("/") + 1,
                        question.getClassicTestVector().get(tCont).toString().
                        lastIndexOf(".")));
       }
     }

     if (question.getAdaptiveTestVector() != null &&
         question.getAdaptiveTestVector().isEmpty() == false) {
       for(int tCont = 0; tCont < question.getAdaptiveTestVector().size();
            tCont++) {
         testVector.add(question.getAdaptiveTestVector().get(tCont).toString().
                        substring(question.getAdaptiveTestVector().get(tCont).
                                  toString().lastIndexOf("/") + 1,
                        question.getAdaptiveTestVector().get(tCont).toString().
                        lastIndexOf(".")));
       }
     }

     if(testVector == null || testVector.isEmpty())
       continue;
     else
     {
       //Mensaje indicando el n�mero de test de los que forma parte la
       //pregunta.
       jTextAreaStatus.append("\n" + "Number of test to examine: " +
                              testVector.size());
       jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                        getLength());
     }

     studentTestVector = new Vector();
     Vector testFileNameVector = new Vector();

     //Bucle para la obtenci�n de los datos de los test de los que forma
     //parte la pregunta.
     for(int tCont = 0; tCont < testVector.size(); tCont++)
     {
       //Obtenci�n de los datos del test.
       test = ((TestEditor)parent).getTest(jComboCourse.getSelectedItem().
                                           toString().trim(),
                                           testVector.get(tCont).toString());
       if(test == null)
         continue;

       //Vector con los alumnos que han realizado el test.
       Vector studentTestVectorAux = test.getStudentVector();

       if(studentTestVectorAux != null && !studentTestVectorAux.isEmpty())
         for(int i = 0; i < studentTestVectorAux.size(); i++)
         {
           studentTestVector.add(studentTestVectorAux.get(i));
           testFileNameVector.add(test.getTestFileName());
         }
     }

     if(studentTestVector == null || studentTestVector.isEmpty())
       continue;
     else
     {
       //Mensaje indicando el n�mero de estudiantes a exaimar
       jTextAreaStatus.append("\n" + "Number of students to examine: " +
                              studentTestVector.size());
       jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                        getLength());
     }

     jTextAreaStatus.append("\n");
     jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                      getLength());

     //C�lculo del n�mero de los datos para actualizar los datos
     //estad�sticos de la pregunta.

     //Mensaje para indicar el c�lculo de los datos estad�sticos.
     jTextAreaStatus.append("\n" +
     "Calculating the statistical data...");
     jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                      getLength());
     //Bucle para examinar cada alumno.
     for(int k = 0; k < studentTestVector.size(); k++)
     {
       Student student = ((TestEditor)parent).getStudentAndFinishTest(
                                              jComboCourse.getSelectedItem().
                                              toString().trim(),
                                              ((StudentTest)studentTestVector.
                                              get(k)).getLogin(),
                                              testFileNameVector.get(k).
                                              toString().trim());

       //Mensaje para indicar el estudiante que se est� examinando.
       jTextAreaStatus.append("\n" + student.getLogin() + "...");
       jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                        getLength());

       Vector testLogStudentVector = student.getTest();
       TestLogStudent testLogStudent = (TestLogStudent)
                                          testLogStudentVector.
                                          firstElement();

       EvalTestLogStudent evalTestLogStudent = (EvalTestLogStudent)
                                                  testLogStudent.
                                                  getEvaluatedTest().
                                                  firstElement();

       Vector questionsFileTestVector = evalTestLogStudent.getTestVector();
       QuestionsFileTest questionsFileTest = null;
       boolean found = false;

       //Bucle para la b�squeda del fichero de preguntas.
       for(int r = 0; r < questionsFileTestVector.size(); r++)
       {
         questionsFileTest=(QuestionsFileTest)questionsFileTestVector.get(r);
         if(questionsFileTest.getQuestionsFileName().
            equals(question.getQuestionsFileName()))
         {
           found = true;
           break;
         }
       }

       if(found == true)
       {
         Vector codeQuestionVector = questionsFileTest.getCodeQuestions();
         //Busqueda de la pregunta.
         for(int r = 0; r < codeQuestionVector.size(); r++)
         {
           String codeQuestion = codeQuestionVector.get(r).toString();
           if(codeQuestion.trim().equals(question.getCodeQuestion()))
           {
             if(questionsFileTest.getDone().get(r).toString().trim().
                equals("true"))
             {
               numberOfUses++;
               timeToAnswer = timeToAnswer + Integer.valueOf(questionsFileTest.
                                             getTimes().get(r).toString().
                                             trim()).intValue();

               //Comprobaci�n de si es correcta.
               if(questionsFileTest.getAnswersValues().get(r).toString().
                  trim().equals("correct"))
                 numberOfSuccesses++;

               break;
             }
           }
         }
       }
     }

     //Mensaje para indicar que se comienza el calculo.
     jTextAreaStatus.append("\n" + "Updating...");
     jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                      getLength());

     //AQU� VAN LAS F�RMULAS.
     double exhibitionRateOld = question.getExhibitionRate();
     double successRateOld = question.getSuccessRate();
     int timeToAnswerOld = question.getAnswerTime();

     question.setNumberOfUses(numberOfUses);
     question.setNumberOfSuccesses(numberOfSuccesses);
     question.setAnswerTime(Integer.valueOf(String.valueOf(
                            Math.round(timeToAnswer /
                            numberOfUses*1.0))).intValue());
     question.setSuccessRate(numberOfSuccesses*1.0 / numberOfUses*1.0);
     question.setExhibitionRate(numberOfUses*1.0 / studentTestVector.size()*1.0);

     if(question.getPathImage().equals("") == false)
       question.setPathImage(question.getPathImage().substring(
                             question.getPathImage().
                             lastIndexOf(".")));

     //Envio de los datos modificados.
     String modify = ((TestEditor)parent).modifyQuestion(question, null);
     if(modify == null)
     {
       jTextAreaStatus.append("\n" + "ERROR: not modified data.");
       jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                        getLength());
     }
     else
     {
       String exhibitionRateString = String.valueOf(question.
                                     getExhibitionRate());
       if(exhibitionRateString.length() > 6)
         exhibitionRateString = exhibitionRateString.substring(0, 7);

       String successRateString = String.valueOf(question.getSuccessRate());
       if(successRateString.length() > 6)
         successRateString = successRateString.substring(0, 7);



       String exhibitionRateOldString = String.valueOf(exhibitionRateOld);
       if(exhibitionRateOldString.length() > 6)
         exhibitionRateOldString = exhibitionRateOldString.substring(0, 7);

       String successRateOldString = String.valueOf(successRateOld);
       if(successRateOldString.length() > 6)
         successRateOldString = successRateOldString.substring(0, 7);


       jTextAreaStatus.append("\n" + "Exhibition rate old: " +
                              exhibitionRateOldString +
                              " Exhibition rate new: " +
                              exhibitionRateString +
                              "\n" +
                              "Success rate old: " +
                              successRateOldString +
                              " Success rate new: " +
                              successRateString +
                              "\n" +
                              "Time to answer old: " +
                              String.valueOf(timeToAnswerOld) +
                              " Time to answer new: " +
                              String.valueOf(question.getAnswerTime()) +
                              "\n" + "OK");
       jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                        getLength());
     }
   }

   jTextAreaStatus.append("\n");
   jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());

   jTextAreaStatus.append("\n" + "FINISHED");
   jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());

   //Habilitaci�n del OkButton, ContinueButton, jComboCourse, jListQuestionsFile
   //y jTextFieldIteration.
   OkButton.setEnabled(true);
   ContinueButton.setEnabled(false);
   jComboCourse.setFocusable(true);
   jListQuestionsFile.setFocusable(true);
 }



  /*
  NOMBRE: GetQuestionDataByOrder.
  FUNCION: Clase contenida dentro de la clase DlgUpdateStatisticData. Es llamada
           por el m�todo OkButtonActionPerformed y el m�todo
           ContinueButtonActionPerformed. Se encarga de invocar
           al m�todo de la clase padre (TestEditor) que conecta con el
           servidor para obtener los datos de una pregunta de un fichero de
           de preguntas.
  */

  class GetQuestionDataByOrder extends Thread
  {
    String courseName = "";
    String questionsFileName = "";
    int order = 0;

    public GetQuestionDataByOrder(String courseName, String questionsFileName,
                                  int order)
    {
      this.courseName = courseName;
      this.questionsFileName = questionsFileName;
      this.order = order;
    }

    public void run()
    {
      taskDone = false;

      try{
        Thread.sleep(100);
      }catch(java.lang.InterruptedException e){
      }

      //Llamada al m�todo de la clase padre que devuelve los datos de una
      //pregunta para ser actualizados.
      question = ((TestEditor)parent).getQuestionByOrder(courseName,
                                                         questionsFileName,
                                                         order);
      taskDone = true;
    }
  }



  /*
  NOMBRE: Update.
  FUNCION: Clase contenida dentro de la clase DlgUpdateStatisticData. Es llamada
           por el m�todo ContinueButtonActionPerformed. Se encarga de invocar
           al m�todo update, de la misma clase donde est� contenida esta.
  */

  class Update extends Thread
  {
    String courseName = "";
    Vector questionsFileNameVectorUpdate = null;

    public Update(String courseName, Vector questionsFileNameVectorUpdate)
    {
      this.courseName = courseName;
      this.questionsFileNameVectorUpdate = questionsFileNameVectorUpdate;
    }

    public void run()
    {
      taskDone = false;

      try{
        Thread.sleep(100);
      }catch(java.lang.InterruptedException e){
      }

      jProgressBarQuestionsFile.setMinimum(0);
      jProgressBarQuestionsFile.setValue(0);
      jProgressBarQuestionsFile.setMaximum(
                                questionsFileNameVectorUpdate.size());
      jProgressBarQuestionsFile.setVisible(true);

      while(questionsFileNameVectorUpdate.isEmpty() == false)
      {
        jProgressBarQuestionsFile.setValue(jProgressBarQuestionsFile.
                                           getValue() + 1);

        String questionsFileName = questionsFileNameVectorUpdate.get(0).
            toString().trim();

        jTextAreaStatus.append("\n" + "Loading question file data... " +
                               "\n" + questionsFileName);
        jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                         getLength());

        Question question = ((TestEditor)parent).getQuestionByOrder(courseName,
                                                          questionsFileName, 0);
        //Si la respuesta del servidor es null, se muestra un mensaje de error.
        if (question != null)
        {
          int numberOfQuestionsInFile = question.getTotalQuestionsInFile();

          //Mostrado del n�mero de preguntas que tiene el fichero para actualizar.
          jTextAreaStatus.append("\n" + "The file has " +
                                 String.valueOf(numberOfQuestionsInFile)
                                 + " questions to update.");
          jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                           getLength());

          //Habilitaci�n y mostrado del ContinueButton.
          ContinueButton.setVisible(true);

          //Llamada al m�todo encargado de actualizar.
          update(courseName, questionsFileName, numberOfQuestionsInFile);
        }
        else
        {
          //Mensaje para indicar al usuario que se ha producido un error al
          //solicitar los datos de una pregunta del fichero a actualizar.
          jTextAreaStatus.append("\n" + "ERROR WHEN CONNECTING TO THE SERVER.");
          jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                           getLength());
          jTextAreaStatus.append("\n"+"It is possible that the file is empty.");
          jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                           getLength());
          jProgressBarQuestions.setValue(0);
        }

        //Borrado del elemento ya analizado.
        questionsFileNameVectorUpdate.removeElementAt(0);
        jTextAreaStatus.append("\n"+"****************************************");
        jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().
                                         getLength());
        jProgressBarQuestions.setValue(0);

        taskDone = true;
      }

      StopButton.setVisible(false);
      StopButton.setEnabled(false);
      ExitButton.setVisible(true);
      ExitButton.setEnabled(true);
    }
  }
}