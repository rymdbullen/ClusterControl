package se.avegagroup.clustercontrol.action;

import se.avegagroup.clustercontrol.logic.WorkerManager;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/home.html")
public class DefaultAction extends BaseActionBean {

	@DefaultHandler
	public Resolution view() {
		if(WorkerManager.isInitialized()) {
			return new ForwardResolution("/WEB-INF/jsp/page/controller.jsp");
		}
		return new ForwardResolution("/WEB-INF/jsp/page/home.jsp");
	}
}
