package myGroup.action;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

// Referenced classes of package myGroup.action:
//            BaseActionBean

public class HomeActionBean extends BaseActionBean {

	public Resolution view() {
		return new ForwardResolution("/WEB-INF/jsp/home.jsp");
	}

	public String getJavaVersion() {
		return System.getProperty("java.version");
	}

	public String getOsName() {
		return System.getProperty("os.name");
	}
}
