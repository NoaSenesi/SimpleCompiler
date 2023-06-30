package fr.senesi.simplecompiler.lexing.tokens;

public class Value extends Token {
	private ValueType type;

	public Value(int line, int column, String value, ValueType type) {
		super(line, column, value);
		this.type = type;
	}

	public String getTerminalName() {
		return "value";
	}

	public ValueType getType() {
		return type;
	}

	public enum ValueType {
		INTEGER, DECIMAL, STRING
	}
}