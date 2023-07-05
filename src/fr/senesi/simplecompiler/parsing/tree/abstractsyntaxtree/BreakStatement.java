package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.ArrayList;

import fr.senesi.simplecompiler.codegen.CodeGenUtils;

public class BreakStatement extends Statement {
	public BreakStatement() {
		super(new ArrayList<>());
	}

	public String generateCode() {
		System.out.println("Error: break statement outside of loop");
		System.exit(1);

		return "";
	}

	public String generateCode(String labelStart, String labelEnd) {
		CodeGenUtils.pushLine("goto " + labelEnd);
		return "";
	}
}