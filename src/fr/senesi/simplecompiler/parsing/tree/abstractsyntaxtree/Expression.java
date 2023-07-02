package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.List;

import fr.senesi.simplecompiler.optimizing.Evaluation;
import fr.senesi.simplecompiler.parsing.tree.Node;

public abstract class Expression extends ASTNode {
	public Expression(List<Node> children) {
		super(children);

		for (Node child : children) {
			if (!(child instanceof Expression)) {
				System.out.println("Error: expression children must be expressions");
			}
		}
	}

	public boolean isDeterministic() {
		for (Node child : getChildren()) {
			if (!((Expression) child).isDeterministic()) return false;
		}

		return true;
	}

	public abstract Evaluation evaluate();
}