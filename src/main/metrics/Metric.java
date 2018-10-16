package main.metrics;

import org.designwizard.design.ClassNode;

public interface Metric {
	
	public int calculate(ClassNode classNode);
}
