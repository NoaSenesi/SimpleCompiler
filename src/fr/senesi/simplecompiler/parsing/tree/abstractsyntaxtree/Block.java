package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.List;

import fr.senesi.simplecompiler.parsing.tree.Node;

public class Block extends ASTNode {
	public Block(List<Node> children) {
		super(children);
	}

	public String generateCode() {
		for (Node child : getChildren()) {
			((ASTNode) child).generateCode();
		}

		return "";
	}

	public String generateCode(String labelStart, String labelEnd) {
		for (Node child : getChildren()) {
			((ASTNode) child).generateCode(labelStart, labelEnd);
		}

		return "";
	}
}