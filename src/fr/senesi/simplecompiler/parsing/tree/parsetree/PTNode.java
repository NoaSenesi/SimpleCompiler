package fr.senesi.simplecompiler.parsing.tree.parsetree;

import java.util.List;

import fr.senesi.simplecompiler.lexing.tokens.Keyword.KeywordType;
import fr.senesi.simplecompiler.lexing.tokens.Special.SpecialType;
import fr.senesi.simplecompiler.lexing.tokens.Value;
import fr.senesi.simplecompiler.lexing.tokens.Value.ValueType;
import fr.senesi.simplecompiler.parsing.tree.Node;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Addition;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.And;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Assignment;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Block;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.BreakStatement;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.ContinueStatement;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Division;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Equals;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Evaluation;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Expression;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.GreaterEquals;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.GreaterThan;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Identifier;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.IfStatement;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.LowerEquals;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.LowerThan;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Modulo;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Multiplication;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.NotEquals;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Or;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.PrintStatement;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.PrintlnStatement;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Substraction;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.UnaryMinus;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.WhileStatement;

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

				if (nonTerminal.getGrammarIdentification().equals("GreaterThan") && nonTerminal.getChildren().size() == 4) nonTerminal.setGrammarIdentification("GreaterThanEquals");
				if (nonTerminal.getGrammarIdentification().equals("LowerThan") && nonTerminal.getChildren().size() == 4) nonTerminal.setGrammarIdentification("LowerThanEquals");
				if (nonTerminal.getGrammarIdentification().equals("PrintStatement") && ((PTNode) nonTerminal.getChildren().get(0)).getGrammarIdentification().equals("println")) nonTerminal.setGrammarIdentification("PrintlnStatement");

				if (nonTerminal.getGrammarIdentification().equals("ExpressionStatement")) {
					getChildren().remove(i--);
					continue;
				}

				if (nonTerminal.getChildren().size() == 0) {
					if (nonTerminal.getGrammarIdentification().equals("ElseBlock")
					|| nonTerminal.getGrammarIdentification().equals("Statements")) {
						getChildren().remove(i--);

						continue;
					}
				} else while (nonTerminal.getGrammarIdentification().equals("Parenthesed")) {
					getChildren().set(i, nonTerminal.getChildren().get(1));
					if (getChildren().get(i) instanceof NonTerminal) nonTerminal = (NonTerminal) getChildren().get(i);
					else break;
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

	private void transform() {
		for (int i = 0; i < getChildren().size(); i++) {
			Node child = getChildren().get(i);
			if (!(child instanceof PTNode)) continue;
			PTNode pt = (PTNode) child;

			pt.transform();

			switch (pt.getGrammarIdentification()) {
				case "Add":
					getChildren().set(i, new Addition((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "And":
					getChildren().set(i, new And((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "Assignment":
					getChildren().set(i, new Assignment((Identifier) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "Block":
					getChildren().set(i, new Block(pt.getChildren()));
					break;
				case "BreakStatement":
					getChildren().set(i, new BreakStatement());
					break;
				case "ContinueStatement":
					getChildren().set(i, new ContinueStatement());
					break;
				case "Div":
					getChildren().set(i, new Division((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "Equals":
					getChildren().set(i, new Equals((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "GreaterThanEquals":
					getChildren().set(i, new GreaterEquals((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "GreaterThan":
					getChildren().set(i, new GreaterThan((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "IfStatement":
					if (pt.getChildren().size() >= 3) getChildren().set(i, new IfStatement((Expression) pt.getChildren().get(0), pt.getChildren().get(1), pt.getChildren().get(2).getChildren().get(0)));
					else getChildren().set(i, new IfStatement((Expression) pt.getChildren().get(0), pt.getChildren().get(1)));
					break;
				case "LowerThanEquals":
					getChildren().set(i, new LowerEquals((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "LowerThan":
					getChildren().set(i, new LowerThan((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "Mod":
					getChildren().set(i, new Modulo((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "Mul":
					getChildren().set(i, new Multiplication((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "Negate":
					getChildren().set(i, new UnaryMinus((Expression) pt.getChildren().get(0)));
					break;
				case "NotEquals":
					getChildren().set(i, new NotEquals((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "Or":
					getChildren().set(i, new Or((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "PrintStatement":
					getChildren().set(i, new PrintStatement((Expression) pt.getChildren().get(0)));
					break;
				case "PrintlnStatement":
					getChildren().set(i, new PrintlnStatement((Expression) pt.getChildren().get(0)));
					break;
				case "Sub":
					getChildren().set(i, new Substraction((Expression) pt.getChildren().get(0), (Expression) pt.getChildren().get(1)));
					break;
				case "WhileStatement":
					getChildren().set(i, new WhileStatement((Expression) pt.getChildren().get(0), pt.getChildren().get(1)));
					break;

				case "ElseBlock":
					break;

				case "value":
					Value value = (Value) ((Terminal) pt).getToken();
					if (value.getType() == ValueType.INTEGER) getChildren().set(i, new Evaluation(Integer.valueOf(value.getValue())));
					if (value.getType() == ValueType.DECIMAL) getChildren().set(i, new Evaluation(Double.valueOf(value.getValue())));
					if (value.getType() == ValueType.STRING) getChildren().set(i, new Evaluation(value.getValue()));
					break;
				case "identifier":
					getChildren().set(i, new Identifier(((Terminal) pt).getToken().getValue()));
					break;

				default:
					System.out.println("Error: undefined " + pt.getGrammarIdentification() + " node");
					System.exit(1);
			}
		}
	}

	public void toAST() {
		compact();
		simplify();

		transform();
	}
}
