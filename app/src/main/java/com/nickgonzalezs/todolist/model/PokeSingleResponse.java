package com.nickgonzalezs.todolist.model;

import java.util.ArrayList;

public class PokeSingleResponse {

    private ArrayList<Abilities> abilities;
    private String name;
    private Sprites sprites;

    public PokeSingleResponse(ArrayList<Abilities> abilities, String name, Sprites sprites) {
        this.abilities = abilities;
        this.name = name;
        this.sprites = sprites;
    }

    public ArrayList<Abilities> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<Abilities> abilities) {
        this.abilities = abilities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    @Override
    public String toString() {
        return "PokeSingleResponse{" +
                "abilities=" + abilities +
                ", name='" + name + '\'' +
                ", sprites=" + sprites +
                '}';
    }
}
