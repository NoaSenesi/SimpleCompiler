package fr.senesi.simplecompiler.parsing.tree.abstractsyntaxtree;

import java.util.ArrayList;

public class Identifier extends Expression {
	private String name;
	private Evaluation value;

	public Identifier(String name) {
		super(new ArrayList<>());
		this.name = name;
	}

	public Identifier(String name, Evaluation value) {
		this(name);
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Evaluation evaluate() {
		return value;
	}
}
