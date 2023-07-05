package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

import fr.senesi.simplecompiler.codegen.CodeGenUtils;

public class PrintlnStatement extends Statement {
	public PrintlnStatement(Expression expression) {
		super(Arrays.asList(expression));
	}

	public Expression getExpression() {
		return (Expression) getChildren().get(0);
	}

	public String generateCode() {
		String exp = getExpression().generateCode();

		if (!(getExpression() instanceof ASTTerminal)) {
			String code = exp;
			exp = CodeGenUtils.generateVariableName();
			CodeGenUtils.pushLine(exp + " = " + code);
		}

		CodeGenUtils.pushLine("println " + exp);

		return "";
	}

	public String generateCode(String labelStart, String labelEnd) {
		return generateCode();
	}
}