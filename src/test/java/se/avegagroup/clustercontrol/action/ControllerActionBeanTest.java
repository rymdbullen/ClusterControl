package se.avegagroup.clustercontrol.action;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.avegagroup.clustercontrol.configuration.Constants;
import se.avegagroup.clustercontrol.domain.JkBalancerType;
import junit.framework.TestCase;

public class ControllerActionBeanTest extends TestCase {
	private static final Logger logger = LoggerFactory.getLogger(ControllerActionBeanTest.class);
	public void testSetUrl() {
		logger.debug("Running tests against: "+Constants.TEST_URL);
		ArrayList<JkBalancerType> balancerList = ControllerActionBean.setUrl(Constants.TEST_URL);
		assertEquals(1, balancerList.size());
	}
}
