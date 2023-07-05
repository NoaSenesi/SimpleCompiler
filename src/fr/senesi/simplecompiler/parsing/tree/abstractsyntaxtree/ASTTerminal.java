package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.List;

import fr.senesi.simplecompiler.parsing.tree.Node;

public abstract class ASTTerminal extends Expression {
	public ASTTerminal(List<Node> children) {
		super(children);
	}
}
