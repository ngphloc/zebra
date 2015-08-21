/**
 * 
 */
package vn.spring.zebra.evaluation;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.DefaultPieDataset;

import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.server.TriUMServer;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TestEvalChartUtil {
	
	public final static JFreeChart getEvalChartOfKnowledge(String userid, String course, String briefConceptName, QUERY_TYPE evaltype) throws ZebraException, Exception  {
		TriUMQuery query = (TriUMQuery) (TriUMServer.getInstance().getCommunicateService().getQueryDelegator());
		if(!query.doesExist(userid, course, briefConceptName, evaltype, true))
			return null;
		double knowledge = query.knowledgeQuery(userid, course, briefConceptName, evaltype, true);
		return getEvalChart("The mastery of knowledge \"" + briefConceptName + "\"", knowledge);
	}
	private final static JFreeChart getEvalChart(String title, double knowledge) {
		DefaultPieDataset dataset = new DefaultPieDataset();

		dataset.setValue("Not mastered", 1-knowledge);
		dataset.setValue("Mastered", knowledge);
		return ChartFactory.createPieChart("", dataset, true, false, false);
	}
}
