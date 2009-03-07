package ru.vladykin.saxgen.state;

import java.util.List;
import ru.vladykin.saxgen.model.Group;
import ru.vladykin.saxgen.model.InputElement;
import ru.vladykin.saxgen.model.Tag;

/**
 * @author Alexey Vladykin
 */
public class StateGenerator {

    /**
     * Generates state and transition table from list of input elements.
     *
     * @param input  list of input elements
     * @return state and transition table
     */
    public static TransitionTable generate(List<InputElement> input) {
        TransitionTable table = new TransitionTable();
        generate(input, table, new StateSet(0), table.addState());
        return table;
    }

    /**
     * Generates state and transition table from list of input elements.
     *
     * @param input  list of input elements
     * @param table  state and transition table
     * @param currentStates  states before input
     * @param finalState  primary state after input
     * @return states after input
     */
    private static StateSet generate(List<InputElement> input, TransitionTable table, StateSet initStates, int finalState) {
        StateSet states = initStates.clone();
        for (int i = 0; i < input.size(); ++i) {
            InputElement elem = input.get(i);
            int localFinalState = (i + 1 == input.size()) ? finalState : table.addState();
            switch (elem.getType()) {
                case START_TAG:
                case END_TAG: {
                    for (Integer source : states.getAll()) {
                        table.addTransition(source, (Tag) elem, localFinalState);
                    }
                    states.setAll(localFinalState);
                    break;
                }
                case GROUP: {
                    Group group = (Group) elem;
                    StateSet finalStates = generate(group, table, states, localFinalState);
                    switch (group.getQuantifier()) {
                        case NONE:
                        case QUESTION:
                            states = finalStates;
                            break;
                        case ASTERISK:
                            table.copyTransitions(states.getPrimary(), localFinalState);
                            states.add(finalStates, localFinalState);
                            break;
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException();
            }
        }
        return states;
    }

    private static StateSet generate(Group group, TransitionTable table, StateSet initStates, int finalState) {
        StateSet states = null;
        for (int j = 0; j < group.getAlternativeCount(); ++j) {
            StateSet finalStates = generate(group.getAlternative(j), table, initStates, finalState);
            if (states == null) {
                states = finalStates;
            } else {
                states.add(finalStates);
            }
        }
        return states;
    }
}
