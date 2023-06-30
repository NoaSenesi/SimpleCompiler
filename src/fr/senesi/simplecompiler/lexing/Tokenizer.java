package fr.senesi.simplecompiler.lexing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import fr.senesi.simplecompiler.lexing.tokens.EOF;
import fr.senesi.simplecompiler.lexing.tokens.Identifier;
import fr.senesi.simplecompiler.lexing.tokens.Keyword;
import fr.senesi.simplecompiler.lexing.tokens.Keyword.KeywordType;
import fr.senesi.simplecompiler.lexing.tokens.Special;
import fr.senesi.simplecompiler.lexing.tokens.Special.SpecialType;
import fr.senesi.simplecompiler.lexing.tokens.Token;
import fr.senesi.simplecompiler.lexing.tokens.Value;
import fr.senesi.simplecompiler.lexing.tokens.Value.ValueType;

public class Tokenizer {
	private String stream;
	private List<Token> tokens;

	public Tokenizer(File file) {
		try {
			stream = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Token> getTokens() {
		if (tokens != null) return tokens;

		tokens = new ArrayList<>();

		int line = 1, column = 1, cursor = 0;
		boolean lastError = false;

		// {,},if,(,),while,print,;,println,break,continue,else,+,-,*,/,%,=,!,>,<,identifier,value

		while (cursor < stream.length()) {
			char c = stream.charAt(cursor);

			if (c == '\n') {
				line++;
				column = 1;
				lastError = false;
			} else if (c == '\r') {

			} else if (c == ' ') {
				column++;
				lastError = false;
			} else if (c == '\t') {
				column += 4;
				lastError = false;
			} else if (c >= '0' && c <= '9') {
				int start = cursor;
				boolean isDecimal = false;

				while (cursor < stream.length() && (stream.charAt(cursor) >= '0' && stream.charAt(cursor) <= '9' || stream.charAt(cursor) == '.')) {
					if (stream.charAt(cursor) == '.') {
						if (isDecimal) System.out.println("Warning: invalid decimal number at line " + line + ", column " + column + ", ignoring");
						isDecimal = true;
					}

					cursor++;
				}

				tokens.add(new Value(line, column, stream.substring(start, cursor), isDecimal ? ValueType.DECIMAL : ValueType.INTEGER));
				column += cursor - start + 1;

				continue;
			} else if (c == '"' || c == '\'') {
				char quote = c;

				int start = cursor++;

				while (cursor < stream.length() && stream.charAt(cursor) != quote) {
					if (stream.charAt(cursor) == '\\') cursor++;

					if (stream.charAt(cursor) == '\n') {
						System.out.println("Error: illegal line break at line " + line);
						System.exit(1);
					}

					cursor++;
				}

				if (cursor == stream.length()) {
					System.out.println("Warning: missing closing quote at line " + line);
					System.exit(1);
				}

				tokens.add(new Value(line, column, stream.substring(start + 1, cursor), ValueType.STRING));
				column += cursor - start + 1;
				lastError = false;
			} else if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_') {
				int start = cursor;

				while (cursor < stream.length() && (stream.charAt(cursor) >= 'A' && stream.charAt(cursor) <= 'Z' || stream.charAt(cursor) >= 'a' && stream.charAt(cursor) <= 'z' || stream.charAt(cursor) == '_' || stream.charAt(cursor) >= '0' && stream.charAt(cursor) <= '9')) {
					cursor++;
				}

				KeywordType type = KeywordType.match(stream.substring(start, cursor));

				if (type == null) tokens.add(new Identifier(line, column, stream.substring(start, cursor)));
				else tokens.add(new Keyword(line, column, type));

				column += cursor - start + 1;

				continue;
			} else {
				SpecialType type = SpecialType.match(String.valueOf(c));

				if (type == null) {
					if (!lastError) {
						System.out.println("Error: unexpected character '" + c + "' at line " + line + ", column " + column + ", ignoring");
						lastError = true;
					}
				} else {
					tokens.add(new Special(line, column, type));
					lastError = false;
				}

				column++;
			}

			cursor++;
		}

		tokens.add(new EOF(line, column));

		return tokens;
	}
}