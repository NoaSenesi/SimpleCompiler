package fr.senesi.simplecompiler.lexing.tokens;

public class EOF extends Token {
	public EOF(int line, int column) {
		super(line, column, "");
	}

	public String getTerminalName() {
		return "$";
	}

	public String toString() {
		return "EOF";
	}
}