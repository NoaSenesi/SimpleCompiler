package fr.senesi.simplecompiler;

import fr.senesi.simplecompiler.parsing.tree.Node;

public class Output {
	private static void tree(Node node, String indent, boolean first, boolean last) {
		String marker = first ? "" : (last ? "└── " : "├── ");

		System.out.print(indent + marker + node);
		System.out.println();

		indent += first ? "" : (last ? "    " : "│   ");

		if (node.getChildren().size() > 0) {
			for (int i = 0; i < node.getChildren().size(); i++) {
				tree(node.getChildren().get(i), indent, false, i == node.getChildren().size() - 1);
			}
		}
	}


	public static void tree(Node node) {
		if (node == null) {
			System.out.println("null");
			return;
		}

		tree(node, "", true, true);
	}
}