package fr.senesi.simplecompiler.lexing.tokens;

public abstract class Token {
	private int line, column;
	private String value;

	public Token(int line, int column, String value) {
		this.line = line;
		this.column = column;
		this.value = value;
	}

	public abstract String getTerminalName();

	public String toString() {
		return getTerminalName() + " at " + line + ":" + column + " - " + value;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	public String getValue() {
		return value;
	}
}
