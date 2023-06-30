package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.List;

import fr.senesi.simplecompiler.parsing.tree.Node;

public class ASTNode extends Node {
	public ASTNode(List<Node> children) {
		super(children);
	}

	public String toString() {
		return getClass().getSimpleName();
	}

	public void toAST() {

	}
}
