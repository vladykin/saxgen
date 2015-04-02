package name.vladykin.saxgen.state;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import name.vladykin.saxgen.model.Tag;

/**
 * @author Alexey Vladykin
 */
public class TransitionTable {

    private final List<Map<Tag, Integer>> table;

    public TransitionTable() {
        table = new ArrayList<Map<Tag, Integer>>();
        addState(); // initial state
    }

    public int addState() {
        int newState = table.size();
        table.add(new LinkedHashMap<Tag, Integer>());
        return newState;
    }

    public void addTransition(int source, Tag trigger, int dest) {
        table.get(source).put(trigger, dest);
    }

    public Map<Tag, Integer> getTransitions(int source) {
        return table.get(source);
    }

    public void copyTransitions(int from, int to) {
        Map<Tag, Integer> toMap = table.get(to);
        for (Map.Entry<Tag, Integer> entry : table.get(from).entrySet()) {
            toMap.put(entry.getKey(), entry.getValue());
        }
    }

    public int size() {
        return table.size();
    }
}
