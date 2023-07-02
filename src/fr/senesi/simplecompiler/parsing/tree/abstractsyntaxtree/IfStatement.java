package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

import fr.senesi.simplecompiler.parsing.tree.Node;

public abstract class IfStatement extends Statement {
	public IfStatement(Expression condition, Node block) {
		super(Arrays.asList(condition, block));
	}

	public IfStatement(Expression condition, Node block, Node elseBlock) {
		super(Arrays.asList(condition, block, elseBlock));
	}

	public Expression getCondition() {
		return (Expression) getChildren().get(0);
	}

	public Node getBlock() {
		return getChildren().get(1);
	}

	public Node getElseBlock() {
		return getChildren().size() > 2 ? getChildren().get(2) : null;
	}
}