/**
 * 
 */
package vn.spring.zebra.search;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.mining.textmining.AbstractKeywordDoc;
import vn.spring.zebra.mining.textmining.Keyword;
import vn.spring.zebra.mining.textmining.TextMiningUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class SearchUserDoc extends AbstractKeywordDoc {
	protected String userid = null;
	protected String course = null;
	
	public SearchUserDoc(String userid, String course) {
		this.userid = userid;
		this.course = course;
	}
	
	public void load(String[] keywordNamePattern) throws Exception {
		clear();
		load(ZebraStatic.searchLogService.getCourseSearchLog(userid, course, ZebraStatic.MINER_LOG_BEGIN_DATE),
				keywordNamePattern);
	}
	
	public void load(CourseSearchLog log, String[] keywordNamePattern) throws Exception {
		clear();
		if(!log.course.equals(this.course)) throw new Exception("Invalid parameter");
		int idx = log.findUserLog(userid);
		if(idx == -1) return;
		
		for(int i = 0; i < keywordNamePattern.length; i++) {
			String keywordName = keywordNamePattern[i];
			if(keywordName == null || keywordName.length() == 0) throw new Exception("Invalid parameter");;
			Keyword keyword = new Keyword();
			keyword.name = keywordName;
			
			addKeyword(keyword);
		}
		
		UserSearchLog userLog = log.getUserLog(idx);
		int totalTermFreq = 0;
		for(UserSearchSession session : userLog.userSearchSessions) {
			for(UserSearchRecord record : session.userSearchRecords) {
				if(record.query == null || record.query.length() == 0) continue;
				
				for(int i = 0; i < getKeywordCount(); i++) {
					Keyword keyword = getKeyword(i);
					if(TextMiningUtil.doesQueryContains(record.query, keyword.name)) {
						keyword.termFreq++;
						totalTermFreq++;
					}
				}
			}
		}//end for

		for(int i = 0; i < getKeywordCount(); i++) {
			Keyword keyword = getKeyword(i);
			keyword.totalTermFreq = totalTermFreq;
		}
	}
	
}
