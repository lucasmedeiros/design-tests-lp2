package main.metrics;

import java.util.HashSet;
import java.util.Set;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;
import org.designwizard.design.MethodNode;

public abstract class AbstractCouplingMetric implements CouplingMetric {

	private DesignWizard dw;
	private static final String OBJECT_CLASS_NAME = "java.lang.Object";

	public AbstractCouplingMetric(DesignWizard dw) {
		this.dw = dw;
	}

	protected Set<ClassNode> getParameters(MethodNode method) {
		Set<ClassNode> parameters = method.getParameters();

		if (parameters == null) {
			return new HashSet<>();
		}

		return parameters;
	}

	private boolean existsInProjectDesign(ClassNode classNode) {
		return getAllClasses().contains(classNode);
	}

	private Set<ClassNode> getAllClasses() {
		if (this.dw == null) {
			return new HashSet<>();
		}
		return dw.getAllClasses();
	}

	protected boolean isNewRelatedClass(ClassNode classNode, ClassNode newClass) {
		return newClass != null && !newClass.equals(classNode) && !OBJECT_CLASS_NAME.equals(newClass.getClassName())
				&& existsInProjectDesign(newClass);
	}
}
