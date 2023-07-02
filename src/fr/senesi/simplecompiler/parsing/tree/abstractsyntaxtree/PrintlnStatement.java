package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

public class PrintlnStatement extends Statement {
	public PrintlnStatement(Expression expression) {
		super(Arrays.asList(expression));
	}

	public Expression getExpression() {
		return (Expression) getChildren().get(0);
	}

	public String generateCode() {
		return "println " + getExpression().generateCode().replaceAll("^\\((.*)\\)$", "$1") + ";\n";
	}
}