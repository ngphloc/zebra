/**
 * 
 */
package vn.spring.zebra.um;

import vn.spring.WOW.engine.CourseInfo;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.ConceptUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserCourseInfo {
	protected UserInfo userInfo = null;
	protected CourseInfo courseInfo = null;
	public UserCourseInfo(String userid, String course) throws Exception {
		userInfo = new UserInfo(userid);
		courseInfo = ConceptUtil.getCourseInfo(course);
	}
	
	public String getUserId() {return userInfo.getUserId();}
	public String getCourse() {return courseInfo.courseName;}
	public UserInfo getUserInfo() {return userInfo;}
	public CourseInfo getCourseInfo() {return courseInfo;}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ZebraStatic.makeId(userInfo.getUserId(), courseInfo.courseName);
	}
	
}
