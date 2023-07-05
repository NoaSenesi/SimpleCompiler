package fr.senesi.simplecompiler.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fr.senesi.simplecompiler.parsing.tree.Node;
import fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree.ASTNode;

public class CodeGen {
	public static void generateCode(String output, ASTNode ast, boolean showCode) {
		String[] name = output.split("/");
		name[name.length - 1] = "runtime." + name[name.length - 1];
		String runtimePath = String.join("/", name), headerPath = runtimePath.replaceAll("c$", "h");
		String headerName = headerPath.split("/")[headerPath.split("/").length - 1], runtimeName = name[name.length - 1];

		generateCompiledCode(output, headerName, ast);
		generateRuntime(runtimePath, headerName, ast);
		generateHeader(headerPath, ast);
		String build = generateBuild(output, runtimeName);

		if (!showCode) {
			String path = output.lastIndexOf("/") <= 1 ? "" : output.substring(0, output.lastIndexOf("/")), buildFile = build.split("/")[build.split("/").length - 1];
			String outputName = output.split("/")[output.split("/").length - 1];

			try {
				ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "./" + buildFile + " && rm " + buildFile + " " + buildFile + ".bat " + runtimeName + " " + headerName + " " + outputName);
				pb.directory(new File(System.getProperty("user.dir") + File.separator + path));
				pb.inheritIO();
				pb.start();
			} catch (IOException e) {
				try {
					ProcessBuilder pb = new ProcessBuilder("cmd", "/c", buildFile + " && del " + buildFile + " " + buildFile + ".bat " + runtimeName + " " + headerName + " " + outputName);
					pb.directory(new File(System.getProperty("user.dir") + File.separator + path));
					pb.inheritIO();
					pb.start();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private static String generateBuild(String output, String runtimeName) {
		String outputName = output.split("/")[output.split("/").length - 1].split("\\.")[0];
		String build = output.substring(0, output.length() - 6) + "Build";

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(build));
			writer.write("#!/bin/sh\n\n");
			writer.write("gcc -o " + outputName + " " + outputName + ".out.c " + runtimeName + "\n");
			writer.close();

			try {
				Runtime.getRuntime().exec(new String[] {"chmod", "+x", build});
			} catch (Exception e) {}

			writer = new BufferedWriter(new FileWriter(build + ".bat"));
			writer.write("@echo off\n\n");
			writer.write("gcc -o " + outputName + ".exe " + outputName + ".out.c " + runtimeName + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return build;
	}

	private static void generateCompiledCode(String output, String headerName, ASTNode ast) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			writer.write("#include \"" + headerName + "\"\n\n");
			writer.write(ast.generateCode());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateRuntime(String runtimePath, String headerName, ASTNode ast) {
		boolean add = ASTHas("Addition", ast),
				and = ASTHas("And", ast),
				div = ASTHas("Division", ast),
				eq = ASTHas("Equals", ast),
				ge = ASTHas("GreaterEquals", ast),
				gt = ASTHas("GreaterThan", ast),
				le = ASTHas("LowerEquals", ast),
				lt = ASTHas("LowerThan", ast),
				minus = ASTHas("UnaryMinus", ast),
				mod = ASTHas("Modulo", ast),
				mul = ASTHas("Multiplication", ast),
				ne = ASTHas("NotEquals", ast),
				or = ASTHas("Or", ast),
				sub = ASTHas("Subtraction", ast),
				print = ASTHas("PrintStatement", ast),
				println = ASTHas("PrintlnStatement", ast);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(runtimePath));
			writer.write("#include \"" + headerName + "\"\n");
			writer.write("#include <stdio.h>\n");
			writer.write("#include <stdlib.h>\n\n");
			writer.write("#include <string.h>\n");
			if (mod) writer.write("#include <math.h>\n");
			writer.write("\n");

			writer.write("free_string_stack *new_free_string_stack() {\n");
			writer.write("	free_string_stack *stack = malloc(sizeof(free_string_stack));\n");
			writer.write("	stack->head = NULL;\n\n");
			writer.write("	return stack;\n");
			writer.write("}\n\n");

			if (print) {
				writer.write("void print(evaluation eval) {\n");
				writer.write("	if (eval.type == INTEGER) printf(\"%ld\", eval.integer);\n");
				writer.write("	if (eval.type == DECIMAL) printf(\"%f\", eval.decimal);\n");
				writer.write("	if (eval.type == STRING) printf(\"%s\", eval.string);\n");
				writer.write("}\n\n");
			}

			if (println) {
				writer.write("void println(evaluation eval) {\n");
				writer.write("	if (eval.type == INTEGER) printf(\"%ld\\n\", eval.integer);\n");
				writer.write("	if (eval.type == DECIMAL) printf(\"%f\\n\", eval.decimal);\n");
				writer.write("	if (eval.type == STRING) printf(\"%s\\n\", eval.string);\n");
				writer.write("}\n\n");
			}

			if (add || mul) {
				writer.write("void push_free_string_stack(free_string_stack *stack, char *val) {\n");
				writer.write("	free_string_stack_node *node = malloc(sizeof(free_string_stack_node));\n");
				writer.write("	node->val = val;\n");
				writer.write("	node->next = stack->head;\n");
				writer.write("	stack->head = node;\n");
				writer.write("}\n\n");
			}

			writer.write("void free_all(free_string_stack *stack) {\n");
			writer.write("	free_string_stack_node *node = stack->head;\n\n");
			writer.write("	while (node != NULL) {\n");
			writer.write("		free_string_stack_node *next = node->next;\n");
			writer.write("		free(node->val);\n");
			writer.write("		free(node);\n");
			writer.write("		node = next;\n");
			writer.write("	}\n\n");
			writer.write("	free(stack);\n");
			writer.write("}\n\n");

			writer.write("evaluation new_integer(long integer) {\n");
			writer.write("	return (evaluation) { .type = INTEGER, .integer = integer };\n");
			writer.write("}\n\n");
			writer.write("evaluation new_decimal(double decimal) {\n");
			writer.write("	return (evaluation) { .type = DECIMAL, .decimal = decimal };\n");
			writer.write("}\n\n");
			writer.write("evaluation new_string(char *string) {\n");
			writer.write("	return (evaluation) { .type = STRING, .string = string };\n");
			writer.write("}\n\n");

			if (add) {
				writer.write("evaluation add(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	char number[50];\n");
				writer.write("	char *result;\n\n");
				writer.write("	switch (a.type) {\n");
				writer.write("		case INTEGER:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.integer + b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_decimal(a.integer + b.decimal);\n");
				writer.write("				case STRING:\n");
				writer.write("					sprintf(number, \"%ld\", a.integer);\n\n");
				writer.write("					result = malloc(strlen(number) + strlen(b.string) + 1);\n");
				writer.write("					strcpy(result, number);\n");
				writer.write("					strcat(result, b.string);\n\n");
				writer.write("					push_free_string_stack(stack, result);\n\n");
				writer.write("					return new_string(result);\n");
				writer.write("			}\n");
				writer.write("		case DECIMAL:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_decimal(a.decimal + b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_decimal(a.decimal + b.decimal);\n");
				writer.write("				case STRING:\n");
				writer.write("					sprintf(number, \"%f\", a.decimal);\n\n");
				writer.write("					result = malloc(strlen(number) + strlen(b.string) + 1);\n");
				writer.write("					strcpy(result, number);\n");
				writer.write("					strcat(result, b.string);\n\n");
				writer.write("					push_free_string_stack(stack, result);\n\n");
				writer.write("					return new_string(result);\n");
				writer.write("			}\n");
				writer.write("		case STRING:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					sprintf(number, \"%ld\", b.integer);\n\n");
				writer.write("					result = malloc(strlen(a.string) + strlen(number) + 1);\n");
				writer.write("					strcpy(result, a.string);\n");
				writer.write("					strcat(result, number);\n\n");
				writer.write("					push_free_string_stack(stack, result);\n\n");
				writer.write("					return new_string(result);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					sprintf(number, \"%f\", b.decimal);\n\n");
				writer.write("					result = malloc(strlen(a.string) + strlen(number) + 1);\n");
				writer.write("					strcpy(result, a.string);\n");
				writer.write("					strcat(result, number);\n\n");
				writer.write("					push_free_string_stack(stack, result);\n\n");
				writer.write("					return new_string(result);\n");
				writer.write("				case STRING:\n");
				writer.write("					result = malloc(strlen(a.string) + strlen(b.string) + 1);\n");
				writer.write("					strcpy(result, a.string);\n");
				writer.write("					strcat(result, b.string);\n\n");
				writer.write("					push_free_string_stack(stack, result);\n\n");
				writer.write("					return new_string(result);\n");
				writer.write("			}\n");
				writer.write("	}\n\n");
				writer.write("	exception(\"Error: invalid addition\", stack);");
				writer.write("}\n\n");
			}

			if (sub) {
				writer.write("evaluation subtract(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	char number[50];\n");
				writer.write("	char *result;\n\n");
				writer.write("	switch (a.type) {\n");
				writer.write("		case INTEGER:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.integer - b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_decimal(a.integer - b.decimal);\n");
				writer.write("			}\n");
				writer.write("		case DECIMAL:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_decimal(a.decimal - b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_decimal(a.decimal - b.decimal);\n");
				writer.write("			}\n");
				writer.write("	}\n\n");
				writer.write("	exception(\"Error: subtraction can only be applied to numbers\", stack);\n");
				writer.write("}\n\n");
			}

			if (mul) {
				writer.write("evaluation multiply(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	switch (a.type) {\n");
				writer.write("		case INTEGER:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.integer * b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_decimal(a.integer * b.decimal);\n");
				writer.write("			}\n");
				writer.write("		case DECIMAL:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_decimal(a.decimal * b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_decimal(a.decimal * b.decimal);\n");
				writer.write("			}\n");
				writer.write("		case STRING:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					char *result = malloc(strlen(a.string) * b.integer + 1);\n");
				writer.write("					strcpy(result, \"\");\n\n");
				writer.write("					for (int i = 0; i < b.integer; i++) strcat(result, a.string);\n\n");
				writer.write("					push_free_string_stack(stack, result);\n\n");
				writer.write("					return new_string(result);\n");
				writer.write("				}\n");
				writer.write("	}\n\n");
				writer.write("	exception(\"Error: multiplication can only be applied to numbers, or onto a string\", stack);\n");
				writer.write("}\n\n");
			}

			if (div) {
				writer.write("evaluation divide(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	switch (a.type) {\n");
				writer.write("		case INTEGER:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					if (b.integer == 0) exception(\"Error: division by zero\", stack);\n");
				writer.write("					return new_integer(a.integer / b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					if (b.decimal == 0) exception(\"Error: division by zero\", stack);\n");
				writer.write("					return new_decimal(a.integer / b.decimal);\n");
				writer.write("			}\n");
				writer.write("		case DECIMAL:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					if (b.integer == 0) exception(\"Error: division by zero\", stack);\n");
				writer.write("					return new_decimal(a.decimal / b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					if (b.decimal == 0) exception(\"Error: division by zero\", stack);\n");
				writer.write("					return new_decimal(a.decimal / b.decimal);\n");
				writer.write("			}\n");
				writer.write("	}\n\n");
				writer.write("	exception(\"Error: division can only be applied to numbers\", stack);\n");
				writer.write("}\n\n");
			}

			if (mod) {
				writer.write("evaluation modulo(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	switch (a.type) {\n");
				writer.write("		case INTEGER:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					if (b.integer == 0) exception(\"Error: modulo by zero\", stack);\n");
				writer.write("					return new_integer(a.integer % b.integer);\n");
				writer.write("			}\n");
				writer.write("	}\n\n");
				writer.write("	exception(\"Error: modulo can only be applied to integers\", stack);\n");
				writer.write("}\n\n");
			}

			if (and) {
				writer.write("evaluation and(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	switch (a.type) {\n");
				writer.write("		case INTEGER:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.integer * b.integer >= 1);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_integer(a.integer * b.decimal >= 1);\n");
				writer.write("				case STRING:\n");
				writer.write("					return new_integer(a.integer * strlen(b.string) >= 1);\n");
				writer.write("			}\n");
				writer.write("		case DECIMAL:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.decimal * b.integer >= 1);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_integer(a.decimal * b.decimal >= 1);\n");
				writer.write("				case STRING:\n");
				writer.write("					return new_integer(a.decimal * strlen(b.string) >= 1);\n");
				writer.write("			}\n");
				writer.write("		case STRING:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(strlen(a.string) * b.integer >= 1);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_integer(strlen(a.string) * b.decimal >= 1);\n");
				writer.write("				case STRING:\n");
				writer.write("					return new_integer(strlen(a.string) * strlen(b.string) >= 1);\n");
				writer.write("			}\n");
				writer.write("	}\n\n");
				writer.write("	exception(\"Error: invalid and\", stack);\n");
				writer.write("}\n\n");
			}

			if (or) {
				writer.write("evaluation or(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	switch (a.type) {\n");
				writer.write("		case INTEGER:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.integer + b.integer >= 1);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_integer(a.integer + b.decimal >= 1);\n");
				writer.write("				case STRING:\n");
				writer.write("					return new_integer(a.integer + strlen(b.string) >= 1);\n");
				writer.write("			}\n");
				writer.write("		case DECIMAL:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.decimal + b.integer >= 1);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_integer(a.decimal + b.decimal >= 1);\n");
				writer.write("				case STRING:\n");
				writer.write("					return new_integer(a.decimal + strlen(b.string) >= 1);\n");
				writer.write("			}\n");
				writer.write("		case STRING:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(strlen(a.string) + b.integer >= 1);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_integer(strlen(a.string) + b.decimal >= 1);\n");
				writer.write("				case STRING:\n");
				writer.write("					return new_integer(strlen(a.string) + strlen(b.string) >= 1);\n");
				writer.write("			}\n");
				writer.write("	}\n\n");
				writer.write("exception(\"Error: invalid or\", stack);\n");
				writer.write("}\n\n");
			}

			if (eq || ne) {
				writer.write("evaluation equals(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	double tolerance = 0.000000000000001;\n\n");
				writer.write("	switch (a.type) {\n");
				writer.write("		case INTEGER:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.integer == b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_integer(a.integer - b.decimal <= tolerance && b.decimal - a.integer <= tolerance);\n");
				writer.write("			}\n");
				writer.write("		case DECIMAL:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.decimal + b.integer >= 1);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_integer(a.decimal + b.decimal >= 1);\n");
				writer.write("			}\n");
				writer.write("		case STRING:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case STRING:\n");
				writer.write("					return new_integer(1 - strcmp(a.string, b.string));\n");
				writer.write("			}\n");
				writer.write("	}\n\n");
				writer.write("	exception(\"Error: comparison can only be applied between numbers or between strings\", stack);\n");
				writer.write("}\n\n");
			}

			if (ne) {
				writer.write("evaluation not_equals(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	return new_integer(1 - equals(a, b, stack).integer);\n");
				writer.write("}\n\n");
			}

			if (gt || le) {
				writer.write("evaluation greater_than(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	switch (a.type) {\n");
				writer.write("		case INTEGER:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.integer > b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_integer(a.integer > b.decimal);\n");
				writer.write("			}\n");
				writer.write("		case DECIMAL:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.decimal > b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_integer(a.decimal > b.decimal);\n");
				writer.write("			}\n");
				writer.write("	}\n\n");
				writer.write("	exception(\"Error: relative comparison can only be applied to numbers\", stack);\n");
				writer.write("}\n\n");
			}

			if (lt || ge) {
				writer.write("evaluation lower_than(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	switch (a.type) {\n");
				writer.write("		case INTEGER:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.integer < b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_integer(a.integer < b.decimal);\n");
				writer.write("			}\n");
				writer.write("		case DECIMAL:\n");
				writer.write("			switch (b.type) {\n");
				writer.write("				case INTEGER:\n");
				writer.write("					return new_integer(a.decimal < b.integer);\n");
				writer.write("				case DECIMAL:\n");
				writer.write("					return new_integer(a.decimal < b.decimal);\n");
				writer.write("			}\n");
				writer.write("	}\n\n");
				writer.write("	exception(\"Error: relative comparison can only be applied to numbers\", stack);\n");
				writer.write("}\n\n");
			}

			if (ge) {
				writer.write("evaluation greater_equals(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	return new_integer(1 - lower_than(a, b, stack).integer);\n");
				writer.write("}\n\n");
			}

			if (le) {
				writer.write("evaluation lower_equals(evaluation a, evaluation b, free_string_stack *stack) {\n");
				writer.write("	return new_integer(1 - greater_than(a, b, stack).integer);\n");
				writer.write("}\n\n");
			}

			if (minus) {
				writer.write("evaluation minus(evaluation a, free_string_stack *stack) {");
				writer.write("	switch (a.type) {\n");
				writer.write("		case INTEGER:\n");
				writer.write("			return new_integer(-a.integer);\n");
				writer.write("		case DECIMAL:\n");
				writer.write("			return new_decimal(-a.decimal);\n");
				writer.write("	}\n\n");
				writer.write("	exception(\"Error: unary minus can only be applied to numbers\", stack);\n");
				writer.write("}\n\n");
			}

			writer.write("int is_true(evaluation a) {\n");
			writer.write("	switch (a.type) {\n");
			writer.write("		case INTEGER:\n");
			writer.write("			return a.integer >= 1;\n");
			writer.write("		case DECIMAL:\n");
			writer.write("			return a.decimal >= 1;\n");
			writer.write("		case STRING:\n");
			writer.write("			return strlen(a.string) >= 1;\n");
			writer.write("	}\n");
			writer.write("}\n\n");
			writer.write("void exception(char *message, free_string_stack *stack) {\n");
			writer.write("	printf(\"%s\\n\", message);\n");
			writer.write("	free_all(stack);\n");
			writer.write("	exit(1);\n");
			writer.write("}\n");

			writer.close();
		} catch (IOException e) {
			System.out.println("Couldn't write in " + runtimePath + ": " + e.getMessage());
		}
	}

	private static void generateHeader(String headerPath, ASTNode ast) {
		boolean add = ASTHas("Addition", ast),
				and = ASTHas("And", ast),
				div = ASTHas("Division", ast),
				eq = ASTHas("Equals", ast),
				ge = ASTHas("GreaterEquals", ast),
				gt = ASTHas("GreaterThan", ast),
				le = ASTHas("LowerEquals", ast),
				lt = ASTHas("LowerThan", ast),
				minus = ASTHas("UnaryMinus", ast),
				mod = ASTHas("Modulo", ast),
				mul = ASTHas("Multiplication", ast),
				ne = ASTHas("NotEquals", ast),
				or = ASTHas("Or", ast),
				sub = ASTHas("Subtraction", ast),
				print = ASTHas("PrintStatement", ast),
				println = ASTHas("PrintlnStatement", ast);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(headerPath));

			writer.write("#ifndef RUNTIME_H\n");
			writer.write("#define RUNTIME_H\n\n");
			writer.write("typedef enum {\n");
			writer.write("	INTEGER,\n");
			writer.write("	DECIMAL,\n");
			writer.write("	STRING\n");
			writer.write("} evaluation_type;\n\n");
			writer.write("typedef struct {\n");
			writer.write("	long integer;\n");
			writer.write("	double decimal;\n");
			writer.write("	char* string;\n");
			writer.write("	evaluation_type type;\n");
			writer.write("} evaluation;\n\n");
			writer.write("typedef struct free_string_stack_node free_string_stack_node;\n");
			writer.write("struct free_string_stack_node {\n");
			writer.write("	char *val;\n");
			writer.write("	free_string_stack_node *next;\n");
			writer.write("};\n\n");
			writer.write("typedef struct {\n");
			writer.write("	free_string_stack_node *head;\n");
			writer.write("} free_string_stack;\n\n");
			writer.write("free_string_stack *new_free_string_stack();\n");
			if (add || mul) writer.write("void push_free_string_stack(free_string_stack *stack, char *val);\n");
			writer.write("void free_all(free_string_stack *stack);\n\n");
			writer.write("evaluation new_integer(long integer);\n");
			writer.write("evaluation new_decimal(double decimal);\n");
			writer.write("evaluation new_string(char *string);\n\n");
			if (print) writer.write("void print(evaluation eval);\n");
			if (println) writer.write("void println(evaluation eval);\n\n");
			if (add) writer.write("evaluation add(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (sub) writer.write("evaluation subtract(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (mul) writer.write("evaluation multiply(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (div) writer.write("evaluation divide(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (mod) writer.write("evaluation modulo(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (and) writer.write("evaluation and(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (or) writer.write("evaluation or(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (eq || ne) writer.write("evaluation equals(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (ne) writer.write("evaluation not_equals(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (gt || le) writer.write("evaluation greater_than(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (lt || ge) writer.write("evaluation lower_than(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (ge) writer.write("evaluation greater_equals(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (le) writer.write("evaluation lower_equals(evaluation a, evaluation b, free_string_stack *stack);\n");
			if (minus) writer.write("evaluation unary_minus(evaluation a, free_string_stack *stack);\n");
			writer.write("int is_true(evaluation eval);\n\n");
			writer.write("void exception(char *message, free_string_stack *stack);\n\n");
			writer.write("#endif\n");

			writer.close();
		} catch (IOException e) {
			System.out.println("Couldn't write in " + headerPath + ": " + e.getMessage());
		}
	}

	private static boolean ASTHas(String name, ASTNode ast) {
		if (ast.getClass().getSimpleName().equals(name)) return true;

		for (Node child : ast.getChildren()) {
			if (ASTHas(name, (ASTNode) child)) return true;
		}

		return false;
	}
}