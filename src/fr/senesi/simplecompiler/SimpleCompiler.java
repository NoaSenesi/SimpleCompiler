package fr.senesi.simplecompiler;

import java.io.File;

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


		Output.tree(optimizer.getAST());
	}
}