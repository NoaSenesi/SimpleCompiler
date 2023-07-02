package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

public abstract class BinaryExpression extends Expression {
	public BinaryExpression(Expression left, Expression right) {
		super(Arrays.asList(left, right));
	}

	public Expression getLeft() {
		return (Expression) getChildren().get(0);
	}

	public Expression getRight() {
		return (Expression) getChildren().get(1);
	}
}