package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

import fr.senesi.simplecompiler.parsing.tree.Node;

public class WhileStatement extends Statement {
	public WhileStatement(Expression condition, Node block) {
		super(Arrays.asList(condition, block));
	}

	public Expression getCondition() {
		return (Expression) getChildren().get(0);
	}

	public ASTNode getBlock() {
		return (ASTNode) getChildren().get(1);
	}

	public String generateCode() {
		return "while (is_true(" + getCondition().generateCode() + ")) " + getBlock().generateCode();
	}
}