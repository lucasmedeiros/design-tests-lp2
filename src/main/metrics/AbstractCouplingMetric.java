package main.metrics;

import java.util.HashSet;
import java.util.Set;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;
import org.designwizard.design.FieldNode;
import org.designwizard.design.MethodNode;

public abstract class AbstractCouplingMetric implements CouplingMetric {

	private DesignWizard dw;
	private static final String OBJECT_CLASS_NAME = "java.lang.Object";

	public AbstractCouplingMetric(DesignWizard dw) {
		this.dw = dw;
	}

	public Set<ClassNode> getRelatedEntities(ClassNode classNode) {
		Set<ClassNode> directRelatedEntities = new HashSet<>();

		directRelatedEntities.addAll(getDirectRelatedEntities(classNode));
		directRelatedEntities.addAll(getMethodRelatedEntities(classNode));
		directRelatedEntities.addAll(getFieldDeclaredEntities(classNode));

		return directRelatedEntities;
	}

	public Set<MethodNode> getRelatedMethods(ClassNode classNode) {
		Set<MethodNode> directRelatedMethods = new HashSet<>();

		directRelatedMethods.addAll(getDirectRelatedMethods(classNode));
		directRelatedMethods.addAll(getMethodRelatedMethods(classNode));
		directRelatedMethods.addAll(getFieldDeclaredMethods(classNode));

		return directRelatedMethods;
	}

	public Set<ClassNode> getDependentsEntities(ClassNode classNode) {
		Set<ClassNode> directDependentsEntities = new HashSet<>();

		directDependentsEntities.addAll(getDirectDependentsEntities(classNode));
		directDependentsEntities.addAll(getMethodDependentsEntities(classNode));

		return directDependentsEntities;
	}

	public Set<MethodNode> getDependentsMethods(ClassNode classNode) {
		Set<MethodNode> directDependentsMethods = new HashSet<>();

		directDependentsMethods.addAll(getDirectDependentsMethods(classNode));

		return directDependentsMethods;
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

	private Set<MethodNode> getDirectRelatedMethods(ClassNode classNode) {
		Set<MethodNode> feedback = new HashSet<>();
		Set<MethodNode> callees = classNode.getCalleeMethods();

		for (MethodNode callee : callees) {
			if (isNewRelatedMethod(classNode, callee)) {
				feedback.add(callee);
			}
		}
		return feedback;
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

	private Set<MethodNode> getDirectDependentsMethods(ClassNode classNode) {
		Set<MethodNode> feedback = new HashSet<>();
		Set<MethodNode> callers = classNode.getCallerMethods();

		for (MethodNode caller : callers) {
			if (isNewRelatedMethod(classNode, caller)) {
				feedback.add(caller);
			}
		}
		return feedback;
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

	private Set<MethodNode> getFieldDeclaredMethods(ClassNode classNode) {
		Set<MethodNode> feedback = new HashSet<>();
		Set<FieldNode> fieldsDeclared = classNode.getAllFields();

		for (FieldNode field : fieldsDeclared) {
			ClassNode type = field.getType();
			MethodNode constructor = getDefaultConstructor(type);
			if (isNewRelatedMethod(classNode, constructor)) {
				feedback.add(constructor);
			}
		}

		return feedback;
	}

	private MethodNode getDefaultConstructor(ClassNode classNode) {
		if (classNode == null || classNode.getClassName().equals("void")) {
			return null;
		}

		Set<MethodNode> constructores = classNode.getConstructors();
		MethodNode constructor;

		if (constructores.isEmpty()) {
			constructor = new MethodNode(classNode.getClassName() + ".<init>()", true);
		} else {
			constructor = (MethodNode) (constructores.toArray())[0];
		}
		return constructor;
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

	private Set<MethodNode> getMethodRelatedMethods(ClassNode classNode) {
		Set<MethodNode> feedback = new HashSet<>();

		Set<MethodNode> methods = new HashSet<>();
		methods.addAll(classNode.getAllMethods());
		methods.addAll(classNode.getCalleeMethods());

		for (MethodNode method : methods) {
			MethodNode constructor;
			if (method.isConstructor()) {
				constructor = method;
			} else {
				ClassNode type = method.getReturnType();
				constructor = getDefaultConstructor(type);
			}
			if (isNewRelatedMethod(classNode, constructor)) {
				feedback.add(constructor);
			}

			Set<ClassNode> parameters = getParameters(method);

			for (ClassNode parameter : parameters) {
				constructor = getDefaultConstructor(parameter);
				if (isNewRelatedMethod(classNode, constructor)) {
					feedback.add(constructor);
				}
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

	private Set<ClassNode> getParameters(MethodNode method) {
		Set<ClassNode> parameters = method.getParameters();

		if (parameters == null) {
			return new HashSet<>();
		}

		return parameters;
	}

	private boolean existsInProjectDesign(ClassNode classNode) {
		return getAllClasses().contains(classNode);
	}

	private boolean isInTheDesign(MethodNode methodNode) {
		return getAllMethods().contains(methodNode);
	}

	protected Set<ClassNode> getAllClasses() {
		if (this.dw == null) {
			return new HashSet<>();
		}
		return dw.getAllClasses();
	}

	protected Set<MethodNode> getAllMethods() {
		if (this.dw == null) {
			return new HashSet<>();
		}
		return dw.getAllMethods();
	}

	private boolean isNewRelatedClass(ClassNode classNode, ClassNode newClass) {
		return newClass != null && !newClass.equals(classNode) 
				&& !OBJECT_CLASS_NAME.equals(newClass.getClassName())
				&& existsInProjectDesign(newClass);
	}

	private boolean isNewRelatedMethod(ClassNode classNode, MethodNode newMethod) {
		if (newMethod != null && !classNode.getAllMethods().contains(newMethod) && isInTheDesign(newMethod)) {
			return true;
		}
		return false;
	}

	public Set<MethodNode> methodDependenciesBetweenEntities(ClassNode classNodeA, ClassNode classNodeB) {
		Set<MethodNode> dependencies = new HashSet<>();

		Set<MethodNode> calleesA = classNodeA.getCalleeMethods();
		Set<MethodNode> calleesB = classNodeB.getCalleeMethods();

		for (MethodNode methodNode : calleesA) {
			if (classNodeB.equals(methodNode.getDeclaringClass())) {
				dependencies.add(methodNode);
			}
		}

		for (MethodNode methodNode : calleesB) {
			if (classNodeA.equals(methodNode.getDeclaringClass())) {
				dependencies.add(methodNode);
			}
		}

		Set<ClassNode> fieldsA = getFieldDeclaredEntities(classNodeA);
		Set<ClassNode> fieldsB = getFieldDeclaredEntities(classNodeB);

		if (fieldsA.contains(classNodeB)) {
			Set<MethodNode> constructors = classNodeB.getConstructors();
			if (constructors != null && !constructors.isEmpty()) {
				dependencies.add((MethodNode) constructors.toArray()[0]);
			}

		}

		if (fieldsB.contains(classNodeA)) {
			Set<MethodNode> constructors = classNodeA.getConstructors();
			if (constructors != null && !constructors.isEmpty()) {
				dependencies.add((MethodNode) constructors.toArray()[0]);
			}
		}
		return dependencies;
	}

}
