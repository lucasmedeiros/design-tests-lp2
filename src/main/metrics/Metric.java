package main.metrics;

import org.designwizard.design.ClassNode;

/**
 * Calculates the metric value for the {@link ClassNode}
 * @author Lucas de Medeiros Nunes Fernandes - lucasmed812@gmail.com
 */

public interface Metric {
	
	/**
	 * Calculates the value of the metric for a given classNode.
	 * 
	 * @param classNode class to be used to calculate the metric, which is contained in
	 * project design.
	 * @return the value of the metric.
	 */
	public int calculate(ClassNode classNode);
}
