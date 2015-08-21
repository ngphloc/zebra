/**
 * 
 */
package vn.spring.zebra.util;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public final class FireEventUtil {
	
	public final static void fireEvent(FireEventTask task, boolean waitDone) {
		if(waitDone) task.run();
		else         new FireEventThread(task);
	}
	
	private final static class FireEventThread extends Thread {
		private FireEventTask task = null;
		
		public FireEventThread(FireEventTask task) {
			super();
			this.task = task;
			start();
		}

		@Override
		public void run() {
			try {
				task.run();
			}
			catch(Throwable e) {e.printStackTrace();}
		}
	}
}
