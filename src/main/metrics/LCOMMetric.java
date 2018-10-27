package main.metrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.designwizard.design.ClassNode;
import org.designwizard.design.FieldNode;
import org.designwizard.design.MethodNode;

import util.Util;

public class LCOMMetric implements Metric {

	private static final String CONSTRUCTOR_METHOD_NAME = "<init>";
	private static final int MIN_NUMBER_COMPONENTS = 1;

	public LCOMMetric() {
	}

	@Override
	public int calculate(ClassNode classNode) {
		return getNumberOfComponents(classNode);
	}

	private int getNumberOfComponents(ClassNode classNode) {
		int numberOfComponents = 0;

		if (classNode != null) {
			Set<FieldNode> fields = getFields(classNode);

			if (!fields.isEmpty()) {
				Map<FieldNode, Set<MethodNode>> relations = getFieldConnectedMethods(fields, classNode);

				numberOfComponents = calculateNumberOfComponents(fields, relations);
			}
		}

		return numberOfComponents;
	}

	private int calculateNumberOfComponents(Set<FieldNode> fields, Map<FieldNode, Set<MethodNode>> relations) {
		int numberOfComponents = 1;

		if (fields.size() > 1) {
			for (FieldNode fn : fields) {
				int maxNumber = numberOfComponents + 1;
				for (FieldNode fn2 : fields) {
					if (!fn.equals(fn2)) {
						Set<MethodNode> intersection = Util.intersectionBetweenSets(relations.get(fn),
								relations.get(fn2));

						if (intersection.isEmpty()) {
							numberOfComponents++;
							numberOfComponents = Math.min(numberOfComponents, maxNumber);
						} else {
							numberOfComponents = Math.max(numberOfComponents - 1, MIN_NUMBER_COMPONENTS);
							break;
						}
					} else {
						break;
					}
				}
			}
		}

		return numberOfComponents;
	}

	public Set<Set<MethodNode>> getComponents(Map<FieldNode, Set<MethodNode>> relations) {
		Set<Set<MethodNode>> components = new HashSet<>();

		// TODO get all components...

		return components;
	}

	private Map<FieldNode, Set<MethodNode>> getFieldConnectedMethods(Set<FieldNode> fields, ClassNode classNode) {
		Map<FieldNode, Set<MethodNode>> fieldRelations = new HashMap<>();

		for (FieldNode field : fields) {
			Set<MethodNode> fieldCallers = getFieldCallerMethods(field);
			fieldRelations.put(field, fieldCallers);
		}

		for (FieldNode field : fields) {
			Set<MethodNode> fieldCallers = getFieldCallerMethods(field);
			addFieldConnectedMethods(field, fieldCallers, fieldRelations, classNode);
		}

		return fieldRelations;
	}

	private void addFieldConnectedMethods(FieldNode field, Set<MethodNode> fieldCallers,
			Map<FieldNode, Set<MethodNode>> fieldRelations, ClassNode classNode) {

		for (MethodNode methodNode : fieldCallers) {
			for (MethodNode callee : methodNode.getCalleeMethods()) {
				if (existsInClassMethods(callee, classNode) && isNotConstructor(callee)) {
					fieldRelations.get(field).add(callee);
				}
			}

			for (MethodNode caller : methodNode.getCallerMethods()) {
				if (existsInClassMethods(caller, classNode) && isNotConstructor(caller)) {
					fieldRelations.get(field).add(caller);
				}
			}
		}
	}

	private boolean existsInClassMethods(MethodNode method, ClassNode classNode) {
		return getMethods(classNode).contains(method);
	}

	private Set<MethodNode> getFieldCallerMethods(FieldNode fieldNode) {
		Set<MethodNode> methods = new HashSet<>();

		for (MethodNode callerMethodNode : fieldNode.getCallerMethods()) {
			if (isNotConstructor(callerMethodNode))
				methods.add(callerMethodNode);
		}

		return methods;
	}

	private boolean isNotConstructor(MethodNode methodNode) {
		return !(methodNode.getShortName().startsWith(CONSTRUCTOR_METHOD_NAME));
	}

	private Set<FieldNode> getFields(ClassNode classNode) {
		return classNode.getDeclaredFields();
	}

	private Set<MethodNode> getMethods(ClassNode classNode) {
		return classNode.getDeclaredMethods();
	}
}
