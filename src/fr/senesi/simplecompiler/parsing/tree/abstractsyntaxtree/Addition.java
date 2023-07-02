package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Evaluation.EvaluationType;

public class Addition extends BinaryExpression {
	public Addition(Expression left, Expression right) {
		super(left, right);
	}

	public Evaluation evaluate() {
		if (!isDeterministic()) return null;

		if (getLeft().evaluate().getType() == EvaluationType.INTEGER) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				return new Evaluation((int) getLeft().evaluate().getValue() + (int) getRight().evaluate().getValue());
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				return new Evaluation((int) getLeft().evaluate().getValue() + (double) getRight().evaluate().getValue());
			} else {
				return new Evaluation((int) getLeft().evaluate().getValue() + (String) getRight().evaluate().getValue());
			}
		} else if (getLeft().evaluate().getType() == EvaluationType.DECIMAL) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				return new Evaluation((double) getLeft().evaluate().getValue() + (int) getRight().evaluate().getValue());
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				return new Evaluation((double) getLeft().evaluate().getValue() + (double) getRight().evaluate().getValue());
			} else {
				return new Evaluation((double) getLeft().evaluate().getValue() + (String) getRight().evaluate().getValue());
			}
		} else {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				return new Evaluation((String) getLeft().evaluate().getValue() + (int) getRight().evaluate().getValue());
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				return new Evaluation((String) getLeft().evaluate().getValue() + (double) getRight().evaluate().getValue());
			} else {
				return new Evaluation((String) getLeft().evaluate().getValue() + (String) getRight().evaluate().getValue());
			}
		}
	}

	public String generateCode() {
		return "(" + getLeft().generateCode() + "+" + getRight().generateCode() + ")";
	}
}
