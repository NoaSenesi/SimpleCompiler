package fr.senesi.simplecompiler.parsing.tree.parsetree;

import java.util.List;

import fr.senesi.simplecompiler.parsing.tree.Node;

public class NonTerminal extends ParseTreeNode {
	private String grammarIdentification;

	public NonTerminal(String grammarIdentification, List<Node> children) {
		super(children);
		this.grammarIdentification = grammarIdentification;
	}

	public String getGrammarIdentification() {
		return grammarIdentification;
	}

	public String toString() {
		return grammarIdentification;
	}
}
