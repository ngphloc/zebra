/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.util.ArrayList;

import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.util.ConceptUtil;
import vn.spring.zebra.util.UserUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMContainerFinder {
	protected TriUMContainer      container = null;
	protected TriUMContainerQuery query = null;
	protected ArrayList<TriUM>    founds = new ArrayList<TriUM>();
	protected int                 start = -1;
	
	public TriUMContainerFinder(TriUMContainer container) {
		this.container = container;
	}
	
	public TriUM find(String query) {
		return find(new TriUMContainerQuery(query));
	}
	public TriUM find(TriUMContainerQuery query) {
		this.query = query;
		this.founds.clear();
		this.start = -1;
		
		if(this.query == null)   return null;
		if(!this.query.isValid()) return null;
		
		int n = container.getUMCount();
		for(int i = 0; i < n; i++) {
			TriUM um = container.getUM(i);
			if(match(um, query)) founds.add(um);
		}
		if(this.founds.size() == 0) {
			return null;
		}
		else {
			this.start = 0;
			return this.founds.get(start);
		}
		
	}

	public TriUM findNext() {
		if(start == -1) return null;
		start ++;
		if(start >= founds.size()) {
			start = -1;
			founds.clear();
			return null;
		}
		return founds.get(start);
	}
	protected boolean match(TriUM um, String query) {
		if(um == null || query == null) return false;
		query = query.trim();
		if(query.length() == 0) return false;
		return match(um, new TriUMContainerQuery(query));
	}
	protected boolean match(TriUM um, TriUMContainerQuery query) {
		if(um == null || query == null) return false;
		if(query.user == null && query.course == null) return false;
		
		boolean match = true;
		if(query.user != null) {
			match &= (match(um.getUserId(), query.user) || 
					match(UserUtil.getUserName(um.getUserId()), query.user));
		}
		if(query.course != null) {
			match &= (match(um.getCourse(), query.course) || 
					match(ConceptUtil.getCourseTitle(um.getCourse()), query.course));
		}
		return match;
	}
	
	protected boolean match(String data, String query) {
		if(data == null || query == null) return false;
		data = data.trim().toLowerCase();
		query = query.trim().toLowerCase();
		if(data.length() == 0 || query.length() == 0) return false;
		
		String[] s_data = data.split(" ");
		String[] s_query = query.split(" ");
		
		ArrayList<String> a_data = new ArrayList<String>();
		for(String word : s_data) {
			if(word == null) continue;
			word = word.trim();
			if(word.length() == 0) continue;
			a_data.add(word);
		}

		ArrayList<String> a_query = new ArrayList<String>();
		for(String key : s_query) {
			if(key == null) continue;
			key = key.trim();
			if(key.length() == 0) continue;
			a_query.add(key);
		}
		
		for(String key : a_query) {
			boolean found = false;
			for(String word : a_data) {
				if(word.indexOf(key) != -1) {
					found = true;
					break;
				}
			}
			if(!found) return false;
		}
		return true;
	}
	
	public class TriUMContainerQuery {
		public static final String PARAM_SEPARATOR = "&";
		
		public String user = null;
		public String course = null;
		
		public TriUMContainerQuery(String user, String course) {
			this.user = user;
			this.course = course;
			
			if(this.user != null) {
				this.user = this.user.trim();
				if(this.user.length() == 0) this.user = null;
			}
			if(this.course != null) {
				this.course = this.course.trim();
				if(this.course.length() == 0) this.course = null;
			}
		}

		public TriUMContainerQuery(String query) {
			if(query == null) return;
			query = query.trim();
			if(query.length() == 0) return;
			
			String[] params = query.split(PARAM_SEPARATOR);
			if(params.length == 0) return;
			for(String param : params) {
				String[] attr_value = param.split("=");
				if(attr_value.length != 2) continue;
				attr_value[0] = attr_value[0].trim();
				attr_value[1] = attr_value[1].trim();
				if(attr_value[0].length() == 0 || attr_value[1].length() == 0)
					continue;
				
				if(attr_value[0].equals("user")) 
					this.user = attr_value[1];
				else if(attr_value[0].equals("course"))
					this.course = attr_value[1];
			}
		}
		
		public boolean isValid() {
			return (user != null || course != null);
		}
	}
}
