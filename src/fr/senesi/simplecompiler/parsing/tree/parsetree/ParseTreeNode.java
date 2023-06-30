package fr.senesi.simplecompiler.parsing.tree.parsetree;

import java.util.List;

import fr.senesi.simplecompiler.lexing.tokens.Keyword.KeywordType;
import fr.senesi.simplecompiler.lexing.tokens.Special.SpecialType;
import fr.senesi.simplecompiler.parsing.tree.Node;

public abstract class ParseTreeNode extends Node {
	public abstract String getGrammarIdentification();

	public ParseTreeNode(List<Node> children) {
		super(children);
	}

	public ParseTreeNode() {
		super();
	}

	protected void compact() {
		for (int i = 0; i < getChildren().size(); i++) {
			Node child = getChildren().get(i);

			if (child instanceof NonTerminal) {
				((NonTerminal) child).compact();
			}

			if (child.getChildren().size() == 1) {
				getChildren().set(i, child.getChildren().get(0));
			}
		}
	}

	protected void simplify() {
		for (int i = 0; i < getChildren().size(); i++) {
			Node child = getChildren().get(i);

			if (child instanceof Terminal) {
				Terminal terminal = (Terminal) child;

				if (SpecialType.match(terminal.getGrammarIdentification()) != null
				 || KeywordType.match(terminal.getGrammarIdentification()) != null) {
					getChildren().remove(i--);

					continue;
				}
			}

			if (child instanceof NonTerminal) {
				NonTerminal nonTerminal = (NonTerminal) child;

				if (nonTerminal.getChildren().size() == 0) {
					if (nonTerminal.getGrammarIdentification().equals("ElseBlock")
					|| nonTerminal.getGrammarIdentification().equals("Statements")) {
						getChildren().remove(i--);

						continue;
					}
				}

				switch (getGrammarIdentification()) {
					case "S":
					case "Statements":
					case "Block":
					case "ElseBlock":
						switch (nonTerminal.getGrammarIdentification()) {
							case "Statements":
							case "Block":
								getChildren().addAll(i, nonTerminal.getChildren());
								getChildren().remove(i + nonTerminal.getChildren().size());
								break;
						}

						break;
				}
			}

			if (child instanceof ParseTreeNode) ((ParseTreeNode) child).simplify();
		}
	}

	public Node toAST() {
		compact();
		simplify();

		return this;
	}
}
