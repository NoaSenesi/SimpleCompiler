package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

public class Assignment extends ASTNode {
	private Identifier identifier;

	public Assignment(Identifier identifier, Expression expression) {
		super(Arrays.asList(expression));
		this.identifier = identifier;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public Expression getExpression() {
		return (Expression) getChildren().get(0);
	}

	public boolean isDeterministic() {
		return getExpression().isDeterministic();
	}
}