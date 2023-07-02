package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.List;

import fr.senesi.simplecompiler.parsing.tree.Node;

public abstract class Statement extends ASTNode {
	public Statement(List<Node> children) {
		super(children);
	}
}