package fr.senesi.simplecompiler.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import fr.senesi.simplecompiler.lexing.Tokenizer;
import fr.senesi.simplecompiler.parsing.table.Action;
import fr.senesi.simplecompiler.parsing.table.Action.ActionType;
import fr.senesi.simplecompiler.parsing.table.ParseTable;
import fr.senesi.simplecompiler.parsing.tree.Node;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.ASTNode;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Program;
import fr.senesi.simplecompiler.parsing.tree.parsetree.NonTerminal;
import fr.senesi.simplecompiler.parsing.tree.parsetree.PTNode;
import fr.senesi.simplecompiler.parsing.tree.parsetree.Terminal;

public class Parser {
	private Tokenizer tokenizer;
	private Stack<Integer> stateStack;
	private Stack<Node> parseTree;
	private int cursor;
	private ParseTable table;
	private PTNode parseTreeNode;
	private boolean ast_generated = false;

	public Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;

		stateStack = new Stack<>();
		parseTree = new Stack<>();
		table = new ParseTable("grammar.csv");
	}

	public Tokenizer getTokenizer() {
		return tokenizer;
	}

	private void buildParseTree() {
		stateStack.push(0);

		PTNode peek = new Terminal(tokenizer.getTokens().get(cursor));
		Action action = table.getAction(stateStack.peek(), peek);

		while (action.getActionType() != ActionType.ACCEPT) {
			if (action.getActionType() == ActionType.ERROR) {
				System.out.print("Unexpected " + peek.toString().toLowerCase());
				if (peek instanceof Terminal) System.out.print(" at line " + ((Terminal) peek).getToken().getLine() + ", column " + ((Terminal) peek).getToken().getColumn());
				System.out.println(", expected: " + table.expected(stateStack.peek()));
				return;
			}

			if (action.getActionType() == ActionType.SHIFT) {
				stateStack.push(action.getNextState());
				parseTree.push(peek);
				cursor++;
				peek = new Terminal(tokenizer.getTokens().get(cursor));
			} else if (action.getActionType() == ActionType.REDUCE) {
				List<Node> childrenReverse = new ArrayList<>(), children = new ArrayList<>();

				for (int i = 0; i < action.getPopCount(); i++) {
					stateStack.pop();
					childrenReverse.add(parseTree.pop());
				}

				for (int i = childrenReverse.size() - 1; i >= 0; i--) {
					children.add(childrenReverse.get(i));
				}

				parseTree.push(peek = new NonTerminal(action.getReduceTo(), children));
			} else if (action.getActionType() == ActionType.GOTO) {
				stateStack.push(action.getNextState());
				peek = new Terminal(tokenizer.getTokens().get(cursor));
			}

			action = table.getAction(stateStack.peek(), peek);
		}

		parseTreeNode = (PTNode) parseTree.peek();
	}

	public ASTNode getAST() {
		if (parseTreeNode == null) {
			buildParseTree();
		}

		if (parseTreeNode == null) return null;

		if (!ast_generated) {
			parseTreeNode.toAST();
			ast_generated = true;
		}

		return new Program(parseTreeNode.getChildren());
	}
}
