package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Identifier extends Expression {
	private String name;
	private Evaluation value;

	public static final Map<String, Identifier> IDENTIFIERS = new HashMap<>();

	public Identifier(String name) {
		super(new ArrayList<>());
		this.name = name;

		if (IDENTIFIERS.containsKey(name)) value = IDENTIFIERS.get(name).evaluate();
	}

	public Identifier(String name, Evaluation value) {
		this(name);
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setValue(Evaluation value) {
		this.value = value;
	}

	public Evaluation evaluate() {
		return value;
	}

	public boolean isDeterministic() {
		return false;
	}

	public String generateCode() {
		return name;
	}
}
