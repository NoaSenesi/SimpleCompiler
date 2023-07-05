package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

import fr.senesi.simplecompiler.codegen.CodeGenUtils;
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

	public boolean hasElseBlock() {
		return getChildren().size() > 2;
	}

	public ASTNode getElseBlock() {
		return hasElseBlock() ? (ASTNode) getChildren().get(2) : null;
	}

	public String generateCode() {
		String exp = getCondition().generateCode();

		if (!(getCondition() instanceof ASTTerminal)) {
			String code = exp;
			exp = CodeGenUtils.generateVariableName();
			CodeGenUtils.pushLine(exp + " = " + code);
		}

		CodeGenUtils.pushLine("compare " + exp + " 0");
		String label = CodeGenUtils.generateLabelName(), label2 = null;
		CodeGenUtils.pushLine("gotoeq " + label);

		getBlock().generateCode();

		if (hasElseBlock()) {
			label2 = CodeGenUtils.generateLabelName();
			CodeGenUtils.pushLine("goto " + label2);
		}

		CodeGenUtils.pushLine(label + ":");


		if (hasElseBlock()) {
			getElseBlock().generateCode();
			CodeGenUtils.pushLine(label2 + ":");
		}

		return "";
	}

	public String generateCode(String labelStart, String labelEnd) {
		return generateCode();
	}
}