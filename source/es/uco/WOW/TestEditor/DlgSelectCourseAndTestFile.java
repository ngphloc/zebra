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
 NOMBRE: DlgSelectCourseAndTestFile.
 FUNCION: JDialog que muestra al usuario la lista de cursos de los que es
          propietario y la lista de nombres de ficheros de test para
          cada uno de los cursos anteriores.
 LAST MODIFICATION: 06-02-2008
*/

public class DlgSelectCourseAndTestFile extends JDialog
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//Variables de usuario.
  private Vector courseVector; //Almacena la lista de nombres de los
                                  //ficheros de test para cada curso.
  //private String courseName = ""; //Almacena el curso seleccionado.
  //private String testFile = ""; //Almacena el nombre del fichero de
                                //test seleccionado.

  //Componentes de la ventana.
  private JPanel jPanelTitle; //JPanel que muestra el t�tulo en jPanelMain.
  private JPanel jPanelMain; //JPanel principal del JDialog.

  private JLabel jLabelTitle;
  private JLabel jLabelCourse;
  private JLabel jLabelTestFile;

  public boolean id = false;
  public boolean changeJComboTestFile = true;
  public boolean changeJComboCourse = true;

  private JButton OkButton;
  private JButton CancelButton;

  public JComboBox jComboCourse;
  public JComboBox jComboTestFile;




  /*
  NOMBRE: DlgSelectCourseAndTestFile (Constructor de esta misma clase).
  PERTENECE A: Clase DlgSelectCourseAndTestFile.
  LLAMADA POR: Al invocar la clase.
  LLAMA A:  JPanelInit();
            JLabelInit();
            JButtonInit();
            JComboInit();
            JDialogInit();
  RECIBE:
          title: Objeto de tipo String. Indica el t�tulo del JDialog.
          courseVector: Objeto de tipo Vector. Contiene en cada posici�n
                           un objeto de tipo Course con los datos usados
                           del curso.
          parent: Objeto de tipo JFrame. Hace referencia al JFrame que invoc� a
                este JDialog, osea su padre.

  DEVUELVE: nada.
  FUNCI�N: Constructor de la clase DlgSelectCourseAndTestFile. Se encarga
           de configurar el aspecto del dialogo, su funcionalidad, asi como de
           inicializar los componentes que componen el mismo.
  */

  public DlgSelectCourseAndTestFile(String title, Vector courseVector,
                                    JFrame parent)
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

    //Inicializaci�n de los componentes de la clase.
    try{
      JPanelInit();
      JLabelInit();
      JButtonInit();
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
  PERTENECE A: Clase DlgSelectCourseAndTestFile.
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
  PERTENECE A: Clase DlgSelectCourseAndTestFile.
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
    jLabelTestFile = new JLabel();

    //Establecimiento de sus tama�os, textos y  bordes.
    jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 16));
    jLabelTitle.setBorder(BorderFactory.createLoweredBevelBorder());
    jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelTitle.setText(this.getTitle());
    jLabelTitle.setBounds(new Rectangle(10, 5, 407, 44));

    jLabelCourse.setBounds(new Rectangle(20, 17, 50, 17));
    jLabelCourse.setText("Course:");
    jLabelCourse.setFont(new java.awt.Font("Dialog", 1, 12));

    jLabelTestFile.setBounds(new Rectangle(20, 47, 140, 17));
    jLabelTestFile.setText("Test file name:");
    jLabelTestFile.setFont(new java.awt.Font("Dialog", 1, 12));
  }



  /*
  NOMBRE: JButtonInit
  PERTENECE A: Clase DlgSelectCourseAndTestFile.
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
  NOMBRE: JComboInit
  PERTENECE A: Clase DlgSelectCourseAndTestFile.
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
    jComboTestFile = new JComboBox();

    //Borrado de sus contenidos previos.
    jComboCourse.removeAllItems();
    jComboTestFile.removeAllItems();

    //Establecimiento de sus tama�os.
    jComboCourse.setBounds(new Rectangle(86, 15, 295, 25));
    jComboTestFile.setBounds(new Rectangle(117, 45, 264, 25));

    //Adici�n a jComboCourse de la lista de cursos del usuario.
    for(int i = 0; i < courseVector.size(); i++)
      jComboCourse.addItem(((Course)courseVector.get(i)).getName());


    Vector testFileNameVector=((Course)courseVector.get(0)).getTestsFileNames();

    if(testFileNameVector != null && testFileNameVector.isEmpty() == false)
    {
      //Adici�n a jComboTestFile de la lista de ficheros de test para
      //el curso mostrado por defecto.
      for(int i = 0; i < testFileNameVector.size(); i++)
        jComboTestFile.addItem(testFileNameVector.get(i).toString());

      OkButton.setEnabled(true);
    }
    else
    {
      jComboTestFile.removeAllItems();
      OkButton.setEnabled(false);
    }


    //Establecimiento de la funcionalidad de jComboCourse.
    jComboCourse.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jComboCourseActionPerformed();
      }
    });
  }



  /*
  NOMBRE: JDialogInit
  PERTENECE A: Clase DlgSelectCourseAndTestFile.
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
    jPanelMain.add(jLabelTestFile, null);
    jPanelMain.add(jComboTestFile, null);
  }



////////////////////////////////////////////////////////////////////////////////
////////// FUNCIONALIDADES DE LOS COMPONETES DEL JDIALOG ///////////////////////
////////////////////////////////////////////////////////////////////////////////



  /*
  NOMBRE: OkButtonActionPerformed.
  PERTENECE A: Clase DlgSelectCourseAndTestFile.
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
  PERTENECE A: Clase DlgSelectCourseAndTestFile.
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
    changeJComboTestFile = false;

    //Borrado del contenido de los componentes del JDialog.
    jComboCourse.removeAllItems();
    jComboTestFile.removeAllItems();

    changeJComboCourse = true;
    changeJComboTestFile = true;

    this.dispose();
  }



  /*
  NOMBRE: jComboCourseActionPerformed.
  PERTENECE A: Clase DlgSelectCourseAndTestFile.
  LLAMADA POR: Al cambiar el valor mostrado por el jComboCourse.
  LLAMA A: Nada.
  RECIBE: Nada.
  DEVUELVE: void.
  FUNCI�N: Cambia el contenido del jComboTestFile, estableciendo en el
           mismo la lista de ficheros de test correspondientes al curso
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
    //la lista de ficheros de test del curso mostrado en jComboCourse,
    //en el array de objetos Vector que contiene los nombres de todos los
    //ficheros de test para cada curso propiedad del usuario.
    Vector testFileNameVector = new Vector();
    int j = 0;
    for(j = 0; j < courseVector.size(); j++)
    {
      if(nameCourse.equals(((Course)courseVector.get(j)).getName()))
      {
        testFileNameVector = ((Course)courseVector.get(j)).getTestsFileNames();
        break;
      }
    }

    //Si la lista de ficheros de test para el curso mostrado en
    //jComboCourse no es vac�a, entonces se muestra dicha lista, en caso
    //contrario, el contenido del jComboTestFile queda vac�a,
    // y adem�s se deshabilita el OkButton.
    if(testFileNameVector != null && testFileNameVector.isEmpty() == false)
    {
      //Eliminado del contenido previo del jComboTestFile.
      jComboTestFile.removeAllItems();

      for (int i = 0; i < testFileNameVector.size(); i++)
        jComboTestFile.addItem(testFileNameVector.get(i).toString());

      //Habilitacion de OkButton.
      OkButton.setEnabled(true);
    }
    else
    {
      //Borrado del contenido previo de jComboTestFile,
      // y deshabilitaci�n del OkButton.
      jComboTestFile.removeAllItems();
      OkButton.setEnabled(false);
    }
  }
}
