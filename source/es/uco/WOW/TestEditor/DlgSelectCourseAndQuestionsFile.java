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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import es.uco.WOW.Utils.Course;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/*
 NOMBRE: DlgSelectCourseAndQuestionsFile.
 FUNCION: JDialog que muestra al usuario la lista de cursos de los que es
          propietario y la lista de nombres de ficheros de preguntas para
          cada uno de los cursos anteriores. Tambi�n se proporciona informaci�n
          adicional como el n�mero de respuetas por defecto que tienen las
          preguntas del fichero de preguntas seleccionado para un curso.
 LAST MODIFICATION: 06-02-2008
*/

public class DlgSelectCourseAndQuestionsFile extends JDialog
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//Variables de usuario.
  private Vector courseNameVector = new Vector(); //Almacena la lista de nombres
                                                  //cursos pertenecientes al
                                                  //usuario.
  private Vector courseVector; //Almacena la lista de nombres de los
                                 //ficheros de preguntas para cada curso.
  //private String courseName = ""; //Almacena el curso seleccionado.
  //private String questionsFileName = ""; //Almacena el nombre del fichero de
                                         //preguntas seleccionado.

  //Componentes de la ventana.
  private JPanel jPanelTitle; //JPanel que muestra el t�tulo en jPanelMain.
  private JPanel jPanelMain; //JPanel principal del JDialog.

  private JLabel jLabelTitle;
  private JLabel jLabelCourse;
  private JLabel jLabelQuestionsFile;
  private JLabel jLabelNumAnswers;

  public boolean id = false;
  public boolean changeJComboQuestionsFile = true;
  public boolean changeJComboCourse = true;

  private JButton OkButton;
  private JButton CancelButton;

  public JComboBox jComboCourse;
  public JComboBox jComboQuestionsFile;

  public JTextField jTextFieldNumAnswers;



  /*
  NOMBRE: DlgSelectCourseAndQuestionsFile (Constructor de esta misma clase).
  PERTENECE A: Clase DlgSelectCourseAndQuestionsFile.
  LLAMADA POR: Al invocar la clase.
  LLAMA A:  JPanelInit();
            JLabelInit();
            JButtonInit();
            JTextFieldInit();
            JComboInit();
            JDialogInit();
  RECIBE:
          title: Objeto de tipo String. Indica el t�tulo del JDialog.
          courseVector: Objeto de tipo Vector. Contiene objeto de tipo
                           Course que contienen los nombres de los ficheros
                           de preguntas de cada curso.
          parent: Objeto de tipo JFrame. Hace referencia al JFrame que invoc� a
                este JDialog, osea su padre.
  DEVUELVE: nada.
  FUNCI�N: Constructor de la clase DlgSelectCourseAndQuestionsFile. Se encarga
           de configurar el aspecto del dialogo, su funcionalidad, asi como de
           inicializar los componentes que componen el mismo.
  */

  public DlgSelectCourseAndQuestionsFile(String title,
                                         Vector courseVector, JFrame parent)
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
    this.courseVector = courseVector;

    this.courseNameVector = new Vector();

    for(int i = 0; i < courseVector.size(); i++)
      this.courseNameVector.add(((Course)courseVector.get(i)).getName().trim());


    //Inicializaci�n de los componentes de la clase.
    try{
      JPanelInit();
      JLabelInit();
      JButtonInit();
      JTextFieldInit();
      JComboInit();
      JDialogInit();

      //Establecimiento de las caracter�sticas del JDialog, tama�o...
      this.setSize(433, 255);
      this.setResizable(false);
      this.setLocation(200, 150);
      this.getContentPane().add(jPanelTitle);
      this.setVisible(true);
      this.setModal(true);
      this.setTitle(title);
      this.setBackground(SystemColor.window);
    }catch (Exception e){
    }
  }



////////////////////////////////////////////////////////////////////////////////
////////// INICIALIZACI�N DE LOS COMPONENTES DEL JDIALOG ///////////////////////
////////////////////////////////////////////////////////////////////////////////



  /*
  NOMBRE: JPanelInit
  PERTENECE A: Clase DlgSelectCourseAndQuestionsFile.
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

    //Establecimiento de sus tama�os, bordes y layout.
    jPanelTitle.setLayout(null);
    jPanelMain.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanelMain.setBounds(new Rectangle(10, 50, 407, 110));
    jPanelMain.setLayout(null);
  }



  /*
  NOMBRE: JLabelInit
  PERTENECE A: Clase DlgSelectCourseAndQuestionsFile.
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
    jLabelNumAnswers = new JLabel();

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

    jLabelNumAnswers.setBounds(new Rectangle(20, 77, 420, 17));
    jLabelNumAnswers.setText("Minimum number of answers for each question:");
    jLabelNumAnswers.setFont(new java.awt.Font("Dialog", 1, 12));
  }



  /*
  NOMBRE: JButtonInit
  PERTENECE A: Clase DlgSelectCourseAndQuestionsFile.
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
    OkButton = new JButton();
    CancelButton = new JButton();

    //Establecimiento del tama�o, textos y tipo de letra para OkButton.
    OkButton.setBounds(new Rectangle(195, 165, 100, 30));
    OkButton.setText("Ok");
    OkButton.setFont(new java.awt.Font("Button", 1, 12));

    //Establecimiento de la funcionalidad de OkButton.
    OkButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        OkButtonActionPerformed(e);
      }
    });

    //Establecimiento del tama�o, texto y tipo de letra para CancelButton.
    CancelButton.setBounds(new Rectangle(310, 165, 100, 30));
    CancelButton.setText("Cancel");
    CancelButton.setFont(new java.awt.Font("Button", 1, 12));

    //Establecimiento de la funcionalidad de CancelButton.
    CancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        CancelButtonActionPerformed(e);
      }
    });
  }



  /*
  NOMBRE: JTextFieldInit
  PERTENECE A: Clase DlgSelectCourseAndQuestionsFile.
  LLAMADA POR: El constructor de la clase a la que pertenece.
  LLAMA A: nada.
  RECIBE: void.
  DEVUELVE: void.
  FUNCI�N: Inicializa los JTextField del JDialog, reservando memoria para los
           mismo y estableciendo su tama�o y su funcionalidad.
  */

  private void JTextFieldInit()
  {
    //Reserva de memoria para el JTextFieldNewQuestionsFile.
    jTextFieldNumAnswers = new JTextField();

    //Establecimiento de su tama�o y funcionalidad.
    jTextFieldNumAnswers.setBounds(new Rectangle(310, 75, 71, 25));
    jTextFieldNumAnswers.setFocusable(false);
  }



  /*
  NOMBRE: JComboInit
  PERTENECE A: Clase DlgSelectCourseAndQuestionsFile.
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
    jComboQuestionsFile = new JComboBox();

    //Borrado de sus contenidos previos.
    jComboCourse.removeAllItems();
    jComboQuestionsFile.removeAllItems();

    //Establecimiento de sus tama�os.
    jComboCourse.setBounds(new Rectangle(86, 15, 295, 25));
    jComboQuestionsFile.setBounds(new Rectangle(166, 45, 215, 25));

    //Adici�n a jComboCourse de la lista de cursos del usuario.
    for(int i = 0; i < courseNameVector.size(); i++)
      jComboCourse.addItem(courseNameVector.get(i));


    boolean initJTextField = false;

    //Adici�n a jComboQuestionsFile de la lista de ficheros de preguntas para
    //el curso mostrado por defecto y calculo del n�mero de respuestas por
    //defecto de dicho fichero de preguntas.
    Vector questionsFileNameVector = ((Course)courseVector.get(0)).
                                              getQuestionsFileNames();
    if(questionsFileNameVector != null && questionsFileNameVector.isEmpty() == false)
    {
      for(int i = 0; i < questionsFileNameVector.size(); i++)
        jComboQuestionsFile.addItem(questionsFileNameVector.get(i).toString());


      //Establecimiento del valor mostrado por jTextFieldNumAnswers.
      if(initJTextField == false)
      {
        jTextFieldNumAnswers.setText(((Course)courseVector.get(0)).
                                       getNumAnswersOfQuestionsFile().
                                        get(0).toString());
        initJTextField = true;
      }

      OkButton.setEnabled(true);
    }
    else
    {
      jComboQuestionsFile.removeAllItems();
      OkButton.setEnabled(false);
    }

    //Establecimiento de la funcionalidad de jComboCourse.
    jComboCourse.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jComboCourseActionPerformed();
      }
    });

    //Establecimiento de la funcionalidad de jComboQuestionsFile.
    jComboQuestionsFile.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jComboQuestionsFileActionPerformed();
      }
    });
  }



  /*
  NOMBRE: JDialogInit
  PERTENECE A: Clase DlgSelectCourseAndQuestionsFile.
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
    jPanelTitle.add(OkButton, null);
    jPanelTitle.add(CancelButton, null);
    jPanelTitle.add(jPanelMain, null);
    jPanelMain.add(jLabelCourse, null);
    jPanelMain.add(jComboCourse, null);
    jPanelMain.add(jLabelQuestionsFile, null);
    jPanelMain.add(jComboQuestionsFile, null);
    jPanelMain.add(jLabelNumAnswers, null);
    jPanelMain.add(jTextFieldNumAnswers, null);
  }



////////////////////////////////////////////////////////////////////////////////
////////// FUNCIONALIDADES DE LOS COMPONETES DEL JDIALOG ///////////////////////
////////////////////////////////////////////////////////////////////////////////



  /*
  NOMBRE: OkButtonActionPerformed.
  PERTENECE A: Clase DlgSelectCourseAndQuestionsFile.
  LLAMADA POR: Al hacer click sobre OkButton.
  LLAMA A: nada.
  RECIBE:
          e: Objeto de tipo ActionEvent. Contiene el evento que produjo
             la llamada a este m�todo.
  DEVUELVE: void.
  FUNCI�N: Oculta el JDialog, permaneciendo los valores en el contenido.
  */

  void OkButtonActionPerformed(ActionEvent e)
  {
    id = true;
    this.setVisible(false);
    this.dispose();
  }



  /*
  NOMBRE: CancelButtonActionPerformed.
  PERTENECE A: Clase DlgSelectCourseAndQuestionsFile.
  LLAMADA POR: Al hacer click sobre CancelButton.
  LLAMA A: nada.
  RECIBE:
          e: Objeto de tipo ActionEvent. Contiene el evento que produjo
          la llamada a este m�todo.
  DEVUELVE: void.
  FUNCI�N: Oculta el JDialog, eliminando los valores en el contenido.
  */

  void CancelButtonActionPerformed(ActionEvent e)
  {
    id = false;
    this.setVisible(false);

    //Indicaci�n de que al disparar el evento producido por el cambio en el
    //jCombocourse y el jComboQuestionsFile, estos no realizen ninguna tarea.
    changeJComboCourse = false;
    changeJComboQuestionsFile = false;

    //Borrado del contenido de los componentes del JDialog.
    jComboCourse.removeAllItems();
    jComboQuestionsFile.removeAllItems();

    changeJComboCourse = true;
    changeJComboQuestionsFile = true;

    jTextFieldNumAnswers.setText("");

    this.dispose();
  }



  /*
  NOMBRE: jComboCourseActionPerformed.
  PERTENECE A: Clase DlgSelectCourseAndQuestionsFile.
  LLAMADA POR: Al cambiar el valor mostrado por el jComboCourse.
  LLAMA A: Nada.
  RECIBE: Nada.
  DEVUELVE: void.
  FUNCI�N: Cambia el contenido del jComboQuestionsFile, estableciendo en el
           mismo la lista de ficheros de preguntas correspondientes al curso
           actualmente mostrado en el jComboCourse. Adem�s muestra en el
           jTextFieldNunAnswers el n�mero de respuetas por defecto para las
           preguntas del fichero de preguntas mostrado por defecto en el
           jComboQuestionsFile.
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

    Vector questionsFileNameVector = new Vector();
    Vector numAnswersOfQuestionsFile = new Vector();
    int j = 0;
    for(j = 0; j < courseVector.size(); j++)
    {
      if(nameCourse.equals(((Course)courseVector.get(j)).
                           getName().trim()))
      {
        questionsFileNameVector = ((Course)courseVector.get(j)).
                                           getQuestionsFileNames();
        numAnswersOfQuestionsFile = ((Course)courseVector.get(j)).
                                             getNumAnswersOfQuestionsFile();
        break;
      }
    }

    //Si la lista de ficheros de preguntas para el curso mostrado en
    //jComboCourse no es vac�a, entonces se muestra dicha lista, en caso
    //contrario, el contenido del jComboQuestionsFile queda vac�a, al igual que
    //el jTextNumAnswers y adem�s se deshabilita el OkButton.
    if(questionsFileNameVector != null && questionsFileNameVector.isEmpty() == false &&
       numAnswersOfQuestionsFile != null &&
       numAnswersOfQuestionsFile.isEmpty() == false)
    {
      //Indicaci�n de que al disparar el evento producido por el cambio en el
      //jComboBoxQuestionsFile, este no realize ninguna tarea.
      changeJComboQuestionsFile = false;

      //Eliminado del contenido previo del jComboQuestionsFile.
      jComboQuestionsFile.removeAllItems();

      //Indicaci�n de que al disparar el evento producido por el cambio en el
      //jComboBoxQuestionsFile, este ya puede realizar sus tareas.
      changeJComboQuestionsFile = true;

      boolean initJTextField = false;

      //Bucle para la adici�n de los
      //nombres de los ficheros de preguntas, correspondientes al curso
      //mostrado en jComboCourse, en jComboQuestionsFile.
      for (int i = 0; i < questionsFileNameVector.size(); i++)
      {
        jComboQuestionsFile.addItem(questionsFileNameVector.get(i).toString());
      }

      //Establecimiento del valor mostrado por jTextFieldNumAnswers.
      if(initJTextField == false)
      {
        jTextFieldNumAnswers.setText(numAnswersOfQuestionsFile.get(0).toString());
        initJTextField = true;
      }

      //Habilitacion de OkButton.
      OkButton.setEnabled(true);
    }
    else
    {
      //Borrado del contenido previo de jComboQuestionsFile,
      //de jTextFieldNumAnswers y deshabilitaci�n del OkButton.
      changeJComboQuestionsFile = false;
      jComboQuestionsFile.removeAllItems();
      changeJComboQuestionsFile = true;
      jTextFieldNumAnswers.setText("");
      OkButton.setEnabled(false);
    }
  }



  /*
  NOMBRE: jComboQuestionsFileActionPerformed.
  PERTENECE A: Clase DlgSelectCourseAndQuestionsFile.
  LLAMADA POR: Al cambiar el valor mostrado por el jComboQuestionsFile.
  LLAMA A: Nada.
  RECIBE: Nada.
  DEVUELVE: void.
  FUNCI�N: Cambia el contenido de jTextFieldNumAnswers seg�n el nombre
           del fichero de preguntas mostrado actualmente en jComboQuestionsFile.
  */

  void jComboQuestionsFileActionPerformed()
  {
    //Comprobaci�n de si es correcto realizar las tareas al ser disparado
    //este evento.
    if(changeJComboQuestionsFile == false)
      return;

    //Obtenci�n del curso y nombre del fichero de preguntas mostrados
    //actualmente en jComboCourse y jComboQuestionsFile.
    String nameCourse = jComboCourse.getSelectedItem().toString();
    String nameQuestionsFile = jComboQuestionsFile.getSelectedItem().toString();

    //Bucle para calcular la posici�n que ocupa el vector que contiene
    //la lista de ficheros de preguntas del curso mostrado en jComboCourse,
    //en el array de objetos Vector que contiene los nombres de todos los
    //ficheros de preguntas para cada curso propiedad del usuario.
    Vector questionsFileNameVector = new Vector();
    Vector numAnswersOfQuestionsFile = new Vector();
    int j = 0;
    for(j = 0; j < courseVector.size(); j++)
    {
      if(nameCourse.equals(((Course)courseVector.get(j)).getName()))
      {
        questionsFileNameVector = ((Course)courseVector.get(j)).
                                           getQuestionsFileNames();
        numAnswersOfQuestionsFile = ((Course)courseVector.get(j)).
                                             getNumAnswersOfQuestionsFile();
        break;
      }
    }

    //Si la lista de nombres de ficheros de preguntas para el curso actualmente
    //mostrado en jComboCourse no es vac�a, entonces se rellena el
    //jTextFieldNumAnswers con el valor correspondiente.
    if(questionsFileNameVector != null && questionsFileNameVector.isEmpty() == false &&
       numAnswersOfQuestionsFile != null &&
       numAnswersOfQuestionsFile.isEmpty() == false)
    {
      for (int i = 0; i < questionsFileNameVector.size(); i++)
      {
        if(nameQuestionsFile.equals(questionsFileNameVector.get(i).toString()))
        {
          jTextFieldNumAnswers.setText(numAnswersOfQuestionsFile.get(i).
                                       toString());
          break;
        }
      }
    }
    else
      jTextFieldNumAnswers.setText("");
  }
}