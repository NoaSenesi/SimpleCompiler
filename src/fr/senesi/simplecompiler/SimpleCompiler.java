package fr.senesi.simplecompiler;

import java.io.File;

import fr.senesi.simplecompiler.codegen.CodeGen;
import fr.senesi.simplecompiler.lexing.Tokenizer;
import fr.senesi.simplecompiler.optimizing.Optimizer;
import fr.senesi.simplecompiler.parsing.Parser;

public class SimpleCompiler {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: simple <file>");
			System.exit(1);
		}

		boolean showTree = false, showCode = false;

		for (int i = 1; i < args.length; i++) {
			if (args[i].equals("-t")) showTree = true;
			if (args[i].equals("-c")) showCode = true;
		}

		File file = new File(args[0]);

		if (!file.exists()) {
			System.out.println("File " + args[0] + " not found.");
			System.exit(1);
		}

		Tokenizer tokenizer = new Tokenizer(file);

		Parser parser = new Parser(tokenizer);
		if (parser.getAST() == null) return;

		Optimizer optimizer = new Optimizer(parser);
		optimizer.optimize();

		if (showTree) Output.tree(optimizer.getAST());

		String output = args[0].replaceAll("\\.([^.]*)$", ".out.c");

		CodeGen.generateCode(output, optimizer.getAST(), showCode);
	}
}