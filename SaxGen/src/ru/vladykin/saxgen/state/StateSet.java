package ru.vladykin.saxgen.state;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexey Vladykin
 */
public class StateSet implements Cloneable {

    private int primary;
    private Set<Integer> all;

    public StateSet(int primary) {
        this.primary = primary;
        this.all = new HashSet<Integer>();
        all.add(primary);
    }

    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        assert all.contains(primary);
        this.primary = primary;
    }

    public Set<Integer> getAll() {
        return all;
    }

    public void setAll(int primary) {
        this.all.clear();
        this.primary = primary;
        this.all.add(primary);
    }

    public void add(StateSet states) {
        assert primary == states.primary;
        all.addAll(states.all);
    }

    public void add(StateSet states, int primary) {
        all.addAll(states.all);
        this.primary = primary;
    }

    @Override
    public StateSet clone() {
        try {
            StateSet clone = (StateSet) super.clone();
            clone.all = new HashSet<Integer>(clone.all);
            return clone;
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return all.toString() + ", primary = " + primary;
    }

}
