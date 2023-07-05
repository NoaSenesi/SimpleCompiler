package fr.senesi.simplecompiler.codegen;

public class CodeGenUtils {
	private static int variableCount = 0, labelCount = 0;
	private static String code = "";

	public static String generateVariableName() {
		return "var" + variableCount++;
	}

	public static String generateLabelName() {
		return "label" + labelCount++;
	}

	public static String getLastVariableName() {
		return "var" + (variableCount - 1);
	}

	public static String getLastLabelName() {
		return "label" + (labelCount - 1);
	}

	public static void pushLine(String code) {
		CodeGenUtils.code += code + "\n";
	}

	public static String getCode() {
		return CodeGenUtils.code;
	}
}