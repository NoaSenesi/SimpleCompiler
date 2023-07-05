package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.List;

import fr.senesi.simplecompiler.parsing.tree.Node;

public abstract class ASTNode extends Node {
	public ASTNode(List<Node> children) {
		super(children);
	}

	public String toString() {
		return getClass().getSimpleName();
	}

	public void toAST() {

	}

	public abstract String generateCode();
	public abstract String generateCode(String labelStart, String labelEnd);
}
