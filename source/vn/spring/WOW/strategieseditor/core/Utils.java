package vn.spring.WOW.strategieseditor.core;

import java.util.ArrayList;
import javax.swing.JTextArea;

public class Utils
{

    public Utils()
    {
    }

    public static int getSelectedLineNumber(JTextArea jtextarea)
        throws Exception
    {
        return jtextarea.getLineOfOffset(jtextarea.getCaretPosition());
    }

    public static String serializeArrayForTextArea(ArrayList arraylist)
    {
        String s = "";
        for(int i = 0; i < arraylist.size(); i++)
        {
            s = (new StringBuilder()).append(s).append(arraylist.get(i)).toString();
            s = (new StringBuilder()).append(s).append("\n").toString();
        }

        return s;
    }
}
