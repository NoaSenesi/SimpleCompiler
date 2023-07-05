package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.Arrays;

import fr.senesi.simplecompiler.codegen.CodeGenUtils;

public class Assignment extends ASTNode {
	private Identifier identifier;

	public Assignment(Identifier identifier, Expression expression) {
		super(Arrays.asList(expression));
		this.identifier = identifier;

		if (expression.isDeterministic()) {
			identifier.setValue(expression.evaluate());
			Identifier.IDENTIFIERS.put(identifier.getName(), identifier);
		}
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public Expression getExpression() {
		return (Expression) getChildren().get(0);
	}

	public boolean isDeterministic() {
		return getExpression().isDeterministic();
	}

	public String generateCode() {
		CodeGenUtils.pushLine(identifier.generateCode() + " = " + getExpression().generateCode());
		return "";
	}

	public String generateCode(String labelStart, String labelEnd) {
		return generateCode();
	}
}