/**
 * 
 */
package vn.spring.zebra.evaluation;

import java.awt.BorderLayout;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import vn.spring.WOW.datacomponents.DotString;
import vn.spring.zebra.client.TriUMQueryArg;
import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class KnowledgeEvalChartApplet extends JApplet {
	private static final long serialVersionUID = 1L;

	private String userid = null;
	private String course = null;
	private String briefConceptName = null;
	private QUERY_TYPE evaltype = QUERY_TYPE.OVERLAY;
	
	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public void start() {
		try {
			userid = getParameter("userid").trim();
			course = getParameter("course").trim();
			briefConceptName = getParameter("concept");
			if(briefConceptName == null) briefConceptName = course;
			else {
				DotString ds = new DotString(briefConceptName);
				if(ds.size() > 1 && ds.get(0).equals(course)) ds.set(0, null);
				briefConceptName = ds.toString();
			}
			evaltype = TriUMQueryArg.translateKnowledgeQueryType(getParameter("evaltype").trim());
			
			ImageIcon chart = new ImageIcon(getChart(getCodeBase(), userid, course, briefConceptName, getWidth(), getHeight(), evaltype));
			
			JLabel labelChart = new JLabel();
			labelChart.setIcon(chart);
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(labelChart, BorderLayout.CENTER);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error when init KnowledgeEvalChartApplet: " + e.getMessage(),
					"Error when init KnowledgeEvalChartApplet",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private static byte [] getChart(URL codebase, String userid, String course, String briefConceptName,int width, int height, QUERY_TYPE evaltype)
	{
		try {
			String query = "userid=" + userid + "&course=" + course + "&concept=" + briefConceptName + 
				"&evaltype=" + TriUMQueryArg.rTranslateKnowledgeQueryType(evaltype) +
				"&width=" + width + "&height=" + height;
			
			String path = codebase.getPath();
			String pathttemp = path.substring(1, path.length());
			int index = pathttemp.indexOf("/");
			index++;

			// Gets the work folder of the TestEditor tool
			String wowPath = path.substring(0, index);
			URL url = new URL(codebase.getProtocol() + "://" + codebase.getHost() + ":" + codebase.getPort() + 
					wowPath + "/KnowledgeEvalChartServlet?" + query);

			URLConnection connection = url.openConnection();
			
			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);

			ObjectInputStream in = new ObjectInputStream(connection
					.getInputStream());
			byte[] chart = (byte[]) in.readObject();

			in.close();
			return chart;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(),
							"Error connecting to the server",
							JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
}
