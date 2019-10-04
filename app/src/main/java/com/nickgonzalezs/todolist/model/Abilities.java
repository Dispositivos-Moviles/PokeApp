package com.nickgonzalezs.todolist.model;

public class Abilities {

    private Ability ability;
    private boolean is_hidden;
    private int slot;

    public Abilities(Ability ability, boolean is_hidden, int slot) {
        this.ability = ability;
        this.is_hidden = is_hidden;
        this.slot = slot;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public boolean isIs_hidden() {
        return is_hidden;
    }

    public void setIs_hidden(boolean is_hidden) {
        this.is_hidden = is_hidden;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public String toString() {
        return "Abilities{" +
                "ability=" + ability +
                ", is_hidden=" + is_hidden +
                ", slot=" + slot +
                '}';
    }
}
