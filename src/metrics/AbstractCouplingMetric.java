package metrics;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;
import org.designwizard.design.MethodNode;
import org.designwizard.design.PackageNode;

public abstract class AbstractCouplingMetric implements Metric {
	
	private DesignWizard dw;
	
	public AbstractCouplingMetric(DesignWizard dw) {
		this.dw = dw;
	}
	
	private void test() {
		if (!wizardIsNull()) {
			for (ClassNode cn : dw.getAllClasses()) {
				// TODO teste com todas as classes
			}
			
			for (PackageNode pn : dw.getAllPackages()) {
				// TODO teste com todos os pacotes
			}
			
			for (MethodNode mn : dw.getAllMethods()) {
				// TODO teste com todos os metodos
			}
		}
	}
	
	protected boolean wizardIsNull() {
		return dw == null;
	}

}
