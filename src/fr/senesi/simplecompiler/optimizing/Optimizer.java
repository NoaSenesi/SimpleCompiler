package fr.senesi.simplecompiler.optimizing;

import fr.senesi.simplecompiler.parsing.Parser;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.ASTNode;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.BreakStatement;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.ContinueStatement;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Evaluation;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Evaluation.EvaluationType;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.Expression;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.IfStatement;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.WhileStatement;

public class Optimizer {
	private ASTNode ast;

	public Optimizer(Parser parser) {
		ast = parser.getAST();
	}

	public void optimize() {
		if (ast == null) return;

		precalculate(ast);
		removeDeadCode(ast);
		removeConditionalBlocks(ast);
	}

	private void precalculate(ASTNode node) {
		for (int i = 0; i < node.getChildren().size(); i++) {
			ASTNode child = (ASTNode) node.getChildren().get(i);

			if (child instanceof Expression && ((Expression) child).isDeterministic()) {
				node.getChildren().set(i, ((Expression) child).evaluate());
			} else {
				precalculate(child);
			}
		}
	}

	private void removeDeadCode(ASTNode node) {
		for (int i = 0; i < node.getChildren().size(); i++) {
			ASTNode child = (ASTNode) node.getChildren().get(i);

			if (child instanceof BreakStatement || child instanceof ContinueStatement) {
				for (int j = i + 1; j < node.getChildren().size(); j++) {
					node.getChildren().remove(j--);
				}
			}

			removeDeadCode(child);
		}
	}

	private void removeConditionalBlocks(ASTNode node) {
		for (int i = 0; i < node.getChildren().size(); i++) {
			ASTNode child = (ASTNode) node.getChildren().get(i);

			if (child instanceof IfStatement) {
				IfStatement ifst = (IfStatement) child;

				if (ifst.getCondition().isDeterministic()) {
					Evaluation e = ifst.getCondition().evaluate();

					boolean passes = e.getType() == EvaluationType.INTEGER && (int) e.getValue() != 0
								  || e.getType() == EvaluationType.DECIMAL && (double) e.getValue() != 0
								  || e.getType() == EvaluationType.STRING && ((String) e.getValue()).length() != 0;

					if (passes) node.getChildren().set(i--, ifst.getBlock());
					else if (ifst.hasWhileblock()) node.getChildren().set(i--, ifst.getElseBlock());
					else node.getChildren().remove(i--);
				}
			}

			if (child instanceof WhileStatement) {
				WhileStatement whst = (WhileStatement) child;

				if (whst.getCondition().isDeterministic()) {
					Evaluation e = whst.getCondition().evaluate();

					boolean passes = e.getType() == EvaluationType.INTEGER && (int) e.getValue() != 0
								  || e.getType() == EvaluationType.DECIMAL && (double) e.getValue() != 0
								  || e.getType() == EvaluationType.STRING && ((String) e.getValue()).length() != 0;

					if (!passes) node.getChildren().remove(i--);
				}
			}
		}
	}

	public ASTNode getAST() {
		return ast;
	}
}
