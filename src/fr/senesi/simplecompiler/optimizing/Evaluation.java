package fr.senesi.simplecompiler.optimizing;

public class Evaluation {
	private EvaluationType type;
	private int intValue;
	private double decimalValue;
	private String stringValue;

	public Evaluation(int value) {
		type = EvaluationType.INTEGER;
		intValue = value;
	}

	public Evaluation(double value) {
		type = EvaluationType.DECIMAL;
		decimalValue = value;
	}

	public Evaluation(String value) {
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


	public enum EvaluationType {
		INTEGER,
		DECIMAL,
		STRING
	}
}
