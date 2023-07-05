package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import fr.senesi.simplecompiler.codegen.CodeGenUtils;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Evaluation.EvaluationType;

public class Modulo extends BinaryExpression {
	public Modulo(Expression left, Expression right) {
		super(left, right);
	}

	public Evaluation evaluate() {
		if (!isDeterministic()) return null;

		if (getLeft().evaluate().getType() == EvaluationType.INTEGER) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				if ((int) getRight().evaluate().getValue() == 0) divisionByZero();
				return new Evaluation((int) getLeft().evaluate().getValue() % (int) getRight().evaluate().getValue());
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				if ((double) getRight().evaluate().getValue() == 0) divisionByZero();
				return new Evaluation((int) getLeft().evaluate().getValue() % (double) getRight().evaluate().getValue());
			}
		} else if (getLeft().evaluate().getType() == EvaluationType.DECIMAL) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				if ((int) getRight().evaluate().getValue() == 0) divisionByZero();
				return new Evaluation((double) getLeft().evaluate().getValue() % (int) getRight().evaluate().getValue());
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				if ((double) getRight().evaluate().getValue() == 0) divisionByZero();
				return new Evaluation((double) getLeft().evaluate().getValue() % (double) getRight().evaluate().getValue());
			}
		}

		System.out.println("Error: modulo can only be applied to numbers");
		System.exit(1);

		return null;
	}

	private void divisionByZero() {
		System.out.println("Error: modulo by zero");
		System.exit(1);
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

		return left + " % " + right;
	}

	public String generateCode(String labelStart, String labelEnd) {
		return generateCode();
	}
}
