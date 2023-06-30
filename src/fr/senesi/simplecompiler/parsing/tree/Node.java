package fr.senesi.simplecompiler.parsing.tree;

import java.util.List;

public class Node {
	private List<Node> children;

	public Node(List<Node> children) {
		this.children = children;
	}

	public List<Node> getChildren() {
		return children;
	}

	public Node toAST() {
		return this;
	}
}