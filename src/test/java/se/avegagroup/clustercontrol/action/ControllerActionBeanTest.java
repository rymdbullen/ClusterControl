package se.avegagroup.clustercontrol.action;

import java.util.ArrayList;

import se.avegagroup.clustercontrol.domain.JkBalancerType;
import junit.framework.TestCase;

public class ControllerActionBeanTest extends TestCase {

	public void testSetUrl() {
		String url = "http://localhost/jkmanager";
		ArrayList<JkBalancerType> balancerList = ControllerActionBean.setUrl(url);
		assertEquals(1, balancerList.size());
	}

}
