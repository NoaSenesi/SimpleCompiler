package fr.senesi.simplecompiler.parsing.tree.parsetree;

import java.util.List;

import fr.senesi.simplecompiler.lexing.tokens.Keyword.KeywordType;
import fr.senesi.simplecompiler.lexing.tokens.Special.SpecialType;
import fr.senesi.simplecompiler.parsing.tree.Node;

public abstract class PTNode extends Node {
	public abstract String getGrammarIdentification();

	public PTNode(List<Node> children) {
		super(children);
	}

	public PTNode() {
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
				} else while (nonTerminal.getGrammarIdentification().equals("Parenthesed")) {
					getChildren().set(i, nonTerminal.getChildren().get(1));
					nonTerminal = (NonTerminal) getChildren().get(i);
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

			if (child instanceof PTNode) ((PTNode) child).simplify();
		}
	}

	protected void removeDeadCode() {
		for (int i = 0; i < getChildren().size(); i++) {
			Node child = getChildren().get(i);

			if (child instanceof NonTerminal) {
				NonTerminal nonTerminal = (NonTerminal) child;

				if (nonTerminal.getGrammarIdentification().equals("BreakStatement")) {
					for (int j = i + 1; j < getChildren().size(); j++) {
						getChildren().remove(j--);
					}

					break;
				}
			}

			if (child instanceof PTNode) ((PTNode) child).removeDeadCode();
		}
	}

	private void transform() {
		if (getGrammarIdentification().equals("S")) {

		}
	}

	public void toAST() {
		compact();
		simplify();
		removeDeadCode();

		transform();
	}
}
