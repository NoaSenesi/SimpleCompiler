package fr.senesi.simplecompiler.lexing.tokens;

public class Identifier extends Token {
	public Identifier(int line, int column, String value) {
		super(line, column, value);
	}

	public String getTerminalName() {
		return "identifier";
	}
}