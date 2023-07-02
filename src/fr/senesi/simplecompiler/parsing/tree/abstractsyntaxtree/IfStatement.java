package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

import fr.senesi.simplecompiler.parsing.tree.Node;

public class IfStatement extends Statement {
	public IfStatement(Expression condition, Node block) {
		super(Arrays.asList(condition, block));
	}

	public IfStatement(Expression condition, Node block, Node elseBlock) {
		super(Arrays.asList(condition, block, elseBlock));
	}

	public Expression getCondition() {
		return (Expression) getChildren().get(0);
	}

	public ASTNode getBlock() {
		return (ASTNode) getChildren().get(1);
	}

	public boolean hasWhileblock() {
		return getChildren().size() > 2;
	}

	public ASTNode getElseBlock() {
		return hasWhileblock() ? (ASTNode) getChildren().get(2) : null;
	}

	public String generateCode() {
		String ret = "if (" + getCondition().generateCode().replaceAll("^\\((.*)\\)$", "$1") + ") " + getBlock().generateCode();
		if (hasWhileblock()) ret += "else " + getElseBlock().generateCode();

		return ret;
	}
}