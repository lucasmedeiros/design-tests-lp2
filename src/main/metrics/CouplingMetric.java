package main.metrics;

import org.designwizard.design.ClassNode;

public interface CouplingMetric {
	
	public int calculate(ClassNode classNode);
}
