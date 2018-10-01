package main.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;

public class RelationsTool {

	private DesignWizard dw;

	public RelationsTool(DesignWizard dw) {
		this.dw = dw;
	}

	/**
	 * Method that allows obtaining all existing relations in a Java project.
	 * 
	 * @return a map with a caller class as key and a set of its callee classes as value.
	 */
	public Map<ClassNode, Set<ClassNode>> getRelations() {
		Map<ClassNode, Set<ClassNode>> relations = new HashMap<>();

		if (this.dw != null) {
			relations = getAllRelations();
		}

		return relations;
	}

	/**
	 * Method that gets all existing relations in project.
	 * 
	 * @return map with caller class as key and a set of its callee classes as value
	 */
	private Map<ClassNode, Set<ClassNode>> getAllRelations() {
		Map<ClassNode, Set<ClassNode>> relations = new HashMap<>();

		for (ClassNode classNode : this.dw.getAllClasses()) {
			Set<ClassNode> callees = getCallees(classNode);
			relations.put(classNode, callees);
		}

		return relations;
	}

	/**
	 * Method that gets all callees from a class.
	 * 
	 * @param classNode class to get all callee methods
	 * @return set with callee classes.
	 */
	private Set<ClassNode> getCallees(ClassNode classNode) {
		Set<ClassNode> callees = new HashSet<>();

		for (ClassNode calleeClass : classNode.getCalleeClasses()) {

			if (existsInDesign(calleeClass) && !classNode.equals(calleeClass)) {
				callees.add(calleeClass);
			}
		}

		return callees;
	}

	/**
	 * Method that checks if a class is in the project's design. This prevents returning
	 * java.lang classes for example, or inner classes.
	 * 
	 * @param classNode class to check if it's in design
	 * @return true if it exists, false otherwise.
	 */
	private boolean existsInDesign(ClassNode classNode) {
		return this.dw.getAllClasses().contains(classNode);
	}

}
