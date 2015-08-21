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

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/*
 NOMBRE: DlgSelectCourse.
 FUNCION: JDialog que muestra al usuario la lista de cursos de los que es
          propietario para que este seleccione uno.
 LAST MODIFICATION: 06-02-2008
*/

public class DlgSelectCourse extends JDialog
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//Variables de usuario.
  private Vector courseNameVector = new Vector(); //Almacena la lista de cursos
                                                  //pertenecientes al usuario.
  //Componentes de la ventana.
  private JPanel jPanelTitle; //JPanel que muestra el t�tulo en jPanelMain.
  private JPanel jPanelMain; //JPanel principal del JDialog.

  private JLabel jLabelTitle;
  private JLabel jLabelCourse;

  public boolean id = false;

  private JButton OkButton;
  private JButton CancelButton;

  public JComboBox jComboCourse;



  /*
  NOMBRE: DlgSelectCourse (Constructor de esta misma clase).
  PERTENECE A: Clase DlgSelectCourse.
  LLAMADA POR: Al invocar la clase.
  LLAMA A:  JPanelInit();
            JLabelInit();
            JButtonInit();
            JComboInit();
            JDialogInit();
  RECIBE:
          title: Objeto de tipo String. Indica el t�tulo del JDialog.
          courseNameVector: Objeto de tipo Vector. Contiene una lista con
                            el nombre de los cursos propiedad del usuario.
          parent: Objeto de tipo JFrame. Hace referencia al JFrame que invoc� a
                este JDialog, osea su padre.
  DEVUELVE: nada.
  FUNCI�N: Constructor de la clase DlgSelectCourse. Se encarga
           de configurar el aspecto del dialogo, su funcionalidad, asi como de
           inicializar los componentes que componen el mismo.
   */

  public DlgSelectCourse(String title, Vector theCourseNameVector, JFrame parent) {
    //Llamada al constructor de la clase padre.
    super(parent, title, true);

    //Establecimiento del aspecto del JDialog.
    try{
      UIManager.setLookAndFeel(((TestEditor)parent).lookAndFeelClassName);
    }catch(Exception e){
    }


    //Establecimiento de los valores de las variables propias de la clase
    //con los recibidos.
    this.courseNameVector = theCourseNameVector;

    //Inicializaci�n de los componentes de la clase.
    try {
      JPanelInit();
      JLabelInit();
      JButtonInit();
      JComboInit();
      JDialogInit();

      //Establecimiento de las caracter�sticas del JDialog, tama�o...
      this.setSize(433, 215);
      this.setResizable(false);
      this.setLocation(200, 150);
      this.getContentPane().add(jPanelTitle);
      this.setVisible(true);
      this.setModal(true);
      this.setTitle(title);
      this.setBackground(SystemColor.window);
    }catch (Exception e) {
    }
  }



///////////////////////////////////////////////////////////////////////////////
////////// INICIALIZACI�N DE LOS COMPONENTES DEL JDIALOG ///////////////////////
////////////////////////////////////////////////////////////////////////////////



  /*
  NOMBRE: JPanelInit
  PERTENECE A: Clase DlgSelectCourse.
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
    jPanelMain.setBounds(new Rectangle(10, 50, 407, 60));
    jPanelMain.setLayout(null);
  }



  /*
  NOMBRE: JLabelInit
  PERTENECE A: Clase DlgSelectCourse.
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

    //Establecimiento de sus tama�os, textos y  bordes.
    jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 16));
    jLabelTitle.setBorder(BorderFactory.createLoweredBevelBorder());
    jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelTitle.setText(this.getTitle());
    jLabelTitle.setBounds(new Rectangle(10, 5, 407, 44));

    jLabelCourse.setBounds(new Rectangle(20, 17, 50, 17));
    jLabelCourse.setText("Course:");
    jLabelCourse.setFont(new java.awt.Font("Dialog", 1, 12));
  }



  /*
  NOMBRE: JButtonInit
  PERTENECE A: Clase DlgSelectCourse.
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
    OkButton.setBounds(new Rectangle(195, 115, 100, 30));
    OkButton.setText("Ok");
    OkButton.setFont(new java.awt.Font("Button", 1, 12));

    //Establecimiento de la funcionalidad de OkButton.
    OkButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        OkButtonActionPerformed(e);
      }
    });

    //Establecimiento del tama�o, texto y tipo de letra para CancelButton.
    CancelButton.setBounds(new Rectangle(310, 115, 100, 30));
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
  PERTENECE A: Clase DlgSelectCourse.
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
    for(int i = 0; i < courseNameVector.size(); i++)
      jComboCourse.addItem(courseNameVector.get(i));
  }



  /*
  NOMBRE: JDialogInit
  PERTENECE A: Clase DlgSelectCourse.
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
  }



////////////////////////////////////////////////////////////////////////////////
////////// FUNCIONALIDADES DE LOS COMPONETES DEL JDIALOG ///////////////////////
////////////////////////////////////////////////////////////////////////////////



  /*
  NOMBRE: OkButtonActionPerformed.
  PERTENECE A: Clase DlgSelectCourse.
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
  PERTENECE A: Clase DlgSelectCourse.
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

    //Borrado del contenido de los componentes del JDialog.
    jComboCourse.removeAllItems();

    this.dispose();
  }
}