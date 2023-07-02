package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.List;

import fr.senesi.simplecompiler.parsing.tree.Node;

public class Program extends ASTNode {
	public Program(List<Node> children) {
		super(children);
	}

	public String generateCode() {
		String ret = "";
		for (Node node : getChildren()) ret += ((ASTNode) node).generateCode();
		return ret;
	}
}