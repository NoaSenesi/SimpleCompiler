package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.ArrayList;

public class ContinueStatement extends Statement {
	public ContinueStatement() {
		super(new ArrayList<>());
	}

	public String generateCode() {
		return "continue;\n";
	}
}