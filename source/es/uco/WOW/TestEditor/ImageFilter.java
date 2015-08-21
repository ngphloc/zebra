package es.uco.WOW.TestEditor;

import java.io.File;

import javax.swing.filechooser.FileFilter;


public class ImageFilter extends FileFilter
{
  public static final String jpeg = "jpeg";
  public static final String jpg = "jpg";
  public static final String gif = "gif";
  public static final String tiff = "tiff";
  public static final String tif = "tif";
  public static final String png = "png";

  public boolean accept(File file)
  {
    if(file.isDirectory() == true)
      return true;

    String fileName = file.getName();
    int i = fileName.lastIndexOf('.');

    if(i > 0 && i < fileName.length() - 1)
    {
      String extension = fileName.substring(i + 1).toLowerCase();
      if(tiff.equals(extension) || tif.equals(extension) ||
         gif.equals(extension) || jpg.equals(extension) ||
         jpeg.equals(extension) || png.equals(extension))
        return true;
      else
        return false;
    }
    else
      return false;
  }

  public String getDescription()
  {
    return "Just Images";
  }
}