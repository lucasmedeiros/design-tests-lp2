package main.tools;

import java.util.HashSet;
import java.util.Set;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;
import org.designwizard.design.MethodNode;

public class PrintChecker {

	private static final String PRINTLN_METHOD = "println(java.lang.String)";
	private static final String PRINT_METHOD = "print(java.lang.String)";
	private DesignWizard dw;

	public PrintChecker(DesignWizard dw) {
		this.dw = dw;
	}

	public Set<ClassNode> getClassesWithPrintMethods() {
		Set<ClassNode> classesWithPrint = new HashSet<>();

		if (this.dw != null)
			classesWithPrint = getAllClassesWithPrintMethods();

		return classesWithPrint;
	}

	private Set<ClassNode> getAllClassesWithPrintMethods() {
		Set<ClassNode> classes = new HashSet<>();

		for (ClassNode cn : dw.getAllClasses()) {
			for (MethodNode mn : cn.getAllMethods()) {
				if (methodContainsPrintMethod(mn))
					classes.add(cn);
			}
		}

		return classes;
	}

	private boolean methodContainsPrintMethod(MethodNode methodNode) {

		for (MethodNode calleeMethod : methodNode.getCalleeMethods()) {
			if (calleeMethod.getShortName().equals(PRINT_METHOD)
					|| calleeMethod.getShortName().equals(PRINTLN_METHOD)) {
				return true;
			}
		}

		return false;
	}
}
