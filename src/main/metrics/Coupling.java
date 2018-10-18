package main.metrics;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;

public class Coupling {
	
	private Metric afferent;
	private Metric efferent;

	public Coupling (DesignWizard dw) {
		this.afferent = new AfferentCouplingMetric(dw);
		this.efferent = new EfferentCouplingMetric(dw);
	}
	
	public double getInstability(ClassNode classNode) {
		return calculateInstability(classNode);
	}

	public int getAfferentCoupling(ClassNode classNode) {
		return this.afferent.calculate(classNode);
	}
	
	public int getEfferentCoupling(ClassNode classNode) {
		return this.efferent.calculate(classNode);
	}
	
	private double calculateInstability(ClassNode classNode) {
		double ca = (double) getAfferentCoupling(classNode);
		double ce = (double) getEfferentCoupling(classNode);
		double instability = 0.0;
		
		if (ce > 0.0)
			instability = ce / (ce + ca);
		
		return instability;
	}
}
