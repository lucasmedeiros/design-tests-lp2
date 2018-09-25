package main.tools;

import java.io.IOException;

import org.designwizard.api.DesignWizard;

public class Testar {

	public static void main(String[] args) {
		try 
		{
			DesignWizard dw = new DesignWizard("jars/campominado.jar");
			MutualAssociationChecker checker = new MutualAssociationChecker(dw);
			checker.getMutualAssociationClasses();
		} catch(IOException exception) {
			System.out.println("Algum erro aconteceu... ");
		}

	}

}
