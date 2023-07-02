package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

public abstract class UnaryExpression extends Expression {
	public UnaryExpression(Expression node) {
		super(Arrays.asList(node));
	}

	public Expression getChild() {
		return (Expression) getChildren().get(0);
	}
}
