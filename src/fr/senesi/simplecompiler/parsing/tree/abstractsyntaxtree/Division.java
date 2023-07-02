package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Evaluation.EvaluationType;

public class Division extends BinaryExpression {
	public Division(Expression left, Expression right) {
		super(left, right);
	}

	public Evaluation evaluate() {
		if (!isDeterministic()) return null;

		if (getLeft().evaluate().getType() == EvaluationType.INTEGER) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				if ((int) getRight().evaluate().getValue() == 0) divisionByZero();
				return new Evaluation(Double.valueOf((int) getLeft().evaluate().getValue()) / (int) getRight().evaluate().getValue());
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				if ((double) getRight().evaluate().getValue() == 0) divisionByZero();
				return new Evaluation((int) getLeft().evaluate().getValue() / (double) getRight().evaluate().getValue());
			}
		} else if (getLeft().evaluate().getType() == EvaluationType.DECIMAL) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				if ((int) getRight().evaluate().getValue() == 0) divisionByZero();
				return new Evaluation((double) getLeft().evaluate().getValue() / (int) getRight().evaluate().getValue());
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				if ((double) getRight().evaluate().getValue() == 0) divisionByZero();
				return new Evaluation((double) getLeft().evaluate().getValue() / (double) getRight().evaluate().getValue());
			}
		}

		System.out.println("Error: division can only be applied to numbers");
		System.exit(1);

		return null;
	}

	private void divisionByZero() {
		System.out.println("Error: division by zero");
		System.exit(1);
	}

	public String generateCode() {
		return "(" + getLeft().generateCode() + "/" + getRight().generateCode() + ")";
	}
}
