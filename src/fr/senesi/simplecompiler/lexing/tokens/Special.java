package fr.senesi.simplecompiler.lexing.tokens;

public class Special extends Token {
	private SpecialType type;

	public Special(int line, int column, SpecialType type) {
		super(line, column, type.getValue());
		this.type = type;
	}

	public String getTerminalName() {
		return getValue();
	}

	public SpecialType getType() {
		return type;
	}

	public enum SpecialType {
		SEMICOLON(";"),
		OPENING_BRACE("{"),
		CLOSING_BRACE("}"),
		OPENING_PARENTHESIS("("),
		CLOSING_PARENTHESIS(")"),
		PLUS("+"),
		MINUS("-"),
		MULTIPLY("*"),
		DIVIDE("/"),
		MODULO("%"),
		EQUALS("="),
		NOT("!"),
		GREATER_THAN(">"),
		LESS_THAN("<"),
		AMPERSAND("&"),
		PIPELINE("|");

		private String value;

		private SpecialType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static SpecialType match(String value) {
			for (SpecialType type : values()) {
				if (type.getValue().equals(value)) return type;
			}

			return null;
		}
	}
}