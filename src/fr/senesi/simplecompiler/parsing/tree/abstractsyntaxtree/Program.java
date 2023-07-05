package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.ArrayList;
import java.util.List;

import fr.senesi.simplecompiler.parsing.tree.Node;

public class Program extends ASTNode {
	public Program(List<Node> children) {
		super(children);
	}

	public String generateCode() {
		String ret = "int main() {\n";
		ret += "free_string_stack *stack = new_free_string_stack();\n\n";

		List<String> variables = new ArrayList<>();
		getVariables(this, variables);
		for (String name : variables) ret += "evaluation " + name + ";\n";

		ret += "\n";
		for (Node node : getChildren()) ret += ((ASTNode) node).generateCode();
		ret += "free_all(stack);\n\n";
		ret += "return 0;\n";
		ret += "}\n";

		return ret;
	}

	private void getVariables(ASTNode node, List<String> list) {
		if (node instanceof Identifier) {
			String name = ((Identifier) node).generateCode();
			if (!list.contains(name)) list.add(name);
		} else {
			if (node instanceof Assignment) {
				String name = ((Assignment) node).getIdentifier().generateCode();
				if (!list.contains(name)) list.add(name);
			}

			for (Node child : node.getChildren()) getVariables((ASTNode) child, list);
		}
	}
}