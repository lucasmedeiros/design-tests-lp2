package main.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;

public class MutualAssociationChecker {
	private DesignWizard dw;

	public MutualAssociationChecker(DesignWizard dw) {
		this.dw = dw;
	}

	public Set<String[]> getMutualAssociationClasses() {
		Set<String[]> mutualAssociationClasses = new HashSet<>();
		
		if (this.dw != null) {
			Map<ClassNode, Set<ClassNode>> associations = getAllClassAssociations(dw.getAllClasses());
			for(Map.Entry<ClassNode, Set<ClassNode>> entry : associations.entrySet()) {
				System.out.println(entry.getKey().getShortName() + " chama as seguintes classes: ");
				for (ClassNode callee : entry.getValue()) {
					System.out.println(callee.getShortName());
				}
				System.out.println("============================================================");
			}
		}
		
		return mutualAssociationClasses;
	}

	private Map<ClassNode, Set<ClassNode>> getAllClassAssociations(Set<ClassNode> allClasses) {
		Map<ClassNode, Set<ClassNode>> mapClasses = new HashMap<>();
		
		for (ClassNode classNode: allClasses) {
			for (ClassNode calleeClass : classNode.getCalleeClasses()) {
				
				if (existsInProject(calleeClass, allClasses) && !classNode.equals(calleeClass)){
					insertIntoMap(classNode, calleeClass, mapClasses);
				}
			}
		}
		
		return mapClasses;
	}

	private void insertIntoMap(ClassNode classNode, ClassNode calleeClass, Map<ClassNode, Set<ClassNode>> mapClasses) {
		Set<ClassNode> calleeSet = new HashSet<>();
		
		if (mapClasses.containsKey(classNode)) {
			calleeSet = mapClasses.get(classNode);
		}
		
		calleeSet.add(calleeClass);
		mapClasses.put(classNode, calleeSet);
	}

	private boolean existsInProject(ClassNode classNode, Set<ClassNode> allClasses) {
		return allClasses.contains(classNode);
	}
}
