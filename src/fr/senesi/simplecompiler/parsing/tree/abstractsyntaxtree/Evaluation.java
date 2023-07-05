package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.ArrayList;

public class Evaluation extends ASTTerminal {
	private EvaluationType type;
	private int intValue;
	private double decimalValue;
	private String stringValue;

	private Evaluation() {
		super(new ArrayList<>());
	}

	public Evaluation(int value) {
		this();
		type = EvaluationType.INTEGER;
		intValue = value;
	}

	public Evaluation(double value) {
		this();
		type = EvaluationType.DECIMAL;
		decimalValue = value;
	}

	public Evaluation(String value) {
		this();
		type = EvaluationType.STRING;
		stringValue = value;
	}

	public EvaluationType getType() {
		return type;
	}

	public Object getValue() {
		switch (type) {
			case INTEGER:
				return intValue;
			case DECIMAL:
				return decimalValue;
			case STRING:
				return stringValue;
			default:
				return null;
		}
	}

	public Evaluation evaluate() {
		if (type == EvaluationType.INTEGER) return new Evaluation((int) getValue());
		if (type == EvaluationType.DECIMAL) return new Evaluation((double) getValue());
		if (type == EvaluationType.STRING) return new Evaluation((String) getValue());
		return null;
	}

	public boolean isDeterministic() {
		return true;
	}


	public enum EvaluationType {
		INTEGER,
		DECIMAL,
		STRING
	}

	public String generateCode() {
		if (type == EvaluationType.INTEGER) return String.valueOf((int) getValue());
		if (type == EvaluationType.DECIMAL) return String.valueOf((double) getValue());
		if (type == EvaluationType.STRING) return "\"" + (String) getValue() + "\"";

		return "";
	}

	public String generateCode(String labelStart, String labelEnd) {
		return generateCode();
	}
}
