package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import fr.senesi.simplecompiler.codegen.CodeGenUtils;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Evaluation.EvaluationType;

public class UnaryMinus extends UnaryExpression {
	public UnaryMinus(Expression node) {
		super(node);
	}

	public Evaluation evaluate() {
		if (!isDeterministic()) return null;

		if (getChild().evaluate().getType() == EvaluationType.INTEGER) {
			return new Evaluation(- (int) getChild().evaluate().getValue());
		} else if (getChild().evaluate().getType() == EvaluationType.DECIMAL) {
			return new Evaluation(- (double) getChild().evaluate().getValue());
		}

		System.out.println("Error: unary minus can only be applied to numbers");
		System.exit(1);

		return null;
	}

	public String generateCode() {
		String child = getChild().generateCode();

		if (!(getChild() instanceof ASTTerminal)) {
			String code = child;
			child = CodeGenUtils.generateVariableName();
			CodeGenUtils.pushLine(child + " = " + code);
		}

		return "-" + child;
	}

	public String generateCode(String labelStart, String labelEnd) {
		return generateCode();
	}
}
