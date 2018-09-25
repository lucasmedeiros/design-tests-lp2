package main.tools;

import java.util.HashSet;
import java.util.Set;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;
import org.designwizard.design.FieldNode;
import org.designwizard.design.MethodNode;

public class VerificadorStrings {

	private static final String STRING_CLASS_NAME = "java.lang.String";
	private static final String TOSTRING_METHOD_NAME = "toString()";

	private DesignWizard dw;

	public VerificadorStrings(DesignWizard dw) {
		this.dw = dw;
	}

	/**
	 * Returns a set with methods that return strings
	 * 
	 * @return
	 */
	public Set<MethodNode> getMethodsNotCallingToString() {
		HashSet<MethodNode> setMethods = new HashSet<>();

		if (dw != null) {
			for (ClassNode cn : dw.getAllClasses()) {
				if (!cn.isInterface()) {
					for (MethodNode mn : cn.getAllMethodsThatReturn(STRING_CLASS_NAME)) {

						if (!mn.getShortName().equals(TOSTRING_METHOD_NAME)) {
							System.out.println("O método " + mn.getName() + " retorna String!");

							if (methodCallsToString(mn))
								System.out.println("Também chama um toString() dentro dele");
						}

					}
				}
			}
		}

		return setMethods;
	}

	private boolean methodCallsToString(MethodNode method) {

		for (MethodNode callee : method.getCalleeMethods()) {
			if (callee.getShortName().equals(TOSTRING_METHOD_NAME))
				return true;
		}
		return false;
	}

	/*
	 * private boolean calleeToString() {
	 * 
	 * }
	 */
}