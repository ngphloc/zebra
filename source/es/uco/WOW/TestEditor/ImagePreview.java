package es.uco.WOW.TestEditor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class ImagePreview extends JComponent implements PropertyChangeListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
ImageIcon thumbnail = null;
  File file = null;

  public ImagePreview(JFileChooser fileChooser)
  {
    setPreferredSize(new Dimension(200, 100));
    fileChooser.addPropertyChangeListener(this);
  }

  public void LoadImage()
  {
    if(file == null)
      return;

    ImageIcon tmpIcon = new ImageIcon(file.getPath());
    if(tmpIcon.getIconWidth() > 190)
      thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(190, -1, Image.SCALE_DEFAULT));
    else
      thumbnail = tmpIcon;
  }

  public void propertyChange(PropertyChangeEvent e)
  {
    String prop = e.getPropertyName();

    if(prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY))
    {
      file = (File)e.getNewValue();
      if(isShowing())
      {
        LoadImage();
        repaint();
      }
    }
  }

  public void paint(Graphics graphics)
  {
    if(thumbnail == null)
      LoadImage();

    if(thumbnail != null)
    {
      int x = getWidth() / 2 - thumbnail.getIconWidth();
      int y = getHeight() / 2 - thumbnail.getIconHeight();

      if(y < 0)
        y = 0;
      if(x < 5)
        x = 5;

      thumbnail.paintIcon(this, graphics, x, y);
    }
  }
}