package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.List;

import fr.senesi.simplecompiler.parsing.tree.Node;

public class Block extends ASTNode {
	public Block(List<Node> children) {
		super(children);
	}
}