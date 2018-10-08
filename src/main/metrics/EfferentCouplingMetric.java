package main.metrics;

import java.util.HashSet;
import java.util.Set;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;
import org.designwizard.design.FieldNode;
import org.designwizard.design.MethodNode;

public class EfferentCouplingMetric extends AbstractCouplingMetric {

	public EfferentCouplingMetric(DesignWizard dw) {
		super(dw);
	}

	@Override
	public int calculate(ClassNode classNode) {
		int ce = 0;

		if (classNode != null)
			ce = getRelatedEntities(classNode).size();

		return ce;
	}

	private Set<ClassNode> getRelatedEntities(ClassNode classNode) {
		Set<ClassNode> directRelatedEntities = new HashSet<>();

		directRelatedEntities.addAll(getDirectRelatedEntities(classNode));
		directRelatedEntities.addAll(getMethodRelatedEntities(classNode));
		directRelatedEntities.addAll(getFieldDeclaredEntities(classNode));

		return directRelatedEntities;
	}

	private Set<ClassNode> getDirectRelatedEntities(ClassNode classNode) {
		Set<ClassNode> directedRelatedEntities = new HashSet<>();

		for (ClassNode calleeClass : classNode.getCalleeClasses()) {
			if (isNewRelatedClass(classNode, calleeClass)) {
				directedRelatedEntities.add(calleeClass);
			}
		}
		return directedRelatedEntities;
	}

	private Set<ClassNode> getFieldDeclaredEntities(ClassNode classNode) {
		Set<ClassNode> feedback = new HashSet<>();
		Set<FieldNode> fieldsDeclared = classNode.getAllFields();

		for (FieldNode field : fieldsDeclared) {
			ClassNode type = field.getType();
			if (isNewRelatedClass(classNode, type)) {
				feedback.add(type);
			}
		}

		return feedback;
	}

	private Set<ClassNode> getMethodRelatedEntities(ClassNode classNode) {
		Set<ClassNode> feedback = new HashSet<>();

		Set<MethodNode> methods = new HashSet<>();
		methods.addAll(classNode.getAllMethods());
		methods.addAll(classNode.getCalleeMethods());

		for (MethodNode method : methods) {
			ClassNode type = method.getReturnType();
			if (isNewRelatedClass(classNode, type)) {
				feedback.add(type);
			}

			Set<ClassNode> parameters = getParameters(method);

			for (ClassNode parameter : parameters) {
				if (isNewRelatedClass(classNode, parameter)) {
					feedback.add(parameter);
				}
			}
		}
		return feedback;
	}
}
