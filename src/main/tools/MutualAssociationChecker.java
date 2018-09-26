package main.tools;

import java.util.HashSet;
import java.util.Set;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;

import util.Util;

public class MutualAssociationChecker {
	private DesignWizard dw;

	public MutualAssociationChecker(DesignWizard dw) {
		this.dw = dw;
	}

	public Set<Set<ClassNode>> getMutualAssociationClasses() {
		Set<Set<ClassNode>> mutualAssociationClasses = new HashSet<>();
		
		if (this.dw != null)
			mutualAssociationClasses = getAllCircularDependencies();
		
		return mutualAssociationClasses;
	}

	private Set<Set<ClassNode>> getAllCircularDependencies() {
		Set<Set<ClassNode>> setDependencies = new HashSet<>();
		
		for (ClassNode classNode: this.dw.getAllClasses()) {
			Set<ClassNode> callees = getCallees(classNode);
			Set<ClassNode> callers = getCallers(classNode);
			
			Set<ClassNode> intersection = Util.intersectionBetweenSets(callees, callers);
			
			if (!intersection.isEmpty()) {
				setDependencies.add(intersection);
			}
		}
		
		return setDependencies;
	}
	
	private Set<ClassNode> getCallees(ClassNode classNode) {
		Set<ClassNode> callees = new HashSet<>();
		
		for (ClassNode calleeClass : classNode.getCalleeClasses()) {
			
			if (existsInDesign(calleeClass) && !classNode.equals(calleeClass)){
				callees.add(calleeClass);
			}
		}
		
		return callees;
	}

	private Set<ClassNode> getCallers(ClassNode classNode) {
		Set<ClassNode> callers = new HashSet<>();
		
		for (ClassNode callerClass : classNode.getCallerClasses()) {
			
			if (existsInDesign(callerClass) && !classNode.equals(callerClass)) {
				callers.add(callerClass);
			}
		}
		
		return callers;
	}

	private boolean existsInDesign(ClassNode classNode) {
		return this.dw.getAllClasses().contains(classNode);
	}
}
