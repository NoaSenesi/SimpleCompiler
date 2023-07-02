package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import fr.senesi.simplecompiler.optimizing.Evaluation;
import fr.senesi.simplecompiler.optimizing.Evaluation.EvaluationType;

public class Substraction extends BinaryExpression {
	public Substraction(Expression left, Expression right) {
		super(left, right);
	}

	public Evaluation evaluate() {
		if (!isDeterministic()) return null;

		if (getLeft().evaluate().getType() == EvaluationType.INTEGER) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				return new Evaluation((int) getLeft().evaluate().getValue() - (int) getRight().evaluate().getValue());
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				return new Evaluation((int) getLeft().evaluate().getValue() - (double) getRight().evaluate().getValue());
			}
		} else if (getLeft().evaluate().getType() == EvaluationType.DECIMAL) {
			if (getRight().evaluate().getType() == EvaluationType.INTEGER) {
				return new Evaluation((double) getLeft().evaluate().getValue() - (int) getRight().evaluate().getValue());
			} else if (getRight().evaluate().getType() == EvaluationType.DECIMAL) {
				return new Evaluation((double) getLeft().evaluate().getValue() - (double) getRight().evaluate().getValue());
			}
		}

		System.out.println("Error: substraction can only be applied to numbers");
		System.exit(1);

		return null;
	}
}
