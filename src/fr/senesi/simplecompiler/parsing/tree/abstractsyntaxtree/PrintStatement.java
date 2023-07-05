package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

public class PrintStatement extends Statement {
	public PrintStatement(Expression expression) {
		super(Arrays.asList(expression));
	}

	public Expression getExpression() {
		return (Expression) getChildren().get(0);
	}

	public String generateCode() {
		return "print(" + getExpression().generateCode() + ");\n";
	}
}