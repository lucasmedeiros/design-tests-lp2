package main.tools;

import java.util.HashMap;
import java.util.Map;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;

public class MutualAssociationChecker {
	private DesignWizard dw;
	
	public MutualAssociationChecker(DesignWizard dw) {
		this.dw = dw;
	}
	
	public Map<ClassNode, ClassNode> getMutualAssociationClasses() {
		Map<ClassNode, ClassNode> mapClasses = new HashMap<>();
		
		if (this.dw != null)
			mapClasses = getAllMutualAssociationClasses();
		
		return mapClasses;
	}

	private Map<ClassNode, ClassNode> getAllMutualAssociationClasses() {
		// TODO Auto-generated method stub
		return null;
	}
}
