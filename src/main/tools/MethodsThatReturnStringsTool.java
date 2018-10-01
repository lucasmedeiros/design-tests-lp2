package main.tools;

import java.util.HashSet;
import java.util.Set;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;
import org.designwizard.design.MethodNode;

public class MethodsThatReturnStringsTool {

	private static final String STRING_CLASS_NAME = "java.lang.String";
	private static final String TOSTRING_METHOD_NAME = "toString()";

	private DesignWizard dw;

	public MethodsThatReturnStringsTool(DesignWizard dw) {
		this.dw = dw;
	}

	/**
	 * Returns a set with methods that return strings and don't call toString()
	 * 
	 * @return
	 */
	public Set<MethodNode> getStringMethodsThatDontCallToString() {
		Set<MethodNode> methodsSet = new HashSet<>();

		if (dw != null) {
			methodsSet = getAllStringMethodsThatDontCallToString();
		}

		return methodsSet;
	}

	private Set<MethodNode> getAllStringMethodsThatDontCallToString() {
		Set<MethodNode> methods = new HashSet<>();

		for (ClassNode classNode : this.dw.getAllClasses()) {
			if (!classNode.isInterface()) {
				for (MethodNode methodNode : classNode.getAllMethodsThatReturn(STRING_CLASS_NAME)) {
					insertMethodsIntoSet(methodNode, methods);
				}
			}
		}

		return methods;
	}

	private void insertMethodsIntoSet(MethodNode methodNode, Set<MethodNode> methods) {
		if (!methodNode.getShortName().equals(TOSTRING_METHOD_NAME) 
				&& !methodCallsToString(methodNode)) {
			methods.add(methodNode);
		}
	}

	private boolean methodCallsToString(MethodNode method) {
		for (MethodNode callee : method.getCalleeMethods()) {
			if (callee.getShortName().equals(TOSTRING_METHOD_NAME))
				return true;
		}
		
		return false;
	}

}