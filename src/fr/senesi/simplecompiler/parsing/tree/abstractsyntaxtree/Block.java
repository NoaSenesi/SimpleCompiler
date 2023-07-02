package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.List;

import fr.senesi.simplecompiler.parsing.tree.Node;

public class Block extends ASTNode {
	public Block(List<Node> children) {
		super(children);
	}

	public String generateCode() {
		String ret = "{\n";
		for (Node node : getChildren()) ret += ((ASTNode) node).generateCode();
		ret += "\n}\n";

		return ret;
	}
}