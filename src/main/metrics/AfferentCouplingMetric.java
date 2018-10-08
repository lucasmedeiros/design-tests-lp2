package main.metrics;

import java.util.HashSet;
import java.util.Set;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;
import org.designwizard.design.MethodNode;

public class AfferentCouplingMetric extends AbstractCouplingMetric {

	public AfferentCouplingMetric(DesignWizard dw) {
		super(dw);
	}

	@Override
	public int calculate(ClassNode classNode) {
		int ca = 0;

		if (classNode != null)
			ca = getDependentsEntities(classNode).size();

		return ca;
	}

	private Set<ClassNode> getDependentsEntities(ClassNode classNode) {
		Set<ClassNode> directDependentsEntities = new HashSet<>();

		directDependentsEntities.addAll(getDirectDependentsEntities(classNode));
		directDependentsEntities.addAll(getMethodDependentsEntities(classNode));

		return directDependentsEntities;
	}

	private Set<ClassNode> getDirectDependentsEntities(ClassNode classNode) {
		Set<ClassNode> feedback = new HashSet<>();
		Set<ClassNode> callers = classNode.getCallerClasses();

		for (ClassNode caller : callers) {
			if (isNewRelatedClass(classNode, caller)) {
				feedback.add(caller);
			}
		}
		return feedback;
	}

	private Set<ClassNode> getMethodDependentsEntities(ClassNode classNode) {
		Set<ClassNode> feedback = new HashSet<>();

		Set<ClassNode> callerClasses = classNode.getCallerClasses();
		for (ClassNode callerClass : callerClasses) {
			Set<MethodNode> methods = callerClass.getAllMethods();

			for (MethodNode method : methods) {
				feedback.addAll(getDependentsDeclaringClasses(classNode, method));
			}
		}
		return feedback;
	}

	private Set<ClassNode> getDependentsDeclaringClasses(ClassNode classNode, MethodNode method) {
		Set<ClassNode> feedback = new HashSet<>();
		ClassNode type = method.getReturnType();
		Set<ClassNode> parameters = getParameters(method);

		if (type.equals(classNode) || parameters.contains(classNode)) {
			Set<MethodNode> callerMethods = method.getCallerMethods();

			for (MethodNode callerMethod : callerMethods) {
				ClassNode declaringClass = callerMethod.getDeclaringClass();

				if (isNewRelatedClass(classNode, declaringClass)) {
					feedback.add(declaringClass);
				}
			}
		}
		return feedback;
	}
}
