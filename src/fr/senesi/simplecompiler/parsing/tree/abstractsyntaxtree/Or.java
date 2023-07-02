package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import fr.senesi.simplecompiler.optimizing.Evaluation;
import fr.senesi.simplecompiler.optimizing.Evaluation.EvaluationType;

public class Or extends BinaryExpression {
	public Or(Expression left, Expression right) {
		super(left, right);
	}

	public Evaluation evaluate() {
		if (!isDeterministic()) return null;

		if (getLeft().evaluate().getType() == EvaluationType.INTEGER) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				return new Evaluation((int) getLeft().evaluate().getValue() + (int) getRight().evaluate().getValue() >= 1 ? 1 : 0);
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				return new Evaluation((int) getLeft().evaluate().getValue() + (double) getRight().evaluate().getValue() >= 1 ? 1 : 0);
			} else if (getRight().evaluate().getType() == EvaluationType.STRING) {
				return new Evaluation((int) getLeft().evaluate().getValue() + ((String) getRight().evaluate().getValue()).length() >= 1 ? 1 : 0);
			}
		} else if (getLeft().evaluate().getType() == EvaluationType.DECIMAL) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				return new Evaluation((double) getLeft().evaluate().getValue() + (int) getRight().evaluate().getValue() >= 1 ? 1 : 0);
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				return new Evaluation((double) getLeft().evaluate().getValue() + (double) getRight().evaluate().getValue() >= 1 ? 1 : 0);
			} else if (getRight().evaluate().getType() == EvaluationType.STRING) {
				return new Evaluation((double) getLeft().evaluate().getValue() + ((String) getRight().evaluate().getValue()).length() >= 1 ? 1 : 0);
			}
		} else if (getLeft().evaluate().getType() == EvaluationType.STRING) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				return new Evaluation(((String) getLeft().evaluate().getValue()).length() + (int) getRight().evaluate().getValue() >= 1 ? 1 : 0);
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				return new Evaluation(((String) getLeft().evaluate().getValue()).length() + (double) getRight().evaluate().getValue() >= 1 ? 1 : 0);
			} else if (getRight().evaluate().getType() == EvaluationType.STRING) {
				return new Evaluation(((String) getLeft().evaluate().getValue()).length() + ((String) getRight().evaluate().getValue()).length() >= 1 ? 1 : 0);
			}
		}

		return null;
	}
}
