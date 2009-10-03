package se.avegagroup.clustercontrol.action;

import java.util.ArrayList;

import se.avegagroup.clustercontrol.configuration.Constants;
import se.avegagroup.clustercontrol.domain.JkBalancerType;
import junit.framework.TestCase;

public class ControllerActionBeanTest extends TestCase {
	public void testSetUrl() {
		ArrayList<JkBalancerType> balancerList = ControllerActionBean.setUrl(Constants.TEST_URL);
		assertEquals(1, balancerList.size());
	}
}
