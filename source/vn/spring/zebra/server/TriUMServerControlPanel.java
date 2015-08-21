/**
 * 
 */
package vn.spring.zebra.server;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerControlPanel {
	protected TriUMServer server = null;
	
	public TriUMServerControlPanel(TriUMServer server) {
		this.server = server;
	}
	
	public boolean isZebraNetworkerStarted() {return server.zebraNetworker.isStarted();}
	public void startZebraNetworker()        {server.zebraNetworker.start();}
	public void restartZebraNetworker()      {server.zebraNetworker.restart();}
	public void stopZebraNetworker()         {server.zebraNetworker.stop();}

	public boolean isZebraMinerStarted()     {return server.zebraMiner.isStarted();}
	public void startZebraMiner()            {server.zebraMiner.start();}
	public void restartZebraMiner()          {server.zebraMiner.restart();}
	public void stopZebraMiner()             {server.zebraMiner.stop();}
	
	public boolean isClustererServiceStarted()  {return server.userClustererService.isStarted();}
	public void startClustererService()         {server.userClustererService.start();}
	public void restartClustererService()       {server.userClustererService.restart();}
	public void stopClustererService()          {server.userClustererService.stop();}

	public boolean isGarbageServiceStarted()  {return server.garbageService.isStarted();}
	public void startGarbageService()         {server.garbageService.start();}
	public void restartGarbageService()       {server.garbageService.restart();}
	public void stopGarbageService()          {server.garbageService.stop();}

	public boolean isCollabServiceStarted()  {return server.collabService.isStarted();}
	public void startCollabService()         {server.collabService.startResume();}
	public void stopCollabService()          {server.collabService.stopSuspend();}

	public boolean isCommunicateServiceStarted()  {return server.communicateService.isStarted();}
	public void startCommunicateService()         {server.communicateService.startResume();}
	public void stopCommunicateService()          {server.communicateService.stopSuspend();}

	public boolean isMLServiceStarted()      {return server.mailingListService.isStarted();}
	public void startMLService()             {server.mailingListService.restart();}
	public void stopMLService()              {server.mailingListService.stop();}

	public TriUMServer getServer() {return server;}
}
