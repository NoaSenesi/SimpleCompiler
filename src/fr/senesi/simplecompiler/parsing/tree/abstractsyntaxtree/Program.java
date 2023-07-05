package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.List;

import fr.senesi.simplecompiler.codegen.CodeGenUtils;
import fr.senesi.simplecompiler.parsing.tree.Node;

public class Program extends ASTNode {
	public Program(List<Node> children) {
		super(children);
	}

	public String generateCode() {
		for (Node node : getChildren()) ((ASTNode) node).generateCode();
		CodeGenUtils.pushLine("end");
		return "";
	}

	public String generateCode(String labelStart, String labelEnd) {
		return generateCode();
	}
}