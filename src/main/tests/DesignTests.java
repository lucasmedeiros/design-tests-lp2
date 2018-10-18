package main.tests;

import java.io.IOException;
import org.designwizard.api.DesignWizard;
import junit.framework.TestCase;
import main.tools.ClassesWithPrintMethodTool;
import main.tools.MutualAssociationTool;

public class DesignTests extends TestCase {

	private DesignWizard dw;

	public DesignTests() {
		try {
			this.dw = new DesignWizard("jars/lab5.jar");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testPrintlnMethods() {
		ClassesWithPrintMethodTool printlnMethsTool = new ClassesWithPrintMethodTool(dw);
		assertTrue(printlnMethsTool.getClassesWithPrintMethods().size() <= 1);
	}
	
	public void testMutualAssociation() {
		MutualAssociationTool mutualAssocTool = new MutualAssociationTool(dw);
		assertEquals(0, mutualAssocTool.getMutualAssociationClasses().size());
	}
}