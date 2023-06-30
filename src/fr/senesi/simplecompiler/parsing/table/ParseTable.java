package fr.senesi.simplecompiler.parsing.table;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import fr.senesi.simplecompiler.parsing.table.Action.ActionType;
import fr.senesi.simplecompiler.parsing.tree.parsetree.PTNode;

public class ParseTable {
	private List<String> grammar;

	public ParseTable(String filename) {
		try {
			grammar = Files.readAllLines(Paths.get(filename));
		} catch (IOException e) {
			System.out.println("Error: parse table " + filename + " not found");
		}
	}

	public String getSymbol(int index) {
		return grammar.get(0).split(",")[index];
	}

	public int getSymbolIndex(String symbol) {
		int i = 0;

		for (String s : grammar.get(0).split(",")) {
			if (s.equals(symbol)) return i;
			i++;
		}

		return -1;
	}

	public Action getAction(int state, String symbol) {
		int symbolIndex = getSymbolIndex(symbol);

		if (symbolIndex == -1) return null;

		String[] line = grammar.get(state + 1).split(",");
		if (line.length <= symbolIndex) return new Action(ActionType.ERROR);


		String action = line[symbolIndex];
		if (action.length() == 0) return new Action(ActionType.ERROR);

		if (action.charAt(0) == 'A') return new Action(ActionType.ACCEPT);
		if (action.charAt(0) == 'S') return new Action(ActionType.SHIFT, Integer.parseInt(action.substring(1)));
		if (action.charAt(0) == 'G') return new Action(ActionType.GOTO, Integer.parseInt(action.substring(1)));
		if (action.charAt(0) == 'R') return new Action(ActionType.REDUCE, Integer.parseInt(action.split(" ")[1]), getSymbol(Integer.parseInt(action.split(" ")[0].substring(1))));

		return new Action(ActionType.ERROR);
	}

	public Action getAction(int state, PTNode node) {
		return getAction(state, node.getGrammarIdentification());
	}

	public List<String> expected(int state) {
		List<String> expected = new ArrayList<>();

		for (int i = 0; i < grammar.get(0).split(",").length; i++) {
			String symbol = getSymbol(i);
			Action action = getAction(state, symbol);

			if (action.getActionType() != ActionType.ERROR && action.getActionType() != ActionType.GOTO) expected.add(symbol);
		}

		return expected;
	}
}
