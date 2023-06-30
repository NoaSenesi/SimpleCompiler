package fr.senesi.simplecompiler.parsing.table;

public class Action {
	private int popCount, nextState;
	private ActionType actionType;
	private String reduceTo;

	public Action(ActionType actionType) {
		this.actionType = actionType;
	}

	public Action(ActionType actionType, int value) {
		this(actionType, value, null);
	}

	public Action(ActionType actionType, int value, String reduceTo) {
		this.actionType = actionType;
		this.reduceTo = reduceTo;

		if (actionType == ActionType.SHIFT || actionType == ActionType.GOTO) {
			nextState = value;
		} else if (actionType == ActionType.REDUCE) {
			popCount = value;
		}
	}

	public int getPopCount() {
		return popCount;
	}

	public int getNextState() {
		return nextState;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public String getReduceTo() {
		return reduceTo;
	}

	public enum ActionType {
		SHIFT, REDUCE, ACCEPT, GOTO, ERROR
	}
}
