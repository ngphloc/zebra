package vn.spring.zebra.portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.*;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.helperservice.AccessService;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class WowPortletEx extends GenericPortlet {
	
	public void init(PortletConfig config) throws PortletException {
		super.init(config);
	}
	public void doView(RenderRequest request,
			RenderResponse response) throws PortletException,
			IOException {
		doEdit(request, response);
	}
	
	public void doEdit(RenderRequest request,
			RenderResponse response) throws PortletException,
			IOException {
		if((request.getWindowState() == WindowState.MINIMIZED) ) { 
			ZebraStatic.traceService.trace("Wow portlet in minimize state");
		}
		if(!request.getContextPath().equals(ZebraStatic.getWowContextPath())) {
			//ZebraConfig.traceService.trace("Portlet path=\"" + request.getContextPath() + "\"" +
			//	" is not identity with ZebraConfig.getWowContextPath()=" + ZebraConfig.getWowContextPath() + "\"");
		}
		String wowPath = request.getScheme() + "://" +
			request.getServerName() + ":" + request.getServerPort() + 
			ZebraStatic.getWowContextPath();
		String userid = request.getRemoteUser();

		PortletSession session = request.getPortletSession(true);

		String course = (String)session.getAttribute(AccessService.WOW_COURSE);
		String directory = (String)session.getAttribute(AccessService.WOW_DIRECTORY);
		String briefConceptName = request.getParameter(AccessService.WOW_CONCEPT);
		PortletPreferences prefs = request.getPreferences();
		course = (course == null ? prefs.getValue(AccessService.WOW_COURSE, null) : course);
		directory = (directory == null ? prefs.getValue(AccessService.WOW_DIRECTORY, null) : directory);
		briefConceptName = (briefConceptName == null ? prefs.getValue(AccessService.WOW_CONCEPT, null) : briefConceptName);
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(AjaxBackendPortletService.createAjaxCode(wowPath, userid, "", course, directory, briefConceptName, "edit"));
	}

	public void doHelp(RenderRequest request,
			RenderResponse response) throws PortletException,
			IOException {
	}

	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
	}
	
}
