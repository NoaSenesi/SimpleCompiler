package fr.senesi.simplecompiler.lexing.tokens;

public class Keyword extends Token {
	private KeywordType type;

	public Keyword(int line, int column, KeywordType type) {
		super(line, column, type.getValue());
		this.type = type;
	}

	public String getTerminalName() {
		return getValue();
	}

	public KeywordType getType() {
		return type;
	}

	public enum KeywordType {
		IF("if"),
		WHILE("while"),
		PRINT("print"),
		PRINTLN("println"),
		BREAK("break"),
		CONTINUE("continue"),
		ELSE("else");

		private String value;

		private KeywordType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static KeywordType match(String value) {
			for (KeywordType type : values()) {
				if (type.getValue().equals(value)) return type;
			}

			return null;
		}
	}
}