package fr.senesi.simplecompiler.lexing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import fr.senesi.simplecompiler.lexing.tokens.Token;

public class Tokenizer {
	private String stream;

	public Tokenizer(File file) {
		try {
			stream = String.valueOf(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Token> getTokens() {
		return null;
	}
}