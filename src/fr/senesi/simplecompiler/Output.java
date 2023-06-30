package fr.senesi.simplecompiler;

import fr.senesi.simplecompiler.parsing.tree.Node;
import fr.senesi.simplecompiler.parsing.tree.parsetree.NonTerminal;

public class Output {
	private static void parseTree(Node node, String indent, boolean first, boolean last) {
		String marker = first ? "" : (last ? "└── " : "├── ");

		System.out.print(indent + marker + node);
		System.out.println();

		indent += first ? "" : (last ? "    " : "│   ");

		if (node instanceof NonTerminal) {
			NonTerminal treeNode = (NonTerminal) node;

			for (int i = 0; i < treeNode.getChildren().size(); i++) {
				parseTree(treeNode.getChildren().get(i), indent, false, i == treeNode.getChildren().size() - 1);
			}
		}
	}


	public static void parseTree(Node node) {
		if (node == null) {
			System.out.println("null");
			return;
		}

		parseTree(node, "", true, true);
	}
}