package fr.senesi.simplecompiler.parsing.tree.parsetree;

import java.util.List;

import fr.senesi.simplecompiler.parsing.tree.Node;

public abstract class ParseTreeNode extends Node {
	public abstract String getGrammarIdentification();

	public ParseTreeNode(List<Node> children) {
		super(children);
	}

	public Node toAST() {
		/*
		program, statements, if, else, unless (if not), while, do while, for, first assignment, assignment, function, return, break, continue, commas, ternary
		or, and, equals, not equals (equals not), lower than, greater than, lower than or equals (greater than not), greater than or equals (lower than not)
		+, unary +, -, unary -, *, /, %, **, !
		access, function call

		S, Block, Statement, CommaOrNothing, Comma, Expression, And, Equality, Relation, Assignment, AssignmentPrefix, Arrayable,
		Addition, Substraction, Multiplication, Division, Modulo, Power, Unary, Ternary, Atom, Access, Accessible
		*/

		return null;
	}
}
