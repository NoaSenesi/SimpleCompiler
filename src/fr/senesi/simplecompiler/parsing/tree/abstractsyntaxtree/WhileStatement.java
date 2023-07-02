package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

import fr.senesi.simplecompiler.parsing.tree.Node;

public abstract class WhileStatement extends Statement {
	public WhileStatement(Expression condition, Node block) {
		super(Arrays.asList(condition, block));
	}

	public Expression getCondition() {
		return (Expression) getChildren().get(0);
	}

	public Node getBlock() {
		return getChildren().get(1);
	}
}