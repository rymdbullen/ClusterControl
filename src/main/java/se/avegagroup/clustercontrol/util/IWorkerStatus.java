package se.avegagroup.clustercontrol.util;

import se.avegagroup.clustercontrol.domain.JkStatus;

public abstract class IWorkerStatus {
	public JkStatus jkStatus = null;
	
	public JkStatus getStatus() {
		return jkStatus;
	}
}
