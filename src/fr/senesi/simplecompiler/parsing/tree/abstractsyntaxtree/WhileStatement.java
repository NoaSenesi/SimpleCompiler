package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

import fr.senesi.simplecompiler.codegen.CodeGenUtils;
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
		String labelStart = CodeGenUtils.generateLabelName(), labelEnd = CodeGenUtils.generateLabelName();
		CodeGenUtils.pushLine(labelStart + ":");

		String exp = getCondition().generateCode();

		if (!(getCondition() instanceof ASTTerminal)) {
			String code = exp;
			exp = CodeGenUtils.generateVariableName();
			CodeGenUtils.pushLine(exp + " = " + code);
		}

		CodeGenUtils.pushLine("compare " + exp + " 0");
		CodeGenUtils.pushLine("gotoeq " + labelEnd);

		getBlock().generateCode(labelStart, labelEnd);

		CodeGenUtils.pushLine("goto " + labelStart);
		CodeGenUtils.pushLine(labelEnd + ":");

		return "";
	}

	public String generateCode(String labelStart, String labelEnd) {
		return generateCode();
	}
}