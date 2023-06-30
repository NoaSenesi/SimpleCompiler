package fr.senesi.simplecompiler.parsing.tree.parsetree;

import fr.senesi.simplecompiler.lexing.tokens.Token;

public class Terminal extends ParseTreeNode {
	private Token token;

	public Terminal(Token token) {
		super(null);
		this.token = token;
	}

	public String getGrammarIdentification() {
		return token.getTerminalName();
	}

	public Token getToken() {
		return token;
	}

	public String toString() {
		return token.toString();
	}
}
