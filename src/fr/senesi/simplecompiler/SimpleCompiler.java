package fr.senesi.simplecompiler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fr.senesi.simplecompiler.lexing.Tokenizer;
import fr.senesi.simplecompiler.optimizing.Optimizer;
import fr.senesi.simplecompiler.parsing.Parser;

public class SimpleCompiler {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: simple <file>");
			System.exit(1);
		}

		File file = new File(args[0]);

		if (!file.exists()) {
			System.out.println("File " + args[0] + " not found.");
			System.exit(1);
		}

		Tokenizer tokenizer = new Tokenizer(file);

		Parser parser = new Parser(tokenizer);
		
		Optimizer optimizer = new Optimizer(parser);
		optimizer.optimize();

		String output = args[0].replaceAll("\\.([^.]*)$", ".out.$1");

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			writer.write(optimizer.getAST().generateCode());
			writer.close();
		} catch (IOException e) {
			System.out.println("Couldn't write in " + output + ": " + e.getMessage());
		}
	}
}