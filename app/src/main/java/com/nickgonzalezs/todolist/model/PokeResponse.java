package com.nickgonzalezs.todolist.model;

import java.util.ArrayList;

public class PokeResponse {

    private ArrayList<Pokemon> results;

    public PokeResponse(ArrayList<Pokemon> results) {
        this.results = results;
    }

    public PokeResponse() {
    }

    public ArrayList<Pokemon> getResults() {
        return results;
    }

    public void setResults(ArrayList<Pokemon> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "PokeResponse{" +
                "results=" + results +
                '}';
    }
}
