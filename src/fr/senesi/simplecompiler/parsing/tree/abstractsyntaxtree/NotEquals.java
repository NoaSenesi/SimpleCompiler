package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import fr.senesi.simplecompiler.codegen.CodeGenUtils;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Evaluation.EvaluationType;

public class NotEquals extends BinaryExpression {
	public NotEquals(Expression left, Expression right) {
		super(left, right);
	}

	public Evaluation evaluate() {
		if (!isDeterministic()) return null;

		if (getLeft().evaluate().getType() == EvaluationType.INTEGER) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				return new Evaluation((int) getLeft().evaluate().getValue() != (int) getRight().evaluate().getValue() ? 1 : 0);
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				return new Evaluation((int) getLeft().evaluate().getValue() != (double) getRight().evaluate().getValue() ? 1 : 0);
			}
		} else if (getLeft().evaluate().getType() == EvaluationType.DECIMAL) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				return new Evaluation((double) getLeft().evaluate().getValue() != (int) getRight().evaluate().getValue() ? 1 : 0);
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				return new Evaluation((double) getLeft().evaluate().getValue() != (double) getRight().evaluate().getValue() ? 1 : 0);
			}
		} else if (getLeft().evaluate().getType() == EvaluationType.STRING) {
			if (getRight().evaluate().getType() == EvaluationType.STRING) {
				return new Evaluation(!((String) getLeft().evaluate().getValue()).equals((String) getRight().evaluate().getValue()) ? 1 : 0);
			}
		}

		System.out.println("Error: comparison can only be applied between numbers or between strings");
		System.exit(1);

		return null;
	}

	public String generateCode() {
		String left = getLeft().generateCode();
		String right = getRight().generateCode();

		if (!(getLeft() instanceof ASTTerminal)) {
			String code = left;
			left = CodeGenUtils.generateVariableName();
			CodeGenUtils.pushLine(left + " = " + code);
		}

		if (!(getRight() instanceof ASTTerminal)) {
			String code = right;
			right = CodeGenUtils.generateVariableName();
			CodeGenUtils.pushLine(right + " = " + code);
		}

		return left + " != " + right;
	}

	public String generateCode(String labelStart, String labelEnd) {
		return generateCode();
	}
}
