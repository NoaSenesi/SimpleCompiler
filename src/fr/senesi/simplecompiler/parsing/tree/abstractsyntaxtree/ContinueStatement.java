package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.ArrayList;

import fr.senesi.simplecompiler.codegen.CodeGenUtils;

public class ContinueStatement extends Statement {
	public ContinueStatement() {
		super(new ArrayList<>());
	}

	public String generateCode() {
		System.out.println("Error: continue statement outside of loop");
		System.exit(1);

		return "";
	}

	public String generateCode(String labelStart, String labelEnd) {
		CodeGenUtils.pushLine("goto " + labelStart);
		return "";
	}
}